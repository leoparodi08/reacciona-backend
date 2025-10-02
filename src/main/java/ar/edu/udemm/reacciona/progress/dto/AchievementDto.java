package ar.edu.udemm.reacciona.progress.dto;

import java.time.LocalDateTime;

public record AchievementDto(String codigo, String nombre, String descripcion, String icono, LocalDateTime fechaObtencion) {}
