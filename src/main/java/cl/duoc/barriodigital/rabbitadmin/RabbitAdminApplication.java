package cl.duoc.barriodigital.rabbitadmin;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

// Microservicio administrador de RabbitMQ: crea, consulta y elimina colas, exchanges y
// bindings vía REST. No conoce el dominio de trámites; solo administra el broker.
@SpringBootApplication
public class RabbitAdminApplication {

	public static void main(String[] args) {
		SpringApplication.run(RabbitAdminApplication.class, args);
	}

}
