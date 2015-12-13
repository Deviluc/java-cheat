package events;

import java.util.List;

import interfaces.Event;
import libs.Constants;

public class FilterEvent implements Event {
	
	private Object[] filters;
	private int target;

	public FilterEvent(int target, Object[] filters) {
		this.filters = filters;
		this.target = target;
	}
	
	public FilterEvent(int target) {
		this.target = target;
	}
	
	public Object[] getFilters() {
		return filters;
	}

	@Override
	public int getEventClass() {
		return Constants.EVENT_FILTER;
	}

	@Override
	public int getTarget() {
		return target;
	}

}
