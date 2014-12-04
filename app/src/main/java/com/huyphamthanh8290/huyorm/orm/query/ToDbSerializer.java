package com.huyphamthanh8290.huyorm.orm.query;

import android.database.sqlite.SQLiteDatabase;

import com.huyphamthanh8290.huyorm.orm.annotation.serialize.ToChildTable;
import com.huyphamthanh8290.huyorm.orm.annotation.serialize.ToColumn;
import com.huyphamthanh8290.huyorm.orm.annotation.serialize.ToTable;

import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.util.ArrayList;
import java.util.List;

/**
 * THis class perform inserting data to db
 */
public class ToDbSerializer {
    /**
     * get the SQLite database
     */
    private static SQLiteDatabase getDb() {
        return null;
    }

    /**
     * Inser data to a table, cascade insert into related tables if exist using recursion
     * @param model the class based on which the table will be select
     * @param rawData data to be inserted
     * @param <T>
     * @return
     */
    public static <T> boolean saveToDb(Class<T> model, Object rawData) {
        if (rawData==null) {
            return false;
        }

        // main table name
        String tableName = model.getAnnotation(ToTable.class).name();
        Field[] fields = model.getDeclaredFields();

        // Store columns that will be inserted data into
        List<Field> storeFields = new ArrayList<Field>();

        // Store child table names (tables that will be cascade inserted into)
        List<Field> childTables = new ArrayList<Field>();

        for (Field field : fields) {
            if (field.isAnnotationPresent(ToColumn.class)) {
                field.setAccessible(true);
                storeFields.add(field);
            }
            else if (field.isAnnotationPresent(ToChildTable.class)) {
                field.setAccessible(true);
                childTables.add(field);
            }
        }

        // Create the 1st part of INSERT clause, output is like: INSERT or REPLACE into tablename(col1, col2, col3) values
        StringBuilder columnList = new StringBuilder("(");
        for (int i = 0; i < storeFields.size(); i++) {
            if (i != 0) {
                columnList.append(",");
            }
            columnList.append(storeFields.get(i).getAnnotation(ToColumn.class).name());
        }
        columnList.append(")");
        String insertHeader = String.format("INSERT or REPLACE into %s%s values", tableName, columnList);

        // Loop through list of data, for each data item, create the 2nd part of INSERT clause & perform insertion
        @SuppressWarnings("unchecked")
        List<T> lData = (List<T>) rawData;
        for (T data : lData) {
            // valueList is the value part of the INSERT clause, output is like: (val1, val2, val3)
            StringBuilder valueList = new StringBuilder("(");
            for (int i = 0; i < storeFields.size(); i++) {
                if (i != 0) {
                    valueList.append(",");
                }
                try {
                    Object fData = storeFields.get(i).get(data);
                    String val = fData == null ? null : fData.toString().replace("'", "''");
                    valueList.append(val == null ? "null" : String.format("'%s'", val));
                }
                catch (Exception e) {
                    e.printStackTrace();
                    return false;
                }
            }
            valueList.append(")");

            // Execute query, which is made of the 1st part and the 2nd part of INSERT clause
            getDb().execSQL(String.format("%s%s", insertHeader, valueList.toString()));

            // Recursively insert into related tables
            for (Field child : childTables) {
                try {
                    saveToDb((Class<?>) ((ParameterizedType) child.getGenericType()).getActualTypeArguments()[0], child.get(data));
                }
                catch (Exception e) {
                    e.printStackTrace();
                    return false;
                }
            }
        }
        return true;

    }

    /**
     * Delete all data from a table, cascade to related table if exist using recursion
     * @param model the class based on which the table will be select
     * @param <T>
     */
    public static <T> void clearData(Class<T> model) {
        String tableName = model.getAnnotation(ToTable.class).name();
        String query = String.format("DELETE FROM %s", tableName);
        // Delete from main table
        getDb().execSQL(query);

        // Recursively delete related tables
        Field[] fields = model.getDeclaredFields();
        for (Field field : fields) {
            if (field.isAnnotationPresent(ToChildTable.class)) {
                field.setAccessible(true);
                clearData((Class<?>) ((ParameterizedType) field.getGenericType()).getActualTypeArguments()[0]);
            }
        }
    }
}
