package id.co.amqp.demo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class DemoApplication implements CommandLineRunner {

	public static void main(String[] args) {
		SpringApplication.run(DemoApplication.class, args);
	}

	@Autowired
	private PublisherConfirmProducer producer;

	@Override
	public void run(String... args) throws Exception {
		var dummyMessage_1 = new DummyMessage("Message 1", 1);
		producer.sendMessage_ValidExchange_ValidQueue(dummyMessage_1);

		var dummyMessage_2 = new DummyMessage("Message 2", 2);
		producer.sendMessage_ValidExchange_InvalidQueue(dummyMessage_2);
	}

}
