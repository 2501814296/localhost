package com.ssh.controller;

import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang.math.RandomUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.util.HashMap;

@Controller
@RequestMapping("/upload")
public class UploadImgController {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    /**
     * 个人信息上传
     *
     * @return {Result}
     */
    @RequestMapping(value = "/headImg", method = {RequestMethod.POST})
    @ResponseBody
    public Object headImg(@RequestParam(value = "file", required = false) MultipartFile attach, HttpServletRequest request, HttpServletResponse response) throws Exception {
        String carPictuerUrl = null;
        HashMap<String, Object> map2 = new HashMap<String, Object>();
        HashMap<String, Object> map = new HashMap<String, Object>();
        try {
            //判断文件是否为空
            if (!attach.isEmpty()) {
                // path1用来判断项目环境是linux还是windows
                String path1 =
                        request.getSession().getServletContext().getRealPath("/");
                System.out.println("path1:" + path1);
                String oldFileName = attach.getOriginalFilename();//原文件名
                System.out.println("oldFileName:" + oldFileName);
                String prefix = FilenameUtils.getExtension(oldFileName);//原文件后缀
                System.out.println("prefix:" + prefix);
                String fileName = System.currentTimeMillis() + RandomUtils.nextInt(1000000) + "." + prefix;
                System.out.println("fileName:" + fileName);
                if (path1.indexOf("target") > -1) {
                    // windows环境
                    String path = request.getSession().getServletContext().getRealPath("uploadImg");
                    System.out.println("path:" + path);
                    String pathx = path.substring(0, path.indexOf("springmvc-hibernate")) + "springmvc-hibernate\\src\\main\\webapp\\uploadImg";
                    System.out.println("pathx:" + pathx);
                    /**
                     * RandomUtils.nextInt(1, 1000000)
                     * 返回一个在指定区间内的整数
                     * startInclusive 可以返回的最小值必须是非负的
                     * endExclusive 上限（不包括）
                     */
                    File targetFilex = new File(pathx, fileName);  //创建文件
                    if (!targetFilex.exists()) {  //判断文件夹是否存在
                        targetFilex.mkdirs();
                    }
                    attach.transferTo(targetFilex);
                    copyFile(pathx + "\\" + fileName, path + "\\" + fileName);
                } else {
                    // linux环境
                    String path = request.getSession().getServletContext().getRealPath("uploadImg");
                    System.out.println("path:" + path);
                    File targetFilex = new File(path, fileName);  //创建文件
                    if (!targetFilex.exists()) {  //判断文件夹是否存在
                        targetFilex.mkdirs();
                    }
                    attach.transferTo(targetFilex);
                }
                carPictuerUrl = "/uploadImg/" + fileName;
                String sql = "update user t set t.touxiang = '" +
                        carPictuerUrl + "' where t.id = 1";
                jdbcTemplate.update(sql);
            }
            map.put("code", 0);
            map.put("msg", "");
            map.put("data", map2);
            map2.put("src", carPictuerUrl);
        } catch (Exception e) {
            System.out.println("----------------------/upload/headImg方法报错了-------------------------");
            e.printStackTrace();
        }
        return map;
    }


    public void copyFile(String oldPath, String newPath) {
        try {
            int bytesum = 0;
            int byteread = 0;
            File oldfile = new File(oldPath);
            if (oldfile.exists()) { //文件存在时
                InputStream inStream = new FileInputStream(oldPath); //读入原文件
                FileOutputStream fs = new FileOutputStream(newPath);
                byte[] buffer = new byte[1444];
                int length;
                while ((byteread = inStream.read(buffer)) != -1) {
                    bytesum += byteread; //字节数 文件大小
                    fs.write(buffer, 0, byteread);
                }
                inStream.close();
            }
        } catch (Exception e) {
            e.printStackTrace();

        }

    }


    public void inputStreamToFile(InputStream ins, File file) {
        try {
            OutputStream os = new FileOutputStream(file);
            int bytesRead = 0;
            byte[] buffer = new byte[8192];
            while ((bytesRead = ins.read(buffer, 0, 8192)) != -1) {
                os.write(buffer, 0, bytesRead);
            }
            os.close();
            ins.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
