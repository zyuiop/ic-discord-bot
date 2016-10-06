package net.zyuiop.discordbot;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Properties;
import net.zyuiop.discordbot.commands.ChangeGroupCommand;
import net.zyuiop.discordbot.commands.CountCommand;
import net.zyuiop.discordbot.commands.HelpCommand;
import net.zyuiop.discordbot.commands.RandomMemeCommand;
import net.zyuiop.discordbot.commands.SystemCommand;
import sx.blah.discord.api.ClientBuilder;
import sx.blah.discord.api.IDiscordClient;
import sx.blah.discord.util.DiscordException;

/**
 * @author zyuiop
 */
public class DiscordBot {
	private static File archiveDir;
	private static IDiscordClient client;

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
		new CountCommand();

		String groups = properties.getProperty("groups");
		if (groups != null) {
			for (String groupString : groups.split(",")) {
				String[] parts = groupString.split(":");
				if (parts.length == 2) {
					new ChangeGroupCommand(parts[0], parts[1]);
				}
			}
		}

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
}
