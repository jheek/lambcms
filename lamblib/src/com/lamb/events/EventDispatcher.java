package com.lamb.events;

import com.lamb.events.filters.EventFilter;

public interface EventDispatcher {

	/**
	 * adds an event listener
	 * @param name the name of the event
	 * @param listener the listener which will receive the event
	 */
	public void addEventListener(String name, EventListener listener);
	/**
	 * adds an event listener
	 * @param filter the filter that decides wheter an event is dispatched to the listener
	 * @param listener the listener which will receive the events
	 */
	public void addEventListener(EventFilter filter, EventListener listener);
	
	
	public boolean removeEventListener(String name, EventListener listener);
	public boolean removeEventListener(EventFilter filter, EventListener listener);
	
	public boolean containsEventListener(String name);
	public boolean containsEventListener(String name, EventListener listener);
	public boolean containsEventListener(EventFilter filter, EventListener listener);
	
	public void dispatchEvent(Event event);
	
}
