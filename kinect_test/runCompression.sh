javac -classpath ./lib/lz4-1.3-SNAPSHOT.jar:./lib/freenect-jna.jar:./lib/jna-4.1.0.jar src/main/java/*.java -d .

java -Djna.library.path=./lib/native -classpath .:./lib/jna-4.1.0.jar:./lib/freenect-jna.jar:./lib/lz4-1.3-SNAPSHOT.jar CompressionTest
java -Djna.library.path=./lib/native -classpath .:./lib/jna-4.1.0.jar:./lib/freenect-jna.jar:./lib/lz4-1.3-SNAPSHOT.jar CompressionTest