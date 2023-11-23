# WebApp Cursos
## Desafío Quinto Elemento
### Características:
- Menú admin: gestión de cursos, profesores, alumnos y listado inscripciones.
- Menú profesor: actualizar perfil propio, cursos propios y listado de inscripciones correspondientes a los cursos brindados.
- Menú alumno: actualización de perfil propo e inscripción/baja de cursos.

### Diseño
![Diagrama de Clases](/imgs/cursos-Diagrama%20de%20Clases.png)
[Diagrama de Clases y Diagrama ER](https://drive.google.com/file/d/110WeFUXv8-4IYYI2K75dTfcX-1R-rXDP/view?usp=sharing)

### Requisitos:
- Java 17
- Cliente/Servidor MySQL o MariaDB
- IDE
- Maven

### Instrucciones:
1. Extrar el repositorio localmente.
2. Crear base de datos "cursos_db". Crear usuario con permisos completos sobre la misma (usuario/contraseña por defecto: "cursoAdmin":"usuarioAdmin"). Si no se usan las credenciales por defecto, utilizar variables de entorno MySQL.
3. Abrir proyecto con IDE (VS Code, Intellij, NetBeans, etc).
4. Construir y ejecutar proyecto.


### Pendiente
#### Funcionalidades pendientes
- Autenticación de sesiones mediante token. Autorización de usuarios según el rol asignado.

- Generación de logs por cada transacción realizado por los usuarios.

#### Tareas pendientes

- Añadir entradas a log, por cada transacción realizada por un usuarios.

- Hacer release, con ejecutable jar.
