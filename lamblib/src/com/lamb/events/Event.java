package com.lamb.events;

public class Event {

	private static final int FLAG_DISPATCHING = 1;
	private static final int FLAG_CANCELABLE = 2;
	private static final int FLAG_CANCELED = 2;
	
	private String mName;
	private int mFlags;
	
	public Event(String name) {
		mName = name;
	}
	
	protected boolean onBeforeDispatch() {
		return true;
	}
	
	public boolean isDispatching() {
		return (mFlags & FLAG_DISPATCHING) == FLAG_DISPATCHING;
	}
	
	protected void onDispatch() {
		mFlags |= FLAG_DISPATCHING;
	}
	
	protected void onAfterDispatch(int numTriggers) {
		mFlags = mFlags & ~FLAG_DISPATCHING;
	}
	
	public boolean isCancelable() {
		return (mFlags & FLAG_CANCELABLE) == FLAG_CANCELABLE;
	}
	
	public void setCancelable(boolean isCancelable) {
		if (isCancelable) {
			mFlags |= FLAG_CANCELABLE;
		} else {
			mFlags &= ~FLAG_CANCELABLE;
		}
	}
	
	public String getName() {
		return mName;
	}
	
	public boolean isCanceled() {
		return (mFlags & FLAG_CANCELED) == FLAG_CANCELED;
	}
	
	public void cancel() {
		if (!isCancelable()) {
			throw new IllegalAccessError("Called cancel on a event which is not cancelable");
		}
		if (!isDispatching()) {
			throw new IllegalAccessError("Called cancel on a event which is not executing");
		}
		mFlags |= FLAG_CANCELED;
	}
	
	public void reset() {
		if(isDispatching()) {
			throw new IllegalAccessError("Can't reset an event when executing");
		}
		mFlags &= ~FLAG_CANCELED;
	}
	
}
