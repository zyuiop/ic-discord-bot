package net.zyuiop.discordbot;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.net.MalformedURLException;
import java.util.ArrayDeque;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

import net.zyuiop.discordbot.commands.*;
import net.zyuiop.discordbot.lua.LuaCommand;
import sx.blah.discord.api.ClientBuilder;
import sx.blah.discord.api.IDiscordClient;
import sx.blah.discord.handle.obj.IChannel;
import sx.blah.discord.handle.obj.IMessage;
import sx.blah.discord.util.DiscordException;
import sx.blah.discord.util.MissingPermissionsException;
import sx.blah.discord.util.RateLimitException;

/**
 * @author zyuiop
 */
public class DiscordBot {
	private static File archiveDir;
	private static IDiscordClient client;
	private static BlockingQueue<DiscordDelayTask> messages = new LinkedBlockingQueue<>();
	private static Map<String, ArrayDeque<IMessage>> lastMessages = new HashMap<>();

	public static void main(String... args) throws Exception, DiscordException, MalformedURLException {
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

		if (!archiveDir.exists()) { archiveDir.mkdir(); }

		System.out.println("Initializing questions directory...");
		File questionsDir = new File(properties.getProperty("questionspath"));

		if (!questionsDir.exists()) { questionsDir.mkdir(); }

		System.out.println("Initializing commands...");
		new HelpCommand();
		new RandomMemeCommand();
		new SystemCommand();
		new AboutCommand();
		new GitCommand();
		new CountCommand();
		new CleanCommand();
		new SafeBooruCommand();
		new LuaCommand();
		new AnimeCommand("anime");
		new AnimeCommand("manga");
		new AnimeRecommendCommand();
		new CommandEat();
		new CwgCommand(questionsDir);
		new CityCommand();
		new InsultCommand();
		new JokeCommand();
		new TopAnimeCommand();
		new DanbooruCommand();
		new KonachanCommand();
		new ZerochanCommand();
		new ArchilectCommand();

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
			while (true) {
				DiscordDelayTask message = null;
				try {
					message = messages.take();

					if (!message.send()) { messages.add(message); }
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
		def.setProperty("questionspath", "questions");

		return def;
	}

	public static File getArchiveDir() {
		return archiveDir;
	}

	public static void sendMessage(IChannel channel, String message) {
		messages.add(new SendableMessage(channel, message));
	}

	public static void sendMessageAutoSplit(IChannel channel, String message) {
		if (message.length() > 2000) {
			while (message.length() > 2000) {
				sendMessage(channel, message.substring(0, 2000));
				message = message.substring(2000);
			}
		}
		sendMessage(channel, message);
	}

	public static boolean removeLastMessage(IChannel channel) {
		ArrayDeque<IMessage> lastMessages = DiscordBot.lastMessages.get(channel.getID());
		if (lastMessages == null) { return false; }

		if (lastMessages.size() > 0) {
			deleteMessage(lastMessages.pollLast());
			return true;
		}
		return false;
	}

	public static void deleteMessage(IMessage message) {
		messages.add(new DeleteMessage(message));
	}

	private static abstract class DiscordDelayTask {
		private Date rateLimit = null;

		protected abstract long doSend();

		public final boolean send() {
			if (rateLimit != null) {
				if (rateLimit.after(new Date())) { return false; }
			}

			long limit = doSend();
			if (limit > 0) {
				rateLimit = new Date(System.currentTimeMillis() + limit + 100);
				return false;
			}

			return true;
		}
	}

	private static class DeleteMessage extends DiscordDelayTask {
		private final IMessage delete;

		private DeleteMessage(IMessage delete) {
			this.delete = delete;
		}

		public long doSend() {
			try {
				delete.delete();
			} catch (MissingPermissionsException | DiscordException e) {
				e.printStackTrace();
			} catch (RateLimitException e) {
				return e.getRetryDelay();
			}
			return 0;
		}
	}

	private static class SendableMessage extends DiscordDelayTask {
		private final IChannel channel;
		private final String message;

		private SendableMessage(IChannel channel, String message) {
			this.channel = channel;
			this.message = message;
		}

		public long doSend() {
			try {
				IMessage msg = channel.sendMessage(message);
				if (!lastMessages.containsKey(msg.getChannel().getID())) {
					lastMessages.put(msg.getChannel().getID(), new ArrayDeque<>());
				}
				lastMessages.get(msg.getChannel().getID()).addLast(msg);
				if (lastMessages.get(msg.getChannel().getID()).size() > 20) {
					lastMessages.get(msg.getChannel().getID()).removeFirst();
				}
			} catch (MissingPermissionsException | DiscordException e) {
				e.printStackTrace();
			} catch (RateLimitException e) {
				return e.getRetryDelay();
			}
			return 0;
		}
	}
}
