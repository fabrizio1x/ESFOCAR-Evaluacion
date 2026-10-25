v.1
- Modelos de dominio Rubrica y Criterio (desacoplados de la UI)
- RubricaRepository con datos de:
  - Procedimientos Policiales (Control/Detención, Inspección Vehicular)
  - Instrucción Física (Condición Física, Defensa Personal)
  - Práctica de Tiro (Tiro Estático, Tiro en Movimiento)
- API de consulta: obtenerRubricas() y obtenerCriteriosPorRubrica(id)
- Criterios con ponderación (suma 1.0) y escala de notas 1.0–7.0
