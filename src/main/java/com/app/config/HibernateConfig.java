package com.app.config;

import java.util.Properties;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.orm.hibernate5.LocalSessionFactoryBean;

@Configuration
@PropertySource({ "classpath:application.properties" })
public class HibernateConfig {

	@Autowired
	private Environment env;

	@Bean
	public LocalSessionFactoryBean sessionFactory() {
		LocalSessionFactoryBean sessionFactory = new LocalSessionFactoryBean();
		sessionFactory.setDataSource(restDataSource());
		sessionFactory.setPackagesToScan(new String[] { "com.app.dto" });
		sessionFactory.setHibernateProperties(hibernateProperties());
		return sessionFactory;
	}

	@Bean
	public DataSource restDataSource() {
		DriverManagerDataSource dataSource = new DriverManagerDataSource();
		dataSource.setDriverClassName(env.getProperty("jdbc.driverClassName"));
		dataSource.setUrl(env.getProperty("jdbc.url"));
		dataSource.setUsername(env.getProperty("jdbc.user"));
		dataSource.setPassword(env.getProperty("jdbc.pass"));
		return dataSource;
	}

	private Properties hibernateProperties() {
		Properties properties = new Properties();
		properties.put("hibernate.dialect", env.getRequiredProperty("hibernate.dialect"));
		properties.put("hibernate.show_sql", env.getRequiredProperty("hibernate.show_sql"));
		properties.put("hibernate.format_sql", env.getRequiredProperty("hibernate.format_sql"));
		properties.put("hibernate.temp.use_jdbc_metadata_defaults", env.getRequiredProperty("hibernate.temp.use_jdbc_metadata_defaults"));

		properties.put("hibernate.c3p0.max_size", env.getRequiredProperty("hibernate.c3p0.max_size"));
		properties.put("hibernate.c3p0.min_size", env.getRequiredProperty("hibernate.c3p0.min_size"));
		properties.put("hibernate.c3p0.initial_pool_size", env.getRequiredProperty("hibernate.c3p0.initial_pool_size"));
		properties.put("hibernate.c3p0.min_pool_size", env.getRequiredProperty("hibernate.c3p0.min_pool_size"));
		properties.put("hibernate.c3p0.max_pool_size", env.getRequiredProperty("hibernate.c3p0.max_pool_size"));
		properties.put("hibernate.c3p0.timeout", env.getRequiredProperty("hibernate.c3p0.timeout"));
		properties.put("hibernate.c3p0.max_statements", env.getRequiredProperty("hibernate.c3p0.max_statements"));

		properties.put("hibernate.hbm2ddl.auto", env.getRequiredProperty("hibernate.hbm2ddl.auto"));

		properties.put("hibernate.cache.provider_class", env.getRequiredProperty("hibernate.cache.provider_class"));
		properties.put("hibernate.cache.region.factory_class", env.getRequiredProperty("hibernate.cache.region.factory_class"));
		properties.put("hibernate.cache.use_query_cache", env.getRequiredProperty("hibernate.cache.use_query_cache"));
		properties.put("hibernate.cache.use_second_level_cache", env.getRequiredProperty("hibernate.cache.use_second_level_cache"));

		return properties;
	}

}