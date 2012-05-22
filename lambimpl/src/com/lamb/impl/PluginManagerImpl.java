package com.lamb.impl;

import java.io.Externalizable;
import java.io.File;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;
import java.nio.file.FileSystems;
import java.nio.file.Path;
import java.nio.file.StandardWatchEventKinds;
import java.nio.file.WatchEvent;
import java.nio.file.WatchKey;
import java.nio.file.WatchService;

import com.lamb.events.BaseEventDispatcher;
import com.lamb.plugin.Plugin;
import com.lamb.plugin.PluginInfo.PluginPriority;
import com.lamb.plugin.PluginManager;
import com.lamb.utils.FastArrayList;

public class PluginManagerImpl extends BaseEventDispatcher implements PluginManager, Externalizable {

	private FastArrayList<PluginLoaderInfo> mPlugins = new FastArrayList<PluginLoaderInfo>();
	private FastArrayList<Plugin> mLoadedPlugins = new FastArrayList<Plugin>();
	
	@Override
	public void addPluginDir(File dir) {
		if (dir.isDirectory()) {
			
		} else {
			// TODO throw exception
		}
	}

	@Override
	public void removePluginDir(File arg0) {
		// TODO Auto-generated method stub
		
	}
	
	@Override
	public Plugin findPlugin(String name) {
		return null;
	}
	
	@Override
	public Plugin startPlugin(String arg0) {
		// TODO Auto-generated method stub
		return null;
	}
	
	@Override
	public boolean stopPlugin(Plugin arg0) {
		// TODO Auto-generated method stub
		return false;
	}
	
	@Override
	public boolean enablePlugin(String arg0) {
		// TODO Auto-generated method stub
		return false;
	}
	
	@Override
	public boolean disablePlugin(String arg0) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void postTask(Plugin arg0, Runnable arg1) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void postTask(Plugin arg0, Runnable arg1, long arg2) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void postTask(Plugin arg0, Runnable arg1, long arg2, int arg3) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void postTaskDelayed(Plugin arg0, Runnable arg1) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void postTaskDelayed(Plugin arg0, Runnable arg1, int arg2) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void removeTask(Plugin arg0, Runnable arg1) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void readExternal(ObjectInput in) throws IOException, ClassNotFoundException {
		int pluginCount = in.readInt();
		mPlugins.ensureCapacity(pluginCount);
		for (int i = 0; i < pluginCount; i++) {
			PluginLoaderInfo info = new PluginLoaderInfo();
			info.className = in.readUTF();
			info.pluginName = in.readUTF();
			info.isEnabled = in.readBoolean();
			info.startOnStartup = in.readBoolean();
			info.priority = PluginPriority.valueOf(in.readUTF());
			mPlugins.add(info);
			if (info.startOnStartup) {
				startPlugin(info.pluginName);
			}
		}
	}

	@Override
	public void writeExternal(ObjectOutput out) throws IOException {
		int pluginCount = mPlugins.size();
		out.writeInt(pluginCount);
		for (int i = 0; i < pluginCount; i++) {
			PluginLoaderInfo info = mPlugins.get(i);
			out.writeUTF(info.className);
			out.writeUTF(info.pluginName);
			out.writeBoolean(info.isEnabled);
			out.writeBoolean(info.startOnStartup);
			out.writeUTF(info.priority.name());
		}
	}
	
	private static class PluginLoaderInfo {
		
		public String pluginName;
		public String className;
		
		
		public boolean isEnabled;
		public boolean startOnStartup;
		
		public PluginPriority priority;
		
	}
	
}
