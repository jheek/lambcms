package com.lamb.http.server;

import com.lamb.plugin.Service;

public interface Server extends Service {

	public boolean isRunning();
	
	public void start();
	public void start(int port);
	
	public void stop();
	
	
}
