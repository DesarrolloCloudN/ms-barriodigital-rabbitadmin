package cl.duoc.barriodigital.rabbitadmin.dto;

// Métricas de una cola: cuántos mensajes están pendientes y cuántos consumidores la escuchan.
public record QueueInfo(String nombre, int mensajesPendientes, int consumidores) {
}
