# Reservation System

This repository contains two Spring Boot services for managing reservations.

## Services

1. **reservation-receiver**
   - This service provides a REST API with a single endpoint `POST: /send-reservation` where you can send plain text messages. It also starts a message queue to handle incoming reservations.

2. **reservation-processor**
   - This service is responsible for processing reservations received via the message queue.

## Queue Broker

For message queuing, `ActiveMQ Artemis` is used as the queue broker.

- **Download:** [ActiveMQ Artemis](https://activemq.apache.org/components/artemis/download/)
- **Queue Name:** reservations-queue
- **Credentials:** admin/admin
- **URL:** http://localhost:8161/console/artemis/artemisStatus?nid=root-org.apache.activemq.artemis-0.0.0.0

To start the queue broker, follow these steps:

1. Download the archive from the provided link.
2. Navigate to the `bin` folder and execute: `./artemis create hotelbroker` to create your own profile.
3. Navigate to the created profile folder (e.g., `cd hotelbroker/bin`) and execute `./artemis run` to start Artemis.

For more detailed instructions, refer to the [ActiveMQ Artemis Documentation](https://activemq.apache.org/components/artemis/documentation/).

## Local Testing

To test the entire system locally, follow these steps:

1. Start the `reservation-receiver` service. Once it's up, you can send POST requests to `http://localhost:8080/send-reservation` with plain text in the body.
2. Start the ActiveMQ Artemis queue.
3. Start the `reservation-processor` service.
4. Use Postman or curl to send test requests.


## Developer Notes

1. **Reservation Model:**
   - The `Reservation` model, serving as the main DTO, currently contains `message` and `timestamp` fields. Additional fields such as `reservationId` and `correlationId` could be added for further processing, filtering, or notification purposes. The `timestamp` field is utilized for sorting messages with the `compareTo` method. While the current implementation uses the time when the message is received, alternative timestamps (e.g., from the frontend) could be considered.

2. **Reservation Buffer:**
   - A `ReservationBuffer` was introduced to fulfill the requirement of processing messages in the right order. Sorting/grouping messages before processing posed a challenge. One approach employed is to use a buffer with a fixed capacity and a flushing time period. Messages are collected in the buffer, and before flushing, they are added to a `TreeSet` to ensure sorting by timestamp. This allows for processing messages in the correct order. Parameters such as `capacity` and `flushingTime` can be adjusted to optimize performance. A drawback of this method is that the buffer is flushed and runs even if there are no messages in the queue.

3. **Concurrency Settings:**
   - Concurrency settings for the processor can be configured in the `JMSListener` or listener connection factory to fetch messages from the queue in parallel. This can improve processing efficiency, especially in scenarios with high message volumes.


