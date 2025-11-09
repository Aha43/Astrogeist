package aha.common.io.simpledataformat;

import java.util.Map;

public record Block(String name, Map<String, String> data) { }
