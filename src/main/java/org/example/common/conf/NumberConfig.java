package org.example.common.conf;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author Ethan
 * @version 1.0
 * @date 9/2 9:29
 */
public class NumberConfig {
    private static Map<String, String> NUM_MAP = new ConcurrentHashMap<>();

    static {
        NUM_MAP.put("", "300000");
    }

    public static String get(String name){
        String number = NUM_MAP.get("");
        if (number == null || "".equals(number.trim())){
            return "";
        }
        return number;
    }
}
