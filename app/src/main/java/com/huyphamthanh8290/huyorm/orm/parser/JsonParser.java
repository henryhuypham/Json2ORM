package com.huyphamthanh8290.huyorm.orm.parser;

import com.huyphamthanh8290.huyorm.orm.annotation.json.JsonArray;
import com.huyphamthanh8290.huyorm.orm.annotation.json.JsonObject;
import com.huyphamthanh8290.huyorm.orm.annotation.json.JsonValue;
import com.huyphamthanh8290.huyorm.orm.postProcess.PostProcessJson;

import org.json.JSONArray;
import org.json.JSONObject;

import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.util.ArrayList;
import java.util.List;

/**
 * This class parse JSON into model class recursively
 */
public class JsonParser {
    /**
     * Parse JSON object into model object
     * @param modelClass
     * @param data
     * @param <T>
     * @return
     */
    public static <T> T parseJsonObject(Class<T> modelClass, JSONObject data) {
        if (data == null)
            return null;

        Field[] fields = modelClass.getDeclaredFields();
        T rsModel = newInstance(modelClass);

        for (Field f : fields) {
            if (f.isAnnotationPresent(JsonValue.class)) {
                assignValueToField(data, rsModel, f, f.getAnnotation(JsonValue.class));
            }
            else if (f.isAnnotationPresent(JsonObject.class)) {
                assignObjectToField(data, rsModel, f);
            }
            else if (f.isAnnotationPresent(JsonArray.class))
                assignArrayToField(data, rsModel, f);
        }

        if (rsModel instanceof PostProcessJson) {
            ((PostProcessJson) rsModel).postGetDataProcess();
        }

        return rsModel;
    }

    /**
     * Parse JSON array into model array
     * @param modelClass
     * @param data
     * @param <T>
     * @return
     */
    @SuppressWarnings("unchecked")
    public static <T> List<T> parseJsonArray(Class<T> modelClass, JSONArray data) {
        if (data == null)
            return null;

        List<T> result = new ArrayList<T>();
        for (int i = 0; i < data.length(); i++) {
            T obj = modelClass == String.class ? (T) data.opt(i) : parseJsonObject(modelClass, data.optJSONObject(i));
            result.add(obj);
        }
        return result;
    }

    public static String fixUrlSlashEscape(String url) {
        return url == null ? null : url.replace("\\/", "/");
    }

    private static <T> void assignArrayToField(JSONObject data, T rsModel, Field f) {
        JsonArray anno = f.getAnnotation(JsonArray.class);
        JSONArray arrData = data.optJSONArray(anno.name());
        f.setAccessible(true);
        try {
            Class<?> itemClass = (Class<?>) ((ParameterizedType) f.getGenericType()).getActualTypeArguments()[0];
            f.set(rsModel, parseJsonArray(itemClass, arrData));
        }
        catch (IllegalArgumentException e) {
            e.printStackTrace();
        }
        catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    private static <T> void assignObjectToField(JSONObject data, T rsModel, Field f) {
        String jsonObjName = f.getAnnotation(JsonObject.class).name();
        f.setAccessible(true);
        try {
            f.set(rsModel, parseJsonObject(f.getType(), data.optJSONObject(jsonObjName)));
        }
        catch (IllegalArgumentException e) {
            e.printStackTrace();
        }
        catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    private static <T> void assignValueToField(JSONObject data, T rsModel, Field f, JsonValue anno) {
        f.setAccessible(true);
        try {
            f.set(rsModel, valueOf(data, anno.name(), f.getType()));
        }
        catch (IllegalArgumentException e) {
            e.printStackTrace();
        }
        catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    private static <T> T newInstance(Class<T> model) {
        T rsModel = null;
        try {
            rsModel = model.newInstance();
        }
        catch (InstantiationException e) {
            e.printStackTrace();
        }
        catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        return rsModel;
    }

    private static Object valueOf(JSONObject data, String name, Class<?> type) {
        if (data.isNull(name))
            return null;

        if (type == String.class) {
            return data.optString(name);
        }
        else if (type == Boolean.class) {
            return data.optBoolean(name);
        }
        else if (type == Integer.class) {
            return data.optInt(name);
        }
        else if (type == Float.class) {
            return Float.parseFloat(data.optString(name));
        }
        else if (type == Double.class) {
            return Double.parseDouble(data.optString(name));
        }
        else {
            return null;
        }
    }
}
