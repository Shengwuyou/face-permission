package com.face.permission.server.controller;

import com.face.permission.service.interfaces.ITestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * @Description
 * @Author xuyizhong
 * @Date 2019-07-05 17:35
 */
@Controller
public class TestController {

    @Autowired
    ITestService testService;

    @RequestMapping(method = RequestMethod.GET, value = "/test")
    @ResponseBody
    public String getIp(){
        testService.test();
        return "OK!";
    }
}
