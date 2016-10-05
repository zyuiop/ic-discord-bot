package net.zyuiop.discordbot.commands;

import sx.blah.discord.handle.obj.IMessage;
import sx.blah.discord.handle.obj.IUser;

/**
 * @author zyuiop
 */
public class CountCommand extends DiscordCommand {
	public CountCommand() {
		super("count", "compte les utilisateurs humains du discord");
	}

	@Override
	public void run(IMessage message) throws Exception {
		int totalUsers = 0;
		for (IUser user : message.getGuild().getUsers())
			if (!user.isBot())
				totalUsers ++;

		message.getChannel().sendMessage("Il y a actuellement *" + totalUsers + " utilisateurs humains* sur ce Discord.");

		String[] parts = message.getContent().split(" ");
		if (parts.length > 1) {
			try {
				int wa = Integer.parseInt(parts[1]);
				double rate = ((double) totalUsers / (double) wa) * 100D;
				message.getChannel().sendMessage("Cela représente *" + String.format("%.2f", rate) + "%* des utilisateurs de la conv WhatsApp");
			} catch (NumberFormatException ignored) {}
		}
	}
}
