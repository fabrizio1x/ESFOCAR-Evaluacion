package com.duoc.esfocar.ui.rubricas

data class CriterioItem(
    val id: String,
    val nombre: String,
    val descripcion: String,
    val ponderacion: Int
)

data class RubricaItem(
    val id: String,
    val titulo: String,
    val descripcion: String,
    val categoria: String,
    val tiempoEstimadoMinutos: Int,
    val criterios: List<CriterioItem> = emptyList()
)

object RubricasDemoData {

    fun obtenerRubricasBase(): List<RubricaItem> {
        return listOf(
            RubricaItem(
                id = "rub-1",
                titulo = "Control y Fiscalización Vehicular",
                descripcion = "Evaluación del procedimiento reglamentario de detención, control de identidad al conductor y revisión visual del móvil en terreno.",
                categoria = "Tránsito",
                tiempoEstimadoMinutos = 20,
                criterios = listOf(
                    CriterioItem("crit-1", "Posicionamiento de Seguridad", "Ubicación del binomio y ángulo de resguardo respecto al vehículo fiscalizado.", 25),
                    CriterioItem("crit-2", "Revisión Documental", "Solicitud de licencia de conducir, permiso de circulación y seguro obligatorio.", 25),
                    CriterioItem("crit-3", "Inspección Visual del Móvil", "Detección de irregularidades externas, placa patente y verificación de ocupantes.", 25),
                    CriterioItem("crit-4", "Trato y Comunicación", "Uso correcto de modulación reglamentaria, respeto y claridad en las indicaciones.", 25)
                )
            ),
            RubricaItem(
                id = "rub-2",
                titulo = "Procedimientos de Seguridad y Registro Preventivo",
                descripcion = "Verificación de la técnica de registro superficial de vestimentas e incautación preventiva de elementos cortopunzantes o de riesgo.",
                categoria = "Seguridad",
                tiempoEstimadoMinutos = 15,
                criterios = listOf(
                    CriterioItem("crit-5", "Instrucciones Verbales", "Comandos claros para la postura de seguridad previa al registro.", 30),
                    CriterioItem("crit-6", "Técnica de Palpación Sistemática", "Revisión por cuadrantes corporales de arriba hacia abajo sin omitir zonas críticas.", 40),
                    CriterioItem("crit-7", "Manejo de Pertenencias", "Resguardo del equipo, billetera o bultos asegurando control del entorno.", 30)
                )
            ),
            RubricaItem(
                id = "rub-3",
                titulo = "Control de Detención y Procedimiento Táctico",
                descripcion = "Procedimiento ante flagrancia, técnica reglamentaria de esposamiento y traslado seguro manteniendo la integridad física.",
                categoria = "Operativo",
                tiempoEstimadoMinutos = 25,
                criterios = listOf(
                    CriterioItem("crit-8", "Cobertura y Comunicación Táctica", "Coordinación permanente con el compañero de patrulla durante la intervención.", 30),
                    CriterioItem("crit-9", "Técnica de Esposamiento", "Colocación segura de medidas de sujeción verificando traba de seguridad.", 35),
                    CriterioItem("crit-10", "Lectura de Derechos", "Lectura inmediata y formal de los derechos legales del detenido.", 35)
                )
            )
        )
    }
}
