package visuals;

import java.awt.EventQueue;
import java.awt.ScrollPane;
import java.io.IOException;
import java.util.Hashtable;
import java.util.Set;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.JPanel;
import javax.swing.JScrollBar;
import javax.swing.JScrollPane;
import javax.swing.JList;
import javax.swing.AbstractListModel;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

import org.mapdb.Caches.HashTable;

import libs.Process;
import tools.ProcessManager;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.awt.event.ActionEvent;
import java.awt.event.InputMethodListener;
import java.awt.event.InputMethodEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class ProcessPickerWindow {

	private JFrame frmSelectProcess;
	private JTextField textField;
	private JTable table;
	private ProcessManager processManager;
	private Hashtable<Integer, String> processTable;
	private MainWindow window;

	/**
	 * Create the window.
	 * @throws IOException 
	 */
	public ProcessPickerWindow(final MainWindow window) throws IOException {
		processManager = new ProcessManager();
		this.window = window;
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		
		
		frmSelectProcess = new JFrame();
		frmSelectProcess.setTitle("Select Process");
		frmSelectProcess.setBounds(100, 100, 450, 300);
		frmSelectProcess.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		frmSelectProcess.getContentPane().setLayout(null);
		frmSelectProcess.setVisible(true);
		frmSelectProcess.setAlwaysOnTop(true);
		
		JLabel lblFilter = new JLabel("Filter:");
		lblFilter.setBounds(12, 12, 70, 15);
		frmSelectProcess.getContentPane().add(lblFilter);
		
		textField = new JTextField();	
		
		textField.setBounds(100, 10, 328, 19);
		frmSelectProcess.getContentPane().add(textField);
		textField.setColumns(10);

		
		
		table = new JTable();
		
		
		final DefaultTableModel model = createModel();
		
		table.setModel(model);
		
		table.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				
				if (e.getClickCount() == 2) {
					window.setProcess((int) model.getValueAt(table.getSelectedRow(), 0));
					frmSelectProcess.dispose();
				}
			}
		});
		
		JScrollPane scroll = new JScrollPane();
		scroll.setBounds(22, 39, 406, 221);
		frmSelectProcess.getContentPane().add(scroll);
		
		//table.setBounds(panel.getBounds());
		scroll.setViewportView(table);
		scroll.setHorizontalScrollBar(new JScrollBar(JScrollBar.HORIZONTAL));
		
		processTable = processManager.getProcessTable();
		
		Set<Integer> keySet = processTable.keySet();
		
		for (int key : keySet) {
			model.addRow(new Object[] {key, processTable.get(key)});
		}
		
		textField.addKeyListener(new KeyAdapter() {
			@Override
			public void keyReleased(KeyEvent e) {
				while (model.getRowCount() > 0) {
					model.removeRow(model.getRowCount() - 1);
				}
				
				Set<Integer> keySet = processTable.keySet();
				
				for (int key : keySet) {
					String name = processTable.get(key);
					
					if (name.toLowerCase().contains(textField.getText().toLowerCase())) {
						model.addRow(new Object[] {key, name});
					}
				}
			}
			@Override
			public void keyTyped(KeyEvent e) {
				if (e.getKeyCode() == KeyEvent.VK_ENTER && model.getRowCount() == 1) {
					window.setProcess((int) model.getValueAt(0, 0));
					frmSelectProcess.dispose();
				}
			}
		});
		
	}
	
	private DefaultTableModel createModel() {
		return new DefaultTableModel(
				new Object[][] {},
				new String[] {
					"PID", "Name"
				}
			) {
				Class[] columnTypes = new Class[] {
					Integer.class, String.class
				};
				public Class getColumnClass(int columnIndex) {
					return columnTypes[columnIndex];
				}
				boolean[] columnEditables = new boolean[] {
					false, false
				};
				public boolean isCellEditable(int row, int column) {
					return columnEditables[column];
				}
			};
	}
}
