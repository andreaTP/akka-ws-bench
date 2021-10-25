Example server implementation for this client:
https://github.com/matttomasetti/NodeJS_Websocket-Benchmark-Client


Run in development mode with:

```
sbt run
```

Produce a docker image with:

```
sbt docker:publishLocal
```

Run the docker image with:
```
docker run -p 8080:8080 --rm akka-ws-bench:0.1.0-SNAPSHOT
```
