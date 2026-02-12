package astrogeist.ui.swing.component.observatory;

import static astrogeist.ui.swing.component.observatory.SelectConfigurationDialog.showDialog;
import static java.util.Objects.requireNonNull;
import static aha.common.guard.StringGuards.requireNonEmpty;

import java.awt.Frame;

import astrogeist.engine.observatory.Configuration;
import astrogeist.engine.observatory.ConfigurationMatcher;
import astrogeist.engine.observatory.DefaultConfigurationMatcher;
import astrogeist.engine.observatory.Observatory;

public final class SelectConfigurationOptions {
	private Frame frame = null;
	private final Observatory observatory;
	private ConfigurationMatcher matcher = null;
	private String selected = null;
	
	private SelectConfigurationOptions(Observatory observatory) {
		this.observatory = requireNonNull(observatory, "observatory"); }
	
	public final SelectConfigurationOptions with(Frame frame) {
		this.frame = requireNonNull(frame,"frame");
		return this;
	}
	
	public final SelectConfigurationOptions with(
		ConfigurationMatcher matcher) {
		
		this.matcher = requireNonNull(matcher, "matcher");
		return this;
	}
	
	public final SelectConfigurationOptions with(String selected) {
		this.selected = requireNonEmpty(selected, "selected");
		return this;
	}
	
	public Configuration show() {
		var matcher = this.matcher == null ? 
			DefaultConfigurationMatcher.INSTANCE : this.matcher;
		return showDialog(this.frame, this.observatory, matcher, this.selected);
	}
	
	public static SelectConfigurationOptions create(Observatory 
		observatory) {
		
		return new SelectConfigurationOptions(observatory);
	}
	
}
