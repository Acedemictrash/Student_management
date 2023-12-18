package com.example.student_ssm.controller;

import cn.hutool.core.io.FileUtil;
import com.example.student_ssm.common.Result;
import jakarta.servlet.ServletOutputStream;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.net.URLEncoder;

@RestController
@RequestMapping("/files")
public class FileController {

    @Value("${ip}")
    private String ip;

    @Value("${server.port}")
    private String port;

    private static final String BASE_URL = System.getProperty("user.dir") + "/files";

    @PostMapping("/upload")
    public Result upload(MultipartFile file) throws IOException {
        long flag = System.currentTimeMillis(); // 时间戳
        String originalFilename = file.getOriginalFilename(); // 文件名
        String filename = flag + "_" + originalFilename; // 新文件名

        File finalFile = new File(BASE_URL + "/" + filename); // 新文件

        if (!finalFile.getParentFile().exists()) {
            finalFile.getParentFile().mkdirs();// 创建父级文件路径
        }
        file.transferTo(finalFile);

        String url = "http://" + ip  + ":" + port + "/files/download?filename=" + filename;
        // 返回文件的 url
        return Result.success(url);
    }

    @GetMapping("/download")
    public void download(String filename, HttpServletResponse response) throws IOException {
        File file = new File(BASE_URL + "/" + filename);
        response.setHeader("Content-Disposition", "attachment;filename=" + URLEncoder.encode(filename, "UTF-8"));
        response.setContentType("application/octet-stream");
        ServletOutputStream os = response.getOutputStream();
        FileUtil.writeToStream(file, os);
        os.flush();
        os.close();
    }

}
