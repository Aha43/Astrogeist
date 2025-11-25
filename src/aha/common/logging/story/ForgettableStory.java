package aha.common.logging.story;

import static aha.common.util.Guards.requireNonEmpty;

import aha.common.abstraction.logging.Story;

public final class ForgettableStory implements Story {
	
	public static final ForgettableStory FORGETTABLE_STORY =
		new ForgettableStory();
	
	private ForgettableStory() {}

	@Override public Story episode(String what) {
		requireNonEmpty(what, "what");
		return this;
	}

	@Override public Story section(String name) {
		requireNonEmpty(name, "name");
		return this;
	}
	
	@Override public Story section() { return this; }
	@Override public Story tell() { return this; }
	@Override public void appendTo(String p, StringBuilder sb) {}
}
