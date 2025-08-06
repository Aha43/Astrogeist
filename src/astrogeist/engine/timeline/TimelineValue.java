package astrogeist.engine.timeline;

import astrogeist.engine.typesystem.Type;

public record TimelineValue(String value, Type type) { 
	public final static TimelineValue Empty = new TimelineValue("", Type.Void());
	public final static TimelineValue[] EmptyArray = new TimelineValue[0]; 
	
	public boolean noData() { 
		return type == Type.Void() || value == null || value.trim().length() == 0; } 
}
