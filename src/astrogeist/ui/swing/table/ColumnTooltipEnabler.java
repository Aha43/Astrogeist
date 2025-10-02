package astrogeist.ui.swing.table;

import java.awt.Component;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import javax.swing.JComponent;
import javax.swing.JTable;
import javax.swing.table.TableCellRenderer;

import astrogeist.common.Common;

public final class ColumnTooltipEnabler {
    private ColumnTooltipEnabler() { Common.throwStaticClassInstantiateError(); }

    /** Enable tooltips for the given view-column indices. Shows the cell's toString(). */
    public final static void enable(JTable table, int... viewCols) {
        final Set<Integer> allowed = IntStream.of(viewCols).boxed().collect(Collectors.toSet());

        table.setDefaultRenderer(Object.class, new TooltipRenderer(table.getDefaultRenderer(Object.class), allowed));
        // If you have specific renderers per type/column, wrap those too
    }

    private final static class TooltipRenderer implements TableCellRenderer {
        private final TableCellRenderer delegate;
        private final Set<Integer> allowedViewCols;

        TooltipRenderer(TableCellRenderer delegate, Set<Integer> allowedViewCols) {
            this.delegate = delegate;
            this.allowedViewCols = allowedViewCols;
        }

        @Override public final Component getTableCellRendererComponent(
        	JTable table, Object value, boolean isSelected, boolean hasFocus, 
        	int row, int column) {
        	
            var c = delegate.getTableCellRendererComponent(table, value,
            	isSelected, hasFocus, row, column);
            if (c instanceof JComponent jc) {
                if (allowedViewCols.contains(column)) {
                    jc.setToolTipText(value == null ? null : String.valueOf(value));
                } else {
                    jc.setToolTipText(null);
                }
            }
            return c;
        }
    }
    
}
