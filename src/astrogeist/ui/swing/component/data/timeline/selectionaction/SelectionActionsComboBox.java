package astrogeist.ui.swing.component.data.timeline.selectionaction;

import java.util.ArrayList;

import javax.swing.DefaultComboBoxModel;
import javax.swing.DefaultListCellRenderer;
import javax.swing.JComboBox;

import astrogeist.engine.typesystem.Type;

public class SelectionActionsComboBox extends JComboBox<AbstractSelectionAction> {
	private static final long serialVersionUID = 1L;
	
	public SelectionActionsComboBox() {
		var actions = new ArrayList<AbstractSelectionAction>();
	    actions.add(NoSelectionAction.INSTANCE);
	    actions.add(new OpenFileSelectionAction(Type.SerFile()));
	    super.setModel(new DefaultComboBoxModel<>(actions.toArray(AbstractSelectionAction.EMPTY_ARRAY)));
	    
	    super.setRenderer((list, value, index, isSelected, cellHasFocus) -> {
	        var lbl = new DefaultListCellRenderer();
	        lbl = (DefaultListCellRenderer) lbl.getListCellRendererComponent(list,
	        		value == null ? "" : value.getName(), index, isSelected, cellHasFocus);
	        return lbl;
	    });
	    super.setSelectedItem(NoSelectionAction.INSTANCE);
	}

}
