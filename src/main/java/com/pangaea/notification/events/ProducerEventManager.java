package com.pangaea.notification.events;

import java.nio.charset.StandardCharsets;

import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Service;
import org.springframework.util.concurrent.ListenableFuture;
import org.springframework.util.concurrent.ListenableFutureCallback;

@Service
public class ProducerEventManager {
	private static final Logger logger = LoggerFactory.getLogger(ProducerEventManager.class);

	@Autowired
	private KafkaTemplate<String, byte[]> producerTemplate;

	public void publish(final String topic, final String key, final byte[] data) {
		JSONObject publishData = new JSONObject();
		publishData.put("topic", topic);
		publishData.put("data", new JSONObject(new String(data, StandardCharsets.UTF_8)));
		ListenableFuture<SendResult<String, byte[]>> futureRecord = this.producerTemplate.send(topic, 
				key, publishData.toString().getBytes());
		futureRecord.addCallback(new ListenableFutureCallback<SendResult<String, byte[]>>() {

			@Override
			public void onSuccess(SendResult<String, byte[]> result) {
				logger.info("Message sent: key='{}' with offset={}, message={}", key,
						result.getRecordMetadata().offset(), new String(publishData.toString().getBytes(), 
								StandardCharsets.UTF_8));
			}

			@Override
			public void onFailure(Throwable ex) {
				logger.error("Kafka unable to send message: key='{}' error={}", key, ex);
			}

		});
		//return new JSONObject().put("message", "Message published successfully");
	}

}
