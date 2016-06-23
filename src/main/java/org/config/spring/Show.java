package org.config.spring;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class Show {

	
	public static void main(String[] args) {
		ApplicationContext ac = new ClassPathXmlApplicationContext("classpath:/config/applicationContext.xml");
		Test t = (Test) ac.getBean("test");
		System.out.println(t.getDriverClassName());
		System.out.println(t.getDriverUrl());
	}
}
