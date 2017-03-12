package com.thinkgem.jeesite;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ImportResource;
import org.springframework.scheduling.annotation.EnableScheduling;

/**
 * Created by wangganggang on 2017/1/1.
 */
@ImportResource({"classpath:applicationContext.xml",
        "classpath:spring-context.xml",
        "classpath:mybatis-config.xml",
        "classpath:spring-context-activiti.xml",
        "classpath:spring-context-shiro.xml"})
@SpringBootApplication
@EnableScheduling
public class Application {

    private static final Logger logger = LoggerFactory.getLogger(Application.class);

    /**
     * @param args
     */
    public static void main(String[] args) {
        SpringApplication.run(Application.class,args);
        logger.info("The program can be stoped using <ctrl>+<c>");
    }
}
