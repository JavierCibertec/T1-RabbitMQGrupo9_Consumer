# T1 Tipo D (RabbitMQ Consumer) — Grupo 9 · Maria Huaman Pahuara

| Nro. | Código | Nombres | Apellidos | Coordinador | Grupo |
|---|---|---|---|---|---|
| 6 | I202501591 | Maria | Huaman Pahuara |  | Grupo 9 |

Microservicio consumidor de la parte "Sincronización usando RabbitMQ": escucha la cola
`Grupo9Queue`, convierte el mensaje a lista de números con el parseo del enunciado
(`Stream.of(cadenaNumeros.split(";"))…`), espera 20 segundos, calcula la secuencia con
`FibonacciService` (la clase dada, sin modificar su lógica) e imprime el resultado.

| Carpeta | Qué es | Puerto |
|---|---|---|
| `appGrupo9Consumidor` | Consumidor RabbitMQ (escucha `Grupo9Queue`) | 8083 |

Nombres RabbitMQ: cola `Grupo9Queue`, exchange `Grupo9Exchange`, routing key `Grupo9Routing`.

Versiones exigidas: **Spring Boot 4.1.1 · Spring Cloud 2025.1.3 · Java 25** (ya fijadas en el `pom.xml`).

## 1. Requisitos

- **JDK 25** (`java -version` debe decir 25 en la terminal que uses).
  - Windows: instalar desde [oracle.com](https://www.oracle.com/java/technologies/downloads/) o Temurin y dejarlo como JDK por defecto.
  - Linux: el paquete de tu distro (ej. `sudo pacman -S jdk25-openjdk` o `sudo apt install temurin-25-jdk`) y seleccionarlo por defecto.
  - Mac: con Homebrew `brew install openjdk@25` (luego `java -version` debe decir 25; si no, enlázalo con `sudo ln -sfn $(brew --prefix openjdk@25)/libexec/openjdk.jdk /Library/Java/JavaVirtualMachines/openjdk-25.jdk`), o instala el `.pkg` de Oracle/Temurin.
- **IntelliJ IDEA** (Community basta; igual en los tres sistemas). Al abrir el proyecto, en `File → Project Structure → Project → SDK` debe quedar el JDK 25. Si sale "Cannot resolve symbol" en todo, casi seguro es eso.
- **Docker** (Docker Desktop en Windows y Mac, con el motor corriendo; en Linux el servicio `docker`). Puertos libres: 5672, 15672, 8083.
- Internet (Maven descarga dependencias la primera vez).

## 2. Levantar RabbitMQ

Desde la raíz de este repo:

```bash
docker compose up -d
```

Panel web: http://localhost:15672 (guest / guest). Para apagarlo al terminar: `docker compose down`.

> **Credenciales:** lo único que usa usuario y clave en todo el proyecto es RabbitMQ: `guest` / `guest` (conexión AMQP `localhost:5672` desde el `application.yml` y panel web). Son los valores por defecto para desarrollo local.

## 3. Importar en IntelliJ

`File → Open…` → carpeta `appGrupo9Consumidor` → `Trust Project`. Esperar a que termine
"Importing Maven project" (si no descarga dependencias: botón "Reload All Maven Projects").

Correr `AppGrupo9ConsumidorApplication.java` con el ▶ verde (recomendado, no requiere nada más).
Se queda escuchando `Grupo9Queue`.

### Si estás en Windows

Por consola (requiere Maven: descárgalo de [maven.apache.org](https://maven.apache.org/download.cgi), agrega su `bin` al PATH y verifica con `mvn -version` en una terminal nueva):

```powershell
cd appGrupo9Consumidor
mvn spring-boot:run
```

(Si no quieres instalar Maven, usa el ▶ de IntelliJ, que ya trae el suyo.)

### Si estás en Linux o Mac

Por consola (requiere Maven: `sudo pacman -S maven` / `sudo apt install maven` / `brew install maven`; verifica con `mvn -version`):

```bash
cd appGrupo9Consumidor
mvn spring-boot:run
```

(Si no quieres instalar Maven, usa el ▶ de IntelliJ, que ya trae el suyo.)

## 4. Probar

El consumidor por sí solo no expone endpoints: hay que publicarle un mensaje. Con el productor
del grupo (`GET /api/fibonacci/send?numbers=1;2;15;8`) o a mano desde el panel web
http://localhost:15672 → Exchanges → `Grupo9Exchange` → Publish message
(routing key `Grupo9Routing`, payload `1;2;15;8`).

En la consola del consumidor, después de unos 20 segundos aparece:

```text
[Grupo9Queue] Mensaje recibido: 1;2;15;8
Posiciones solicitadas: [1, 2, 15, 8]
Resultado Fibonacci: [1, 1, 610, 21]
```

Evidencia de la prueba en `evidencias/`.

## 5. Si algo falla

- **Puertos ocupados**: cambiar el `port` en el `application.yml`.
- **Maven no resuelve dependencias**: revisar internet/proxy y reintentar el reload de Maven.
