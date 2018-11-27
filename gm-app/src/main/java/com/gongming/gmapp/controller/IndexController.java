package com.gongming.gmapp.controller;

import com.gongming.gmcommon.ftp.FtpUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class IndexController {

    @Autowired
    FtpUtils ftpUtils;

    @RequestMapping(value = "/")
    @ResponseBody
    public String index() {
        return "hello, welcome to my home";
    }

    @GetMapping(value = "/ftp")
    @ResponseBody
    public String ftp() {

        return ftpUtils.getUserName();
    }
}
