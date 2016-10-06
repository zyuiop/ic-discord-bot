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
		message.getChannel().sendMessage("ICBot, non versionné parce que la flemme, par zyuiop");
		message.getChannel().sendMessage("Github : https://github.com/zyuiop/ic-discord-bot");
		message.getChannel().sendMessage("Basé sur Discord4J : https://github.com/austinv11/Discord4J");
	}
}
