package net.zyuiop.discordbot;

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
	private static IDiscordClient client;

	public static void main(String... args) throws DiscordException {
		if (args.length == 0) {
			System.out.println("usage : DiscordBot <token>");
			return;
		}

		String token = args[0];

		new ChangeGroupCommand("info", "syscom");
		new ChangeGroupCommand("syscom", "info");
		new HelpCommand();
		new RandomMemeCommand();
		new SystemCommand();
		new CountCommand();

		client = new ClientBuilder().withToken(token).login();
		client.getDispatcher().registerListener(new DiscordEventHandler());
	}
}
