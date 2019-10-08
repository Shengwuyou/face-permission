package com.face.permission.server.web.controller;

import com.alibaba.fastjson.JSONObject;
import com.face.permission.api.model.request.user.UserRequest;
import com.face.permission.common.utils.CharacterUtils;
import com.face.permission.common.utils.RandomUtil;
import com.face.permission.common.utils.RandomValueUtil;
import com.face.permission.server.Application;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
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
//    @Rollback(value = false)
    public void register() throws Exception {
        UserRequest registerRequest = new UserRequest();
        registerRequest.setParentUserId(null);
        registerRequest.setNickName(RandomValueUtil.getChineseName());
        registerRequest.setMobilePhone(RandomValueUtil.getTelephone());
        registerRequest.setEmail(RandomValueUtil.getEmail(6,10));
        registerRequest.setHeadPic(null);
        registerRequest.setSex(RandomValueUtil.getNum(1,2));
        registerRequest.setStatus(1);
        String loginNameAndPsw = CharacterUtils.getRandomFixLen();
        registerRequest.setLoginName(loginNameAndPsw);
        registerRequest.setPassword(loginNameAndPsw);
        registerRequest.setGrade(0);
        registerRequest.setType(1);
        Integer[] roles = new Integer[2];
        roles[0] = 3;
        roles[1] = 5;
        registerRequest.setRole(roles);

        MvcResult result = this.mockMvc.perform(
                MockMvcRequestBuilders.post("/user/register/cms")
                            .header("token", "eyJhbGciOiJIUzI1NiJ9.eyJ1aWQiOiIxIiwiZnJvbVdheSI6bnVsbCwicm9sZXMiOlsxXSwiaXNzIjoicm9vdCIsIm5pY2tOYW1lXyI6IlJPT1TnrqHnkIblkZgiLCJleHAiOjE1Njk4MzE2MTgsImlhdCI6MTU2OTgzMTMxOCwicGxhdGZvcm0iOm51bGx9.X9OMi5hdqcReKk1N6zU-_pf1VbSGMfIEQzUy94SCCek")
                            .header("trace","{\"fromWay\":0,\"platform\":\"pc\"}")
                        .contentType(MediaType.APPLICATION_JSON_UTF8)
                            .content(JSONObject.toJSONString(registerRequest))
                            )
                .andReturn();

        MockHttpServletResponse response =  result.getResponse();
        System.out.println(response.getContentAsString());
    }

    @Test
    @Rollback(value = false)
    public void createUser() throws Exception {
        for (int i = 0; i < 100; i++) {
            Thread.sleep(300);
            register();
        }
    }

    @Test
    public void login() throws Exception {
        UserRequest registerRequest = new UserRequest();
        registerRequest.setParentUserId(null);
        registerRequest.setLoginName("18368095211");
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