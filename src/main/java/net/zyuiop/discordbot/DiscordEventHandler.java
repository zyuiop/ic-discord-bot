package net.zyuiop.discordbot;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
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
	private final File archiveDir;

	public DiscordEventHandler() {
		archiveDir = new File("memesarchive");

		if (!archiveDir.exists())
			archiveDir.mkdir();
	}

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
				URLConnection connection = website.openConnection();
				connection.setRequestProperty("User-Agent", "Mozilla/5.0 (Macintosh; U; Intel Mac OS X 10.4; en-US; rv:1.9.2.2) Gecko/20100316 Firefox/3.6.2");
				InputStream in = connection.getInputStream();
				Files.copy(in, new File(archiveDir, website.getFile()).toPath(), StandardCopyOption.REPLACE_EXISTING);
				in.close();
			} catch (IOException e) {
				e.printStackTrace();
			}

		}
		CommandRegistry.handle(message);
	}
}
