# UC-005: Visualizar Progreso Personal (Implementación)

## Endpoints

### GET /api/progress/summary
Obtiene el resumen de progreso del usuario autenticado.

Query params opcionales:
- `from` (YYYY-MM-DD)
- `to` (YYYY-MM-DD)
- `tipoEmergencia` (enum según Modulo.tipoEmergencia)

#### Respuesta 200
```json
{
  "totalModules": 5,
  "completedModules": 2,
  "inProgressModules": 2,
  "notStartedModules": 1,
  "totalScore": 340,
  "modules": [
    {
      "moduleId": 1,
      "titulo": "Incendios Básico",
      "status": "IN_PROGRESS",
      "pasosCompletados": 3,
      "pasosTotales": 10,
      "puntajeTotal": 120,
      "porcentaje": 30
    }
  ],
  "achievements": [
    {
      "codigo": "FIRST_STEP",
      "nombre": "Primer Paso",
      "descripcion": "Completaste tu primer paso de simulación",
      "icono": "medal-bronze",
      "fechaObtencion": "2025-09-30T12:00:00"
    }
  ]
}
```

### POST /api/progress/attempt
Registra un intento/paso en una simulación para el usuario autenticado.

Body JSON:
```json
{
  "pasoId": 123,
  "correcto": true,
  "puntaje": 100
}
```
Respuestas:
- 201 Created (sin body) si se registró.
- 400 si el paso no existe / datos inválidos.

Efectos:
- Actualiza `ModuleProgress` (pasosCompletados, puntajeTotal, status).
- Puede disparar logros (`FIRST_STEP`, `MODULE_COMPLETED_*`, `SCORE_1000`).

## Modelo Persistente
- `ModuleProgress` (tabla `progreso_modulo`) estado y puntuación por módulo y usuario.
- `ActivityAttempt` (tabla `actividad_intento`) historial de intentos / pasos.
- `Achievement` (tabla `logro`) logros otorgados al usuario.

## Lógica de Logros (ejemplos iniciales)
- `FIRST_STEP`: primer paso correcto.
- `MODULE_COMPLETED_{id}`: al completar un módulo.
- `SCORE_1000`: alcanzar >= 1000 puntos acumulados.

> Estos criterios se pueden migrar a una tabla de configuraciones si se requiere dinamismo.

## Casos de Error / Vacío
- Sin progreso: lista de módulos vacía => frontend muestra mensaje friendly.
- Error servidor: HTTP 500 y mensaje genérico.
- Filtros sin resultados: `modules` vacío.

## Próximos Pasos Sugeridos
- Endpoint para historial detallado de intentos.
- Cache por usuario (e.g. Caffeine) para resumen (<60s TTL).
- Externalizar reglas de logros.
- Métricas / tracing (Micrometer) para asegurar SLA < 3s.
