package net.zyuiop.discordbot.lua;

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
		String msg = message.getContent().substring(4).trim();
		msg = msg.replace("```", "");

		message.getChannel().setTypingStatus(true);
		try {
			new LuaManager().runScript(msg, message.getChannel());
		} catch (LuaError e) {
			message.getChannel().sendMessage("Erreur lua : ```" + e.getMessage() + "```");
		}
		message.getChannel().setTypingStatus(false);
	}
}
