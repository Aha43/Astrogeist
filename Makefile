SRC_DIR = src
OUT_DIR = bin
MAIN_CLASS = astrogeist.ui.swing.Main
JAR_NAME = astrogeist.jar
MANIFEST = manifest.txt
DOC_DIR = docs/api
SOURCES_FILE = sources.txt

.PHONY: all run clean javadoc

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

run-prod: $(JAR_NAME)
	java -jar $(JAR_NAME) /Users/arnehalvorsen/astrolab-data

clean:
	@echo "Cleaning build artifacts..."
	rm -rf $(OUT_DIR) $(JAR_NAME) sources.txt $(DOC_DIR)

# --- new target ---
javadoc: $(SOURCES_FILE)
	@echo "Generating Javadoc into $(DOC_DIR)…"
	mkdir -p $(DOC_DIR)
	# Use explicit source files to avoid scanning system trees
	javadoc -d $(DOC_DIR) @$(SOURCES_FILE)
	@echo "Open $(DOC_DIR)/index.html"
