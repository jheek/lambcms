package com.lamb.plugin;

import java.io.File;

import com.lamb.events.EventDispatcher;

public interface PluginManager extends EventDispatcher {

	public void addPluginDir(File dir);
	public void removePluginDir(File dir);
	
	public Plugin startPlugin(String name);
	public boolean stopPlugin(Plugin plugin);
	public boolean enablePlugin(String name);
	public boolean disablePlugin(String name);
	
	public Plugin findPlugin(String name);
	
	public void removeTask(Plugin plugin, Runnable r);
	
	public void postTask(Plugin plugin, Runnable r);
	public void postTaskDelayed(Plugin plugin, Runnable r);
	public void postTaskDelayed(Plugin plugin, Runnable r, int priority);
	public void postTask(Plugin plugin, Runnable r, long time);
	public void postTask(Plugin plugin, Runnable r, long time, int priority);
	
}
