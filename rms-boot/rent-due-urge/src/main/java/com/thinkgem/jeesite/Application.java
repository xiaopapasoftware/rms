package com.thinkgem.jeesite;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ImportResource;

/**
 * Created by wangganggang on 2017/1/1.
 */
@ImportResource(locations = "*.xml")
@SpringBootApplication(scanBasePackages = {"com.thinkgem.jeesite"})
@EnableAutoConfiguration()
public class Application {


    /**
     * @param args
     */
    public static void main(String[] args) {
        SpringApplication.run(Application.class,args);
    }
}