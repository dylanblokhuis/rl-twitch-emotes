package com.blokys;

import com.blokys.bttv.BetterTwitchTV;
import com.blokys.bttv.Emote;
import com.google.inject.Provides;

import javax.imageio.ImageIO;
import javax.inject.Inject;

import joptsimple.internal.Strings;
import lombok.extern.slf4j.Slf4j;
import net.runelite.api.*;
import net.runelite.api.events.ChatMessage;
import net.runelite.api.events.GameStateChanged;
import net.runelite.api.events.OverheadTextChanged;
import net.runelite.client.chat.ChatMessageManager;
import net.runelite.client.config.ConfigManager;
import net.runelite.client.eventbus.Subscribe;
import net.runelite.client.plugins.Plugin;
import net.runelite.client.plugins.PluginDescriptor;
import net.runelite.client.util.ImageUtil;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.regex.Pattern;

@Slf4j
@PluginDescriptor(
	name = "Twitch Emotes"
)
public class TwitchEmotesPlugin extends Plugin
{
	private static final Pattern WHITESPACE_REGEXP = Pattern.compile("[\\s\\u00A0]");

	@Inject
	private Client client;

	@Inject
	private TwitchEmotesPluginConfig config;

	@Inject
	private ChatMessageManager chatMessageManager;

	@Override
	protected void startUp() throws Exception
	{
		log.info("Started Twitch Emotes plugin");

		new BetterTwitchTV();
	}

	@Subscribe
	public void onGameStateChanged(GameStateChanged gameStateChanged)
	{
		if (gameStateChanged.getGameState() == GameState.LOGGED_IN)
		{
			Store.setDefaultModIconsLength(client.getModIcons().length);
		}
	}

	@Override
	protected void shutDown() throws Exception
	{
		log.info("Stopped Twitch emotes plugin");
	}

	@Subscribe
	public void onChatMessage(ChatMessage chatMessage)
	{
		chatMessage.getMessageNode().setValue(mutateChatMessage(chatMessage.getMessage()));
		chatMessageManager.update(chatMessage.getMessageNode());
	}

	@Subscribe
	public void onOverheadTextChanged(final OverheadTextChanged event)
	{
		if (!(event.getActor() instanceof Player)) return;

		final String message = event.getOverheadText();
		final String updatedMessage = mutateChatMessage(message);

		if (updatedMessage == null) return;

		event.getActor().setOverheadText(updatedMessage);
	}


	String mutateChatMessage(String message)
	{
		String[] words = WHITESPACE_REGEXP.split(message);

		for (int i = 0; i < words.length; i++) {
			if (!words[i].startsWith(":")) continue;
			if (!words[i].endsWith(":")) continue;

			Integer modIconKey = Emotes.findModIconKey(words[i], client);
			if (modIconKey == null) continue;

			words[i] = words[i].replace(words[i], "<img=" + modIconKey + ">");
		}

		return Strings.join(words, " ");
	}

	@Provides
	TwitchEmotesPluginConfig provideConfig(ConfigManager configManager)
	{
		return configManager.getConfig(TwitchEmotesPluginConfig.class);
	}
}
