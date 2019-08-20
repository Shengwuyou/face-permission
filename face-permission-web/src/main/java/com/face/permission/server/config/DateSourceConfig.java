package com.face.permission.server.config;

import com.alibaba.druid.pool.DruidDataSource;
import org.apache.ibatis.session.SqlSessionFactory;
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
}
