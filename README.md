
#  **Proyecto QuedadasBoardGames**  
### Desarrollo de Aplicaciones Multiplataforma (DAM)  
#### Alumno: Moisés Molina Lancharro

---

## Índice

- [Introducción](#introducción)
- [Funcionalidades y tecnologías utilizadas](#funcionalidades-y-tecnologías-utilizadas)
- [Guía de instalación](#guía-de-instalación)
- [Guía de uso](#guía-de-uso)
- [Documentación del proyecto](#documentación-del-proyecto)
- [Figma - Diseño de la interfaz](#figma---diseño-de-la-interfaz)
- [Conclusión](#conclusión)
- [Contribuciones, agradecimientos y referencias](#contribuciones-agradecimientos-y-referencias)
- [Licencias](#licencias)
- [Contacto](#contacto)


---

## Introducción

**Quedadas Board Games** es una aplicación Android diseñada para facilitar la organización de eventos de juegos de mesa entre usuarios. Ofrece funciones como registro, geolocalización de eventos, gestión de perfiles, amigos y calendario de disponibilidad. La idea nace de la necesidad de encontrar compañeros de juego de forma sencilla y organizada.

### Justificación

En la sociedad actual donde las agendas están cada vez más apretadas y las relaciones muchas veces se descuidan, los juegos de mesa surgen como una herramienta poderosa para volver a conectar. Esta aplicación busca facilitar el reencuentro social a través de la tecnología.

### Público Objetivo

 Personas con interés en juegos de mesa, desde jugadores casuales hasta aficionados, que desean reunirse con otros para disfrutar de su hobby de manera organizada.

---

## Funcionalidades y tecnologías utilizadas

### Funcionalidades principales

- Registro e inicio de sesión de usuarios.
- Geolocalización.
- Creación, eliminación de eventos.
- Inscripción a eventos.
- Desinscripción de eventos.
- Modo claro/oscuro.
- Seguimiento de usuarios entre ellos.
- Notificaciones y recordatorios. (A futuro)

### Tecnologías utilizadas

- **Mobile**: Android Studio & Java
- **Backend**: Spring Boot (API REST)
- **Base de datos**: MySQL
- **Autenticación**: JWT (Json Web Tokens)
- **Diseño UI/UX**: Figma
- **Control de versiones**: Git + GitHub
- **Docker y DockerCompose**: Contenerización del backend y la base de datos

---

## Guía de instalación

Disponemos de dos opciones:

### Pasos para desplegar en local (backend y bd dockerizado)

1. Ir al repositorio https://github.com/Cnon88/proyecto_moises_molina_lancharro[enter link description here](https://github.com/Cnon88/proyecto_moises_molina_lancharro)
2. Clonarlo mediante git
3. Tener instalado docker
4. Colocarnos en la carpeta raíz del proyecto y ejecuta el comando `docker compose up`
5. Este paso previo, nos levanta la base de datos y el backend, el backend lo mapea al 8080 y la base de datos al 3307
6. Abrir el proyecto QuedadasEventos en AndroidStudio
7. Localizar la clase `ApiClient` en el paquete `com.moises.quedadaseventos.retrofit` y cambiar la variable `BASE_URL` para que apunte a la ip de la red donde esta levantado el backend.  Por ejemplo en mi casa sería "http://192.168.1.100:8080/"

### Pasos para descargar la APK y usarla en entorno productivo ya desplegado (solo hace falta instalar la apk, ya que apunta a un entorno de internet donde esta desplegada la bd y la api)
Ir a ... y descargar la APK.
Se debe permitir la instalación de fuentas desconocidas en el móvil android.

---

## Guía de uso

Se trata de una aplicación amigable y fácil de usar. Se puede consultar el prototipo en el apartado de Figma.

---

## Documentación del proyecto

La documentación del proyecto se encuentra en formato Markdown en github en el siguiente enlace ....
La documentación de la api se encuentra desplegada en [https://actively-glad-roughy.ngrok-free.app/swagger-ui/index.html](https://actively-glad-roughy.ngrok-free.app/swagger-ui/index.html)

---

## Figma - Diseño de la interfaz
https://www.figma.com/design/zPR9ezjQ1QaigbX0xQUPQ0/Dise%C3%B1oQuedadas?node-id=0-1&t=D5tsYzFYyAY1yi0S-1

---

## Conclusión
Este proyecto ha sido una experiencia muy enriquecedora que me ha permitido consolidar y ampliar mis conocimientos en diversas tecnologías y herramientas clave para el desarrollo de aplicaciones modernas.

El uso de **Spring Boot** me facilitó la creación rápida y eficiente de servicios backend robustos y escalables, mientras que la integración de **JUnit** reforzó la importancia de las pruebas automatizadas para garantizar la calidad y estabilidad del código. Por otro lado, el desarrollo en **Android** con **Java** me permitió diseñar una interfaz de usuario intuitiva y funcional, enfocada en la experiencia del usuario.

Además, la incorporación de **Docker** en el flujo de trabajo me ayudó a comprender cómo contenerizar aplicaciones para asegurar entornos consistentes y facilitar el despliegue, haciendo que el proyecto sea fácilmente replicable y escalable.

En conjunto, este proyecto ha reforzado mi capacidad para trabajar en entornos multidisciplinares, integrando backend, frontend móvil y herramientas de DevOps, preparando así una base sólida para futuros retos profesionales en desarrollo de software.

---

## Contribuciones, agradecimientos y referencias

Rellenar....

---

## Licencias

MIT. Libre uso para fines académicos y personales.

---

## Contacto

A través de email, en la dirección [cnon@hotmail.es](mailto:cnon@hotmail.es)

---

