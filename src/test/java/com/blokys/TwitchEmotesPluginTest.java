package com.blokys;

import net.runelite.client.RuneLite;
import net.runelite.client.externalplugins.ExternalPluginManager;

public class TwitchEmotesPluginTest
{
	public static void main(String[] args) throws Exception
	{
		ExternalPluginManager.loadBuiltin(TwitchEmotesPlugin.class);
		RuneLite.main(args);
	}
}