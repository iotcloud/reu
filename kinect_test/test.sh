javac -classpath ./lib/freenect-jna.jar:./lib/jna-4.1.0.jar:./lib/lz4-1.3-SNAPSHOT.jar src/main/java/*.java -d .

java -Djna.library.path=./lib/native -classpath .:./lib/jna-4.1.0.jar:./lib/freenect-jna.jar:./lib/lz4-1.3-SNAPSHOT.jar test
java -Djna.library.path=./lib/native -classpath .:./lib/jna-4.1.0.jar:./lib/freenect-jna.jar:./lib/lz4-1.3-SNAPSHOT.jar test
