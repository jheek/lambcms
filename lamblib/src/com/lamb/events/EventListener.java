package com.lamb.events;

public abstract class EventListener {
	
	public abstract void onEvent(Event event);
	
	public final void dispatchEvent(Event event) {
		onEvent(event);
	}
	
}
