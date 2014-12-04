package com.huyphamthanh8290.huyorm.orm.query;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import com.huyphamthanh8290.huyorm.orm.annotation.sql.Column;
import com.huyphamthanh8290.huyorm.orm.annotation.sql.JoinTable;
import com.huyphamthanh8290.huyorm.orm.annotation.sql.JoinWith;
import com.huyphamthanh8290.huyorm.orm.annotation.sql.Table;
import com.huyphamthanh8290.huyorm.orm.postProcess.PostProcessSql;

/**
 * This class perform read, update, delete on db
 */
public class DatabaseQuery {
    /**
     * get the SQLite database
     */
    private static SQLiteDatabase getDb() {
        return null;
    }

    /**
     * Perform SELECT on database, with the table correspond to the model
     * @param model the class based on which the table will be select
     * @param whereClause condition for the SELECT clause
     * @param orderClause order for the SELECT clause
     * @param <T>
     * @return list of selected object
     */
    public static <T> List<T> select(Class<T> model, String whereClause, String orderClause) {
        return select(model, whereClause, orderClause, null);
    }

    /**
     * Perform SELECT on database, with the table correspond to the model
     * @param model the class based on which the table will be select
     * @param whereClause condition for the SELECT clause
     * @param orderClause order for the SELECT clause
     * @param limitClause limit for SELECT clause
     * @param <T>
     * @return list of selected object
     */
    public static <T> List<T> select(Class<T> model, String whereClause, String orderClause, String limitClause) {
        // Get all fields of model
        Field[] fields = model.getDeclaredFields();

        // Array to store column names of table
        String[] colName = new String[fields.length];

        // Array to store types of field of model
        Class<?>[] typeName = new Class<?>[fields.length];

        // Get main table name
        String tableName = model.getAnnotation(Table.class).name();

        // Get joined table names
        JoinTable[] joinTables = model.isAnnotationPresent(JoinWith.class) ? model.getAnnotation(JoinWith.class).value() : null;

        // SELECT query, init as empty
        StringBuilder selectColumns = new StringBuilder();

        // Get fields annotated with @Column, as fields not annotated wont be mapped
        List<Field> columnField = new ArrayList<Field>();
        for (Field f : fields) {
            if (f.isAnnotationPresent(Column.class)) {
                columnField.add(f);
            }
        }
        fields = columnField.toArray(new Field[0]);

        // Build query
        // The output is like: "table.col1, table.col2, table.col3"
        for (int i = 0; i < fields.length; i++) {
            Column annotation = fields[i].getAnnotation(Column.class);
            colName[i] = annotation.table() + "." + annotation.name();
            typeName[i] = fields[i].getType();
            selectColumns.append(i == 0 ? colName[i] : ", " + colName[i]);
        }

        String selectClause = String.format("SELECT %s ", selectColumns.toString(), tableName);
        String fromClause = createFromClause(joinTables, tableName);
        whereClause = (whereClause == null || whereClause.isEmpty()) ? "" : String.format("WHERE %s ", whereClause);
        orderClause = (orderClause == null || orderClause.isEmpty()) ? "" : String.format("ORDER BY %s ", orderClause);
        limitClause = (limitClause == null || limitClause.isEmpty()) ? "" : String.format("LIMIT %s ", limitClause);

        // dbQUery is like: "SELECT table.col1, table.col2, table.col3 FROM (table LEFT JOIN table1) WHERE ... ORDER BY ... LIMIT ...
        String dbQUery = String.format("%s %s %s %s %s", selectClause, fromClause, whereClause, orderClause, limitClause);

        // Perform query, type cast data to the right type & return
        Cursor row = getDb().rawQuery(dbQUery, null);
        List<T> result = new ArrayList<T>();
        if (row.moveToFirst()) {
            do {
                T item = null;

                try {
                    item = model.newInstance();
                }
                catch (InstantiationException e) {
                    e.printStackTrace();
                }
                catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
                for (int i = 0; i < fields.length; i++) {
                    fields[i].setAccessible(true);
                    try {
                        fields[i].set(item, valueOf(row, i, typeName[i]));
                    }
                    catch (IllegalArgumentException e) {
                        e.printStackTrace();
                    }
                    catch (IllegalAccessException e) {
                        e.printStackTrace();
                    }
                }

                // Perform post process action if exist (will be useful, e.g: db store date as dd-MM-yyyy, we want to load as dd/MM/yyyy)
                if (item instanceof PostProcessSql) {
                    ((PostProcessSql) item).postSelectProcess();
                }
                result.add(item);
            }
            while (row.moveToNext());

            row.close();
        }

        return result;
    }

