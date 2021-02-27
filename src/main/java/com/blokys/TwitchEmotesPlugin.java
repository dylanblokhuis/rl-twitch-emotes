package com.blokys;

import com.blokys.fetch.Fetcher;
import joptsimple.internal.Strings;
import lombok.extern.slf4j.Slf4j;
import net.runelite.api.Client;
import net.runelite.api.GameState;
import net.runelite.api.Player;
import net.runelite.api.events.ChatMessage;
import net.runelite.api.events.GameStateChanged;
import net.runelite.api.events.OverheadTextChanged;
import net.runelite.client.chat.ChatMessageManager;
import net.runelite.client.eventbus.Subscribe;
import net.runelite.client.plugins.Plugin;
import net.runelite.client.plugins.PluginDescriptor;
import net.runelite.client.util.Text;

import javax.inject.Inject;
import java.util.regex.Pattern;

@Slf4j
@PluginDescriptor(
	name = "Twitch Emotes",
	description = "Enables Twitch emotes, BetterTwitchTV and FrankerFaceZ emotes.",
	tags = {"chat", "twitch"}
)
public class TwitchEmotesPlugin extends Plugin
{
	private static final Pattern WHITESPACE_REGEXP = Pattern.compile("[\\s\\u00A0]");

	@Inject
	private Client client;

	@Inject
	private ChatMessageManager chatMessageManager;

	@Override
	protected void startUp()
	{
		log.info("Started Twitch Emotes plugin");

		new Fetcher();
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
	protected void shutDown()
	{
		log.info("Stopped Twitch emotes plugin");
	}

	@Subscribe
	public void onChatMessage(ChatMessage chatMessage)
	{
		if (client.getGameState() != GameState.LOGGED_IN)
		{
			return;
		}

		switch (chatMessage.getType())
		{
			case PUBLICCHAT:
			case MODCHAT:
			case FRIENDSCHAT:
			case PRIVATECHAT:
			case PRIVATECHATOUT:
			case MODPRIVATECHAT:
				break;
			default:
				return;
		}

		String updatedMessage = mutateChatMessage(chatMessage.getMessage());

		if (updatedMessage == null)
		{
			return;
		}

		chatMessage.getMessageNode().setValue(updatedMessage);
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


	String mutateChatMessage(String message) {
		String[] words = WHITESPACE_REGEXP.split(message);

		for (int i = 0; i < words.length; i++)
		{
			String word = Text.removeFormattingTags(words[i]);

			if (!word.startsWith(":")) continue;
			if (!word.endsWith(":")) continue;

			Integer modIconKey = Emotes.findModIconKey(word, client);
			if (modIconKey == null) continue;

			words[i] = word.replace(word, "<img=" + modIconKey + ">");
		}

		return Strings.join(words, " ");
	}
}
