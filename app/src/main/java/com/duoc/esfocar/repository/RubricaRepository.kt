package com.duoc.esfocar.repository

import com.duoc.esfocar.model.Rubrica
import com.duoc.esfocar.model.Criterio

class RubricaRepository {

    private val rubricas = listOf(
        Rubrica(1, "RUB-PP-01", "Control y Detención de Personas", "Procedimientos Policiales",
            "Evalúa la correcta aplicación de técnicas de control, identificación y detención en escenarios simulados.", "2025.1"),
        Rubrica(2, "RUB-PP-02", "Inspección y Registro Vehicular", "Procedimientos Policiales",
            "Mide el cumplimiento del protocolo de inspección, seguridad perimetral y registro de vehículos.", "2025.1"),
        Rubrica(3, "RUB-IF-01", "Condición Física Operativa", "Instrucción Física",
            "Evalúa resistencia, fuerza y agilidad según estándares mínimos de aptitud física.", "2025.2"),
        Rubrica(4, "RUB-IF-02", "Técnicas de Defensa Personal", "Instrucción Física",
            "Valora la ejecución de técnicas de defensa, control de agresor y uso proporcional de la fuerza.", "2025.1"),
        Rubrica(5, "RUB-PT-01", "Tiro Estático con Arma de Servicio", "Práctica de Tiro",
            "Evalúa precisión, seguridad en el manejo del arma y cumplimiento de normas de polígono.", "2025.3"),
        Rubrica(6, "RUB-PT-02", "Tiro en Movimiento y Transiciones", "Práctica de Tiro",
            "Mide disparo en desplazamiento, cambio de cargador y transición de posturas bajo estrés controlado.", "2025.2")
    )

    private val criterios = listOf(
        Criterio(1, 1, "Identificación verbal y control inicial del sujeto", 0.20, orden = 1),
        Criterio(2, 1, "Posicionamiento táctico y seguridad del entorno", 0.25, orden = 2),
        Criterio(3, 1, "Aplicación de técnicas de inmovilización y esposamiento", 0.30, orden = 3),
        Criterio(4, 1, "Comunicación y uso de fuerza proporcional", 0.15, orden = 4),
        Criterio(5, 1, "Registro de persona y resguardo de evidencias", 0.10, orden = 5),
        Criterio(6, 2, "Aproximación y control perimetral del vehículo", 0.20, orden = 1),
        Criterio(7, 2, "Orden de salida de ocupantes y control de personas", 0.25, orden = 2),
        Criterio(8, 2, "Inspección sistemática del habitáculo y maletero", 0.30, orden = 3),
        Criterio(9, 2, "Identificación de elementos de riesgo o ilícitos", 0.15, orden = 4),
        Criterio(10, 2, "Documentación del procedimiento y cierre seguro", 0.10, orden = 5),
        Criterio(11, 3, "Resistencia aeróbica (carrera / circuito)", 0.30, orden = 1),
        Criterio(12, 3, "Fuerza de tren superior (flexiones / tracciones)", 0.25, orden = 2),
        Criterio(13, 3, "Fuerza de tren inferior y core", 0.20, orden = 3),
        Criterio(14, 3, "Agilidad y cambio de dirección", 0.15, orden = 4),
        Criterio(15, 3, "Recuperación y control de respiración post-esfuerzo", 0.10, orden = 5),
        Criterio(16, 4, "Postura de guardia y desplazamiento base", 0.15, orden = 1),
        Criterio(17, 4, "Técnicas de bloqueo y esquiva", 0.20, orden = 2),
        Criterio(18, 4, "Control de agarres y proyección del agresor", 0.30, orden = 3),
        Criterio(19, 4, "Uso proporcional de la fuerza y desescalada", 0.20, orden = 4),
        Criterio(20, 4, "Seguridad propia y de terceros durante la intervención", 0.15, orden = 5),
        Criterio(21, 5, "Manejo seguro del arma (reglas de oro del polígono)", 0.25, orden = 1),
        Criterio(22, 5, "Postura, empuñe y alineación de miras", 0.20, orden = 2),
        Criterio(23, 5, "Precisión en serie de tiro estático", 0.30, orden = 3),
        Criterio(24, 5, "Control de gatillo y respiración", 0.15, orden = 4),
        Criterio(25, 5, "Recarga y continuidad del ejercicio", 0.10, orden = 5),
        Criterio(26, 6, "Desplazamiento seguro con arma en posición de alerta", 0.20, orden = 1),
        Criterio(27, 6, "Precisión en disparos en movimiento", 0.25, orden = 2),
        Criterio(28, 6, "Cambio de cargador bajo estrés controlado", 0.20, orden = 3),
        Criterio(29, 6, "Transición de posturas (de pie / rodilla / tendido)", 0.20, orden = 4),
        Criterio(30, 6, "Cumplimiento de normas de seguridad en todo el recorrido", 0.15, orden = 5)
    )

    fun obtenerRubricas(): List<Rubrica> {
        return rubricas.filter { it.activa }
    }

    fun obtenerCriteriosPorRubrica(rubricaId: Int): List<Criterio> {
        return criterios.filter { it.rubricaId == rubricaId }.sortedBy { it.orden }
    }
}