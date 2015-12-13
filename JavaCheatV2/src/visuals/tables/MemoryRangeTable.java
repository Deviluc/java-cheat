package visuals.tables;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JCheckBox;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.TableColumnModel;

import libs.MemoryRange;
import libs.MemoryRangeFilter;
import tools.MemoryMap;
import visuals.tables.CheatValueTable.CheatTableModel;

public class MemoryRangeTable extends JScrollPane {

	/**
	 * 
	 */
	private static final long serialVersionUID = 352718094447834048L;
	
	private MemoryRangeTableModel model;
	private JTable table;

	public MemoryRangeTable(final MemoryMap memoryMap) throws IOException {
		super();
		
		model = new MemoryRangeTableModel(memoryMap);
		table = new JTable(model);
		
		TableColumnModel columnModel = table.getColumnModel();
		
		columnModel.getColumn(0).setWidth(15);
		columnModel.getColumn(1).setPreferredWidth(80);
		columnModel.getColumn(2).setPreferredWidth(80);
		columnModel.getColumn(3).setWidth(15);
		columnModel.getColumn(4).setWidth(15);
		columnModel.getColumn(5).setWidth(15);
		columnModel.getColumn(6).setPreferredWidth(30);
		columnModel.getColumn(7).setPreferredWidth(150);
		columnModel.getColumn(8).setPreferredWidth(30);
		
		//table.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
		
		this.setViewportView(table);
	}
	
	public MemoryRangeTableModel getModel() {
		return model;
	}
	
	public class MemoryRangeTableModel extends AbstractTableModel {

		/**
		 * 
		 */
		private static final long serialVersionUID = -1132532040029110837L;
		
		private List<MemoryRange> memoryRanges;
		private List<MemoryRange> selectedRanges;
		
		private MemoryMap memoryMap;
		
		private String[] columnNames;
		
		public MemoryRangeTableModel(final MemoryMap memoryMap) throws IOException {
			this.memoryRanges = memoryMap.getRanges();
			selectedRanges = new ArrayList<MemoryRange>();
			this.memoryMap = memoryMap;
			
			columnNames = new String[] {"Use", "Start", "End", "Read", "Write", "Execute", "Type", "File", "Size"};

		}
		
		public void filterRanges(List<MemoryRangeFilter> rangeFilters) throws IOException {
			memoryRanges = memoryMap.getFilteredRanges(rangeFilters);
			fireTableDataChanged();
		}
		
		public void resetFilter() throws IOException {
			memoryRanges = memoryMap.getRanges();
			fireTableDataChanged();
		}
		
		public Object[] getSelectedRanges() {
			return selectedRanges.toArray();
		}
		
		@Override
		public String getColumnName(int columnIndex) {
			return columnNames[columnIndex];
		}

		@Override
		public int getRowCount() {
			return memoryRanges.size();
		}

		@Override
		public int getColumnCount() {
			return columnNames.length;
		}
		
		@Override 
        public boolean isCellEditable(int row, int col){
            if(col == 0){
                return true;
            }
            
            return false;
        }
		
		@Override
		public void setValueAt(Object value, int rowIndex, int columnIndex) {
			if (columnIndex == 0) {
				
				MemoryRange range = memoryRanges.get(rowIndex);
				
				if ((Boolean) value && !selectedRanges.contains(range)) {
					selectedRanges.add(range);
				} else if (selectedRanges.contains(range)) {
					selectedRanges.remove(range);
				}
			}
		}
		
		
		@SuppressWarnings({ "unchecked", "rawtypes" })
		@Override
		public Class getColumnClass(int column) {
			switch (column) {
			case 0:
				return Boolean.class;
			case 3:
				return Boolean.class;
			case 4:
				return Boolean.class;
			case 5:
				return Boolean.class;
			default:
				return String.class;
			}
		}

		@Override
		public Object getValueAt(final int rowIndex, final int columnIndex) {
			
			final MemoryRange selectedRange = memoryRanges.get(rowIndex);
			
			switch (columnIndex) {
			case 0:
				return selectedRanges.contains(selectedRange);
				/*
				final JCheckBox btn = new JCheckBox("");
				btn.addActionListener(new ActionListener() {
					
					@Override
					public void actionPerformed(ActionEvent e) {
						if (btn.isSelected() && !selectedRanges.contains(selectedRange)) {
							selectedRanges.add(memoryRanges.get(rowIndex));
						} else if (!btn.isSelected() && selectedRanges.contains(selectedRange)) {
							selectedRanges.remove(memoryRanges.get(rowIndex));
						}
						
					}
				});
				return btn;*/
			case 1:
				return Long.toHexString(selectedRange.getStart()).toUpperCase();
			case 2:
				return Long.toHexString(selectedRange.getEnd()).toUpperCase();
			case 3:
				return selectedRange.canWrite();
			case 4:
				return selectedRange.canWrite();
			case 5:
				return selectedRange.canExecute();
			case 6:
				if (selectedRange.isPrivate()) {
					return "privat";
				} else {
					return "shared";
				}
			case 7:
				return selectedRange.getFile();
			case 8:
				double result = ((double) selectedRange.getByteSize()) / (1024d * 1024d);
				
				if (result < 1d) {
					return fmt(result * 1024d) + "kb";
				} else {
					return fmt(result) + "mb";
				}
				
			default:
				return null;
			}
		}
		
		private String fmt(double d) {
		    if(d == (long) d)
		        return String.format("%d",(long)d);
		    else
		        return String.format("%f",d);
		}
		
	}

}
