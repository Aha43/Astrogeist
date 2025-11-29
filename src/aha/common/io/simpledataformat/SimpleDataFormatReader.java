package aha.common.io.simpledataformat;

import static aha.common.io.TextParseUtil.stripComment;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import aha.common.guard.Guards;

public final class SimpleDataFormatReader {
	private SimpleDataFormatReader() {
		Guards.throwStaticClassInstantiateError(); }
	
	public static Map<String, Section> parse(File file) throws Exception {
		return parse(new FileInputStream(file)); }
	
	public static Map<String, Section> parse(InputStream is) throws Exception {
		try (var reader = new BufferedReader(new InputStreamReader(is,
			StandardCharsets.UTF_8))) {

            Map<String, Section> sections = new LinkedHashMap<>();
            String line;
            int lineNo = 0;

            String currentSectionName = null;
            List<Block> currentBlocks = null;

            String currentBlockName = null;
            Map<String, String> currentBlockData = null;

            while ((line = reader.readLine()) != null) {
                lineNo++;

                String trimmed = stripComment(line);
                if (trimmed.isEmpty()) {
                    continue;
                }

                // Section
                if (trimmed.charAt(0) == '@') {
                    // flush current block
                    if (currentBlockName != null) {
                        currentBlocks.add(new Block(currentBlockName, 
                        	Map.copyOf(currentBlockData)));
                        currentBlockName = null;
                        currentBlockData = null;
                    }
                    // flush previous section
                    if (currentSectionName != null) {
                        putSection(sections, currentSectionName, currentBlocks,
                        	lineNo);
                    }

                    currentSectionName = trimmed.substring(1).trim();
                    if (currentSectionName.isEmpty()) {
                        throw parseError(lineNo, 
                        	"Section name cannot be empty");
                    }
                    currentBlocks = new ArrayList<>();
                    continue;
                }

                if (currentSectionName == null) {
                    throw parseError(lineNo, 
                    	"Content before first section (@SectionName) is not " + 
                    		"allowed");
                }

                // Key/value?
                int colon = trimmed.indexOf(':');
                if (colon > 0) {
                    if (currentBlockName == null) {
                        throw parseError(lineNo,
                        	"Key/value outside of a block");
                    }
                    String key = trimmed.substring(0, colon).trim();
                    String value = trimmed.substring(colon + 1).trim();

                    if (key.isEmpty()) {
                        throw parseError(lineNo, "Empty key before ':'");
                    }

                    if (currentBlockData.containsKey(key)) {
                        // choose: override or error; here: override
                        // throw parseError(lineNo, "Duplicate key '%s' in block '%s'".formatted(key, currentBlockName));
                    }

                    currentBlockData.put(key, value);
                    continue;
                }

                // Otherwise: this is a new block name
                // flush previous block if any
                if (currentBlockName != null) {
                    currentBlocks.add(new Block(currentBlockName,
                    	Map.copyOf(currentBlockData)));
                }

                currentBlockName = trimmed;
                currentBlockData = new LinkedHashMap<>();
            }

            // EOF: flush last block & section
            if (currentBlockName != null) {
                currentBlocks.add(new Block(currentBlockName,
                	Map.copyOf(currentBlockData)));
            }
            if (currentSectionName != null) {
                putSection(sections, currentSectionName, currentBlocks, lineNo);
            }

            return Map.copyOf(sections);
        }	
	}
	
	private static void putSection(Map<String, Section> sections, String name,
		List<Block> blocks, int lineNo) {
			if (sections.containsKey(name)) {
				// Decide policy: here we throw to keep config sane.
				throw parseError(lineNo, "Duplicate section name '%s'".
					formatted(name));
			}
			sections.put(name, new Section(name, blocks.toArray(Block[]::new)));
	}
	
	private static IllegalArgumentException parseError(int lineNo,
		String message) {
        return new IllegalArgumentException(
        	"SimpleDataFormat parse error at line " + lineNo + ": " + message); 
	}
	
}
