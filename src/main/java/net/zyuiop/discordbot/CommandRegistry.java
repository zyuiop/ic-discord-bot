package net.zyuiop.discordbot;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import net.zyuiop.discordbot.commands.CwgCommand;
import net.zyuiop.discordbot.commands.DiscordCommand;
import sx.blah.discord.handle.obj.IMessage;
import sx.blah.discord.util.DiscordException;
import sx.blah.discord.util.MissingPermissionsException;
import sx.blah.discord.util.RateLimitException;

/**
 * @author zyuiop
 */
public class CommandRegistry {
	private static Map<String, DiscordCommand> commandMap = new HashMap<>();

	public static void registerCommand(DiscordCommand command) {
		commandMap.put(command.getName().toLowerCase(), command);
	}

	public static DiscordCommand getCommand(String tag) {
		return commandMap.get(tag.toLowerCase());
	}

	public static Collection<DiscordCommand> getAll() {
		return commandMap.values();
	}

	public static void handle(IMessage message) throws RateLimitException, DiscordException, MissingPermissionsException {
		if (message.getAuthor().getID().equalsIgnoreCase(message.getClient().getApplicationClientID()))
			return;

		if (message.getContent().startsWith("!") || message.getContent().startsWith("/")) {
			String[] data = message.getContent().split(" ");
			if (data[0].length() == 1) {
				return;
			}
			DiscordCommand command = CommandRegistry.getCommand(data[0].substring(1));
			if (command != null) {
				try {
					command.run(message);
				} catch (Exception e) {
					DiscordBot.sendMessage(message.getChannel(), "Erreur pendant l'exécution de la commande : " + e.getClass().getName());
					e.printStackTrace();
				}
			}
		} else if (CwgCommand.isChannelWide(message.getChannel())) {
			String content = message.getContent();
			if (message.getAuthor().isBot()) {
				if (content.toLowerCase().startsWith("[Omegle]")) {
					Pattern pattern = Pattern.compile("\\[Omegle\\]\\[[0-9]{1,2}:[0-9]{1,2}:[0-9]{1,2}\\] <stranger> : (.+)");
					Matcher matcher = pattern.matcher(content);
					if (matcher.find()) {
						content = matcher.group(1);
					} else {
						return;
					}
				}
			}

			try {
				CwgCommand.run(content, message.getChannel());
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
}
