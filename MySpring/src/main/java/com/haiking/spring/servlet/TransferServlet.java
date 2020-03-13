package com.haiking.spring.servlet;

import com.haiking.spring.service.TransferService;
import com.haiking.spring.utils.AnnotationApplicationContext;

public class TransferServlet {

	public static void main(String[] args) throws Exception {
		AnnotationApplicationContext  app = new AnnotationApplicationContext ("com.haiking.spring");
		
		TransferService userService =  (TransferService) app.getBean("transferServiceImpl");
		String fromCardNo ="6221234", toCardNo="6227890";
		double money = 100D;
		userService.transfer(fromCardNo, toCardNo, money);
		
	}

}
