package aha.common.logging.story;

import static java.lang.System.lineSeparator;

import aha.common.abstraction.logging.StoryElement;

public final class Episode implements StoryElement {
	private final String what;
	
	Episode(String what) { this.what = what; }

	@Override public final void appendTo(String p, StringBuilder sb) {
		sb.append(p).append(this.what).append(lineSeparator()); }
}
