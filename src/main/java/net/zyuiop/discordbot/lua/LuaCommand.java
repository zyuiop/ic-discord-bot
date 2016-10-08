package net.zyuiop.discordbot.lua;

import net.zyuiop.discordbot.DiscordBot;
import net.zyuiop.discordbot.commands.DiscordCommand;
import org.luaj.vm2.LuaError;
import sx.blah.discord.handle.obj.IMessage;

/**
 * @author zyuiop
 */
public class LuaCommand extends DiscordCommand {
	public LuaCommand() {
		super("lua", "exécute un script lua");
	}

	@Override
	public void run(IMessage message) throws Exception {
		String msg = message.getContent().substring(4).trim().replace("```", "");

		message.getChannel().setTypingStatus(true);

		try {
			new LuaManager(message.getChannel()).runScript(msg);
		} catch (LuaError e) {
			DiscordBot.sendMessage(message.getChannel(), "Erreur lua : ```" + e.getMessage() + "```");
		}
		message.getChannel().setTypingStatus(false);
	}
}
