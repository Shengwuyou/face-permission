package com.face.permission.server.web.controller;

import com.alibaba.fastjson.JSONObject;
import com.face.permission.api.model.request.user.UserRequest;
import com.face.permission.server.Application;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = {Application.class})
public class UserControllerTest{

    public MockMvc mockMvc;

    @Autowired
    WebApplicationContext webApplicationContext;

    @Before
    public void setUp() throws Exception {
        // initialize mock object
        MockitoAnnotations.initMocks(this);
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
    }

    @Test
    public void register() throws Exception {
        UserRequest registerRequest = new UserRequest();
        registerRequest.setParentUserId(null);
        registerRequest.setNickName("Chengjiaojiao");
        registerRequest.setMobilePhone("12345678901");
        registerRequest.setEmail("1@163.com");
        registerRequest.setHeadPic(null);
        registerRequest.setSex(null);
        registerRequest.setStatus("1");
//        registerRequest.setLoginName("Admin123");
        registerRequest.setPassword("Admin123");
        registerRequest.setGrade(null);
        registerRequest.setType("1");
        registerRequest.setRole(null);

        MvcResult result = this.mockMvc.perform(
                MockMvcRequestBuilders.post("/user/register")
                            .contentType(MediaType.APPLICATION_JSON_UTF8)
                            .content(JSONObject.toJSONString(registerRequest))
                            )
                .andReturn();

        MockHttpServletResponse response =  result.getResponse();
        System.out.println(response.getContentAsString());
    }


    @Test
    public void login() throws Exception {
        UserRequest registerRequest = new UserRequest();
        registerRequest.setParentUserId(null);
//        registerRequest.setMobilePhone("12345678901");
//        registerRequest.setEmail("1@163.com");
//        registerRequest.setLoginName("admin123");  //LoginName 可以是loginName / email / mobilePhone
        registerRequest.setPassword("Admin123");


        MvcResult result = this.mockMvc.perform(
                MockMvcRequestBuilders.post("/user/login")
                        .contentType(MediaType.APPLICATION_JSON_UTF8)
                        .content(JSONObject.toJSONString(registerRequest))
        )
                .andReturn();

        MockHttpServletResponse response =  result.getResponse();
        System.out.println(response.getContentAsString());
    }
    @Test
    public void updateUserInfo() throws Exception {

        //?name='xuyizhong123345'&age=1
        //                                    .param("name","xuyizhong123345")
        //                                    .param("age","1")

        MvcResult result = mockMvc.perform(
                            MockMvcRequestBuilders
                                    .get("/user/update")
                                    .param("name","xuyizhong123")
                                    .param("age","14")
                                    .contentType(MediaType.APPLICATION_FORM_URLENCODED))
                                    .andReturn();

        MockHttpServletResponse response = result.getResponse();
        System.out.println(response.getContentAsString());
    }
}