package org.example.service.impl;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import org.example.pojo.Table;
import org.example.service.IOcrService;
import org.example.util.Base64Util;
import org.example.util.FileUtil;
import org.example.util.HttpUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.net.URLEncoder;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Period;
import java.time.ZoneId;
import java.util.*;

/**
 * @author Ethan
 * @description
 * @date 8/24 22:05
 */
@Service
public class OcrServiceImpl implements IOcrService {
    private static Logger logger = LoggerFactory.getLogger(OcrServiceImpl.class);

    /**
     * ocr识别apiUrl
     */
    private static final String RECOGNISE_URL_FINANCE = "https://aip.baidubce.com/rest/2.0/solution/v1/iocr/recognise/finance";
    private static final String RECOGNISE_URL_DEMO = "https://aip.baidubce.com/rest/2.0/solution/v1/iocr/recognise";
    private static String accessToken;

    static {
        accessToken = AuthService.getAuth();
        logger.info(accessToken);
    }

    @Override
    public String ocr(String filepath){
        try {
            byte[] imgData = FileUtil.readFileByBytes(filepath);
            String imgStr = Base64Util.encode(imgData);
            // 请求模板参数
            String recogniseParams = "templateSign=your_template_sign&image=" + URLEncoder.encode(imgStr, "UTF-8");
            // 请求分类器参数
            String classifierParams = "classifierId=your_classfier_id&image=" + URLEncoder.encode(imgStr, "UTF-8");
            // 请求混贴票据识别参数
            String detectorParams = "detectorId=0&image=" + URLEncoder.encode(imgStr, "UTF-8");

            // 请求模板识别
            // String result = HttpUtil.post(RECOGNISE_URL, accessToken, recogniseParams);
            // 请求分类器识别
            // String result = HttpUtil.post(RECOGNISE_URL, accessToken, classifierParams);
            // 请求混贴票据识别
            return HttpUtil.post(RECOGNISE_URL_FINANCE, accessToken, detectorParams);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }

    @Override
    public String tableOcr(String filepath) {
        try {
            byte[] imgData = FileUtil.readFileByBytes(filepath);
            String imgStr = Base64Util.encode(imgData);
            // 请求模板参数
            String recogniseParams = "templateSign=f51e2842959e0b21de6a6465c8c2f2c2&image=" + URLEncoder.encode(imgStr, "UTF-8");
            return HttpUtil.post(RECOGNISE_URL_DEMO, accessToken, recogniseParams);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }

    @Override
    public String checkTicket(JSONObject table, JSONObject img) throws ParseException {
        if (table == null || img == null){
            return "";
        }
//        JSONObject data = img.getJSONObject("data");
//        JSONArray ret = data.getJSONArray("ret");
//        for (int i = 0; i < ret.size(); i++){
//            JSONObject ticketProperties = ret.getJSONObject(i);
//            JSONArray properties = ticketProperties.getJSONArray("ret");
//            String sign = ticketProperties.getString("templateSign");
//        }

        NotNullMap tableMap = new NotNullMap();
        NotNullMap imgMap = new NotNullMap();
        JSONObject v1 = table.getJSONObject("data");
        JSONArray v2 = v1.getJSONArray("ret");
        for (int i = 0; i < v2.size(); i++){
            JSONObject v3 = v2.getJSONObject(i);
            tableMap.put("word_name", v3.getString("word_name"));
            tableMap.put("word", v3.getString("word"));
        }

        String approvalFormNumber = tableMap.get("商旅申请单号");
        String jobNumber = "";
        String names = tableMap.get("出行人");
        List<String> name = "".equals(names) ? new ArrayList<>() :
                Arrays.asList(names.replaceAll(" ", "").split("、"));
        String travelReason = tableMap.get("出差事由");
        String travelLocation = tableMap.get("出差地点");
        String locationType = "BX00401";
        int travelLocationType = 0;

        // 时间识别
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        String start = tableMap.get("出发日期");
        start = ("".equals(start) ? dateFormat.format(new Date()).toString() : start);
        String end = tableMap.get("返回日期");
        end = ("".equals(end) ? dateFormat.format(new Date()).toString() : end);
        Date departureDate = dateFormat.parse(start);
        Date arrivalDate = dateFormat.parse(end);

        // todo 费用使用票据报销
        BigDecimal transportFee = newBigDecimal(tableMap.get("城市交通费"));
        BigDecimal accommodationFee = newBigDecimal(tableMap.get("住宿费"));

        int employeeType = 0;
        int travelDays = Period.between(departureDate.toInstant().atZone(ZoneId.systemDefault()).toLocalDate(),
                arrivalDate.toInstant().atZone(ZoneId.systemDefault()).toLocalDate()).getDays();

        // todo 票据姓名时间识别
        boolean travelDaysConsisted = true;
        boolean nameConsisted = true;
        boolean feeConsisted = true;

        int transportAllowance = travelDays * 80;
        int mealAllowance = travelDays * 50;
        BigDecimal approvalFormFee = transportFee.add(accommodationFee);
        BigDecimal allFee = approvalFormFee.add(BigDecimal.valueOf(transportAllowance + mealAllowance));

        Table checkedTable = new Table();
        checkedTable.setApprovalFormNumber(approvalFormNumber);
        checkedTable.setJobNumber(jobNumber);
        checkedTable.setName(name);
        checkedTable.setTravelReason(travelReason);
        checkedTable.setTravellocation(travelLocation);
        checkedTable.setLocationType(locationType);
        checkedTable.setTravelLocationType(travelLocationType);
        checkedTable.setDepartureDate(departureDate);
        checkedTable.setArrivalDate(arrivalDate);
        checkedTable.setTransportFee(transportFee);
        checkedTable.setAccommodationFee(accommodationFee);
        checkedTable.setEmployeeType(employeeType);
        checkedTable.setTravelDays(travelDays);
        checkedTable.setTravelDaysConsisted(travelDaysConsisted);
        checkedTable.setNameConsisted(nameConsisted);
        checkedTable.setFeeConsisted(feeConsisted);
        checkedTable.setTransportAllowance(transportAllowance);
        checkedTable.setMealAllowance(mealAllowance);
        checkedTable.setApprovalFormFee(approvalFormFee);
        checkedTable.setAllFee(allFee);
        String jsonString = JSONObject.toJSONString(checkedTable);
        logger.info(jsonString);
        return jsonString;
    }
    
    private static class NotNullMap {
        Map<String, String> map = new HashMap<>(32);
        public String get(String key){
            String value = map.get(key);
            return value == null ? "" : value;
        }

        public void put(String key, String value){
            map.put(key, value);
        }
    }

    private BigDecimal newBigDecimal(String value){
        if ("".equals(value)){
            return new BigDecimal(0);
        }
        return new BigDecimal(value);
    }
}
