package org.example.controller;

import com.alibaba.fastjson.JSONObject;
import org.example.service.IOcrService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.text.ParseException;

import static org.junit.Assert.*;

/**
 * @author Ethan
 * @version 1.0
 * @date 8/31 9:51
 */
@SpringBootTest
@RunWith(SpringRunner.class)
public class ImgControllerTest {

    @Autowired
    IOcrService ocrService;

    @Test
    public void check() throws ParseException {
        String json = "{\"data\":{\"ret\":[{\"probability\":{\"average\":0.995978,\"min\":0.976627,\"variance\":0.000053},\"location\":{\"top\":190,\"left\":242,\"width\":58,\"height\":12},\"word_name\":\"返回日期\",\"word\":\"2019-08-25\"},{\"probability\":{\"average\":0.999753,\"min\":0.999568,\"variance\":0.0},\"location\":{\"top\":246,\"left\":260,\"width\":20,\"height\":13},\"word_name\":\"交通工具\",\"word\":\"火车\"},{\"probability\":{\"average\":0.999121,\"min\":0.999121,\"variance\":0.0},\"location\":{\"top\":209,\"left\":267,\"width\":6,\"height\":12},\"word_name\":\"出差天数\",\"word\":\"8\"},{\"probability\":{\"average\":0.99864,\"min\":0.996615,\"variance\":0.000001},\"location\":{\"top\":265,\"left\":250,\"width\":41,\"height\":13},\"word_name\":\"预算总金额\",\"word\":\"3850.00\"},{\"probability\":{\"average\":0.986603,\"min\":0.874678,\"variance\":0.001126},\"location\":{\"top\":95,\"left\":230,\"width\":81,\"height\":11},\"word_name\":\"商旅申请单号\",\"word\":\"SS201908151843\"},{\"probability\":{\"average\":0.993161,\"min\":0.939111,\"variance\":0.000326},\"location\":{\"top\":169,\"left\":241,\"width\":61,\"height\":14},\"word_name\":\"出发日期\",\"word\":\"2019-08-18\"},{\"probability\":{\"average\":0.978458,\"min\":0.883536,\"variance\":0.001812},\"location\":{\"top\":113,\"left\":237,\"width\":67,\"height\":13},\"word_name\":\"出行人\",\"word\":\"陆凌姣、陈晶\"}],\"templateSign\":\"f51e2842959e0b21de6a6465c8c2f2c2\",\"templateName\":\"demo\",\"scores\":1.0,\"isStructured\":true,\"logId\":\"159884295090480\",\"templateMatchDegree\":0.9821474898231636,\"clockwiseAngle\":360.0},\"error_code\":0,\"error_msg\":\"\",\"log_id\":\"159884295090480\"}\n";
        JSONObject table = JSONObject.parseObject(json);
        String s = ocrService.checkTicket(table, null);
        System.out.println(s);
    }
}