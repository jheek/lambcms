package com.lamb.impl;

import java.io.Externalizable;
import java.io.File;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;
import java.util.ArrayList;
import java.util.HashMap;

import com.lamb.events.BaseEventDispatcher;
import com.lamb.plugin.Plugin;
import com.lamb.plugin.PluginInfo.PluginPriority;
import com.lamb.plugin.PluginManager;
import com.lamb.utils.FastArrayList;

class PluginManagerImpl extends BaseEventDispatcher implements PluginManager {

	private ArrayList<PluginDir> mPluginDirs = new ArrayList<PluginDir>(4);
	
	private HashMap<String, PluginLoaderInfo> mPlugins = new HashMap<String, PluginLoaderInfo>(5000);
	private HashMap<String, Plugin> mLoadedPlugins = new HashMap<String, Plugin>(1000);
	
	private HashMap<String, ClassInfo> mClasses = new HashMap<String, ClassInfo>();
	
	public PluginManagerImpl() {
	}
	
	@Override
	public void addPluginDir(File dir) throws IOException {
		if (dir.isDirectory()) {
			PluginDir pluginDir = new PluginDir(dir);
			synchronized (pluginDir) {
				mPluginDirs.add(pluginDir);
			}
			loadClasses(dir);
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
	
	private void loadClasses(PluginDir dir) {
		ArrayList<File> sourceFiles = new ArrayList<File>();
		
	}
	
	private static void findSourceFiles(ArrayList<File> target, File dir) {
		File[] files = dir.listFiles();
		for (int i = files.length; i >= 0; i--) {
			File file = files[i];
			file.getName().endsWith(".java");
		}
	}
	
	private void unloadClasses(PluginDir dir) {
		
	}
	
	private class PluginClassLoader extends ClassLoader {
		
		@Override
		protected Class<?> findClass(String name) throws ClassNotFoundException {
			// TODO Auto-generated method stub
			return super.findClass(name);
		}
	}
	
	private static class PluginDir {
		public File dir;
		public File configFile;
		public File cacheDir;
		
		public PluginDir(File dir) throws IOException {
			this.dir = dir;
			configFile = new File(dir.getAbsolutePath() + File.pathSeparatorChar + ".plugininfo");
			cacheDir = new File(dir.getAbsolutePath() + File.pathSeparatorChar + ".cache");
			if (!configFile.exists()) {
				configFile.createNewFile();
			}
			if (!cacheDir.exists()) {
				if (!cacheDir.mkdir()) {
					throw new IOException("Failed to create cache directory");
				}
			}
		}
		
	}
	
	private static class ClassInfo implements Externalizable {
		
		public String className;
		public File src;
		public File classCache;
		
		@Override
		public void readExternal(ObjectInput in) throws IOException, ClassNotFoundException {
			className = in.readUTF();
			src = new File(in.readUTF());
			classCache = new File(in.readUTF());
		}
		
		@Override
		public void writeExternal(ObjectOutput out) throws IOException {
			out.writeUTF(className);
			out.writeUTF(src == null ? "" : src.getAbsolutePath());
			out.writeUTF(classCache == null ? "" : classCache.getAbsolutePath());
		}
		
	}
	
	private static class PluginLoaderInfo extends ClassInfo {
		
		public String pluginName;
		
		public boolean isEnabled;
		public boolean startOnStartup;
		
		public PluginPriority priority;
		
		@Override
		public void readExternal(ObjectInput in) throws IOException, ClassNotFoundException {
			
		}
		
		@Override
		public void writeExternal(ObjectOutput out) throws IOException {
			
		}
		
	}
	
}
