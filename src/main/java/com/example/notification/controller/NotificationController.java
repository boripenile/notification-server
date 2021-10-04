package com.example.notification.controller;

import java.util.UUID;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.example.notification.controller.dto.SubscribeDTO;
import com.example.notification.events.ConsumerEventManager;
import com.example.notification.events.ProducerEventManager;

@RestController
public class NotificationController {

	private final ProducerEventManager producerEvent;
	private final ConsumerEventManager consumerEvent;

	@Autowired
	public NotificationController(ProducerEventManager producerEvent, ConsumerEventManager consumerEvent) {
		this.producerEvent = producerEvent;
		this.consumerEvent = consumerEvent;
	}
	
	@PostMapping(value = "/publish/{topic}", consumes = "application/json", 
			produces = "application/json")
	public ResponseEntity<String> publishMessageToTopic(@PathVariable(name = "topic")String topicName, 
			@RequestBody String message) {
		JSONObject json = new JSONObject(message);
		producerEvent.publish(topicName, UUID.randomUUID().toString(), 
				json.toString().getBytes());
//		String response = "Message published successfully";
//		JSONObject resultAfterPublish = new JSONObject();
//		resultAfterPublish.put("message", response);
		
//		if (response != null) {
//			return ResponseEntity.status(HttpStatus.OK)
//					.body(response.toString());
//		}
		return ResponseEntity.status(HttpStatus.OK)
				.body(new JSONObject().put("message", "Message published successfully").toString());
	}
	
	@PostMapping(value = "/subscribe/{topic}", consumes = "application/json", 
			produces = "application/json")
	public ResponseEntity<String> subscribeToTopic(@PathVariable(name = "topic")String topicName, 
			@RequestBody SubscribeDTO subscribe) {
		JSONObject response = consumerEvent.subscribe(topicName, subscribe.getUrl());
		if (response != null) {
			return ResponseEntity.status(HttpStatus.OK)
					.body(response.toString());
		}
		return ResponseEntity.status(HttpStatus.BAD_REQUEST)
				.body(new JSONObject().put("message", "Subscription failed for topic: " + topicName).toString());
	}
}
