package net.zyuiop.discordbot;

import net.zyuiop.discordbot.commands.DiscordCommand;
import sx.blah.discord.api.events.EventSubscriber;
import sx.blah.discord.handle.impl.events.MessageReceivedEvent;
import sx.blah.discord.handle.impl.events.MessageUpdateEvent;
import sx.blah.discord.handle.obj.IMessage;
import sx.blah.discord.util.DiscordException;
import sx.blah.discord.util.MissingPermissionsException;
import sx.blah.discord.util.RateLimitException;

/**
 * @author zyuiop
 */
public class DiscordEventHandler {
	@EventSubscriber
	public void onMessage(MessageReceivedEvent event) throws RateLimitException, DiscordException, MissingPermissionsException {
		IMessage message = event.getMessage();
		CommandRegistry.handle(message);
	}
	@EventSubscriber
	public void onMessage(MessageUpdateEvent event) throws RateLimitException, DiscordException, MissingPermissionsException {
		if (event.getNewMessage().getContent().equalsIgnoreCase(event.getOldMessage().getContent()))
			return;

		IMessage message = event.getNewMessage();
		CommandRegistry.handle(message);
	}
}
