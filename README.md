# DRAWINGS
Pràctica 1 Entorns Servidor.

Requisits per executar l'aplicació localment: Java 25, Maven i Docker Compose.

### Arrencar-ho tot amb Docker

Des de l'arrel del projecte:

```sh
docker compose up --build -d
```

Obre l'aplicació a [http://localhost:8080](http://localhost:8080) i phpMyAdmin a
[http://localhost:8083](http://localhost:8083). La base de dades MySQL queda disponible al port `3306`.

### Arrencar des d'IntelliJ IDEA

Selecciona **DRAWINGS** (configurada a `.run/`) i prem **Play** per aixecar Docker i l'aplicació. La primera vegada, configura a IntelliJ una connexió Docker anomenada `Docker` amb el socket `unix:///var/run/docker.sock`.

### Executar l'aplicació localment

Amb Java 25 i Maven instal·lats, executa des de l'arrel:

```sh
docker compose up -d mysql && mvn spring-boot:run
```
