package org.example.util;

import java.util.Random;
import java.util.UUID;

/**
 * @author Ethan
 * @version 1.0
 * @date 8/30 23:01
 */
public class UUIDUtil {
    public static String uuid(){
        return UUID.randomUUID().toString().replaceAll("-", "");
    }
}
