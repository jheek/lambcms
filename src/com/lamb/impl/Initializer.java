package com.lamb.impl;

import java.io.File;

public class Initializer {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		File cacheDir = new File(".cache");
		if (!cacheDir.exists()) {
			cacheDir.mkdir();
		}
		PluginManagerImpl pluginManager = new PluginManagerImpl(new File(".cache"));
		File pluginsDir = new File("plugins");
		if (!pluginsDir.exists()) {
			pluginsDir.mkdir();
		}
		pluginManager.addPluginDir(pluginsDir);
	}

}
