package com.lamb.plugin;

import java.util.EnumSet;
import java.util.List;

public class PluginInfo {

	
	private String mName;
	private int mVersion;
	
	private List<String> mRequiredPlugins = null;
	
	private PluginType mType = PluginType.UNKNOWN;
	private EnumSet<PluginHints> mHints = EnumSet.noneOf(PluginHints.class);
	private EnumSet<PluginFlags> mFlags = EnumSet.of(PluginFlags.USE_AOT_COMPILATION, PluginFlags.HOT_PLUGGABLE, PluginFlags.NEEDS_PLUGIN_DIR);
	
	private PluginPriority mRequistedPriority = PluginPriority.MEDIUM;
	
	public static enum PluginType {
		UNKNOWN, SINGLE_PAGE, MULTI_PAGE, BACKGROUND, API;
	}
	
	public static enum PluginHints {
		PRELOAD;
	}
	
	public static enum PluginFlags {
		HOT_PLUGGABLE, USE_AOT_COMPILATION, NEEDS_PLUGIN_DIR, START_ON_STARTUP;
	}
	
	public static enum PluginPriority {
		IDLE, VERY_LOW, LOW, MEDIUM, HIGH, VERY_HIGH, REALTIME;
	}
	
}
