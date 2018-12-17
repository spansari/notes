package com.macys.mtech.notes.controller;


import java.time.LocalTime;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import com.jayway.jsonpath.JsonPath;

/**
 * Created by Sanjiv Pansari on 10/02/18.
 */

@RestController
public class IndexController {
	
    @Autowired
    private KafkaTemplate<String, String> kafkaTemplate;

    @Autowired
	RestTemplate restTemplate;

    
    @Value("${config-service-url}")
    private String configServiceUrl;
    
    @Value("${hello-service-url}")
    private String helloServiceUrl;

	@Value("${spring.datasource.username}")
	private String dbUser;

	@Value("${messaging.config}")
	private String messagingConfig;

	@GetMapping("/ping")
    public String ping() {
    	return "OK: Notes Service is working fine";
    }
    
    @GetMapping("/hello")
    public String sayHello() {
    	ResponseEntity<String> response
    	  = restTemplate.getForEntity(helloServiceUrl, String.class);
    	
    	return response.getBody() + " : " + dbUser + " : ";
    }

    
    @GetMapping("/publish/hello")
    public String publishHello() {
    	String message = "hello Sanjiv:"+ LocalTime.now();

    	System.out.println("messagingConfig:"+ messagingConfig);
    
    	String enablePublish = JsonPath.parse(messagingConfig).read("$.enablePublish");
    	
    	if ("Yes".equalsIgnoreCase(enablePublish)) {
    		String topicName = JsonPath.parse(messagingConfig).read("$.topicName");
    		System.out.println("topicName:"+ topicName);	
    		//sendMessage(topicName, message);
    	}
    	
    	return message;
    }
    
    @PostMapping("/publish/message")
    public String publishMessage(@Valid @RequestBody String message) {

    	ResponseEntity<String> response
    	  = restTemplate.getForEntity(configServiceUrl + "/application/application/profile/default/label/master/key/messaging.config", String.class);
    	
    	String configStr = response.getBody();
    	System.out.println("configStr:"+ configStr);

    	String enablePublish = JsonPath.parse(configStr).read("$.enablePublish");
    	
    	if ("Yes".equalsIgnoreCase(enablePublish)) {
    		String topicName = JsonPath.parse(configStr).read("$.topicName");
    		System.out.println("topicName:"+ topicName);	
    		sendMessage(topicName, message);
    	}
    	
    	return response.getBody();
    }
    
     
    private void sendMessage(String topicName, String msg) {
        kafkaTemplate.send(topicName, msg);
    }
}
