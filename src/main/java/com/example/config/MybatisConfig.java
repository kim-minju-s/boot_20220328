package com.example.config;

import javax.sql.DataSource;

import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;

// 환경설정 파일
// 실제적으로 필요없는 파일이나
// mapper를 xml로 사용하기 위해서 설정
// mapper를 현재는 inerface 방식으로 많이 사용
@Configuration
public class MybatisConfig {
    
    // 서버가 구동되기 전에 만들어지는 객체
    @Bean
    public SqlSessionFactory sqlSessionFactory(DataSource dataSource) throws Exception {
		System.out.println("datasource configuration");
		SqlSessionFactoryBean sqlSessionFactoryBean = new SqlSessionFactoryBean();
		sqlSessionFactoryBean.setDataSource(dataSource);
		
		//mappers 위치 설정
		Resource[] arrResource = new PathMatchingResourcePatternResolver()
            .getResources("classpath:/mappers/*Mapper.xml");
		sqlSessionFactoryBean.setMapperLocations(arrResource);
		return sqlSessionFactoryBean.getObject();
	}

}
