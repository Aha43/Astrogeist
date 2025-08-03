package lab;

import org.w3c.dom.*;
import javax.xml.parsers.*;
import java.io.File;
import java.nio.file.Path;
import java.time.*;
import java.util.*;
import java.util.regex.*;

public class AssembledTimestampResolver {

    public static class TimestampPattern {
        public final Pattern pattern;
        public final Map<String, Integer> groupMap; // yyyy -> 1, MM -> 2, etc.

        public TimestampPattern(Pattern pattern, Map<String, Integer> groupMap) {
            this.pattern = pattern;
            this.groupMap = groupMap;
        }

        public Optional<Instant> extract(Path path) {
            Matcher matcher = pattern.matcher(path.toString().replace(File.separatorChar, '/'));
            if (!matcher.find()) return Optional.empty();

            try {
                int yyyy = parseGroup(matcher, groupMap, "yyyy", 1970);
                int MM = parseGroup(matcher, groupMap, "MM", 1);
                int dd = parseGroup(matcher, groupMap, "dd", 1);
                int HH = parseGroup(matcher, groupMap, "HH", 0);
                int mm = parseGroup(matcher, groupMap, "mm", 0);
                int ss = parseGroup(matcher, groupMap, "ss", 0);

                LocalDateTime ldt = LocalDateTime.of(yyyy, MM, dd, HH, mm, ss);
                return Optional.of(ldt.toInstant(ZoneOffset.UTC));
            } catch (Exception e) {
                return Optional.empty();
            }
        }

        private int parseGroup(Matcher matcher, Map<String, Integer> map, String key, int defaultValue) {
            if (!map.containsKey(key)) return defaultValue;
            return Integer.parseInt(matcher.group(map.get(key)));
        }
    }

    public static List<TimestampPattern> parseXml(File file) throws Exception {
        List<TimestampPattern> result = new ArrayList<>();

        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();
        Document doc = builder.parse(file);
        NodeList timestampNodes = doc.getElementsByTagName("timestamp");

        for (int i = 0; i < timestampNodes.getLength(); i++) {
            Element tsElem = (Element) timestampNodes.item(i);

            String patternText = tsElem.getElementsByTagName("pattern").item(0).getTextContent().trim();
            Pattern regex = Pattern.compile(patternText);

            Map<String, Integer> groupMap = new HashMap<>();
            NodeList assemblyList = tsElem.getElementsByTagName("assembly");
            if (assemblyList.getLength() > 0) {
                Element asm = (Element) assemblyList.item(0);
                for (String tag : List.of("yyyy", "MM", "dd", "HH", "mm", "ss")) {
                    NodeList parts = asm.getElementsByTagName(tag);
                    if (parts.getLength() > 0) {
                        Element part = (Element) parts.item(0);
                        int group = Integer.parseInt(part.getAttribute("group"));
                        groupMap.put(tag, group);
                    }
                }
            }

            result.add(new TimestampPattern(regex, groupMap));
        }

        return result;
    }
}

