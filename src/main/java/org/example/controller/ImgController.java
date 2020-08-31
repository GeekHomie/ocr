package org.example.controller;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import javafx.scene.control.Tab;
import org.apache.commons.io.FileUtils;
import org.example.common.consts.CommonConstant;
import org.example.pojo.Table;
import org.example.service.IOcrService;
import org.example.util.UUIDUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.Period;
import java.time.ZoneId;
import java.util.*;

/**
 * @author Ethan
 * @description 接收图片
 * @date 8/24 21:28
 */
@RestController
@Api("图片模块")
public class ImgController {
    private Logger logger = LoggerFactory.getLogger(ImgController.class);
    private static Map<String, JSONObject> DATA = new HashMap<>();

    @Autowired
    IOcrService ocrService;

    /**
     * ocr图片
     * @param img 图像文件
     */
    @ApiOperation("上传图片, 调用ocr接口")
    @RequestMapping(value = "/ocr", method = RequestMethod.POST)
    public String batchOcr(MultipartFile img, HttpServletRequest request, HttpServletResponse response) throws IOException {
        String filepath = saveFile(img);
        String ocrResult = ocrService.ocr(filepath);
        logger.info(ocrResult);
        return markJson(ocrResult);
    }

    private String saveFile(MultipartFile multipartFile) throws IOException {
        InputStream inputStream = multipartFile.getInputStream();
        String filename = multipartFile.getOriginalFilename();
        File outFile = new File(CommonConstant.FILE_PATH + File.separator + filename);
        FileUtils.copyInputStreamToFile(inputStream, outFile);
        return outFile.getAbsolutePath();
    }

    private String markJson(String json){
        String uuid = UUIDUtil.uuid();
        JSONObject jsonObject = JSONObject.parseObject(json);
        JSONObject markedJson = jsonObject.fluentPut("id", uuid);
        DATA.put(uuid, markedJson);
        return uuid;
    }

    @ApiOperation("上传申请表")
    @RequestMapping(value = "/table", method = RequestMethod.POST)
    public String tableOcr(MultipartFile table, HttpServletRequest request, HttpServletResponse response) throws IOException {
        String filepath = saveFile(table);
        String tableResult = ocrService.tableOcr(filepath);
        logger.info(tableResult);
        return markJson(tableResult);
    }

    @ApiOperation("校验结果")
    @RequestMapping(value = "/check", method = RequestMethod.POST)
    public String check(String tableId, String imgId) throws IOException, ParseException {
        JSONObject table = DATA.get(tableId);
        JSONObject img = DATA.get(imgId);
        String excelJson = ocrService.checkTicket(table, img);
        logger.info(excelJson);
        return excelJson;
    }
}
