package astrogeist.engine.userdata;

import java.util.List;

public record UserDataDefinition(String name, String type, List<String> values) {}
