# Java compiler and flags
JAVAC = javac
JFLAGS = -d bin -sourcepath .

# Source and build directories
SRCDIR = .
BINDIR = bin

# List of source files
SOURCES := $(wildcard $(SRCDIR)/myclasses/*.java) $(wildcard $(SRCDIR)/myclasses/tool/*.java)
CLASSES := $(patsubst $(SRCDIR)/%.java, $(BINDIR)/%.class, $(SOURCES))

# Target for compiling the Java classes
all: $(CLASSES)

# Rule to compile Java source files to class files
$(BINDIR)/%.class: $(SRCDIR)/%.java
	@mkdir -p $(BINDIR)
	$(JAVAC) $(JFLAGS) $<

# Target for running the Lox class with an optional argument
run:
	@if [ -n "$(ARGS)" ]; then \
		java -cp $(BINDIR) myclasses.Lox $(ARGS); \
	else \
		java -cp $(BINDIR) myclasses.Lox; \
	fi

# Target for cleaning the build directory
clean:
	rm -rf $(BINDIR)
