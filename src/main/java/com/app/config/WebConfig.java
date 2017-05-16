package com.app.config;

import java.util.concurrent.TimeUnit;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.http.CacheControl;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.servlet.ViewResolver;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;
import org.springframework.web.servlet.view.InternalResourceViewResolver;

@Configuration
@EnableWebMvc 
@ComponentScan(basePackages = "com.app")
@PropertySource("classpath:application.properties")
public class WebConfig extends WebMvcConfigurerAdapter {
	
	@Autowired
	private Environment env;

	@Bean
	CacheControl resourcesCacheControl(){
		return CacheControl.maxAge(	env.getProperty("resource_max_age", Long.class), 
									TimeUnit.valueOf(env.getProperty("resource_time_unit")));
	}
	
	@Bean
	CacheControl webJarsCacheControl(){
		return CacheControl.maxAge(	env.getProperty("webjars_max_age", Long.class), 
									TimeUnit.valueOf(env.getProperty("webjars_time_unit")));
	}
	
	@Override
	public void addResourceHandlers(ResourceHandlerRegistry registry) {
		registry.addResourceHandler("/resources/**")
				.addResourceLocations("/resources/")
				.setCacheControl(resourcesCacheControl().cachePublic());
		registry.addResourceHandler("/webjars/**")
				.addResourceLocations("classpath:/META-INF/resources/webjars/")
				.setCacheControl(webJarsCacheControl().cachePublic());
	}

	@Bean
	public ViewResolver viewRsolver() {
		InternalResourceViewResolver resolver = new InternalResourceViewResolver();
		resolver.setPrefix("/WEB-INF/views/");
		resolver.setSuffix(".jsp");
		return resolver;
	}
	
	@Bean
	public PasswordEncoder getDefaultPasswordEncoder() throws ClassNotFoundException, InstantiationException, IllegalAccessException{
		Class<?> clazz = Class.forName(env.getProperty("encoder_class"));
		return (PasswordEncoder) clazz.newInstance();
	}
}