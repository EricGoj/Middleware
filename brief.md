# Technical Challenge - IT Governance

## Mini Aplicación de Gestión de Tareas con Integración a Jira

### Objetivo

Desarrollar una mini aplicación de gestión de tareas que permita a los usuarios agregar, eliminar y marcar tareas como completadas. La aplicación debe integrarse con un proyecto de Jira, enviando las tareas nuevas y actualizando las existentes a través de un webhook, asegurando que cualquier evento relacionado con issues en Jira se refleje en la aplicación.

### Descripción del Proyecto

Deberás crear una aplicación web utilizando React y Redux para la interfaz de usuario, y desarrollar una API en Go o Java (o el lenguaje que prefieras) para manejar las operaciones de backend.

Implementar la funcionalidad para crear, modificar y eliminar tareas mediante API de Jira y escuchar eventos desde un webhook que notificará sobre cualquier alteración de issues en el proyecto en Jira. Esto permitirá la sincronización de las tareas de la aplicación con las operaciones realizadas en Jira.

### Funcionalidades Clave

1. **Agregar Tareas**: Permitir la creación de nuevas tareas desde el frontend, enviando un POST a la API de Jira para crear el issue correspondiente.

2. **Eliminar Tareas**: Implementar la funcionalidad para eliminar tareas, reflejando dicha eliminación tanto en la base de datos local como en Jira.

3. **Marcar Tareas como Completadas**: Implementar una funcionalidad para cambiar el estado de las tareas y actualizar este estado en Jira mediante la API.

4. **Conexión con el Webhook de Jira**: La API debe manejar las solicitudes entrantes del webhook de Jira, procesando eventos de cambios en issues (como creación, actualización y eliminación) y actualizando la base de datos según sea necesario.

5. **Frontend**: Desarrollar la parte del cliente utilizando React y Redux, asegurando una experiencia de usuario fluida y responsiva.

### Requisitos Técnicos

- **Backend**: Go o Java (o el stack que prefieras) para desarrollar una API REST o GraphQL.
- **Frontend**: React y Redux (preferentemente utilizando Redux Toolkit).
- **Base de Datos**: Implementar una solución de base de datos (puede ser SQL o NoSQL) para almacenar las tareas.
- **Pruebas**: Incluir pruebas unitarias para componentes del frontend, así como para los endpoints de la API.

### Documentación de Referencia

1. **Para crear un API Token de Jira:**  
   [Manage API tokens for your Atlassian account](https://support.atlassian.com/atlassian-account/docs/manage-api-tokens-for-your-atlassian-account/)

2. **Crear un proyecto de Jira:**  
   [Create a new project](https://support.atlassian.com/jira-software-cloud/docs/create-a-new-project/)

3. **Documentación API de Jira:**  
   [Jira Cloud platform REST API documentation](https://developer.atlassian.com/cloud/jira/platform/rest/v3/intro/)

4. **Documentación sobre Webhooks de Jira:**  
   [Webhooks](https://developer.atlassian.com/cloud/jira/platform/webhooks/)

### Entrega

Deberás enviar tu código en un repositorio de GitHub, junto con una breve documentación que explique cómo ejecutar tanto el frontend como el backend de la aplicación, así como cualquier decisión relevante que hayas tomado durante el desarrollo.