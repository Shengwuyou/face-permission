package com.example.demo;

import com.face.permission.server.Application;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = Application.class)
public class DemoApplicationTests {

//    protected final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Test
    public void contextLoads() {
//        logger.trace("I am trace log.");
//        logger.debug("I am debug log.");
//        logger.warn("I am warn log.");
//        logger.error("I am error log.");
//        logger.info("I am info log.");
    }

}
