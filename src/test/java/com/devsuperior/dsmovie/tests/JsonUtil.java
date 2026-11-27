package com.devsuperior.dsmovie.tests;

import java.util.HashMap;
import java.util.Map;

import org.json.simple.JSONObject;

public class JsonUtil {
    
    public static JSONObject createJsonBody(Object... keyValuePairs) {
        if (keyValuePairs.length % 2 != 0) {
            throw new RuntimeException("Key without a value");
        }
        Map<String, Object> map = new HashMap<>();
        for (int i = 0; i < keyValuePairs.length; i += 2) {
            map.put(String.valueOf(keyValuePairs[i]), keyValuePairs[i + 1]);
        }
        return new JSONObject(map);
    }

}
