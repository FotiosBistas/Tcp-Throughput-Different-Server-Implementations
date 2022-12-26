# Tcp-Throughput-Different-Server-Implementations

This project asks a server for specific files and tracks the time it took to complete the request. The protocol used is strictly TCP.

## Maven commands

To check if the project is a valid maven project:

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

## Build the jar file with the dependencies installed

`mvn install`

Or cleaning the target directory first:

`mvn clean install -U`

### You should see something like this:

```
[INFO] Scanning for projects...
[INFO] ------------------------------------------------------------------------
[INFO] Reactor Build Order:
[INFO]
[INFO] Client and server                                                  [pom]
[INFO] Client Side                                                        [jar]
[INFO] Server Side                                                        [jar]
[INFO]
[INFO] ------------------------< gr.AUEB:TCP_METRICS >-------------------------
[INFO] Building Client and server 1.0-SNAPSHOT                            [1/3]
[INFO] --------------------------------[ pom ]---------------------------------
[INFO]
[INFO] --- exec-maven-plugin:1.6.0:exec (update-submodules) @ TCP_METRICS ---
Entering 'TCP3wput-app'
From https://github.com/gsiros/TCP3wput-app
 * branch            main       -> FETCH_HEAD
Already up to date.
[INFO]
[INFO] --- maven-install-plugin:2.4:install (default-install) @ TCP_METRICS ---
[INFO] Installing C:\Users\fotis\GitHub\Tcp-Throughput-Different-Server-Implementations\pom.xml to C:\Users\fotis\.m2\repository\gr\AUEB\TCP_METRICS\1.0-SNAPSHOT\TCP_METRICS-1.0-SNAPSHOT.pom
[INFO]
[INFO] ---------------------< gr.AUEB:CLIENT_TCP_METRICS >---------------------
[INFO] Building Client Side 1.0-SNAPSHOT                                  [2/3]
[INFO] --------------------------------[ jar ]---------------------------------
[INFO]
[INFO] --- exec-maven-plugin:1.6.0:exec (update-submodules) @ CLIENT_TCP_METRICS ---
Entering '../TCP3wput-app'
From https://github.com/gsiros/TCP3wput-app
 * branch            main       -> FETCH_HEAD
Already up to date.
[INFO]
[INFO] --- maven-resources-plugin:2.6:resources (default-resources) @ CLIENT_TCP_METRICS ---
[INFO] Using 'UTF-8' encoding to copy filtered resources.
[INFO] skip non existing resourceDirectory C:\Users\fotis\GitHub\Tcp-Throughput-Different-Server-Implementations\client\src\main\resources
[INFO]
[INFO] --- maven-compiler-plugin:3.8.0:compile (default-compile) @ CLIENT_TCP_METRICS ---
[INFO] Nothing to compile - all classes are up to date
[INFO]
[INFO] --- maven-resources-plugin:2.6:testResources (default-testResources) @ CLIENT_TCP_METRICS ---
[INFO] Using 'UTF-8' encoding to copy filtered resources.
[INFO] skip non existing resourceDirectory C:\Users\fotis\GitHub\Tcp-Throughput-Different-Server-Implementations\client\src\test\resources
[INFO]
[INFO] --- maven-compiler-plugin:3.8.0:testCompile (default-testCompile) @ CLIENT_TCP_METRICS ---
[INFO] No sources to compile
[INFO]
[INFO] --- maven-surefire-plugin:2.12.4:test (default-test) @ CLIENT_TCP_METRICS ---
[INFO] No tests to run.
[INFO]
[INFO] --- maven-jar-plugin:3.2.0:jar (default-jar) @ CLIENT_TCP_METRICS ---
[INFO] Building jar: C:\Users\fotis\GitHub\Tcp-Throughput-Different-Server-Implementations\client\target\CLIENT_TCP_METRICS-1.0-SNAPSHOT.jar
[INFO]
[INFO] --- maven-shade-plugin:3.2.4:shade (default) @ CLIENT_TCP_METRICS ---
[INFO] Including org.json:json:jar:20220924 in the shaded jar.
[WARNING] CLIENT_TCP_METRICS-1.0-SNAPSHOT.jar, json-20220924.jar define 1 overlapping resource:
[WARNING]   - META-INF/MANIFEST.MF
[WARNING] maven-shade-plugin has detected that some class files are
[WARNING] present in two or more JARs. When this happens, only one
[WARNING] single version of the class is copied to the uber jar.
[WARNING] Usually this is not harmful and you can skip these warnings,
[WARNING] otherwise try to manually exclude artifacts based on
[WARNING] mvn dependency:tree -Ddetail=true and the above output.
[WARNING] See http://maven.apache.org/plugins/maven-shade-plugin/
[INFO] Replacing original artifact with shaded artifact.
[INFO] Replacing C:\Users\fotis\GitHub\Tcp-Throughput-Different-Server-Implementations\client\target\CLIENT_TCP_METRICS-1.0-SNAPSHOT.jar with C:\Users\fotis\GitHub\Tcp-Throughput-Different-Server-Implementations\client\target\CLIENT_TCP_METRICS-1.0-SNAPSHOT-shaded.jar
[INFO] Dependency-reduced POM written at: C:\Users\fotis\GitHub\Tcp-Throughput-Different-Server-Implementations\client\dependency-reduced-pom.xml
[INFO]
[INFO] --- maven-install-plugin:2.4:install (default-install) @ CLIENT_TCP_METRICS ---
[INFO] Installing C:\Users\fotis\GitHub\Tcp-Throughput-Different-Server-Implementations\client\target\CLIENT_TCP_METRICS-1.0-SNAPSHOT.jar to C:\Users\fotis\.m2\repository\gr\AUEB\CLIENT_TCP_METRICS\1.0-SNAPSHOT\CLIENT_TCP_METRICS-1.0-SNAPSHOT.jar
[INFO] Installing C:\Users\fotis\GitHub\Tcp-Throughput-Different-Server-Implementations\client\dependency-reduced-pom.xml to C:\Users\fotis\.m2\repository\gr\AUEB\CLIENT_TCP_METRICS\1.0-SNAPSHOT\CLIENT_TCP_METRICS-1.0-SNAPSHOT.pom
[INFO]
[INFO] ---------------------< gr.AUEB:SERVER_TCP_METRICS >---------------------
[INFO] Building Server Side 1.0-SNAPSHOT                                  [3/3]
[INFO] --------------------------------[ jar ]---------------------------------
[INFO]
[INFO] --- exec-maven-plugin:1.6.0:exec (update-submodules) @ SERVER_TCP_METRICS ---
Entering '../TCP3wput-app'
From https://github.com/gsiros/TCP3wput-app
 * branch            main       -> FETCH_HEAD
Already up to date.
[INFO]
[INFO] --- maven-resources-plugin:2.6:resources (default-resources) @ SERVER_TCP_METRICS ---
[INFO] Using 'UTF-8' encoding to copy filtered resources.
[INFO] skip non existing resourceDirectory C:\Users\fotis\GitHub\Tcp-Throughput-Different-Server-Implementations\server\src\main\resources
[INFO]
[INFO] --- maven-compiler-plugin:3.8.0:compile (default-compile) @ SERVER_TCP_METRICS ---
[INFO] Nothing to compile - all classes are up to date
[INFO]
[INFO] --- maven-resources-plugin:2.6:testResources (default-testResources) @ SERVER_TCP_METRICS ---
[INFO] Using 'UTF-8' encoding to copy filtered resources.
[INFO] skip non existing resourceDirectory C:\Users\fotis\GitHub\Tcp-Throughput-Different-Server-Implementations\server\src\test\resources
[INFO]
[INFO] --- maven-compiler-plugin:3.8.0:testCompile (default-testCompile) @ SERVER_TCP_METRICS ---
[INFO] No sources to compile
[INFO]
[INFO] --- maven-surefire-plugin:2.12.4:test (default-test) @ SERVER_TCP_METRICS ---
[INFO] No tests to run.
[INFO]
[INFO] --- maven-jar-plugin:3.2.0:jar (default-jar) @ SERVER_TCP_METRICS ---
[INFO]
[INFO] --- maven-shade-plugin:3.2.4:shade (default) @ SERVER_TCP_METRICS ---
[INFO] Including org.json:json:jar:20220924 in the shaded jar.
[WARNING] SERVER_TCP_METRICS-1.0-SNAPSHOT.jar, json-20220924.jar define 30 overlapping classes and resources:
[WARNING]   - META-INF/MANIFEST.MF
[WARNING]   - META-INF/maven/org.json/json/pom.properties
[WARNING]   - META-INF/maven/org.json/json/pom.xml
[WARNING]   - org.json.CDL
[WARNING]   - org.json.Cookie
[WARNING]   - org.json.CookieList
[WARNING]   - org.json.HTTP
[WARNING]   - org.json.HTTPTokener
[WARNING]   - org.json.JSONArray
[WARNING]   - org.json.JSONException
[WARNING]   - 20 more...
[WARNING] maven-shade-plugin has detected that some class files are
[WARNING] present in two or more JARs. When this happens, only one
[WARNING] single version of the class is copied to the uber jar.
[WARNING] Usually this is not harmful and you can skip these warnings,
[WARNING] otherwise try to manually exclude artifacts based on
[WARNING] mvn dependency:tree -Ddetail=true and the above output.
[WARNING] See http://maven.apache.org/plugins/maven-shade-plugin/
[INFO] Replacing original artifact with shaded artifact.
[INFO] Replacing C:\Users\fotis\GitHub\Tcp-Throughput-Different-Server-Implementations\server\target\SERVER_TCP_METRICS-1.0-SNAPSHOT.jar with C:\Users\fotis\GitHub\Tcp-Throughput-Different-Server-Implementations\server\target\SERVER_TCP_METRICS-1.0-SNAPSHOT-shaded.jar
[INFO] Dependency-reduced POM written at: C:\Users\fotis\GitHub\Tcp-Throughput-Different-Server-Implementations\server\dependency-reduced-pom.xml
[INFO]
[INFO] --- maven-install-plugin:2.4:install (default-install) @ SERVER_TCP_METRICS ---
[INFO] Installing C:\Users\fotis\GitHub\Tcp-Throughput-Different-Server-Implementations\server\target\SERVER_TCP_METRICS-1.0-SNAPSHOT.jar to C:\Users\fotis\.m2\repository\gr\AUEB\SERVER_TCP_METRICS\1.0-SNAPSHOT\SERVER_TCP_METRICS-1.0-SNAPSHOT.jar
[INFO] Installing C:\Users\fotis\GitHub\Tcp-Throughput-Different-Server-Implementations\server\dependency-reduced-pom.xml to C:\Users\fotis\.m2\repository\gr\AUEB\SERVER_TCP_METRICS\1.0-SNAPSHOT\SERVER_TCP_METRICS-1.0-SNAPSHOT.pom
[INFO] ------------------------------------------------------------------------
[INFO] Reactor Summary for Client and server 1.0-SNAPSHOT:
[INFO]
[INFO] Client and server .................................. SUCCESS [  1.860 s]
[INFO] Client Side ........................................ SUCCESS [  2.371 s]
[INFO] Server Side ........................................ SUCCESS [  2.555 s]
[INFO] ------------------------------------------------------------------------
[INFO] BUILD SUCCESS
[INFO] ------------------------------------------------------------------------
[INFO] Total time:  6.864 s
[INFO] Finished at: 2022-12-26T17:22:17+02:00
[INFO] ------------------------------------------------------------------------
```

## run the client

`cd client`

`java -jar target/CLIENT_TCP_METRIC-1.0-SNAPSHOT.jar serverIPA serverIPB portA portB filestorequestA filestorequestB`

## run the server

`cd server`

`java -jar target/SERVER_TCP_METRICS-1.0-SNAPSHOT.jar serverIP port(optional)`

## Android APK

https://mega.nz/file/b0tkBajD#7KNbGdR3sEj1rXT_gJGj6x9cJhlkZa1R85K0XZi8LW4
