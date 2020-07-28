package com.cooperative.controller.bili;

import com.cooperative.common.BookEnum;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.net.URLEncoder;
import java.util.List;
import java.util.Map;

/**
 * @ClassName BiliController
 * @Description TODO
 * @Author zhouliansheng
 * @Date 2020/5/27 16:24
 * @Version 1.0
 **/
@Slf4j
@Controller
@RequestMapping("bili")
public class BiliController {

    @Autowired
    ObjectMapper mapper;

    @RequestMapping(value = "/getNavList", method = RequestMethod.GET)
    @ResponseBody
    public String getNavList() throws JsonProcessingException {
        List<String> navList = Lists.newLinkedList();
        navList.add("首页");
        navList.add("动画");
        navList.add("番剧");
        navList.add("国创");
        navList.add("音乐");
        return mapper.writeValueAsString(navList);
    }

    @RequestMapping(value = "/uploadFile", method = RequestMethod.POST)
    @ResponseBody
    public String uploadFile(@RequestParam(value = "file", required = false) MultipartFile file) throws JsonProcessingException {
        if (!file.isEmpty()) {
            String fileName = file.getOriginalFilename();
            return "上传成功";
        }
        return "上传失败";
    }

    @RequestMapping(value = "/downloadFile/{bookId}.pdf", method = RequestMethod.GET, produces =
            MediaType.APPLICATION_PDF_VALUE)
    @ResponseBody
    public String downloadFile(HttpServletRequest request, HttpServletResponse response,
                               @PathVariable Integer bookId) throws UnsupportedEncodingException {
        BookEnum bookEnum = BookEnum.values()[bookId];
        String name = bookEnum.getName();
        String path = bookEnum.getPath();

        File file = new File(path);

        // 如果文件名存在，则进行下载
        if (file.exists()) {

            // 配置文件下载
            response.setHeader("content-type", "application/pdf");
            response.setContentType("application/pdf");
            // 下载文件能正常显示中文
            response.setHeader("Content-Disposition", "attachment;filename=" +
                    URLEncoder.encode(name, "UTF-8"));

            // 实现文件下载
            byte[] buffer = new byte[1024];
            FileInputStream fis = null; //文件输入流
            BufferedInputStream bis = null;
            try {
                fis = new FileInputStream(file);
                bis = new BufferedInputStream(fis);
                OutputStream os = response.getOutputStream(); //输出流
                int i = bis.read(buffer);
                while (i != -1) {
                    os.write(buffer, 0, i);
                    i = bis.read(buffer);
                }
                System.out.println("Download the  successfully!");
            } catch (Exception e) {
                e.printStackTrace();
                System.out.println("Download the  failed!");
            } finally {
                if (bis != null) {
                    try {
                        bis.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                if (fis != null) {
                    try {
                        fis.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }

        return null;
    }
}
