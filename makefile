# Java compiler and flags
JAVAC = javac
JFLAGS = -d bin -sourcepath craftinginterpreters/myclasses

# Source and build directories
SRCDIR = craftinginterpreters/myclasses
BINDIR = bin

# List of source files
SOURCES := $(wildcard $(SRCDIR)/*.java) $(wildcard $(SRCDIR)/tool/*.java)
CLASSES := $(patsubst $(SRCDIR)/%.java, $(BINDIR)/%.class, $(SOURCES))

# Target for compiling the Java classes
all: $(CLASSES)

# Rule to compile Java source files to class files
$(BINDIR)/%.class: $(SRCDIR)/%.java
	@mkdir -p $(BINDIR)
	$(JAVAC) $(JFLAGS) $<

# Target for cleaning the build directory
clean:
	rm -rf $(BINDIR)
