package com.lamb.plugin;

public interface Plugin {
	
	public abstract void onInstall();
	public abstract void onUpgrade(int oldVersion, int newVersion);
	
	public abstract void onEnabled();
	
	public abstract void onStart();
	public abstract void onStop();
	
	public abstract void onDisabled();
	
	public abstract void onUninstall();
	
	public abstract PluginInfo getInfo();
	
	
	
}
