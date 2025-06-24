# Guión: Sistema de Gestión de Recursos Humanos

## Introducción (30 segundos)
Bienvenidos a la presentación del Sistema de Gestión de Recursos Humanos. Esta aplicación permite administrar una base de datos de recursos humanos mediante una arquitectura cliente-servidor utilizando sockets para la comunicación y una base de datos H2 en memoria para el almacenamiento de datos.

## Arquitectura del Sistema (1 minuto)
El sistema está compuesto por dos componentes principales:

1. **Servidor**: Maneja las conexiones de los clientes, procesa las solicitudes y ejecuta operaciones en la base de datos.
2. **Cliente**: Se conecta al servidor, envía solicitudes y muestra las respuestas al usuario.

La comunicación entre el cliente y el servidor se realiza mediante sockets, utilizando objetos serializados para enviar solicitudes y recibir respuestas.

## Estructura de la Base de Datos (1 minuto)
La base de datos incluye las siguientes entidades:

- **Países**: Almacena información sobre países.
- **Ciudades**: Relacionadas con países.
- **Ubicaciones**: Direcciones físicas relacionadas con ciudades.
- **Departamentos**: Unidades organizativas relacionadas con ubicaciones.
- **Cargos**: Posiciones laborales con rangos salariales.
- **Empleados**: Personal con información detallada y relaciones con departamentos y cargos.
- **Histórico de Empleados**: Registro de empleados eliminados.

## Funcionalidades Principales (2 minutos)
El sistema ofrece cuatro tipos de operaciones principales:

1. **Operaciones de Inserción**:
   - Insertar países, ciudades, ubicaciones, departamentos, cargos y empleados.
   - Ejemplo: Para insertar un empleado, se solicita nombre, apellido, correo, teléfono, fecha de contratación, ID del cargo, salario e ID del departamento.

2. **Operaciones de Actualización**:
   - Actualizar la información de cualquier entidad existente.
   - Ejemplo: Para actualizar un departamento, se solicita su ID y los nuevos datos.

3. **Operaciones de Consulta**:
   - Consultar información detallada de cualquier entidad.
   - Ejemplo: Al consultar un empleado, se muestra su información completa incluyendo detalles del cargo y departamento.

4. **Operación de Eliminación**:
   - Eliminar empleados (marcándolos como eliminados y guardando un registro histórico).
   - Esta operación es transaccional, asegurando la integridad de los datos.

## Flujo de Trabajo Típico (30 segundos)
Un flujo de trabajo típico incluiría:

1. Iniciar el servidor.
2. Iniciar el cliente y conectarse al servidor.
3. Crear entidades en orden jerárquico: país, ciudad, ubicación, departamento, cargo y finalmente empleado.
4. Realizar consultas, actualizaciones o eliminaciones según sea necesario.

## Conclusión (30 segundos)
Este Sistema de Gestión de Recursos Humanos ofrece una solución completa para administrar información de personal y estructura organizativa. Su arquitectura cliente-servidor permite una clara separación de responsabilidades, mientras que la base de datos en memoria proporciona un rendimiento óptimo para entornos de desarrollo y pruebas.

La aplicación es fácil de usar gracias a su interfaz de línea de comandos intuitiva y maneja adecuadamente los errores para garantizar una experiencia de usuario fluida.