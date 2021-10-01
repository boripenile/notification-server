package com.pangaea.notification.events;

import java.util.Base64;
import java.util.Collections;
import java.util.Properties;

import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.pangaea.notification.kafka.CustomConsumerProperties;

@Service
public class ConsumerEventManager {

	private static final Logger logger = LoggerFactory.getLogger(ConsumerEventManager.class);
	
	Properties consumerProperties = null;
	
	@Autowired
	private CustomConsumerProperties values;
	
	private KafkaConsumer<String, byte[]> consumerTemplate;
	
	public JSONObject subscribe(final String topic, final String url) {
		try {
			consumerProperties = new Properties();
			consumerProperties.put("bootstrap.servers", values.getBootstrapServers());
			consumerProperties.put("max.poll.records", values.getMaxPollRecords());
			consumerProperties.put("key.deserializer", values.getKeyDeserializer());
			consumerProperties.put("value.deserializer", values.getValueDeserializer());
			consumerProperties.put("auto.offset.reset", values.getAutoOffsetReset());
			consumerProperties.put("fetch.min.bytes", values.getFetchMinBytes());
			consumerProperties.put("fetch.max.wait.ms", values.getFetchMaxWaitMs());
			consumerProperties.put("heartbeat.interval.ms", values.getHeartbeatIntervalMs());
			consumerProperties.put("max.poll.interval.ms", values.getMaxPollIntervalMs());
			consumerProperties.put("max.partition.fetch.bytes", values.getMaxPartitionFetchBytes());
			consumerProperties.put("session.timeout.ms", values.getSessionTimeoutMs());
			consumerProperties.put("enable.auto.commit", values.isEnableAutoCommit());
			consumerProperties.put("auto.commit.interval.ms", values.getAutoCommitIntervalMs());
			consumerProperties.put("isolation.level", values.getIsolationLevel());
			String groupId = "notify-group-"+Base64.getEncoder().encodeToString(url.getBytes());
			consumerProperties.put("group.id", groupId);
			consumerProperties.put("group.instance.id", Base64.getEncoder().encodeToString(url.getBytes()));
			consumerTemplate = new KafkaConsumer<String, byte[]>(consumerProperties);
			
			consumerTemplate.subscribe(Collections.singletonList(topic));
			logger.info("=> Consumer at url: {} subscribed to topic: {}", url, topic);
			return new JSONObject().put("topic", topic).put("url", url);
		} catch (Exception e) {
			// TODO: handle exception
		}
		return null;
	}
	
}
