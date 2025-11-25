package aha.common.logging.story;

import static aha.common.util.Guards.requireNonEmpty;
import static java.lang.System.lineSeparator;

import java.util.ArrayList;
import java.util.List;

import aha.common.abstraction.logging.Story;
import aha.common.abstraction.logging.StoryElement;
import aha.common.logging.Log;
import aha.common.util.Strings;

public final class StoryWithContent implements Story {
	
	private final String name;
	
	private final List<StoryElement> elements = new ArrayList<>();
	
	public StoryWithContent(String name) {
		name = name == null ? null : name.trim();
		this.name = name;
	}
	
	@Override public final StoryWithContent episode(String what) {
		requireNonEmpty(what, "what");
		this.elements.add(new Episode(what));
		return this;
	}
	
	@Override public final Story section() { return createSection(null); }

	@Override public final Story section(String name) {
		requireNonEmpty(name, "name");
		return createSection(name);
	}
	
	private final Story createSection(String name) {
		var section = new StoryWithContent(name);
		this.elements.add(section);
		return section;
	}

	@Override public final Story tell() { 
		Log.get(this).info(this.toString());
		return this;
	}
	
	@Override public final String toString() {
		var sb = new StringBuilder();
		this.appendTo(Strings.EMPTY, sb);
		return sb.toString();
	}

	@Override public final void appendTo(String p, StringBuilder sb) {
		if (this.name != null)
			sb.append(p).append(this.name).append(lineSeparator());
		if (this.elements.isEmpty()) return;
		var childPrefix = p + "  ";
		for (var e : this.elements) e.appendTo(childPrefix, sb);
	}

}
