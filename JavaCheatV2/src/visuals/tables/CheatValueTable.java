package visuals.tables;

import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.List;
import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.TableColumnModel;
import components.CheatValueManager;
import events.EditEvent;
import exceptions.ProcessNotFoundException;
import exceptions.model.DebuggerException;
import interfaces.Notifiable;
import interfaces.Trigger;
import libs.Constants;
import models.Value;
import tools.MemoryAccess;
import tools.MemoryWatcher;

public class CheatValueTable extends JScrollPane  {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 5125431986269259739L;
	private JTable table;
	private CheatTableModel model;
	private CheatValueManager manager;

	public CheatValueTable(final Trigger parentComponent)  {
		super();
		
		manager = new CheatValueManager();
		model = new CheatTableModel(manager.getValues());
		table = new JTable(model);
		
		manager.setCheatValueTable(this);
		
		TableColumnModel columnModel = table.getColumnModel();
		
		columnModel.getColumn(0).setPreferredWidth(15);
		columnModel.getColumn(1).setPreferredWidth(50);
		columnModel.getColumn(2).setPreferredWidth(30);
		columnModel.getColumn(3).setPreferredWidth(25);
		
		//table.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
		
		this.setViewportView(table);
		
		JPopupMenu popupMenu = new JPopupMenu();
		addPopup(table, popupMenu);
		
		JMenuItem edit = new JMenuItem("Edit");
		edit.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				parentComponent.triggerEvent(new EditEvent(Constants.TARGET_EDIT_CHEAT_VALUE, model.getValue(table.getSelectedRow())));
			}
		});
		
		popupMenu.add(edit);
	}
	
	public void updateTable() {
		int selectedRow = table.getSelectedRow();
		model.fireTableDataChanged();
		if (selectedRow > -1) {
			table.setRowSelectionInterval(selectedRow, selectedRow);
		}
	}
	
	public void setMemoryAccess(final MemoryAccess memoryAcess) {
		manager.setMemoryAccess(memoryAcess);
		
	}
	
	public CheatTableModel getModel() {
		return model;
	}
	
	private void addPopup(Component component, final JPopupMenu popup) {
		component.addMouseListener(new MouseAdapter() {
			public void mousePressed(MouseEvent e) {
				if (e.isPopupTrigger()) {
					showMenu(e);
				}
			}
			public void mouseReleased(MouseEvent e) {
				if (e.isPopupTrigger()) {
					showMenu(e);
				}
			}
			private void showMenu(MouseEvent e) {
				popup.show(e.getComponent(), e.getX(), e.getY());
			}
		});
	}
	
	
	public class CheatTableModel extends AbstractTableModel {
		
		/**
		 * 
		 */
		private static final long serialVersionUID = 776568915730000160L;
		private String[] columnNames;
		private int rowCount;
		private List<Value> values;
		
		public CheatTableModel(final List<Value> values) {
			columnNames = new String[] {"Active", "Description", "Address", "Value"};
			this.values = values;
		}
		
		public void addValue(Value value) {
			if (!values.contains(value)) {
				values.add(value);
			}
		}
		
		public void updateValue(Value value) {
			int index = values.indexOf(value);
			
			if (index > -1) {
				values.set(index, value);
			}
		}
		
		public void removeValue(Value value) {
			int index = values.indexOf(value);
			
			if (index > -1) {
				values.remove(index);
			}
		}
		
		@Override
		public String getColumnName(int columnIndex) {
			return columnNames[columnIndex];
		}
		
		public Value getValue(int rowIndex) {
			return values.get(rowIndex);
		}

		@Override
		public int getRowCount() {
			return values.size();
		}

		@Override
		public int getColumnCount() {
			return 4;
		}

		@Override
		public Object getValueAt(int rowIndex, int columnIndex) {
			if (rowIndex < values.size() && columnIndex < 4) {
				
				Value value = values.get(rowIndex);
				
				switch (columnIndex) {
				case 0:
					return value.getFreeze();
				case 1:
					return value.getDescription();
				case 2:
					return value.getFormattedAddress();
				case 3:
					return value.getFormattedValue();
				default:
					return null;
				}
				
			} else {
				throw new IllegalArgumentException("Expected row-index < " + rowCount + ", column-index < 4; Got " + rowIndex + ", " + columnIndex);
			}
		}

		
		
	}


	

}
