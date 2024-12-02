package com.swiftpay;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class SwiftPayApplication {

	public static void main(String[] args) {
		SpringApplication.run(SwiftPayApplication.class, args);
	}
}

// implement ajax queries on sending money
// make transfer link agency sent and make payable transfer if only agency is different
// replace all exchange response in api controller to use ExchangeUtils, use ExchangeUtils in transfer service
//and delete all unnecessary code