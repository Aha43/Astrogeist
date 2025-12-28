package aha.common.ui.swing.panels;

import static aha.common.util.Cast.as;

import java.awt.Component;

import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.TableCellRenderer;

import aha.common.abstraction.Presentable;

public final class TheTableCellrenderer extends DefaultTableCellRenderer {
	private static final long serialVersionUID = 1L;
	
	public static final TableCellRenderer INSTANCE = new TheTableCellrenderer(); 
	
	private TheTableCellrenderer() {}
	
	@Override public final Component getTableCellRendererComponent(JTable t, 
		Object v, boolean selected, boolean focus, int row, int col) {
		
		return super.getTableCellRendererComponent(t, getValue(v), selected,
			focus, row, col);
	}

	public static Object getValue(Object v) {
		if (v == null) return null;
		var presentation = as(Presentable.class, v);
		return presentation == null ? v : presentation.presentation(); 
	}
}
