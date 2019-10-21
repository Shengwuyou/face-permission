/**
 * Copyright 2014-now amugua.com All right reserved. This software is the
 * confidential and proprietary information of amugua.com ("Confidential
 * Information"). You shall not disclose such Confidential Information and shall
 * use it only in accordance with the terms of the license agreement you entered
 * into with amugua.com.
 */
package com.face.permission.server.swagger;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.face.permission.api.model.response.TokenDTO;
import com.face.permission.common.constants.WebConstant;
import com.face.permission.common.utils.MD5;
import com.face.permission.mapper.dto.request.UserLoginDTO;
import com.face.permission.common.constants.SystemConfig;
import com.face.permission.service.interfaces.user.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.context.request.async.DeferredResult;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.ParameterBuilder;
import springfox.documentation.schema.ModelRef;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.service.Parameter;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 *
 * @author xuyizhong
 * @version $Id: Swagger2
 */
@EnableSwagger2
@Configuration
public class Swagger2 {

	@Autowired
	IUserService userService;
	@Bean
	public Docket apiDocker() {
		Set<String> set = new HashSet<String>();
		set.add("application/x-www-form-urlencoded");
		set.add("application/json");

		List<Parameter> aParameters = new ArrayList<Parameter>();
		aParameters.add(buildLoginToken().build());
		aParameters.add(buildTraceId().build());     //
		aParameters.add(buildDeviceInfo().build());  //构建设备信息

		boolean isOpenSwagger = true;
		if (SystemConfig.isProMode()) {
			isOpenSwagger = false;
		}

		return new Docket(DocumentationType.SWAGGER_2).groupName("njia/v1").
				genericModelSubstitutes(DeferredResult.class).consumes(set).
				globalOperationParameters(aParameters).useDefaultResponseMessages(false).
				forCodeGeneration(true).pathMapping("/").select()// base，最终调用接口后会和paths拼接在一起
				.build().apiInfo(apiInfo()).enable(isOpenSwagger);
	}

	/**
	 * 构建请求接口的设备信息
	 */
	private ParameterBuilder buildDeviceInfo() {
		ParameterBuilder aParameterBuilder3 = new ParameterBuilder();
		JSONObject jsonObject = new JSONObject();
		jsonObject.put("lng", "11.111111");  //经度
		jsonObject.put("lat", "123.111111"); //纬度
		jsonObject.put("city", "zhejiang");  //城市名称
		jsonObject.put("chId", "yingyongbao"); //应用名称
		jsonObject.put("debd", "huawei");     //手机型号
		jsonObject.put("dml", "xmxiao");
		jsonObject.put("did", "12123123123");

		aParameterBuilder3.defaultValue(JSON.toJSONString(jsonObject));

		aParameterBuilder3.name("device").description("设备信息,不允许出现中文chId:渠道编码:应用宝,dml:设备型号,debd:设备品牌").modelRef(new ModelRef("String")).parameterType("header")
				.required(false).build();
		return aParameterBuilder3;
	}


	/**
	 *
	 * @return
	 */
	private ParameterBuilder buildTraceId() {
		ParameterBuilder aParameterBuilder2 = new ParameterBuilder();
		aParameterBuilder2.name("traceId").description("每次请求生成链路id,比如uuid，md5（时间戳+token）等)，必须给").modelRef(new ModelRef("String")).parameterType("header")
				.defaultValue(MD5.getMD5(String.valueOf(System.currentTimeMillis()))).required(false).build();
		return aParameterBuilder2;
	}


	/**
	 *
	 * @return
	 */
	private ParameterBuilder buildLoginToken() {
		ParameterBuilder aParameterBuilder = new ParameterBuilder();
		UserLoginDTO userLoginDTO = new UserLoginDTO();
		userLoginDTO.setLoginName("Admin123");
		userLoginDTO.setPassword("Admin123");
		TokenDTO token = userService.login(userLoginDTO);
		aParameterBuilder.name(WebConstant.TOKENDATA).allowMultiple(true)
				.description(
						"【ios/安卓】header登录认证信息{'token':'登录令牌'}")
				.modelRef(new ModelRef("String")).parameterType("header").required(true).build();

		aParameterBuilder.defaultValue(token.getToken());
		return aParameterBuilder;
	}

	private ApiInfo apiInfo() {
		return new ApiInfoBuilder().title("face-permission").description("face-用户管理").termsOfServiceUrl("http://blog.didispace.com/")
				.contact(new Contact("程序猿", null, null)).version("0.0.1").build();
	}

}
