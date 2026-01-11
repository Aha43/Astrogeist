package aha.common.ui.swing.panels;

import static aha.common.util.Cast.as;

import java.awt.Component;

import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.TableCellRenderer;

import aha.common.abstraction.Named;
import aha.common.abstraction.Presentable;

public final class TheTableCellrenderer extends DefaultTableCellRenderer {
	private static final long serialVersionUID = 1L;
	
	public static final TableCellRenderer INSTANCE = new TheTableCellrenderer(); 
	
	private TheTableCellrenderer() {}
	
	@Override public final Component getTableCellRendererComponent(JTable t, 
		Object v, boolean selected, boolean focus, int row, int col) {
		
		return super.getTableCellRendererComponent(t, getValue(v, col), 
			selected, focus, row, col);
	}

	public static Object getValue(Object v, int col) {
		if (v == null) return null;
		
		if (col == 0) {
			var named = as(Named.class, v);
			if (named != null) return named.displayName();
		}
		else if (col == 1) {
			var presentation = as(Presentable.class, v);
			if (presentation != null) return presentation.presentation();
		}
		
		return v;
	}
}
