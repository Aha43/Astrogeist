package astrogeist.ui.swing.tool.abstraction;

import java.awt.Color;

@FunctionalInterface public interface ColorLut { Color map(double t); } // t in [0..1]
