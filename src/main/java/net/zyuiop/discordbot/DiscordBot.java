package net.zyuiop.discordbot;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayDeque;
import java.util.Properties;
import java.util.Queue;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import net.zyuiop.discordbot.commands.AboutCommand;
import net.zyuiop.discordbot.commands.ChangeGroupCommand;
import net.zyuiop.discordbot.commands.CountCommand;
import net.zyuiop.discordbot.commands.HelpCommand;
import net.zyuiop.discordbot.commands.RandomMemeCommand;
import net.zyuiop.discordbot.commands.SystemCommand;
import net.zyuiop.discordbot.lua.LuaCommand;
import sx.blah.discord.api.ClientBuilder;
import sx.blah.discord.api.IDiscordClient;
import sx.blah.discord.handle.obj.IChannel;
import sx.blah.discord.util.DiscordException;
import sx.blah.discord.util.MissingPermissionsException;
import sx.blah.discord.util.RateLimitException;

/**
 * @author zyuiop
 */
public class DiscordBot {
	private static File archiveDir;
	private static IDiscordClient client;
	private static BlockingQueue<SendableMessage> messages = new LinkedBlockingQueue<>();

	public static void main(String... args) throws DiscordException {
		Properties properties = new Properties(buildDefault());
		File props = new File("icbot.properties");

		if (props.exists() && props.isFile()) {
			try {
				FileReader reader = new FileReader(props);
				properties.load(reader);
			} catch (IOException e) {
				e.printStackTrace();
				System.out.println("Cannot read properties file, aborting startup.");
				return;
			}
		} else {
			try {
				FileWriter writer = new FileWriter(props);
				properties.store(writer, "Created by ICBot");
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

		String token = properties.getProperty("token");
		if (token == null || token.isEmpty()) {
			if (args.length == 0) {
				System.out.println("No token provided.");
				return;
			}

			token = args[0];
		}

		System.out.println("Initiating memes archive...");
		archiveDir = new File(properties.getProperty("archivepath"));

		if (!archiveDir.exists())
			archiveDir.mkdir();

		System.out.println("Initializing commands...");
		new HelpCommand();
		new RandomMemeCommand();
		new SystemCommand();
		new AboutCommand();
		new CountCommand();
		new LuaCommand();

		String groups = properties.getProperty("groups");
		if (groups != null) {
			for (String groupString : groups.split(",")) {
				String[] parts = groupString.split(":");
				if (parts.length == 2) {
					new ChangeGroupCommand(parts[0], parts[1]);
				}
			}
		}

		new Thread(() -> {
			int sentMessages = 0;
			long time = 0;

			while (true) {
				try {
					SendableMessage message = messages.take();
					sentMessages ++;
					if (time + 5000 < System.currentTimeMillis()) {
						time = System.currentTimeMillis();
						sentMessages = 1;
					} else if (sentMessages >= 5) {
						Thread.sleep((time + 5500) - System.currentTimeMillis());
					}
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}).start();

		System.out.println("Connecting to Discord !");
		client = new ClientBuilder().withToken(token).login();
		client.getDispatcher().registerListener(new DiscordEventHandler());
	}

	private static Properties buildDefault() {
		Properties def = new Properties();
		def.setProperty("token", "");
		def.setProperty("groups", "info:syscom,syscom:info");
		def.setProperty("archivepath", "memesarchive");

		return def;
	}

	public static File getArchiveDir() {
		return archiveDir;
	}

	public static void sendMessage(IChannel channel, String message) {
		messages.add(new SendableMessage(channel, message));
	}

	private static class SendableMessage {
		private final IChannel channel;
		private final String message;

		private SendableMessage(IChannel channel, String message) {
			this.channel = channel;
			this.message = message;
		}

		public void send() {
			try {
				channel.sendMessage(message);
			} catch (MissingPermissionsException | RateLimitException | DiscordException e) {
				e.printStackTrace();
			}
		}
	}
}
