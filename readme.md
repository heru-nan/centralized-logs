compilar

javac \*.java

Ejecutar rmi

    rmiregistry 1099 &

En otra terminal, levantar servidor

    java -Djava.rmi.server.codebase=file:. -Djava.rmi.server.hostname=localhost -Djava.security.policy=server.policy LogCentralizadoServidor

En otra terminal, para ejecutar el cliente(s), hay que pasarle un archivo de logs local

    java LogCentralizadoCron localhost log_local1.txt

En otra terminal,

    java LogCentralizadoCron localhost log_local2.txt

En otra terminal,

    java LogCentralizadoCron localhost log_local3.txt
