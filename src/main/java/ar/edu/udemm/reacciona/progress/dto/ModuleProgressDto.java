package ar.edu.udemm.reacciona.progress.dto;

import ar.edu.udemm.reacciona.progress.ModuleProgressStatus;

public record ModuleProgressDto(Long moduleId,
                                 String titulo,
                                 ModuleProgressStatus status,
                                 int pasosCompletados,
                                 int pasosTotales,
                                 int puntajeTotal,
                                 int porcentaje) {
}
