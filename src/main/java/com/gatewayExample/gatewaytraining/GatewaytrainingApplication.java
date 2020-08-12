package com.gatewayExample.gatewaytraining;

import org.jpos.iso.ISOUtil;
import org.jpos.q2.Q2;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.autoconfigure.jdbc.DataSourceTransactionManagerAutoConfiguration;
import org.springframework.boot.autoconfigure.orm.jpa.HibernateJpaAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;

@SpringBootApplication
public class GatewaytrainingApplication {
	// creating a bean for restAPI
	@Bean
	public RestTemplate getRestTemplate(){
		SimpleClientHttpRequestFactory factory = new SimpleClientHttpRequestFactory();
		factory.setConnectTimeout(3000);// setting timeouts
		factory.setReadTimeout(10000);//reading the timeout
		return new RestTemplate(factory);
	}

	private static void startJPosServer(){
		JPosServer.getInstance();
	}

	public static void main(String[] args) {
		SpringApplication.run(GatewaytrainingApplication.class, args);
		startJPosServer();

		System.out.println("------------- Gateway started successfully ------------------");
	}

}

class JPosServer {
	private static volatile JPosServer instance = null;

	static void getInstance(){
		if (instance == null) {
			synchronized (JPosServer.class){
				if (instance == null) {
					instance = new JPosServer();
				}
			}
		}
	}

	private JPosServer(){
		try {
			new Q2("config/jpos").start();
			ISOUtil.sleep(2000);
		} catch (Exception e){
			e.printStackTrace();
		}
	}
}
