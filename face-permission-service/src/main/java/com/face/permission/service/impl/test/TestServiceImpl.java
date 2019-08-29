package com.face.permission.service.impl.test;

import com.face.permission.mapper.dao.PUserMapper;
import com.face.permission.mapper.domain.PUserDO;
import com.face.permission.service.interfaces.test.ITestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Date;

/**
 * @Description
 * @Author xuyizhong
 * @Date 2019-07-05 17:34
 */
@Service
public class TestServiceImpl implements ITestService {

    @Autowired
    PUserMapper userMapper;

    @Override
    public String test() {
        PUserDO pUser = new PUserDO();
        pUser.setuId("snowFlowers");
        pUser.setNickName("mickey");
        pUser.setEmail("987171135@qq.com");
        pUser.setMobilePhone("10000000000");
        pUser.setSex( 1);
        pUser.setStatus(1);
        pUser.setCreateTime(LocalDateTime.now());
        int i = userMapper.insert(pUser);
        System.out.println(
                i>0?pUser.getId():0
        );
        return "pass";
    }

}