JFLAGS = -g
JC = javac
.SUFFIXES: .java .class
.java.class:
	$(JC) $(JFLAGS) $*.java

CLASSES = \
	Token.java \
	Node.java \
	ScannerIn.java \
	Parser.java \
	support.java\
	SS.java\
	codeGen.java\
	comp.java 

default: classes

classes: $(CLASSES:.java=.class)

clean:
	$(RM) *.class
