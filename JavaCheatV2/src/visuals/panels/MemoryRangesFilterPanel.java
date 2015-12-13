package visuals.panels;

import javax.swing.JPanel;
import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.JLabel;
import java.awt.Color;
import javax.swing.SwingConstants;

import interfaces.Trigger;
import libs.Constants;
import libs.MemoryRangeFilter;

import javax.swing.LayoutStyle.ComponentPlacement;

import events.FilterEvent;

import javax.swing.JTextField;
import javax.swing.JRadioButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;
import java.awt.event.ActionEvent;

public class MemoryRangesFilterPanel extends JPanel {
	private JTextField textFieldFilename;
	private JTextField textFieldSizeMin;
	private JTextField textFieldSizeMax;
	
	private JRadioButton rdbtnRegex;
	private JRadioButton rdbtnIncludeHeap;
	private JRadioButton rdbtnIncludeStack;
	private JRadioButton rdbtnIncludeFilemappedMemory;
	
	private JComboBox<String> comboBoxRead;
	private JComboBox<String> comboBoxWrite;
	private JComboBox<String> comboBoxExecute;
	private JComboBox<String> comboBoxType;
	
	private Trigger parent;

	/**
	 * Create the panel.
	 */
	public MemoryRangesFilterPanel(final Trigger parentComponent) {
		parent = parentComponent;
		
		JLabel lblFilter = new JLabel("Filter");
		lblFilter.setHorizontalAlignment(SwingConstants.CENTER);
		lblFilter.setForeground(new Color(0, 128, 0));
		
		JLabel lblFilename = new JLabel("Filename:");
		
		textFieldFilename = new JTextField();
		textFieldFilename.setColumns(10);
		
		rdbtnRegex = new JRadioButton("RegEx");
		
		JLabel lblSizeMin = new JLabel("Size (min):");
		
		textFieldSizeMin = new JTextField();
		textFieldSizeMin.setToolTipText("Size in mb, leave empty to ignore");
		textFieldSizeMin.setColumns(10);
		
		JLabel lblSizemax = new JLabel("Size (max):");
		
		textFieldSizeMax = new JTextField();
		textFieldSizeMax.setToolTipText("Size in mb, leave empty to ignore");
		textFieldSizeMax.setColumns(10);
		
		rdbtnIncludeHeap = new JRadioButton("Include heap");
		rdbtnIncludeHeap.setSelected(true);
		
		rdbtnIncludeStack = new JRadioButton("Include stack");
		
		JLabel lblRead = new JLabel("Read:");
		
		comboBoxRead = new JComboBox();
		comboBoxRead.setModel(new DefaultComboBoxModel(new String[] {"yes", "no", "both"}));
		
		JLabel lblWrite = new JLabel("Write:");
		
		comboBoxWrite = new JComboBox();
		comboBoxWrite.setModel(new DefaultComboBoxModel(new String[] {"yes", "no", "both"}));
		
		JLabel lblType = new JLabel("Type:");
		
		comboBoxType = new JComboBox();
		comboBoxType.setModel(new DefaultComboBoxModel(new String[] {"shared", "private", "both"}));
		comboBoxType.setSelectedIndex(2);
		
		JButton btnFilter = new JButton("Filter");
		btnFilter.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				filter();
			}
		});
		
		JButton btnReset = new JButton("Reset");
		btnReset.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				reset();
			}
		});
		
		JButton btnClear = new JButton("Clear");
		btnClear.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				clear();
			}
		});
		
		JLabel lblExecute = new JLabel("Execute:");
		
		comboBoxExecute = new JComboBox();
		comboBoxExecute.setModel(new DefaultComboBoxModel(new String[] {"yes", "no", "both"}));
		comboBoxExecute.setSelectedIndex(1);
		
		rdbtnIncludeFilemappedMemory = new JRadioButton("Include file mapped memory");
		GroupLayout groupLayout = new GroupLayout(this);
		groupLayout.setHorizontalGroup(
			groupLayout.createParallelGroup(Alignment.LEADING)
				.addGroup(groupLayout.createSequentialGroup()
					.addContainerGap()
					.addGroup(groupLayout.createParallelGroup(Alignment.LEADING)
						.addComponent(rdbtnIncludeFilemappedMemory)
						.addGroup(groupLayout.createSequentialGroup()
							.addComponent(rdbtnIncludeStack)
							.addGap(18)
							.addComponent(rdbtnIncludeHeap))
						.addComponent(lblFilter, Alignment.TRAILING, GroupLayout.DEFAULT_SIZE, 302, Short.MAX_VALUE)
						.addComponent(rdbtnRegex, Alignment.TRAILING)
						.addGroup(Alignment.TRAILING, groupLayout.createSequentialGroup()
							.addGroup(groupLayout.createParallelGroup(Alignment.LEADING)
								.addComponent(lblSizemax)
								.addComponent(lblSizeMin)
								.addComponent(lblFilename))
							.addGap(26)
							.addGroup(groupLayout.createParallelGroup(Alignment.LEADING)
								.addComponent(textFieldFilename, GroupLayout.DEFAULT_SIZE, 199, Short.MAX_VALUE)
								.addComponent(textFieldSizeMin, GroupLayout.DEFAULT_SIZE, 199, Short.MAX_VALUE)
								.addComponent(textFieldSizeMax, GroupLayout.DEFAULT_SIZE, 199, Short.MAX_VALUE)))
						.addGroup(groupLayout.createSequentialGroup()
							.addGroup(groupLayout.createParallelGroup(Alignment.LEADING)
								.addComponent(lblRead)
								.addComponent(lblWrite)
								.addComponent(lblExecute)
								.addComponent(lblType))
							.addGap(26)
							.addGroup(groupLayout.createParallelGroup(Alignment.LEADING)
								.addComponent(comboBoxType, 0, 216, Short.MAX_VALUE)
								.addComponent(comboBoxExecute, 0, 216, Short.MAX_VALUE)
								.addComponent(comboBoxWrite, 0, 216, Short.MAX_VALUE)
								.addComponent(comboBoxRead, 0, 216, Short.MAX_VALUE)))
						.addGroup(groupLayout.createSequentialGroup()
							.addComponent(btnClear)
							.addPreferredGap(ComponentPlacement.UNRELATED)
							.addComponent(btnReset)
							.addPreferredGap(ComponentPlacement.RELATED, 73, Short.MAX_VALUE)
							.addComponent(btnFilter)))
					.addContainerGap())
		);
		groupLayout.setVerticalGroup(
			groupLayout.createParallelGroup(Alignment.LEADING)
				.addGroup(groupLayout.createSequentialGroup()
					.addContainerGap()
					.addComponent(lblFilter)
					.addPreferredGap(ComponentPlacement.RELATED)
					.addGroup(groupLayout.createParallelGroup(Alignment.BASELINE)
						.addComponent(lblFilename)
						.addComponent(textFieldFilename, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
					.addPreferredGap(ComponentPlacement.RELATED)
					.addComponent(rdbtnRegex)
					.addPreferredGap(ComponentPlacement.UNRELATED)
					.addGroup(groupLayout.createParallelGroup(Alignment.BASELINE)
						.addComponent(lblSizeMin)
						.addComponent(textFieldSizeMin, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
					.addPreferredGap(ComponentPlacement.UNRELATED)
					.addGroup(groupLayout.createParallelGroup(Alignment.BASELINE)
						.addComponent(lblSizemax)
						.addComponent(textFieldSizeMax, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
					.addPreferredGap(ComponentPlacement.UNRELATED)
					.addGroup(groupLayout.createParallelGroup(Alignment.BASELINE)
						.addComponent(rdbtnIncludeStack)
						.addComponent(rdbtnIncludeHeap))
					.addPreferredGap(ComponentPlacement.UNRELATED)
					.addComponent(rdbtnIncludeFilemappedMemory)
					.addPreferredGap(ComponentPlacement.RELATED)
					.addGroup(groupLayout.createParallelGroup(Alignment.BASELINE)
						.addComponent(lblRead)
						.addComponent(comboBoxRead, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
					.addPreferredGap(ComponentPlacement.UNRELATED)
					.addGroup(groupLayout.createParallelGroup(Alignment.BASELINE)
						.addComponent(lblWrite)
						.addComponent(comboBoxWrite, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
					.addPreferredGap(ComponentPlacement.UNRELATED)
					.addGroup(groupLayout.createParallelGroup(Alignment.BASELINE)
						.addComponent(lblExecute)
						.addComponent(comboBoxExecute, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
					.addPreferredGap(ComponentPlacement.UNRELATED)
					.addGroup(groupLayout.createParallelGroup(Alignment.BASELINE)
						.addComponent(lblType)
						.addComponent(comboBoxType, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
					.addPreferredGap(ComponentPlacement.UNRELATED)
					.addGroup(groupLayout.createParallelGroup(Alignment.BASELINE)
						.addComponent(btnFilter)
						.addComponent(btnReset)
						.addComponent(btnClear))
					.addContainerGap(164, Short.MAX_VALUE))
		);
		setLayout(groupLayout);

	}
	
	private void filter() {
		List<MemoryRangeFilter> filters = new ArrayList<MemoryRangeFilter>();
		
		String[] permissionMapRead = new String[] {"r", "-", "[r-]"};
		String[] permissionMapWrite = new String[] {"w", "-", "[w-]"};
		String[] permissionMapExecute = new String[] {"x", "-", "[x-]"};
		String[] permissionMapType = new String[] {"s", "p", "[sp]"};
		String permissionRegex = permissionMapRead[comboBoxRead.getSelectedIndex()] + permissionMapWrite[comboBoxWrite.getSelectedIndex()]
				+ permissionMapExecute[comboBoxExecute.getSelectedIndex()] + permissionMapType[comboBoxType.getSelectedIndex()];
		
		MemoryRangeFilter baseFilter = new MemoryRangeFilter(permissionRegex);
		filters.add(baseFilter);
		
		boolean hasText = false;
		
		if (textFieldFilename.getText().length() != 0) {
			if (rdbtnRegex.isSelected()) {
				baseFilter.setFilenameRegex(textFieldFilename.getText());
			} else {
				baseFilter.setFilenameString(textFieldFilename.getText());
			}
			
			hasText = true;
		}
		
		try {
			String minSizeString = textFieldSizeMin.getText().replace(",", ".");
			if (minSizeString.length() != 0) {
				baseFilter.setMinSize(Double.parseDouble(minSizeString));
			}
		} catch (NullPointerException | NumberFormatException e) {
			
		}
		
		try {
			String maxSizeString = textFieldSizeMax.getText().replace(",", ".");
			if (maxSizeString.length() != 0) {
				baseFilter.setMaxSize(Double.parseDouble(maxSizeString));
			}
		} catch (NullPointerException | NumberFormatException e) {
			
		}
		
		baseFilter.setFromFile(false);
		baseFilter.setIsHeap(false);
		baseFilter.setIsStack(false);

		if (rdbtnIncludeFilemappedMemory.isSelected() || hasText) {
			MemoryRangeFilter clone = baseFilter.clone();
			clone.setFromFile(true);
			filters.add(clone);
		}
		
		if (rdbtnIncludeHeap.isSelected()) {
			MemoryRangeFilter clone = baseFilter.clone();
			clone.setIsHeap(true);
			filters.add(clone);
		}
		
		if (rdbtnIncludeStack.isSelected()) {
			MemoryRangeFilter clone = baseFilter.clone();
			clone.setIsStack(true);
			filters.add(clone);
		}
		
		parent.triggerEvent(new FilterEvent(Constants.TARGET_FILTER, filters.toArray()));
		
	}
	
	private void reset() {
		textFieldFilename.setText("");
		textFieldSizeMax.setText("");
		textFieldSizeMin.setText("");
		
		comboBoxRead.setSelectedIndex(0);
		comboBoxWrite.setSelectedIndex(0);
		comboBoxExecute.setSelectedIndex(1);
		comboBoxType.setSelectedIndex(2);
		
		rdbtnIncludeFilemappedMemory.setSelected(false);
		rdbtnIncludeHeap.setSelected(true);
		rdbtnIncludeStack.setSelected(false);
		rdbtnRegex.setSelected(false);
		
	}
	
	private void clear() {
		parent.triggerEvent(new FilterEvent(Constants.TARGET_FILTER_RESET));
	}
}
