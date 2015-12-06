package visuals;

import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JLabel;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JMenu;
import java.awt.Font;
import javax.swing.JTextField;
import javax.swing.SwingConstants;

import org.mapdb.HTreeMap;

import components.LogicManager;
import events.UpdateEvent;
import exceptions.ProcessNotFoundException;
import interfaces.Event;
import interfaces.Trigger;
import libs.Constants;
import visuals.tables.SimpleValueTable;

import javax.swing.JComboBox;
import javax.swing.JButton;
import javax.swing.DefaultComboBoxModel;
import java.io.IOException;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import javax.swing.JProgressBar;

public class MainWindow implements Trigger {

	private JFrame frmJavacheat;
	private JTextField textField;
	private JTextField textField_1;
	private JTextField textField_2;
	private JTextField textField_3;
	
	private JLabel lblProcess;
	private JLabel lblResults;
	
	private JComboBox<String> comboBoxValueType;
	private JComboBox<String> comboBoxScanType;
	
	private LogicManager logicManager;
	
	private SimpleValueTable valueTable;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					MainWindow window = new MainWindow();
					window.frmJavacheat.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public MainWindow() {
		logicManager = new LogicManager(this);
		
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frmJavacheat = new JFrame();
		frmJavacheat.setResizable(false);
		frmJavacheat.setTitle("JavaCheat");
		frmJavacheat.setBounds(100, 100, 700, 800);
		frmJavacheat.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frmJavacheat.getContentPane().setLayout(null);
		
		valueTable = new SimpleValueTable(logicManager.getMemoryWatcher());
		valueTable.setBounds(12, 61, 350, 400);
		frmJavacheat.getContentPane().add(valueTable);
		
		lblResults = new JLabel("Results:");
		lblResults.setFont(new Font("Comic Sans MS", Font.BOLD | Font.ITALIC, 14));
		lblResults.setBounds(12, 34, 350, 15);
		frmJavacheat.getContentPane().add(lblResults);
		
		JLabel label = new JLabel("Search Parameter:");
		label.setFont(new Font("Comic Sans MS", Font.BOLD | Font.ITALIC, 14));
		label.setBounds(374, 61, 200, 30);
		frmJavacheat.getContentPane().add(label);
		
		JLabel label_1 = new JLabel("Value:");
		label_1.setToolTipText("Value to search for");
		label_1.setBounds(380, 111, 70, 15);
		frmJavacheat.getContentPane().add(label_1);
		
		JLabel label_2 = new JLabel("Type:");
		label_2.setToolTipText("Type of searched value");
		label_2.setBounds(380, 138, 70, 15);
		frmJavacheat.getContentPane().add(label_2);
		
		textField = new JTextField();
		textField.setToolTipText("Decimal value");
		textField.setText("0");
		textField.setHorizontalAlignment(SwingConstants.LEFT);
		textField.setColumns(10);
		textField.setBounds(524, 109, 164, 19);
		frmJavacheat.getContentPane().add(textField);
		
		comboBoxValueType = new JComboBox<String>();
		comboBoxValueType.setModel(new DefaultComboBoxModel<String>(logicManager.getValueTypes()));
		comboBoxValueType.setSelectedIndex(2);
		comboBoxValueType.setBounds(524, 133, 164, 24);
		frmJavacheat.getContentPane().add(comboBoxValueType);
		
		JButton button = new JButton("Search");
		button.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					logicManager.search(comboBoxValueType.getSelectedIndex(), comboBoxScanType.getSelectedIndex(), textField.getText());
				} catch (IllegalStateException | IllegalArgumentException | IOException e1) {
					e1.printStackTrace();
				}
			}
		});
		button.setBounds(374, 196, 117, 25);
		frmJavacheat.getContentPane().add(button);
		
		JButton button_1 = new JButton("Reset");
		button_1.setBounds(503, 196, 117, 25);
		frmJavacheat.getContentPane().add(button_1);
		
		comboBoxScanType = new JComboBox<String>();
		comboBoxScanType.setModel(new DefaultComboBoxModel<String>(logicManager.getScanTypes()));
		comboBoxScanType.setSelectedIndex(0);
		comboBoxScanType.setBounds(468, 160, 220, 24);
		frmJavacheat.getContentPane().add(comboBoxScanType);
		
		JLabel label_3 = new JLabel("Filter:");
		label_3.setToolTipText("Set how the entered value should be handled");
		label_3.setBounds(380, 165, 70, 15);
		frmJavacheat.getContentPane().add(label_3);
		
		JLabel label_4 = new JLabel("Debuger:");
		label_4.setFont(new Font("Comic Sans MS", Font.BOLD | Font.ITALIC, 14));
		label_4.setBounds(380, 219, 200, 30);
		frmJavacheat.getContentPane().add(label_4);
		
		JButton button_2 = new JButton("Attach");
		button_2.setToolTipText("Attach debugger to currently selected Process (pause)");
		button_2.setBounds(374, 261, 117, 25);
		frmJavacheat.getContentPane().add(button_2);
		
		JButton button_3 = new JButton("Detach");
		button_3.setToolTipText("Detach debugger from currently selected Process (resume)");
		button_3.setBounds(503, 261, 117, 25);
		frmJavacheat.getContentPane().add(button_3);
		
		JLabel label_5 = new JLabel("Read buffer size:");
		label_5.setToolTipText("Size of the Memory buffer used to scan the process.\n(This alocates memory in the ram, so you need that much free ram.)");
		label_5.setBounds(380, 298, 125, 15);
		frmJavacheat.getContentPane().add(label_5);
		
		textField_1 = new JTextField();
		textField_1.setToolTipText("Size in MB");
		textField_1.setText("512");
		textField_1.setHorizontalAlignment(SwingConstants.RIGHT);
		textField_1.setColumns(10);
		textField_1.setBounds(513, 298, 75, 15);
		frmJavacheat.getContentPane().add(textField_1);
		
		JLabel label_6 = new JLabel("Freeze rate:");
		label_6.setToolTipText("The rate at which frozen values will be set");
		label_6.setBounds(380, 325, 125, 15);
		frmJavacheat.getContentPane().add(label_6);
		
		textField_2 = new JTextField();
		textField_2.setToolTipText("Rate in ms");
		textField_2.setText("200");
		textField_2.setHorizontalAlignment(SwingConstants.RIGHT);
		textField_2.setColumns(10);
		textField_2.setBounds(513, 325, 75, 15);
		frmJavacheat.getContentPane().add(textField_2);
		
		JLabel label_7 = new JLabel("Refresh rate:");
		label_7.setToolTipText("The rate at which found values will be refreshed");
		label_7.setBounds(380, 352, 125, 15);
		frmJavacheat.getContentPane().add(label_7);
		
		textField_3 = new JTextField();
		textField_3.setToolTipText("Rate in ms");
		textField_3.setText("200");
		textField_3.setHorizontalAlignment(SwingConstants.RIGHT);
		textField_3.setColumns(10);
		textField_3.setBounds(513, 352, 75, 15);
		frmJavacheat.getContentPane().add(textField_3);
		
		JScrollPane scrollPane_1 = new JScrollPane();
		scrollPane_1.setBounds(12, 493, 676, 246);
		frmJavacheat.getContentPane().add(scrollPane_1);
		
		JLabel label_8 = new JLabel("Cheat table:");
		label_8.setFont(new Font("Comic Sans MS", Font.BOLD | Font.ITALIC, 14));
		label_8.setBounds(12, 463, 200, 30);
		frmJavacheat.getContentPane().add(label_8);
		
		lblProcess = new JLabel("Process:");
		lblProcess.setHorizontalAlignment(SwingConstants.CENTER);
		lblProcess.setBounds(12, 12, 676, 15);
		frmJavacheat.getContentPane().add(lblProcess);
		
		JProgressBar progressBar = new JProgressBar();
		progressBar.setBounds(12, 12, 676, 14);
		frmJavacheat.getContentPane().add(progressBar);
		
		JMenuBar menuBar = new JMenuBar();
		frmJavacheat.setJMenuBar(menuBar);
		
		JMenu mnTest = new JMenu("File");
		menuBar.add(mnTest);
		
		final MainWindow window = this;
		
		JMenuItem mntmSelectProcess = new JMenuItem("Select process");
		mntmSelectProcess.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					new ProcessPickerWindow(window);
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}
		});
		mnTest.add(mntmSelectProcess);
		
		JMenu mnOptions = new JMenu("Options");
		menuBar.add(mnOptions);
		
		JMenuItem mntmScanRanges = new JMenuItem("Scan ranges");
		mnOptions.add(mntmScanRanges);
	}
	
	public void setProcess(final int pid) {
		try {
			logicManager.setPID(pid);
		} catch (ProcessNotFoundException | IOException e) {
			e.printStackTrace();
		}
	}

	@SuppressWarnings("unchecked")
	@Override
	public void triggerEvent(Event event) {
		if (event.getEventClass() == Constants.EVENT_LOCK) {
			if (event.getTarget() == Constants.TARGET_COMBOBOX_VALUETYPE) {
				comboBoxValueType.setEnabled(false);
			}
		} else if (event.getEventClass() == Constants.EVENT_UPDATE) {
			if (event.getTarget() == Constants.TARGET_COMBOBOX_SCANTYPE) {
				comboBoxScanType.setModel(new DefaultComboBoxModel<String>(logicManager.getScanTypes()));
			} else if (event.getTarget() == Constants.TARGET_LABEL_PROCESS) {
				lblProcess.setText((String) ((UpdateEvent) event).getUpdateValues()[0]);
			} else if (event.getTarget() == Constants.TARGET_LABEL_RESULTS) {
				lblResults.setText((String) ((UpdateEvent) event).getUpdateValues()[0]);
			} else if (event.getTarget() == Constants.TARGET_TABLE_SEARCH_RESULTS) {
				Object[] updateValues = ((UpdateEvent) event).getUpdateValues();
				valueTable.applyAddressMap((HTreeMap<Long, Object>) updateValues[0], (int) updateValues[1]);
			}
		}
		
	}
}
