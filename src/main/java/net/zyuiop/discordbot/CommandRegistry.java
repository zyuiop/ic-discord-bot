package net.zyuiop.discordbot;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import net.zyuiop.discordbot.commands.DiscordCommand;

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
}
