package org.example.service.impl;

import com.alibaba.excel.ExcelWriter;
import com.alibaba.excel.metadata.Sheet;
import com.alibaba.excel.support.ExcelTypeEnum;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import org.example.common.conf.NumberConfig;
import org.example.pojo.Table;
import org.example.service.IOcrService;
import org.example.util.Base64Util;
import org.example.util.FileUtil;
import org.example.util.HttpUtil;
import org.example.util.UUIDUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.OutputStream;
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
    private static Map<String, Table> DATA = new HashMap<>();

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

        NotNullMap tableMap = new NotNullMap();
        List<NotNullMap> imgMaps = new ArrayList<>();
        setWord(table, tableMap);
        setMultiWord(img, imgMaps);

        String approvalFormNumber = tableMap.get("商旅申请单号");
        String name = tableMap.get("出行人");
        String jobNumber = "";
        List<String> nameList = "".equals(name) ? new ArrayList<>() :
                Arrays.asList(name.replaceAll(" ", "").split("、"));
        for (String n : nameList){
            jobNumber += "、" + NumberConfig.get(n);
        }
        jobNumber = jobNumber.substring(1);
        String travelReason = tableMap.get("出差事由详情");
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
        BigDecimal transportFee = newBigDecimal(tableMap.get("城际交通费"));
        BigDecimal accommodationFee = newBigDecimal(tableMap.get("住宿费"));

        int employeeType = 0;
        int travelDays = Period.between(departureDate.toInstant().atZone(ZoneId.systemDefault()).toLocalDate(),
                arrivalDate.toInstant().atZone(ZoneId.systemDefault()).toLocalDate()).getDays() + 1;

        // todo 票据姓名时间识别
        Date startDate = dateFormat.parse(start);
        Date endDate = dateFormat.parse(end);
        boolean travelDaysConsisted = isTravelDaysConsist(imgMaps, startDate, endDate);
        boolean nameConsisted = isNameConsist(imgMaps, nameList);
        List<Object> list = isFeeConsisted(imgMaps, transportFee, accommodationFee);
        boolean feeConsisted = (boolean) list.get(0);

        int transportAllowance = 2 * 80;
        int mealAllowance = travelDays * 50;
        BigDecimal approvalFormFee = ((BigDecimal) list.get(1)).add((BigDecimal)list.get(2));
        BigDecimal allFee = approvalFormFee.add(BigDecimal.valueOf(transportAllowance + mealAllowance));

        Table checkedTable = new Table();
        checkedTable.setUuid(UUIDUtil.uuid());
        checkedTable.setApprovalFormNumber(approvalFormNumber);
        checkedTable.setJobNumber(jobNumber);
        checkedTable.setName(name);
        checkedTable.setTravelReason(travelReason);
        checkedTable.setTravelLocation(travelLocation);
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
        checkedTable.setApprovalFormFee(transportFee.add(accommodationFee));
        checkedTable.setAllFee(allFee);
        DATA.put(checkedTable.getUuid(), checkedTable);
        String jsonString = JSONObject.toJSONString(checkedTable);
        logger.info(jsonString);
        return jsonString;
    }

    @Override
    public void excel(String uuid, OutputStream outputStream) {
        List<List<String>> data = new ArrayList<>();
        Table table = DATA.get(uuid);
        String jsonString = JSONObject.toJSONString(table);
        JSONObject jsonObject = JSONObject.parseObject(jsonString);
        Set<String> keys = jsonObject.keySet();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        for (String key : keys){
            List<String> temp = new ArrayList<>(2);
            if ("uuid".equals(key)){
                continue;
            }
            else if ("arrivalDate".equals(key) || "departureDate".equals(key)){
                temp.add(key);
                temp.add(dateFormat.format(new Date(Long.parseLong(jsonObject.getString(key)))));
            } else {
                temp.add(key);
                temp.add(jsonObject.getString(key));
            }

            data.add(temp);
        }
        ExcelWriter excelWriter;
        try {
            excelWriter = new ExcelWriter(outputStream, ExcelTypeEnum.XLSX,false);
            Sheet sheet1 = new Sheet(1,0);
            sheet1.setSheetName("sheet1");
            excelWriter.write0(data, sheet1);
            excelWriter.finish();
            outputStream.flush();
        } catch (Exception e) {
            e.printStackTrace();
        }
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

    private void setWord(JSONObject json, NotNullMap map){
        JSONObject v1 = json.getJSONObject("data");
        JSONArray v2 = v1.getJSONArray("ret");
        map.put("templateName", v1.getString("templateName"));
        for (int i = 0; i < v2.size(); i++){
            JSONObject v3 = v2.getJSONObject(i);
            map.put(v3.getString("word_name"), v3.getString("word"));
        }
    }

    private void setMultiWord(JSONObject json, List<NotNullMap> list){
        JSONObject v1 = json.getJSONObject("data");
        JSONArray v2 = v1.getJSONArray("ret");
        for (int i = 0; i < v2.size(); i++){
            JSONObject v3 = v2.getJSONObject(i);

            NotNullMap imgMap = new NotNullMap();
            JSONArray v4 = v3.getJSONArray("ret");
            imgMap.put("templateName", v3.getString("templateName"));
            for (int j = 0; j < v4.size(); j++) {
                JSONObject v5 = v4.getJSONObject(j);
                imgMap.put(v5.getString("word_name"), v5.getString("word"));
            }
            list.add(imgMap);
        }
    }

    private BigDecimal newBigDecimal(String value){
        if ("".equals(value)){
            return new BigDecimal(0);
        }
        return new BigDecimal(value);
    }

    private boolean isTravelDaysConsist(List<NotNullMap> list, Date start, Date end) throws ParseException {
        String[] keys = new String[]{"火车票", "增值税发票"};
        String[] values = new String[]{"date", "InvoiceDate"};
        int dateCount = 0;
        for (NotNullMap map : list){
            String templateName = map.get("templateName");
            for (int i = 0; i < keys.length; i++){
                if (keys[i].equals(templateName)){
                    if (i == 0) {
                        dateCount++;
                    }
                    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy年MM月dd日");
                    String startString = map.get(values[i]);
                    Date format = dateFormat.parse(startString);
                    if (format.compareTo(start) < 0 || format.compareTo(end) > 0) {
                        return false;
                    }
                }
            }
        }
        if (dateCount != 2) {
            return false;
        }
        return true;
    }

    private boolean isNameConsist(List<NotNullMap> list, List<String> names){
        String[] keys = new String[]{"火车票"};
        String[] values = new String[]{"name"};
        for (NotNullMap map : list){
            for (int i = 0; i < keys.length; i++){
                String templateName = map.get("templateName");
                if (keys[i].equals(templateName)) {
                    String name = map.get(values[i]);
                    for (String n : names) {
                        if (!n.equals(name)) {
                            return false;
                        }
                    }
                }
            }
        }
        return true;
    }

    private List<Object> isFeeConsisted(List<NotNullMap> list, BigDecimal transportFee, BigDecimal accommodationFee){
        boolean result = true;
        List<Object> results = new ArrayList<>();
        String[] keys = new String[]{"火车票", "增值税发票"};
        String[] values = new String[]{"ticket_rates", "CommodityPrice#1#1"};
        BigDecimal[] compare = new BigDecimal[2];
        compare[0] = new BigDecimal(0);
        compare[1] = new BigDecimal(0);
        for (NotNullMap map : list){
            String templateName = map.get("templateName");
            for (int i = 0; i < keys.length; i++){
                if (keys[i].equals(templateName)){
                    String money = map.get(values[i]);
                    compare[i] = compare[i].add(new BigDecimal(money.replaceAll("元", "")
                            .replaceAll("￥","").replaceAll(" ", "")));
                }
            }
        }

        if (transportFee.compareTo(compare[0]) < 0 || accommodationFee.compareTo(compare[1]) < 0){
            result = false;
        }
        results.add(result);
        results.add(compare[0]);
        results.add(compare[1]);
        return results;
    }
}
