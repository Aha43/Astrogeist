package aha.common.abstraction.logging;

import static aha.common.guard.StringGuards.requireNonEmpty;
import static aha.common.logging.Log.recordStories;
import static aha.common.logging.story.ForgettableStory.FORGETTABLE_STORY;

import aha.common.logging.story.StoryWithContent;

public interface Story extends StoryElement{
	
	Story episode(String what);
	Story section();
	Story section(String name);
	Story tell();

	public static Story create(String name) { 
		requireNonEmpty(name, "name");
		return recordStories() ? new StoryWithContent(name) : FORGETTABLE_STORY;
	}
}
