package id.co.amqp.demo;

import javax.annotation.PostConstruct;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.connection.CorrelationData;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PublisherConfirmProducer {

	private static final Logger log = LoggerFactory.getLogger(PublisherConfirmProducer.class);

	@Autowired
	private RabbitTemplate rabbitTemplate;

	@PostConstruct
	private void postConstruct() {
		this.rabbitTemplate.setConfirmCallback((correlation, ack, reason) -> {
			if (correlation != null) {
				log.info("Received " + (ack ? " ack " : " nack ") + "for correlation: " + correlation);
			}
		});

		this.rabbitTemplate.setReturnCallback((message, replyCode, replyText, exchange, routingKey) -> {
			log.info("Returned: " + message + "\nreplyCode: " + replyCode + "\nreplyText: " + replyText
					+ "\nexchange/rk: " + exchange + "/" + routingKey);
		});
	}

	// not ack-ed (ack = false)
	public void sendMessage_InvalidExchange(DummyMessage message) {
		CorrelationData correlationData = new CorrelationData("Correlation for message " + message.getContent());
		this.rabbitTemplate.convertAndSend("not-valid-exchange", "any-routing-key", message, correlationData);
	}

	// Careful : will be silently dropped, since the exchange is exists, but no
	// route to queue, but ack-ed
	public void sendMessage_ValidExchange_InvalidQueue(DummyMessage message) {
		CorrelationData correlationData = new CorrelationData("Correlation for message " + message.getContent());
		this.rabbitTemplate.convertAndSend("x.test", "not-valid-routing-key", message, correlationData);
	}

	// ack-ed (ack = true)
	public void sendMessage_ValidExchange_ValidQueue(DummyMessage message) {
		CorrelationData correlationData = new CorrelationData("Correlation for message " + message.getContent());
		this.rabbitTemplate.convertAndSend("x.test", "test", message, correlationData);
	}

}
