package net.zyuiop.discordbot.lua;

import net.zyuiop.discordbot.commands.DiscordCommand;
import org.luaj.vm2.LuaError;
import sx.blah.discord.handle.obj.IMessage;
import sx.blah.discord.util.DiscordException;
import sx.blah.discord.util.MissingPermissionsException;
import sx.blah.discord.util.RateLimitException;

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
			try {
				message.getChannel().sendMessage("Erreur lua : ```" + e.getMessage() + "```");
			} catch (MissingPermissionsException | RateLimitException | DiscordException e1) {
				e1.printStackTrace();
			}
		}
		message.getChannel().setTypingStatus(false);
	}
}
