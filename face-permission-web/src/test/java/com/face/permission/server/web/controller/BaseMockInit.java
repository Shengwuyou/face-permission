package com.face.permission.server.web.controller;

import com.face.permission.server.Application;
import com.face.permission.service.interfaces.ITestService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

/**
 * @Description
 * @Author xuyizhong
 * @Date 2019-07-18 16:51
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = {Application.class})
public class BaseMockInit {

    public MockMvc mockMvc;

    @Autowired
    WebApplicationContext webApplicationContext;

    @Before
    public void setUp() throws Exception {
        // initialize mock object
        MockitoAnnotations.initMocks(this);
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
    }

    @Autowired
    ITestService testService;

    @Test
    public void test(){
        testService.test();
    }
}
