package events;

import interfaces.Event;
import libs.Constants;

public class TransferEvent implements Event {
	
	private int target;
	private Object[] data;

	public TransferEvent(final int target, final Object... data) {
		this.target = target;
		this.data = data;
	}
	

	@Override
	public int getEventClass() {
		return Constants.EVENT_TRANSFER;
	}

	@Override
	public int getTarget() {
		return target;
	}
	
	public Object[] getData() {
		return data;
	}

}
