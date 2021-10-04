package com.example.notification;

import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.UUID;

import org.json.JSONObject;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import com.example.notification.controller.NotificationController;
import com.example.notification.events.ConsumerEventManager;
import com.example.notification.events.ProducerEventManager;


@WebMvcTest({ NotificationController.class })
public class NotificationControllerTest {

	@Autowired
	MockMvc mockMvc;
	
	@MockBean
	ConsumerEventManager consumerEventManager;
	
	@MockBean
	ProducerEventManager producerEventManager;
	
	final String TOPIC = "topic1";
	final String SUBSCRIBER_ONE = "http://localhost:9000/test1";
	final String SUBSCRIBER_TWO = "http://localhost:9001/test2";
	
	@Test
	public void testSubscribeReturnTopicAndSubscriberUrlIfSubscribed() throws Exception{
		JSONObject requestBody = new JSONObject();
		requestBody.put("url", SUBSCRIBER_ONE);
		
		JSONObject resultAfterSubscription = new JSONObject();
		resultAfterSubscription.put("topic", TOPIC);
		resultAfterSubscription.put("url", SUBSCRIBER_ONE);
		
		Mockito.when(consumerEventManager.subscribe(TOPIC, SUBSCRIBER_ONE))
				.thenReturn(resultAfterSubscription);
		
		MockHttpServletRequestBuilder mockRequest 
			= MockMvcRequestBuilders.post("/subscribe/"+TOPIC)
			  .contentType(MediaType.APPLICATION_JSON)
			  .accept(MediaType.APPLICATION_JSON)
			  .content(requestBody.toString());
		
		mockMvc.perform(mockRequest)
				.andExpect(status().isOk())
				.andExpect(jsonPath("$", notNullValue()))
				.andExpect(jsonPath("$.url", is("http://localhost:9000/test1")));
	}
	
	@Test
	public void testPublishReturnMessageSuccessfulIfPublished() throws Exception{
		JSONObject requestBody = new JSONObject();
		requestBody.put("name", "Murtadha Ali");
		requestBody.put("greeting", "How are you guys?");
		
		String key = UUID.randomUUID().toString();
		
		String messageText = "Message published successfully";
		JSONObject resultAfterPublish = new JSONObject();
		resultAfterPublish.put("message", messageText);
		
		Mockito.doNothing().when(producerEventManager)
			.publish(TOPIC, key, requestBody.toString().getBytes());
		
		MockHttpServletRequestBuilder mockRequest 
			= MockMvcRequestBuilders.post("/publish/"+TOPIC)
			  .contentType(MediaType.APPLICATION_JSON)
			  .accept(MediaType.APPLICATION_JSON)
			  .content(requestBody.toString());
		
		mockMvc.perform(mockRequest)
				.andExpect(status().isOk())
				.andExpect(jsonPath("$", notNullValue()))
				.andExpect(jsonPath("$.message", is("Message published successfully")));
	}
}
