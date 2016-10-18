package net.zyuiop.discordbot.commands;

import net.zyuiop.discordbot.DiscordBot;
import sx.blah.discord.handle.obj.IMessage;

/**
 * @author zyuiop
 */
public class CleanCommand extends DiscordCommand {
	public CleanCommand() {
		super("clean", "supprime le dernier message du bot dans le salon (max 20)");
	}

	@Override
	public void run(IMessage message) throws Exception {
		DiscordBot.deleteMessage(message);
		if (!DiscordBot.removeLastMessage(message.getChannel()))
			message.getAuthor().getOrCreatePMChannel().sendMessage("Erreur : il n'y a aucun message à supprimer dans le salon " + message.getChannel().getName());
	}
}
