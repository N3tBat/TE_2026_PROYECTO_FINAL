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

## 2. Flujo de Trabajo API-First (Contratos de la Sección de Administración)

Para garantizar la compatibilidad entre el frontend y el backend, acordamos y consolidamos los siguientes flujos e integraciones para el panel de **Administración**:

### A. Autenticación e Información del Administrador
* **Login**: `POST /api/auth/login`
* **JSON de Respuesta**:
  ```json
  {
    "token": "eyJhbGciOiJIUzI1...",
    "rol": "ROLE_ADMIN",
    "nombre": "Dr. Jesús Martínez (Admin)"
  }
  ```
* **Comportamiento**: El nombre y el rol se guardan en el navegador (`localStorage`) para renderizar dinámicamente el perfil del administrador en la cabecera sin hacer peticiones de consulta duplicadas.

### B. Gestión de Profesores y Sincronización Automática
* **Crear Profesor**: `POST /profesores`
  * **JSON Enviado**:
    ```json
    {
      "nombreCompleto": "Juliancito",
      "correo": "juliancito@aragon.unam.mx",
      "password": "contraseña_segura",
      "carrera": "ICO"
    }
    ```
  * **Lógica del Backend (Sincronizada)**: Al guardar el profesor, el backend crea en automático su cuenta de acceso correspondiente en la tabla de inicio de sesión (`usuarios`) con el rol de `PROFESOR` y encripta su contraseña de forma segura con `BCrypt`.
* **Eliminar Profesor**: `DELETE /profesores/{id}`
  * **Lógica del Backend (Sincronizada)**: Al dar de baja a un profesor, el backend busca su correo asociado y elimina automáticamente su usuario de inicio de sesión de la tabla de accesos para revocar accesos de inmediato.
* **Editar Asignaciones de Proyectos**: `PATCH /profesores/{id}`
  * **JSON Enviado**:
    ```json
    {
      "programasAsignados": [
        { "id": 1 },
        { "id": 2 }
      ]
    }
    ```

### C. Prevención de Recursión Infinita en JSON
* En relaciones Muchos a Muchos bidireccionales, se rompió el bucle infinito aplicando la anotación `@JsonIgnore` al atributo `profesores` en el archivo [Programa.java](file:///c:/Users/chich/OneDrive/Desktop/ProyectoChuy/TE_2026_PROYECTO_FINAL/backend-api/src/main/java/mx/proyecto/backend_api/entities/Programa.java). Esto asegura que el backend pueda transferir datos limpios en formato JSON sin errores de StackOverflow al consumir `GET /profesores` y `GET /programas`.

---

## 3. Reglas de Negocio en la Sección de Profesores

### A. Estandarización de Carreras
Para evitar variaciones de escritura, el formulario de registro de profesores utiliza un menú desplegable (`<select>`) con las siglas de carreras de prueba acordadas:
* **ICO** (Ingeniería en Computación)
* **IEE** (Ingeniería Eléctrica y Electrónica)
* **IM** (Ingeniería Mecánica)
* **II** (Ingeniería Industrial)
* **IC** (Ingeniería Civil)

### B. Cálculo Dinámico del Estado del Profesor (Regla de las 2 Semanas)
* **Vigencia de Proyectos**: Un proyecto se considera "Vigente" si su estado es `'activo'`. Si concluye (pasa su `fechaTermino`), entra en una **prórroga de 14 días naturales (2 semanas)** en la cual se le sigue computando como "Activo" en la interfaz.
* **Estado del Profesor**: 
  * Si el docente tiene asignado **al menos un proyecto** vigente o dentro de los 14 días de prórroga post-término ➡️ Su estado en pantalla cambia automáticamente a **"Activo"** (verde).
  * Si el docente no tiene proyectos asignados o todos vencieron hace más de 14 días ➡️ Su estado en pantalla cambia automáticamente a **"Inactivo"** (gris).

---

## 4. Estado de Tareas - Sprint Actual

### 🤖 Tarea de: Antigravity (AI Assistant) (Módulo Administrador y Core Completado)
* [x] **Preparar credenciales por defecto:** Crear `DataSeeder.java` robusto con soporte para verificar datos previos antes de insertar para evitar duplicados. *(¡Completado!)*
* [x] **Diseñar e integrar `admin.html` (Estilo Figma Mockup):** Barra lateral azul marino (`#003366`), logotipo de la UNAM FES Aragón con marco color oro (`#F4D35E`), tarjetas con bordes color oro y estadísticas en tiempo real. *(¡Completado!)*
* [x] **Modal 1 - Consulta de Proyectos:** Pop-up responsivo que muestra el correo electrónico de contacto y enlista los proyectos del docente. *(¡Completado!)*
* [x] **Modal 2 - Asignación de Proyectos:** Pop-up con checkboxes que enlista los programas activos y actualiza dinámicamente mediante `PATCH` la relación del profesor. *(¡Completado!)*
* [x] **Dropdown select de Carreras:** Menú desplegable para estandarizar carreras (ICO, IEE, IM, II, IC). *(¡Completado!)*
* [x] **Corrección de Recursión Infinita JSON:** Añadido `@JsonIgnore` en `Programa.java` para prevenir errores 500 al serializar. *(¡Completado!)*
* [x] **Route Guarding:** Lógica de validación de seguridad de roles al cargar la página. *(¡Completado!)*

### 👤 Tareas Siguientes: Compañero de Equipo (Backend Developer)
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

### 👤 Tarea de: Usuario (Coordinador / Integrador)
* [x] **Levantar la Infraestructura:** Ejecutar `docker-compose up --build` de forma exitosa. *(¡Completado!)*
* [x] **Validar Acceso de Administrador y Registro:** Iniciar sesión y comprobar el guardado de profesores, la inyección del nombre dinámico, los pop-ups de consulta y las ediciones. *(¡Completado!)*
