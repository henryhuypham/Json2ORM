package com.huyphamthanh8290.huyorm.orm.query;

import com.huyphamthanh8290.huyorm.orm.parser.JsonParser;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

/**
 * This class return data as model object from JSON data
 */
public class JsonRemoteDataGetter {
    protected static String getJsonData(HttpResponse response, String fileName) throws IOException {
        HttpEntity entity = response.getEntity();
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        entity.writeTo(out);
        entity.consumeContent();

        if (fileName != null) {
            String path = "get path from filename";
            FileOutputStream jsonFile = new FileOutputStream(path);
            out.writeTo(jsonFile);
            jsonFile.close();
        }

        out.close();

        return out.toString();
    }

    public static <T> List<T> getDataUsingJsonArray(Class<T> dataClass, String url) {
        return getDataUsingJsonArray(dataClass, url, null);
    }

    public static <T> List<T> getDataUsingJsonArray(Class<T> dataClass, String url, String fileName) {
        try {
            // Demo purpose, we should use singleton on http client
            DefaultHttpClient httpClient = new DefaultHttpClient();
            HttpResponse response = httpClient.execute(new HttpGet(url));
            if (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
                JSONArray array = new JSONArray(getJsonData(response, fileName));
                return JsonParser.parseJsonArray(dataClass, array);
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    public static <T> T getDataUsingJsonObject(Class<T> dataClass, String url) {
        return getDataUsingJsonObject(dataClass, url, null);
    }

    public static <T> T getDataUsingJsonObject(Class<T> dataClass, String url, String fileName) {
        try {
            // Demo purpose, we should use singleton on http client
            DefaultHttpClient httpClient = new DefaultHttpClient();
            HttpResponse response = httpClient.execute(new HttpGet(url));
            if (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
                JSONObject object = new JSONObject(getJsonData(response, fileName));
                return JsonParser.parseJsonObject(dataClass, object);
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

}
