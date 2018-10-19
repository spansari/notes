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

    
    @Value("${config-service-url}")
    private String configServiceUrl;
    
    @GetMapping("/ping")
    public String ping() {
    	return "OK: Notes Service is working fine";
    }
    
    @GetMapping("/publish/hello")
    public String publishHello() {
    	RestTemplate restTemplate = new RestTemplate();
    	
    	String message = "hello Sanjiv:"+ LocalTime.now();
    	ResponseEntity<String> response
    	  = restTemplate.getForEntity(configServiceUrl + "/app/BACKSTAGE/module/PUBSUB/configType/GLOBAL/configKey/MESSAGING", String.class);
    	
    	String configStr = response.getBody();
    	System.out.println("configStr:"+ configStr);
    
    	
    	String configValue = JsonPath.parse(configStr).read("$.configValue");
    	System.out.println("configValue:"+ configValue);
    	
    	String enablePublish = JsonPath.parse(configValue).read("$.enablePublish");
    	
    	if ("Yes".equalsIgnoreCase(enablePublish)) {
    		String topicName = JsonPath.parse(configValue).read("$.topicName");
    		System.out.println("topicName:"+ topicName);	
    		sendMessage(topicName, message);
    	}
    	
    	return message;
    }
    
    @PostMapping("/publish/message")
    public String publishMessage(@Valid @RequestBody String message) {
    	RestTemplate restTemplate = new RestTemplate();
    	
    	ResponseEntity<String> response
    	  = restTemplate.getForEntity(configServiceUrl + "/app/BACKSTAGE/module/PUBSUB/configType/GLOBAL/configKey/MESSAGING", String.class);
    	
    	String configStr = response.getBody();
    	System.out.println("configStr:"+ configStr);
    
    	
    	String configValue = JsonPath.parse(configStr).read("$.configValue");
    	System.out.println("configValue:"+ configValue);
    	
    	String enablePublish = JsonPath.parse(configValue).read("$.enablePublish");
    	
    	if ("Yes".equalsIgnoreCase(enablePublish)) {
    		String topicName = JsonPath.parse(configValue).read("$.topicName");
    		System.out.println("topicName:"+ topicName);	
    		sendMessage(topicName, message);
    	}
    	
    	return response.getBody();
    }
    
     
    private void sendMessage(String topicName, String msg) {
        kafkaTemplate.send(topicName, msg);
    }
}
