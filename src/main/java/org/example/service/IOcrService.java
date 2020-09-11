package org.example.service;

import com.alibaba.fastjson.JSONObject;

import java.io.OutputStream;
import java.text.ParseException;

/**
 * @author Ethan
 * @description
 * @date 8/24 22:04
 */
public interface IOcrService {
    /**
     * ocr
     * @param filepath
     * @return
     */
    String ocr(String filepath);

    /**
     * ocr识别模版
     * @param filepath
     * @return
     */
    String tableOcr(String filepath);

    /**
     * 校验票据
     * @param table
     * @param img
     * @return
     */
    String checkTicket(JSONObject table, JSONObject img) throws ParseException;

    void excel(String uuid, OutputStream outputStream);
}
