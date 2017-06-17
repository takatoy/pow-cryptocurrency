javac -J-Dfile.encoding=UTF-8 -d classes src/test/KeyGeneratorTest.java -cp src
java -Dfile.encoding=UTF-8 -cp classes test/KeyGeneratorTest
