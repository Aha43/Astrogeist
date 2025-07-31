# Adjust these paths if needed
SRC_DIR = src
OUT_DIR = bin
MAIN_CLASS = astrogeist.app.Main
JAR_NAME = astrogeist.jar
MANIFEST = manifest.txt

.PHONY: all run clean

all: $(JAR_NAME)

$(JAR_NAME): $(MANIFEST)
	@echo "Compiling Java sources..."
	mkdir -p $(OUT_DIR)
	find $(SRC_DIR) -name "*.java" > sources.txt
	javac -d $(OUT_DIR) @sources.txt
	@echo "Creating JAR..."
	jar cfm $(JAR_NAME) $(MANIFEST) -C $(OUT_DIR) .
	@echo "Build complete: $(JAR_NAME)"

run: $(JAR_NAME)
	java -jar $(JAR_NAME)

clean:
	@echo "Cleaning build artifacts..."
	rm -rf $(OUT_DIR) $(JAR_NAME) sources.txt
