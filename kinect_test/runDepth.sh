javac -classpath ./lib/freenect-jna.jar:./lib/jna-4.1.0.jar src/main/java/*.java -d .

java -Djna.library.path=./lib/native -classpath .:./lib/jna-4.1.0.jar:./lib/freenect-jna.jar KinectDepthTest
java -Djna.library.path=./lib/native -classpath .:./lib/jna-4.1.0.jar:./lib/freenect-jna.jar KinectDepthTest