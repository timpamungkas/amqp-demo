package id.co.amqp.demo;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;

@Service
public class TestListener {

	private static final Logger log = LoggerFactory.getLogger(TestListener.class);

	@RabbitListener(queues = "q.test")
	public void listen(DummyMessage message) {
		log.info("Listening : {}", message);
	}

}
