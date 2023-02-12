## kafka-error-handling
Kafka Application which uses main topic and retry topic, and h2 database to retry and handle the error.

The messages are produced and sent to a Kafka topic.
The messages are consumed by a Kafka consumer that is part of the application.
The consumer processes the messages and performs the desired business logic.
If the consumer is unable to process a message successfully, it publishes the failed message to a "retry" topic.
A separate Kafka consumer, known as the retry consumer, is listening to the "retry" topic.
The retry consumer receives the failed message and tries to process it again.
If the retry consumer is unable to process the message successfully, it stores the message in a SQL database using JPA.


### Versions:
Spring boot: 3.0.2
Apache Kafka: 3.3.2


### To start:
Run the KafkaMainApplication and KafkaRetryApplication as main spring boot application.

### Request endpoint
### POST:
http://localhost:8080/publish

### BODY:
#### Request 1
{
"eventType": "NEWM",
"message": "NEW message consumed."
}
