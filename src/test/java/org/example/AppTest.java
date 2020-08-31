package org.example;

import org.example.controller.ImgController;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

import static org.junit.Assert.*;

/**
 * @author Ethan
 * @description
 * @date 8/24 22:33
 */
@SpringBootTest
@RunWith(SpringRunner.class)
public class AppTest {
    
    @Autowired
    ImgController imgController;

    @Test
    public void main() throws IOException {
        File file = new File(System.getProperty("user.dir") + File.separator + "tmp" + File.separator + "1.png");
        FileInputStream inputStream = new FileInputStream(file);
        MultipartFile multipartFile = new MockMultipartFile("1.png", inputStream);
        String result = imgController.batchOcr(multipartFile, null, null);
    }
}