# Tcp-Throughput-Different-Server-Implementations

## Maven commands

To check if the project is a valid project:

`mvn validate`

```
[INFO] Scanning for projects...
[INFO]
[INFO] ------------------------< gr.AUEB:Tcp_Metrics >-------------------------
[INFO] Building Tcp_Metrics 1
[INFO] --------------------------------[ jar ]---------------------------------
[INFO] ------------------------------------------------------------------------
[INFO] BUILD SUCCESS
[INFO] ------------------------------------------------------------------------
[INFO] Total time:  0.067 s
[INFO] Finished at: 2022-12-16T16:12:14+02:00
[INFO] ------------------------------------------------------------------------
```

## Build the jar file

`mvn package`

```
[INFO] Scanning for projects...
[INFO]
[INFO] ------------------------< gr.AUEB:TCP_METRICS >-------------------------
[INFO] Building TCP_METRICS 1.0-SNAPSHOT
[INFO] --------------------------------[ jar ]---------------------------------
[INFO]
[INFO] --- maven-resources-plugin:2.6:resources (default-resources) @ TCP_METRICS ---
[WARNING] Using platform encoding (UTF-8 actually) to copy filtered resources, i.e. build is platform dependent!
[INFO] skip non existing resourceDirectory C:\Users\fotis\GitHub\Tcp-Throughput-Different-Server-Implementations\src\main\resources
[INFO]
[INFO] --- maven-compiler-plugin:3.8.0:compile (default-compile) @ TCP_METRICS ---
[INFO] Nothing to compile - all classes are up to date
[INFO]
[INFO] --- maven-resources-plugin:2.6:testResources (default-testResources) @ TCP_METRICS ---
[WARNING] Using platform encoding (UTF-8 actually) to copy filtered resources, i.e. build is platform dependent!
[INFO] skip non existing resourceDirectory C:\Users\fotis\GitHub\Tcp-Throughput-Different-Server-Implementations\src\test\resources
[INFO]
[INFO] --- maven-compiler-plugin:3.8.0:testCompile (default-testCompile) @ TCP_METRICS ---
[INFO] No sources to compile
[INFO]
[INFO] --- maven-surefire-plugin:2.12.4:test (default-test) @ TCP_METRICS ---
[INFO] No tests to run.
[INFO]
[INFO] --- maven-jar-plugin:3.2.0:jar (default-jar) @ TCP_METRICS ---
[INFO] ------------------------------------------------------------------------
[INFO] BUILD SUCCESS
[INFO] ------------------------------------------------------------------------
[INFO] Total time:  0.672 s
[INFO] Finished at: 2022-12-16T17:06:00+02:00
[INFO] ------------------------------------------------------------------------
```

### With the manifest file

`java -jar target/TCP_METRICS-1.0-SNAPSHOT.jar`

### Without manifest file

`java -cp targer/<jar-name>.jar StartClient`
