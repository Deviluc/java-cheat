package events;

import interfaces.Event;
import libs.Constants;

public class EditEvent implements Event {
	
	private int target;
	private Object model;
	
	public EditEvent(final int target, final Object model) {
		this.target = target;
		this.model = model;
	}
	
	public Object getModel() {
		return model;
	}

	@Override
	public int getEventClass() {
		return Constants.EVENT_EDIT;
	}

	@Override
	public int getTarget() {
		return target;
	}

}
