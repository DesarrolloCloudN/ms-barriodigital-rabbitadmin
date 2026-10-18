package cl.duoc.barriodigital.rabbitadmin.config;

import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitAdminConfig {

	// Único bean de configuración: el resto del microservicio administra el broker solo a
	// través de este RabbitAdmin, nunca llamando directo a la librería de RabbitMQ.
	@Bean
	public RabbitAdmin rabbitAdmin(ConnectionFactory connectionFactory) {
		RabbitAdmin rabbitAdmin = new RabbitAdmin(connectionFactory);
		rabbitAdmin.setIgnoreDeclarationExceptions(true);
		return rabbitAdmin;
	}

}
