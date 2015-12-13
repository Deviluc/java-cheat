package visuals;

import java.awt.BorderLayout;
import java.awt.EventQueue;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import events.FilterEvent;
import events.TransferEvent;
import interfaces.Event;
import interfaces.Trigger;
import libs.Constants;
import libs.MemoryRangeFilter;
import tools.MemoryMap;
import visuals.panels.MemoryRangesFilterPanel;
import visuals.tables.MemoryRangeTable;

import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.LayoutStyle.ComponentPlacement;
import javax.swing.UIManager;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

public class MemoryRangesWindow extends JFrame implements Trigger {

	private JPanel contentPane;
	private MemoryRangeTable memoryRanges;

	/**
	 * Create the frame.
	 * @throws IOException 
	 */
	public MemoryRangesWindow(final MemoryMap memoryMap, final Trigger parentComponent) throws IOException {
		setTitle("Memory Ranges");
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setBounds(100, 100, 825, 400);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		
		memoryRanges = new MemoryRangeTable(memoryMap);
		
		MemoryRangesFilterPanel panel = new MemoryRangesFilterPanel(this);
		panel.setBorder(UIManager.getBorder("OptionPane.border"));
		
		JButton btnSave = new JButton("Save");
		btnSave.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				TransferEvent event = new TransferEvent(Constants.TARGET_FILTER_LIST, memoryRanges.getModel().getSelectedRanges());
				parentComponent.triggerEvent(event);
				dispose();
			}
		});
		
		JButton btnCancel = new JButton("Cancel");
		btnCancel.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				dispose();
			}
		});
		GroupLayout gl_contentPane = new GroupLayout(contentPane);
		gl_contentPane.setHorizontalGroup(
			gl_contentPane.createParallelGroup(Alignment.TRAILING)
				.addGroup(gl_contentPane.createSequentialGroup()
					.addContainerGap()
					.addGroup(gl_contentPane.createParallelGroup(Alignment.LEADING)
						.addGroup(gl_contentPane.createSequentialGroup()
							.addComponent(btnSave)
							.addPreferredGap(ComponentPlacement.UNRELATED)
							.addComponent(btnCancel))
						.addComponent(memoryRanges, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
					.addPreferredGap(ComponentPlacement.UNRELATED)
					.addComponent(panel, GroupLayout.PREFERRED_SIZE, 280, GroupLayout.PREFERRED_SIZE))
		);
		gl_contentPane.setVerticalGroup(
			gl_contentPane.createParallelGroup(Alignment.LEADING)
				.addGroup(Alignment.TRAILING, gl_contentPane.createSequentialGroup()
					.addContainerGap()
					.addGroup(gl_contentPane.createParallelGroup(Alignment.TRAILING)
						.addComponent(panel, Alignment.LEADING, GroupLayout.DEFAULT_SIZE, 301, Short.MAX_VALUE)
						.addComponent(memoryRanges, Alignment.LEADING, GroupLayout.DEFAULT_SIZE, 301, Short.MAX_VALUE))
					.addPreferredGap(ComponentPlacement.UNRELATED)
					.addGroup(gl_contentPane.createParallelGroup(Alignment.BASELINE)
						.addComponent(btnSave)
						.addComponent(btnCancel))
					.addContainerGap())
		);
		contentPane.setLayout(gl_contentPane);
	}

	@Override
	public void triggerEvent(Event event) {
		
		if (event.getEventClass() == Constants.EVENT_FILTER) {
			try {
				if (event.getTarget() == Constants.TARGET_FILTER_RESET) {
					memoryRanges.getModel().resetFilter();
	
				} else {
					FilterEvent filterEvent = (FilterEvent) event;
					Object[] rangeFilters = filterEvent.getFilters();
					List<MemoryRangeFilter> filters = new ArrayList<MemoryRangeFilter>();
					
					for (Object filter : rangeFilters) {
						filters.add((MemoryRangeFilter) filter);
					}
					memoryRanges.getModel().filterRanges(filters);
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		
	}
}
