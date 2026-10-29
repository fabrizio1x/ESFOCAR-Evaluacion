# Sistema de Registro y Evaluación Práctica — Escuela de Formación (Caso 006D)

Aplicación móvil nativa en Android desarrollada con Jetpack Compose y arquitectura MVVM para la digitalización, estandarización y registro de evaluaciones prácticas en terreno para docentes e instructores de sección.

Proyecto académico desarrollado para la asignatura Desarrollo de Aplicaciones Móviles (DSY1105) — Duoc UC.

---

## Integrantes (Grupo 15)

* Fabrizio Esteban Martínez Rodríguez
* Martín González Donaire
* Dinko Pedro Ocampo Zlosilo

---

## Descripción del Proyecto

El sistema está orientado exclusivamente a docentes e instructores para evaluar el desempeño práctico de estudiantes en simulación de procedimientos, técnicas operativas e instrucción en terreno.

La solución está diseñada con enfoque Offline-First, permitiendo operar con normalidad en patios y sectores con conectividad a internet limitada o inexistente, garantizando que ninguna evaluación ni evidencia se pierda en el dispositivo.

### Funcionalidades Principales:
* Interfaz Declarativa: Construida con Jetpack Compose y componentes Material Design 3.
* Autenticación de Instructores: Acceso por correo institucional y contraseña validada.
* Módulo de Rúbricas: Consulta y selección de pautas e instrumentos oficiales de evaluación.
* Calificaciones Estandarizadas: Escala de notas de 1.00 a 7.00 con 2 decimales y truncamiento estricto (sin redondeo).
* Retroalimentación Pedagógica: Registro de observaciones cualitativas para el estudiante.
* Persistencia Local: Almacenamiento seguro en base de datos local (SQLite / Room) para trabajo en terreno.
* Recursos Nativos: Captura de evidencia mediante la cámara del dispositivo y verificación del estado de conexión de red.

---

## Tablero de Tareas
* Trello: [Tablero de Planificación — Proyecto ESF Grupo 15](https://trello.com/b/7V3ETAD3/proyecto-esf-grupo-15)


---

## Requisitos y Pasos para Ejecutar

1. Clonar el repositorio:
   ```bash
   git clone https://github.com/fabrizio1x/ESFOCAR-Evaluacion.git
   ```
2. Abrir el proyecto en Android Studio (Ladybug / Iguana o superior).
3. Aguardar la sincronización de dependencias de Gradle.
4. Ejecutar la aplicación en un emulador (API 26+) o dispositivo físico vía USB presionando Run 'app'.
