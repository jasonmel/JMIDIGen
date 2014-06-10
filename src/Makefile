JAVAC = javac
JAVA  = java
JAR   = jar

MIDIGen.class:
	$(JAVAC) -cp jmusic.jar *.java

jar:
	rm -f JMIDIGen.jar
	$(JAR) cvfm JMIDIGen.jar JMIDIGen.mf *.class

clean:
	rm -f JMIDIGen.jar
	rm -f *.class *.mid
