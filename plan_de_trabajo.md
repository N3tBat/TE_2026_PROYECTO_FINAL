# Plan de Trabajo y Flujo de Desarrollo (Equipo EJAAPH)

Este documento sirve como guía y referencia para el equipo de desarrollo (desarrolladores y agentes de IA) con el fin de coordinar la construcción del **Sistema de Control de Horas de Servicio Social**. El objetivo principal es trabajar en paralelo de forma eficiente, prevenir conflictos en Git (merge conflicts) y asegurar una integración perfecta entre el Frontend y el Backend.

---

## 1. Estrategia de Trabajo sin Conflictos (Git & Modularización)

Para asegurar que los desarrolladores y las inteligencias artificiales trabajen en paralelo sin pisarse los cambios, seguiremos una estrategia de **Aislamiento por Capas y Archivos**:

* **Frontend (`frontend-web`)**: Modificado principalmente por **Antigravity** (agente de IA). Toda la interfaz gráfica, estilos de diseño y lógica de enrutamiento web se concentrará aquí.
* **Backend (`backend-api`)**: Modificado principalmente por el **Compañero de equipo (Backend Dev)**. La persistencia en base de datos, lógica de negocio y endpoints REST se concentrarán aquí.
* **Política de Ramas (Git Flow)**:
  * Nunca desarrollar directamente sobre `main`.
  * Cada integrante creará su propia rama de trabajo para sus respectivas tareas (ej. `feature/frontend-dashboards` y `feature/backend-registro-horas`).
  * Al finalizar, se realizará un Pull Request (PR) y una revisión conjunta antes de fusionar.

---

## 2. Flujo de Trabajo API-First (Evitando Descoordinación)

Para evitar que el backend y el frontend no encajen al integrarse, se trabajará bajo un **contrato de API acordado**. Antes de programar, se define el contrato JSON.

### Contrato de Ejemplo para Registro de Horas (`RegistroHoras`):
Cuando el Alumno registre entrada/salida, el frontend y el backend se comunicarán a través de este formato:

* **Registrar Entrada**:
  * **Endpoint**: `POST /api/horas/entrada`
  * **Petición (JSON)**: `{ "alumnoId": 1 }`
  * **Respuesta**: `200 OK` con el objeto `RegistroHoras` creado (incluyendo ID de registro, fecha y hora de entrada).

* **Registrar Salida**:
  * **Endpoint**: `POST /api/horas/salida`
  * **Petición (JSON)**: `{ "alumnoId": 1, "actividades": "Apoyo en el laboratorio de cómputo y desarrollo de reportes." }`
  * **Respuesta**: `200 OK` con el objeto `RegistroHoras` actualizado (calculando automáticamente las `horasCalculadas`).

---

## 3. Asignación de Tareas - Sprint Actual

A continuación se detalla la asignación de responsabilidades inmediatas para avanzar de forma paralela:

### 👤 Tarea de: Compañero de Equipo (Backend Developer)
**Objetivo:** Implementar la lógica y endpoints para el Registro y Control de Horas del Alumno.

* [ ] **Crear Repositorio:**
  * Crear la interfaz `RegistroHorasRepository.java` en `mx.proyecto.backend_api.repositorios`.
  * Añadir consulta para obtener registros por alumno: `List<RegistroHoras> findByAlumnoId(Long alumnoId);`.
* [ ] **Crear Servicio:**
  * Crear la interfaz `RegistroHorasService.java` y su implementación `RegistroHorasServiceImpl.java` en sus respectivos paquetes de servicios.
  * Implementar método `registrarEntrada(Long alumnoId)` que guarde la fecha actual y la hora de entrada (`LocalTime.now()`).
  * Implementar método `registrarSalida(Long alumnoId, String actividades)` que busque el último registro abierto de ese alumno, asigne la hora de salida (`LocalTime.now()`), describa las actividades y calcule las horas transcurridas guardándolas en `horasCalculadas`.
* [ ] **Crear Controlador REST:**
  * Crear `RegistroHorasController.java` en `mx.proyecto.backend_api.controladores`.
  * Exponer los endpoints `POST /api/horas/entrada`, `POST /api/horas/salida` y `GET /api/horas/alumno/{alumnoId}`.
  * Proteger estos endpoints para que solo alumnos autenticados puedan registrar horas y profesores/admin puedan consultarlas.

### 🤖 Tarea de: Antigravity (AI Assistant)
**Objetivo:** Crear las pantallas de Dashboard correspondientes para cada rol del sistema con interfaz gráfica moderna y responsiva.

* [x] **Preparar credenciales por defecto:** Crear `DataSeeder.java` para insertar automáticamente usuarios de prueba (`admin@aragon.unam.mx`, `profesor@aragon.unam.mx`, `alumno@aragon.unam.mx`) al arrancar el servidor. *(¡Completado!)*
* [ ] **Diseñar `alumno.html` (Dashboard del Alumno):**
  * Vista de horas totales acumuladas (barra de progreso visual).
  * Tarjeta interactiva para Marcar Entrada / Salida (simulando reloj checador en tiempo real).
  * Tabla responsiva con el historial de días registrados.
* [ ] **Diseñar `profesor.html` (Dashboard del Profesor):**
  * Panel con la lista de alumnos asignados a sus programas de servicio social.
  * Vista de progreso de cada alumno.
  * Herramienta rápida para autorizar/validar horas pendientes.
* [ ] **Diseñar `admin.html` (Dashboard del Administrador):**
  * Tablero general con estadísticas del sistema.
  * Formulario interactivo para dar de alta Programas de Servicio Social.
  * Panel de registro de nuevos profesores y alumnos.
* [ ] **Implementar Seguridad de Rutas en Frontend:**
  * Lógica en JavaScript para leer el JWT de `localStorage` y verificar el rol antes de cargar el contenido de las páginas. Redirigir a `index.html` si no hay sesión iniciada.

### 👤 Tarea de: Usuario (Coordinador / Integrador)
**Objetivo:** Orquestar el proyecto, levantar contenedores e integrar las dos partes.

* [ ] **Levantar la Infraestructura:** Ejecutar `docker-compose up --build` para compilar los cambios del backend y del frontend en contenedores.
* [ ] **Validar Acceso:** Iniciar sesión con los usuarios creados por el `DataSeeder` y comprobar que las redirecciones del login funcionen correctamente.
* [ ] **Integrar API Real:** Una vez que el compañero complete los endpoints de horas y Antigravity complete los HTMLs, coordinar el reemplazo de las respuestas simuladas del JavaScript por peticiones reales a la API.

---

## 4. Compromisos y Comunicación
Cualquier cambio de estructura en la base de datos o modificación de las firmas de los métodos REST definidos en la sección 2 debe ser notificado al resto del equipo inmediatamente para evitar roturas de compatibilidad. ¡Trabajemos en paralelo con orden para lograr una entrega exitosa!
