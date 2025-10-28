package astrogeist.engine.persitence.userdatadefinitions;

import java.util.List;

public record UserDataDefinition(String name, String type, List<String> values) {}
