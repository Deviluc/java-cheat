package events;

import interfaces.Event;
import libs.Constants;

public class UpdateEvent implements Event {
	
	private int updateTarget;
	private Object[] updateValues;

	public UpdateEvent(final int updateTarget) {
		this.updateTarget = updateTarget;
	}
	
	public void setUpdateValues(final Object... values) {
		this.updateValues = values;
	}
	
	public Object[] getUpdateValues() {
		return this.updateValues;
	}
	
	public int getTarget() {
		return updateTarget;
	}

	@Override
	public int getEventClass() {
		return Constants.EVENT_UPDATE;
	}

}
