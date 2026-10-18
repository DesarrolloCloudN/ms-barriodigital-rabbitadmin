package cl.duoc.barriodigital.rabbitadmin.controller;

import cl.duoc.barriodigital.rabbitadmin.dto.QueueInfo;
import cl.duoc.barriodigital.rabbitadmin.service.RabbitAdminService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

// Administración de colas, exchanges y bindings de RabbitMQ, según la tabla de endpoints de
// la guía EP2. Todo queda delegado a RabbitAdminService; el controller solo valida la forma
// HTTP de la petición.
@RestController
@RequestMapping("/api/rabbitmq")
public class RabbitMQController {

	private final RabbitAdminService rabbitAdminService;

	public RabbitMQController(RabbitAdminService rabbitAdminService) {
		this.rabbitAdminService = rabbitAdminService;
	}

	@PostMapping("/queues")
	@ResponseStatus(HttpStatus.CREATED)
	public Map<String, String> crearCola(@RequestParam String queueName) {
		return Map.of("queueName", rabbitAdminService.crearCola(queueName));
	}

	@GetMapping("/queues/{queueName}")
	public QueueInfo consultarCola(@PathVariable String queueName) {
		return rabbitAdminService.consultarCola(queueName);
	}

	@DeleteMapping("/queues/{queueName}")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public void eliminarCola(@PathVariable String queueName) {
		rabbitAdminService.eliminarCola(queueName);
	}

	@PostMapping("/queues/{queueName}/purge")
	public Map<String, Object> purgarCola(@PathVariable String queueName) {
		int purgados = rabbitAdminService.purgarCola(queueName);
		return Map.of("queueName", queueName, "mensajesPurgados", purgados);
	}

	@PostMapping("/exchanges")
	@ResponseStatus(HttpStatus.CREATED)
	public Map<String, String> crearExchange(@RequestParam String exchangeName, @RequestParam String type) {
		rabbitAdminService.crearExchange(exchangeName, type);
		return Map.of("exchangeName", exchangeName, "type", type);
	}

	@DeleteMapping("/exchanges/{exchangeName}")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public void eliminarExchange(@PathVariable String exchangeName) {
		rabbitAdminService.eliminarExchange(exchangeName);
	}

	@PostMapping("/bindings")
	@ResponseStatus(HttpStatus.CREATED)
	public Map<String, String> crearBinding(
			@RequestParam String queueName, @RequestParam String exchangeName, @RequestParam String routingKey) {
		rabbitAdminService.crearBinding(queueName, exchangeName, routingKey);
		return Map.of("queueName", queueName, "exchangeName", exchangeName, "routingKey", routingKey);
	}

	@DeleteMapping("/bindings")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public void eliminarBinding(
			@RequestParam String queueName, @RequestParam String exchangeName, @RequestParam String routingKey) {
		rabbitAdminService.eliminarBinding(queueName, exchangeName, routingKey);
	}

	@GetMapping("/metricas")
	public List<QueueInfo> obtenerMetricas() {
		return rabbitAdminService.obtenerMetricas();
	}

}