    /**
     * Create LIKE clause, to perform search on database
     * @param searchTerm keywords to search
     * @param columns the columns on which keywords are searched
     * @return
     */
    public static String makeLikeClause(String searchTerm, String... columns) {
        String[] term = searchTerm.trim().split("\\s+");
        StringBuilder likeClause = new StringBuilder("'%");

        for (String t : term) {
            likeClause.append(t);
            likeClause.append("%'");
        }
        String likePart = likeClause.toString();

        likeClause = new StringBuilder("(");
        for (int i = 0; i < columns.length; i++) {
            if (i != 0)
                likeClause.append(" OR ");
            likeClause.append(String.format("(%s LIKE %s)", columns[i], likePart));
        }
        likeClause.append(")");

        return likeClause.toString();
    }

    /**
     * Count the number of records satisfied the condition
     * @param model the class based on which the table will be select
     * @param whereClause condition for COUNT clause
     * @param <T>
     * @return
     */
    public static <T> int countRecord(Class<T> model, String whereClause) {
        String tableName = model.getAnnotation(Table.class).name();
        String query = String.format("SELECT count(*) as count FROM %s WHERE %s", tableName, whereClause);
        Cursor row = getDb().rawQuery(query, null);
        if (row.moveToFirst()) {
            return row.getInt(0);
        }
        return -1;
    }

    /**
     * Delete records satisfied the condition
     * @param model the class based on which the table will be select
     * @param whereClause condition for COUNT clause
     * @param <T>
     */
    public static <T> void deleteRecord(Class<T> model, String whereClause) {
        String tableName = model.getAnnotation(Table.class).name();
        String query = String.format("DELETE FROM %s WHERE %s", tableName, whereClause);
        getDb().execSQL(query);
    }

    /**
     * Update a row in a table
     * @param model the class based on which the table will be select
     * @param updateData the new data for the record
     * @param whereClause condition to find the row to be updated
     * @param <T>
     */
    public static <T> void updateRecord(Class<T> model, Map<String, ? extends Object> updateData, String whereClause) {
        String tableName = model.getAnnotation(Table.class).name();
        StringBuilder setClause = new StringBuilder();
        Iterator<String> iter=updateData.keySet().iterator();

        while (iter.hasNext()) {
            String col = iter.next();
            Object val = updateData.get(col);
            String setTerm;
            if (val instanceof Integer) {
                setTerm = String.format("%s=%d", col, (Integer) val);
            }
            else {
                setTerm = String.format("%s='%s'", col, val.toString());
            }
            setClause.append(setTerm);
            if (iter.hasNext()) setClause.append(",");
        }

        String query = String.format("UPDATE %s SET %s WHERE %s", tableName, setClause.toString(), whereClause);
        getDb().execSQL(query);
    }

    /**
     * Create FROM clause, which contains the main table & joined tables
     * @param joinTables list of tables that will be joined to main table
     * @param mainTable the main table
     * @return
     */
    private static String createFromClause(JoinTable[] joinTables, String mainTable) {
        String query = mainTable;
        if (joinTables != null) {
            for (JoinTable table : joinTables) {
                String exp1 = String.format("%s.%s", mainTable, table.localColumn());
                String exp2 = String.format("%s.%s", table.name(), table.joinColumn());

                query = String.format("( %s %s join %s on %s=%s )", query, table.joinType().toString(), table.name(), exp1, exp2);
            }
        }
        query = "FROM " + query;
        return query;
    }

    /**
     * Return the value with correct type
     * @param row the current table row
     * @param index the index of the column
     * @param typeName the name of the type to be returned
     * @return
     */
    private static Object valueOf(Cursor row, int index, Class<?> typeName) {
        if (typeName == Integer.class) {
            return row.getInt(index);
        }
        if (typeName == Boolean.class) {
            return Boolean.parseBoolean(row.getString(index));
        }
        if (typeName == Float.class || typeName == Double.class) {
            return row.getFloat(index);
        }
        return row.getString(index);
    }
}