javac -classpath ./lib/rabbitmq-client.jar:./lib/lz4-1.3-SNAPSHOT.jar:./lib/freenect-jna.jar:./lib/jna-4.1.0.jar:./lib/snappy-java-1.1.1.jar:./lib/jzlib.jar src/main/java/compression/rabbitmq/*.java -d .

java -cp .:./lib/commons-io-1.2.jar:./lib/commons-cli-1.1.jar:./lib/rabbitmq-client.jar:./lib/jna-4.1.0.jar:./lib/freenect-jna.jar:./lib/lz4-1.3-SNAPSHOT.jar:./lib/snappy-java-1.1.1.jar:./lib/jzlib.jar RecvFrame 
