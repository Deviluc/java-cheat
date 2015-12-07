package visuals.tables;

import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import models.Value;
import tools.MemoryWatcher;

import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

import org.mapdb.HTreeMap;

import events.TransferEvent;
import interfaces.Notifiable;
import interfaces.Trigger;
import libs.Constants;

public class SimpleValueTable extends JScrollPane implements Notifiable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -5685761884094929113L;

	private DefaultTableModel model;
	private JTable table;
	private List<Value> valueList;
	private MemoryWatcher memWatch;

	public SimpleValueTable(MemoryWatcher memWatch, final Trigger parentComponent) {
		super();
		model = new DefaultTableModel(new Object[][] {}, new String[] {"Address", "Value"});
		
		table = new JTable();
		table.setModel(model);
		this.setViewportView(table);
		
		JPopupMenu popupMenu = new JPopupMenu();
		addPopup(table, popupMenu);
		
		JMenuItem menuAdd = new JMenuItem("Add");
		menuAdd.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				int index = table.getSelectedRow();
				
				if (index > -1) {
					parentComponent.triggerEvent(new TransferEvent(Constants.TARGET_TABLE_CHEATS, valueList.get(index)));
				}
				
			}
		});
		
		popupMenu.add(menuAdd);
		
		this.memWatch = memWatch;
		//memWatch.registerComponent(this, valueList);
	}
	
	public void applyAddressMap(final HTreeMap<Long, Object> addressMap, final int valueType) {
		clear();
		
		Set<Long> keys = addressMap.keySet();
		
		valueList = new ArrayList<Value>();
		
		for (Long key : keys) {
			Value tmpValue = new Value(key);
			tmpValue.setValueType(valueType);
			
			valueList.add(tmpValue);
		}
		
		refreshModelEntries();
		
	}
	
	public void clear() {
		memWatch.unregisterComponent(this);
		
		clearModel();
		valueList = null;
	}
	
	public void dispose() {
		memWatch.unregisterComponent(this);
	}
	
	@Override
	public void notifyListChanged(List<Integer> indexes) {
		for (int i : indexes) {
			model.setValueAt(valueList.get(i).getFormattedValue(), i, 1);
		}
		
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
	
	private void refreshModelEntries() {
		if (valueList != null) {
			clearModel();
			
			for (Value val : valueList) {
				model.addRow(new Object[] {val.getFormattedAddress(), val.getFormattedValue()});
			}
			
			memWatch.registerComponent(this, valueList);
		}
	}
	
	private void clearModel() {
		while (model.getRowCount() > 0) {
			model.removeRow(model.getRowCount() - 1);
		}
	}

	


}
