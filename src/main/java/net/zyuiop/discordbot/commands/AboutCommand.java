package net.zyuiop.discordbot.commands;

import sx.blah.discord.handle.obj.IMessage;

/**
 * @author zyuiop
 */
public class AboutCommand extends DiscordCommand {
	public AboutCommand() {
		super("about", "affiche des infos sur le bot");
	}

	@Override
	public void run(IMessage message) throws Exception {
		StringBuilder builder = new StringBuilder("ICBot, non versionné parce que la flemme, par zyuiop").append("\n");
		builder.append("Github : https://github.com/zyuiop/ic-discord-bot").append("\n");
		builder.append("Basé sur Discord4J : https://github.com/austinv11/Discord4J");
		message.getChannel().sendMessage(builder.toString());
	}
}
