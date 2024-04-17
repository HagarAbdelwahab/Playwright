package utils;

import io.restassured.response.Response;

public class JSONExtractor {


    public static String getStringFromJson(Response response , String key) {
        return response.jsonPath().getString(key);
    }
    public static Double getDoubleFromJson(Response response , String key) {
        return response.jsonPath().getDouble(key);
    }
}
