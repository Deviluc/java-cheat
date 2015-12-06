package visuals.tables;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import models.Value;
import tools.MemoryWatcher;

import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

import org.mapdb.HTreeMap;

import interfaces.Notifiable;

public class SimpleValueTable extends JScrollPane implements Notifiable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -5685761884094929113L;

	private DefaultTableModel model;
	private JTable table;
	private List<Value> valueList;
	private MemoryWatcher memWatch;

	public SimpleValueTable(MemoryWatcher memWatch) {
		super();
		model = new DefaultTableModel(new Object[][] {}, new String[] {"Address", "Value"});
		
		table = new JTable();
		table.setModel(model);
		this.setViewportView(table);
		
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
