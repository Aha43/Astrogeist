package aha.common.ui.swing.panels.genericlist;

import java.awt.Window;

/**
 * Strategy interface for creating, editing and deleting list items.
 * Implementations typically show dialogs, validate input, etc.
 *
 * @param <T> the element type shown in the list
 */
public interface ListItemHandler<T> {

    /**
     * Called when the user clicks "Add".
     *
     * @param parent parent window (may be {@code null})
     * @return a new item to add, or {@code null} if the user cancelled
     */
    T createNew(Window parent);

    /**
     * Called when the user clicks "Edit".
     *
     * @param parent   parent window (may be {@code null})
     * @param existing the currently selected item
     * @return the updated item, or {@code null} if the user cancelled
     */
    T edit(Window parent, T existing);

    /**
     * Called when the user clicks "Delete".
     * You can show a confirmation dialog here if you want.
     *
     * @param parent   parent window (may be {@code null})
     * @param existing the currently selected item
     * @return {@code true} if the caller should remove the item from the list model
     */
    boolean confirmDelete(Window parent, T existing);
}

