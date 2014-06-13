export JAVA_HOME=/home/supun/tools/jdk1.7.0_51
export M2_HOME=/home/supun/tools/apache-maven-3.1.1
export PATH=$JAVA_HOME/bin:$PATH:$M2_HOME/bin

javac -classpath ./lib/freenect-jna.jar:./lib/jna-4.1.0.jar src/main/java/KinectTest.java -d .

java -Djna.library.path=./lib/native -classpath .:./lib/jna-4.1.0.jar:./lib/freenect-jna.jar KinectTest
java -Djna.library.path=./lib/native -classpath .:./lib/jna-4.1.0.jar:./lib/freenect-jna.jar KinectTest