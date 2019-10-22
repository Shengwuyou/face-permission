package com.face.permission.server.config;

import com.alibaba.druid.pool.DruidDataSource;
import com.aliyun.oss.ClientException;
import com.aliyun.oss.OSSClient;
import com.aliyun.oss.OSSException;
import com.face.permission.common.utils.LoggerUtil;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.logging.log4j.Logger;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.context.EnvironmentAware;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.io.IOException;

/**
 * @Description
 * @Author xuyizhong
 * @Date 2019-07-08 11:09
 */
@Configuration
@PropertySource(value = {"classpath:/env/application-${spring.profiles.active}.properties"})
@MapperScan(basePackages = {"com.face.permission.mapper.dao"}, annotationClass = Repository.class, sqlSessionFactoryRef = "sqlSessionFactoryBean")
public class DateSourceConfig implements EnvironmentAware {

    private Logger logger = LoggerUtil.COMMON_DEFAULT;

    private Environment environment;

    @Override
    public void setEnvironment(Environment environment) {
        this.environment = environment;
    }



    //======> 数据库管理工具的操作步骤 <============
    // 1 创建数据源 dataSource
    // 2 sqlSessionFactory，每次请求获取一个sqlsession


    @Bean(name = "dataSource")
    @Primary
    public DataSource dataSource(){
        DruidDataSource dataSource = new DruidDataSource();
        dataSource.setUrl(environment.getProperty("db.url"));
        dataSource.setUsername(environment.getProperty("db.username"));
        dataSource.setPassword(environment.getProperty("db.password"));
        dataSource.setDriverClassName(environment.getProperty("db.driverClass"));
        return  dataSource;
    }

    @Bean(name = "sqlSessionFactoryBean")
    @Primary
    public SqlSessionFactoryBean sqlSessionFactoryBean(){
        SqlSessionFactoryBean sqlSessionFactoryBean = new SqlSessionFactoryBean();
        sqlSessionFactoryBean.setDataSource(dataSource());
        PathMatchingResourcePatternResolver matchingResourcePatternResolver = new PathMatchingResourcePatternResolver();
        sqlSessionFactoryBean.setConfigLocation(matchingResourcePatternResolver.getResource("classpath:mybatis-config.xml"));

        Resource[] resources = null;
        try {
            resources = matchingResourcePatternResolver.getResources("classpath:mapper/*.xml");
        } catch (IOException e) {
            e.printStackTrace();
        }
        sqlSessionFactoryBean.setMapperLocations(resources);
        return sqlSessionFactoryBean;
    }


    @Bean
    @Primary
    public DataSourceTransactionManager dataSourceTransactionManager() {
        DataSourceTransactionManager dataSourceTransactionManager = new DataSourceTransactionManager(dataSource());
        return dataSourceTransactionManager;
    }



    @Bean(name = "ossClient")
    @Primary
    public OSSClient ossClientInit() {

        OSSClient ossClient = new OSSClient(environment.getProperty("oss.endpoint"),
                environment.getProperty("oss.accessKeyId"),
                environment.getProperty("oss.accessKeySecret"));
        try {
            String bucketName = environment.getProperty("oss.bucketName");
            // 判断Bucket是否存在。详细请参看“SDK手册 > Java-SDK > 管理Bucket”。
            // 链接地址是：https://help.aliyun.com/document_detail/oss/sdk/java-sdk/manage_bucket.html?spm=5176.docoss/sdk/java-sdk/init
            if (ossClient.doesBucketExist(bucketName)) {
                logger.info("您已经创建Bucket：" + bucketName + "。");
            } else {
                logger.info("您的Bucket不存在，需要先创建Bucket：" + bucketName + "。");
                // 创建Bucket。详细请参看“SDK手册 > Java-SDK > 管理Bucket”。
                // 链接地址是：https://help.aliyun.com/document_detail/oss/sdk/java-sdk/manage_bucket.html?spm=5176.docoss/sdk/java-sdk/init
                // ossClient.createBucket(bucketName);
            }

        } catch (OSSException oe) {
            oe.printStackTrace();
        } catch (ClientException ce) {
            ce.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            logger.info("OSSClient初始化成功");
        }
        return ossClient;
    }

    @Bean(name = "bucketName")
    public String bucketName() {
        return environment.getProperty("oss.bucketName");
    }


}
