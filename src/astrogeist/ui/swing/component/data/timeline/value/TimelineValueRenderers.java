package astrogeist.ui.swing.component.data.timeline.value;

import java.awt.Component;

import javax.swing.DefaultListCellRenderer;
import javax.swing.JList;
import javax.swing.ListCellRenderer;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.TableCellRenderer;

import aha.common.Guards;
import astrogeist.engine.timeline.TimelineValue;

public final class TimelineValueRenderers {
    public static ListCellRenderer<Object> listCellRenderer() {
        return new DefaultListCellRenderer() {
            private static final long serialVersionUID = 1L;

            @Override public Component getListCellRendererComponent(
                JList<?> list, Object value, int index, boolean isSelected, boolean cellHasFocus) {

                super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);

                if (value instanceof TimelineValue tv) {
                	if (tv == TimelineValue.Empty) 
                		setText("Empty");
                	else                           
                		setText(tv.value());
                }

                return this;
            }
        };
    }

    public static TableCellRenderer tableCellRenderer() {
        return new DefaultTableCellRenderer() {
            private static final long serialVersionUID = 1L;

            @Override protected void setValue(Object value) {
                if (value instanceof TimelineValue tv) 
                	setText(tv.value());
                else                                   
                	super.setValue(value);
            }
        };
    }
    
    private TimelineValueRenderers() { Guards.throwStaticClassInstantiateError(); }
}

