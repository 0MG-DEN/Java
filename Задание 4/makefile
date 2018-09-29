# makefile package1

.SUFFIXES: .class .java

PACK=package1
TARG=Java_Class1
JC="C:\Program Files\Java\jdk1.8.0_181\bin\javac.exe" -g
JM="C:\Program Files\Java\jdk1.8.0_181\bin\java.exe" -ea -jar
JR="C:\Program Files\Java\jdk1.8.0_181\bin\jar.exe" -cfe

OBJ=$(PACK)\Abiturient.class \
    $(PACK)\Java_Class1.class

.java.class:
	$(JC) $<

run: build
	$(JM) $(TARG).jar

build: 	$(OBJ)
	$(JR) $(TARG).jar $(OBJ)

clean:
	for %%f in ($(OBJ)) do del %%f
	del $(TARG).jar

$(PACK)\Abiturient.class: $(PACK)\Abiturient.java makefile

$(PACK)\Java_Class1.class: $(PACK)\Java_Class1.java makefile

# eof makefile package1
