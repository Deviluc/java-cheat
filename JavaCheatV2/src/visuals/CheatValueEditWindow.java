package visuals;

import java.awt.Label;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;

import components.LogicManager;
import events.TransferEvent;
import interfaces.Event;
import interfaces.Trigger;
import libs.Constants;
import models.Value;
import visuals.layout.LineLayout;

public class CheatValueEditWindow extends JFrame implements Trigger {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 4766325548996010904L;

	private LogicManager logicManager;
	private Trigger parentComponent;
	private Value value;
	
	private JPanel contentPane;
	
	private LineLayout layout;
	
	private JTextField textfieldDescription;
	private JTextField textfieldAddress;
	private JTextField textfieldValue;
	
	private JRadioButton btnPointer;
	private JRadioButton btnHex;
	private JRadioButton btnFreeze;
	
	private JButton btnAdd;
	private JButton btnRemove;
	private JButton btnCancel;
	private JButton btnSave;
	
	private JComboBox<String> comboType;
	
	private List<JTextField> offsetTextFields;

	/**
	 * Create the frame.
	 */
	public CheatValueEditWindow(final LogicManager logicManager, final Trigger parentComponent, final Value value) {
		setTitle("Edit entry");
		this.logicManager = logicManager;
		this.parentComponent = parentComponent;
		this.value = value;
		
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setBounds(100, 100, 225, 175);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		layout = new LineLayout(contentPane);
		
		textfieldDescription = new JTextField();
		layout.addLine("description", new Label("Description:"), textfieldDescription);
		
		textfieldAddress = new JTextField();
		layout.addLine("address", new Label("Address:"), textfieldAddress);
		
		btnPointer = new JRadioButton("Pointer");
		btnPointer.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				if (btnPointer.isSelected()) {
					showOffsets();
				} else {
					hideOffsets();
				}
			}
		});
		layout.addLine("radio-pointer", btnPointer);
		
		offsetTextFields = new ArrayList<JTextField>();
		offsetTextFields.add(new JTextField());
		
		btnAdd = new JButton("Add");
		btnAdd.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				addOffset();
				
			}
		});
		
		btnRemove = new JButton("Remove");
		btnRemove.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				removeOffset();
				
			}
		});
		
		textfieldValue = new JTextField();
		layout.addLine("value", new Label("Value:"), textfieldValue);
		
		comboType = new JComboBox<>(logicManager.getValueTypes());
		layout.addLine("type", new JLabel("Type:"), comboType);
		
		btnHex = new JRadioButton("Hex");
		btnFreeze = new JRadioButton("Freeze");
		layout.addLine("radio-hex-freeze", btnHex, btnFreeze);
		
		
		btnCancel = new JButton("Cancel");
		btnCancel.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				dispose();
				
			}
		});
		
		btnSave = new JButton("Save");
		btnSave.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				save();
				
			}
		});
		
		layout.addLine("control-buttons", btnCancel, btnSave);
		
		contentPane.setLayout(layout);
		setContentPane(contentPane);
		
		textfieldAddress.setText(value.getFormattedAddress());
		textfieldValue.setText(value.getFormattedValue());
		textfieldDescription.setText(value.getDescription());
		
		int[] offsets = value.getOffsets();
		
		btnPointer.setSelected(offsets != null);
		
		if (offsets != null) {
			offsetTextFields = new ArrayList<JTextField>();
			
			for (int i = 0; i < offsets.length; i++) {
				offsetTextFields.add(new JTextField(Integer.toHexString(offsets[i])));
			}
			
			showOffsets();
		}
		
		btnFreeze.setSelected(value.getFreeze());
		btnHex.setSelected(value.getFormatHex());
		
		comboType.setSelectedIndex(value.getValueType());
		
		setVisible(true);
	}
	
	private void showOffsets() {
		
		for (int i = offsetTextFields.size() - 1; i >= 0; i--) {
			layout.addLine("offset" + i, "radio-pointer", new JLabel("Offset " + i + ":"), offsetTextFields.get(i));
		}
		
		layout.addLine("offset-buttons",  "offset" + (offsetTextFields.size() - 1), btnAdd, btnRemove);
		
		setSize(getSize().width, getSize().height + (offsetTextFields.size() * 40));
		
		revalidate();
	}
	
	private void hideOffsets() {
		for (int i = 0; i < offsetTextFields.size(); i++) {
			layout.removeLine("offset" + i);
		}
		
		layout.removeLine("offset-buttons");
		
		setSize(getSize().width, getSize().height - (offsetTextFields.size() * 40));
		
		revalidate();
	}
	
	private void removeOffset() {
		int index = offsetTextFields.size() - 1;
		
		if (index > 0) {
			layout.removeLine("offset" + index);
			offsetTextFields.remove(index);
			
			setSize(getSize().width, getSize().height - 25);
			
			revalidate();
		}
	}
	
	private void addOffset() {
		int index = offsetTextFields.size();
		JTextField field = new JTextField();
	
		layout.addLine("offset" + index, "offset" + (index - 1), new JLabel("Offset " + index + ":"), field);
		offsetTextFields.add(field);
		
		setSize(getSize().width, getSize().height + 25);
		
		revalidate();
	}
	
	private void save() {
		value.setDescription(textfieldDescription.getText());
		value.setAdress(Long.decode("0x"+ textfieldAddress.getText()));
		value.setValueType(comboType.getSelectedIndex());
		
		if (btnPointer.isSelected()) {
			int offsets[] = new int[offsetTextFields.size()];
			
			
			for (int i = 0; i < offsets.length; i++) {
				offsets[i] = Integer.decode("0x" + offsetTextFields.get(i).getText());
			}
			
			value.setOffsets(offsets);
		}
		
		value.setFreeze(btnFreeze.isSelected());
		value.setFormatHex(btnHex.isSelected());
		
		parentComponent.triggerEvent(new TransferEvent(Constants.TARGET_TABLE_CHEATS, new Object[] {value}));
		dispose();
	}

	@Override
	public void triggerEvent(Event event) {
		// TODO Auto-generated method stub
		
	}

}
