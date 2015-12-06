package events;

import interfaces.Event;
import libs.Constants;

public class LockEvent implements Event {
	
	private int target;
	
	public LockEvent(final int target) {
		this.target = target;
	}

	@Override
	public int getEventClass() {
		return Constants.EVENT_LOCK;
	}

	@Override
	public int getTarget() {
		return target;
	}

}
