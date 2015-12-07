package visuals.layout;

import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.swing.GroupLayout;
import javax.swing.JTextField;

public class LineLayout extends GroupLayout {
	
	private Group horizontalGroup;
	private Group verticalGroup;
	
	private List<Group> horizontalGroups;
	private List<Group> verticalGroups;
	private List<String> lineNames;
	private HashMap<String, Component[]> componentMap;

	public LineLayout(Container host) {
		super(host);
		
		horizontalGroup = createParallelGroup();
		verticalGroup = createSequentialGroup();
		
		horizontalGroups = new ArrayList<Group>();
		verticalGroups = new ArrayList<Group>();
		lineNames = new ArrayList<String>();
		componentMap = new HashMap<String, Component[]>();
		
		setHorizontalGroup(horizontalGroup);
		setVerticalGroup(verticalGroup);
		
		
	}
	
	public void addLine(final String name, final Component... components) {
		Group sequentialGroup = createSequentialGroup();
		Group parralelGroup = createParallelGroup(Alignment.LEADING);
		
		for (Component component : components) {
			component.setVisible(true);
			component.setPreferredSize(new Dimension(50, PREFERRED_SIZE));
			
			if (component.getClass() == JTextField.class) {
				sequentialGroup.addComponent(component, DEFAULT_SIZE, 200, Short.MAX_VALUE);
				parralelGroup.addComponent(component);
			} else {
				sequentialGroup.addComponent(component);
				parralelGroup.addComponent(component);
			}
			
			
		}
		
		horizontalGroup.addGroup(sequentialGroup);
		verticalGroup.addGroup(parralelGroup);
		
		horizontalGroups.add(sequentialGroup);
		verticalGroups.add(parralelGroup);
		
		lineNames.add(name);
		componentMap.put(name, components);
	}
	
	public void addLine(final String name, final String precedingName, final Component... components) {
		Group sequentialGroup = createSequentialGroup();
		Group parralelGroup = createParallelGroup();
		
		for (Component component : components) {
			component.setVisible(true);
			component.setPreferredSize(new Dimension(50, PREFERRED_SIZE));
			
			if (component.getClass() == JTextField.class) {
				sequentialGroup.addComponent(component, DEFAULT_SIZE, 200, Short.MAX_VALUE);
				parralelGroup.addComponent(component);
			} else {
				sequentialGroup.addComponent(component);
				parralelGroup.addComponent(component);
			}
		}
		
		int index = getLineIndex(precedingName) + 1;
		
		horizontalGroups.add(index, sequentialGroup);
		verticalGroups.add(index, parralelGroup);
		
		lineNames.add(index, name);
		componentMap.put(name, components);
		
		rebuildLayout();
	}
	
	public void removeLine(final String name) {
		int index = getLineIndex(name);
		
		Component[] components = componentMap.remove(name);
		
		for (Component component : components) {
			removeLayoutComponent(component);
			component.setVisible(false);
		}
		
		horizontalGroups.remove(index);
		verticalGroups.remove(index);
		lineNames.remove(index);
		
		rebuildLayout();
		
	}
	
	private int getLineIndex(final String name) {
		return lineNames.indexOf(name);
	}
	
	private void rebuildLayout() {
		horizontalGroup = createParallelGroup();
		verticalGroup = createSequentialGroup();
		
		for (Group group : horizontalGroups) {
			horizontalGroup.addGroup(group);
		}
		
		for (Group group : verticalGroups) {
			verticalGroup.addGroup(group);
		}
		
		setHorizontalGroup(horizontalGroup);
		setVerticalGroup(verticalGroup);
	}

}
