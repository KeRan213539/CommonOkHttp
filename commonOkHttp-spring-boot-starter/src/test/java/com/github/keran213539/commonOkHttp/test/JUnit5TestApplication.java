package com.github.keran213539.commonOkHttp.test;

import org.springframework.boot.WebApplicationType;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.logging.LoggingSystem;


/**
 * @ClassName: JUnit5TestApplication
 * @Description: 测试用的启动器
 * @author klw
 * @date 2019年4月15日 上午9:43:54
 */
@SpringBootApplication
public class JUnit5TestApplication {

    public static void main(String[] args) {
	System.setProperty("org.springframework.boot.logging.LoggingSystem", LoggingSystem.NONE);  // 彻底关闭 spring boot 自带的 LoggingSystem
	new SpringApplicationBuilder(JUnit5TestApplication.class)
        .web(WebApplicationType.NONE) // 非 Web 应用
        .run(args);
    }
    
}
