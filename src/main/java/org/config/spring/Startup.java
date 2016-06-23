package org.config.spring;

import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * Created by yangyu on 2016/6/2.
 */
public class Startup {
    public static void main(String[] args) {
        new ClassPathXmlApplicationContext("classpath:/config/applicationContext.xml");
    }
}