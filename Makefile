export projdir=$(shell pwd)/..
include ./Makefile.vars
GARBAGE=`find . -name \*.class`

export SRCS=$(shell find Escada/tpc -name \*.java)

all: jar

compile: $(SRCS)
	$(JC) $(JIKES_FLAGS) -g $^
	
jar: compile
	(cd $(classesdir) ; $(JAR) $(JAR_FLAGS) $(PROJ_JAR) `find  Escada/tpc -name \*.class`);

clean:
	rm -rf $(GARBAGE)
	echo $(GARBAGE)
	
docs:
	$(JAVADOC) -d $(docsdir)/tpc -classpath $(classpath) `find tpc -name \*.java`

# arch-tag: 35a104c6-523c-493b-9afe-e85f72d9d865
