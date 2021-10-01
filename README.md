# Notification System - Publisher (Server)

## Tools Used

__Apache Kafka__

__Zookeeper__

__Docker compose__ 


## Functionalities

Publisher of the Notification system will be responsible of the following:

1. Provides subscription capability to clients who are interested in subscribing to the notification service.

2. Publishes information to every subscribed client.

## How it works

__In order to achieve the functionalities of the Publisher, this following approach was adopted in the implementation.__

__1. Publisher exposes an api endpoint that will be used by clients for subscription. A client will subscribe to a topic (which is a category of service) providing its url which will receive notifications sent out by the publisher to the topic.__

	Subscription url of the publisher is:
	
	http://localhost:8000/subscribe/topic1

	Sample deployed clients' urls used to test with the publisher are: 
	
	a. http://localhost:9000/test1
	b. http://localhost:9001/test2
	
	Example of request body:
	{
	  "url": "http://localhost:9000/test1"
	}

	Sample topic is: topic1

__2. Publisher publishes information to the topic and the information will be sent to all subscriber to the topic. The information will also be available to new subscribers.__

	Publishing url of the publisher that will be used to publish information to the topic is:
	http://localhost:8000/publish/topic1
	
	Examples of request body:
	
	{
	  "developerName": "Murtadha Ali",
	  "technologyUsed": "Apache Kafka",
	  "programmingLanguageUsed": "Java Programming",
	  "frameworkUsed": "SpringBoot"
	}
	
	{
	  "schoolName": "Programming language",
	  "principleAdopted": "Programming language is a tool not the solution",
	  "programmingLanguageUsed": "Java Programming",
	  "frameworkUsed": "SpringBoot"
	}

	Publisher publishes the information in the following format to the topic waiting for clients to pick up:
	
	{
	  "topic": "topic1",
	  "data": {
		  "developerName": "Murtadha Ali",
		  "technologyUsed": "Apache Kafka",
		  "programmingLanguageUsed": "Java Programming",
		  "frameworkUsed": "SpringBoot"
		}
	}
	
	{
	  "topic": "topic1",
	  "data": {
		  "schoolName": "Programming language",
		  "principleAdopted": "Programming language is a tool not the solution",
		  "programmingLanguageUsed": "Java Programming",
		  "frameworkUsed": "SpringBoot"
		}
	}
	
__3. On the client application: The client will invoke its subscribed url.__

## Requirements

The following are required to run this project:

1. Java 8 runtime
2. Docker compose
3. Maven

## How to run

1. Clone the repository

__a. Start the docker compose__

2. Run `cd notification-server`

3. Run `cd docker`

4. Run `docker-compose up` or `docker-compose up -d`

__b. Run the publisher server__

5. Navigate to the notification-server folder

6. If on linux system, run `./mvnw spring-boot:run`

7. If on window system, run `mvnw.cmd spring-boot:run`

The publisher server will be running on port 8000.

## Test

_1. Run the following command to subscribe a client's url_

`curl -X POST -H "Content-Type: application/json" -H "Accept: application/json" -d '{"url": "http://localhost:9000/test1"}' http://localhost:8000/subscribe/topic1`

Expected response

`{"topic":"topic1","url":"http://localhost:9000/test1"}`

_2. Run the following command to publish information to clients_

`curl -X POST -H "Content-Type: application/json" -H "Accept: application/json" -d '{"greeting": "Hello there!"}' http://localhost:8000/publish/topic1`

Expected response

`{"message":"Message published successfully"}`

_3. To retrieve the data on the client end, start the client application and run the following command_

`curl -X GET -H "Content-Type: application/json" http://localhost:9000/test1`

Expected response

`{"data":{"greeting":"Hello there!"},"topic":"topic1"}`




