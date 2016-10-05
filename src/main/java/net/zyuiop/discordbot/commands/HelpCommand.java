package net.zyuiop.discordbot.commands;

import net.zyuiop.discordbot.CommandRegistry;
import sx.blah.discord.handle.obj.IChannel;
import sx.blah.discord.handle.obj.IMessage;

/**
 * @author zyuiop
 */
public class HelpCommand extends DiscordCommand {
	public HelpCommand() {
		super("icbot", "affiche l'aide du bot");
	}

	@Override
	public void run(IMessage message) throws Exception {
		IChannel channel = message.getChannel();
		StringBuilder msg = new StringBuilder("Commandes ICBot : \n```");
		for (DiscordCommand command : CommandRegistry.getAll()) {
			msg.append("!").append(command.getName());
			if (command.getDescription() != null) {
				msg.append(" : ").append(command.getDescription());
			}
			msg.append("\n");
		}
		msg.append("```");

		channel.sendMessage(msg.toString());
	}
}
