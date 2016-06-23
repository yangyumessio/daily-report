package org.config.spring;



public class Test {

	private String driverClassName;
	private String driverUrl;
	
	public String getDriverClassName() {
		return driverClassName;
	}
	
	public void setDriverClassName(String name) {
		driverClassName = name;
	}
	
	public String getDriverUrl() {
		return driverUrl;
	}
	
	public void setDriverUrl(String url) {
		driverUrl = url;
	}
//	public static void main(String[] args) {
//	//	ApplicationContext ac =new ClassPathXmlApplicationContext("classpath:/config/app.xml");
//	//	Test t = (Test) ac.getBean("test");
//		System.out.println(t.getDriverClassName());
//	}
}
