package com.everyware.util.ui;

import javax.swing.table.AbstractTableModel;

public abstract class ExtendedTableModel extends AbstractTableModel {
	private static final long serialVersionUID = 1L;

	public Object getDisplayValueAt(int row, int col) {
		return getValueAt(row, col);
	}
}
