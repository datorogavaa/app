package com.application;

//import com.application.model.Home;
//import com.application.model.User;
//import com.application.repository.UserRepository;
//import com.application.service.HomeService;
//import com.application.service.UserService;
//import com.application.smsverification.SmsVerification;
import org.springframework.boot.ApplicationContextFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;

@SpringBootApplication
public class Application {



	public static void main(String[] args) throws InterruptedException {
		ApplicationContext context = SpringApplication.run(Application.class, args);

	}
}
