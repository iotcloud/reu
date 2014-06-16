javac -classpath ./lib/freenect-jna.jar:./lib/jna-4.1.0.jar:./lib/lz4-1.3-SNAPSHOT.jar:./lib/snappy-java-1.1.1.jar src/main/java/compression/*.java -d .

java -Djna.library.path=./lib/native -classpath .:./lib/jna-4.1.0.jar:./lib/freenect-jna.jar:./lib/lz4-1.3-SNAPSHOT.jar:./lib/snappy-java-1.1.1.jar Sample

