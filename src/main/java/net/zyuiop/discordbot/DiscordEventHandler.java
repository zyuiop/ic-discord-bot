package net.zyuiop.discordbot;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;
import net.zyuiop.discordbot.commands.DiscordCommand;
import org.apache.commons.io.IOUtils;
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
		onMessage(message);
	}
	@EventSubscriber
	public void onMessage(MessageUpdateEvent event) throws RateLimitException, DiscordException, MissingPermissionsException {
		if (event.getNewMessage().getContent().equalsIgnoreCase(event.getOldMessage().getContent()))
			return;

		IMessage message = event.getNewMessage();
		onMessage(message);
	}

	private void onMessage(IMessage message) throws RateLimitException, DiscordException, MissingPermissionsException {
		if (message.getContent().contains("i.imgflip.com") && message.getAuthor().isBot()) {
			URL website;
			try {
				website = new URL(message.getContent());
				ReadableByteChannel rbc = Channels.newChannel(website.openStream());
				FileOutputStream fos = new FileOutputStream(new File("memesarchive", website.getFile()));
				fos.getChannel().transferFrom(rbc, 0, Long.MAX_VALUE);
			} catch (IOException e) {
				e.printStackTrace();
			}

		}
		CommandRegistry.handle(message);
	}
}
