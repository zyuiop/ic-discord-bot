package net.zyuiop.discordbot;

import net.zyuiop.discordbot.commands.DiscordCommand;
import sx.blah.discord.api.events.EventSubscriber;
import sx.blah.discord.handle.impl.events.MessageReceivedEvent;
import sx.blah.discord.handle.obj.IMessage;
import sx.blah.discord.util.DiscordException;
import sx.blah.discord.util.MissingPermissionsException;
import sx.blah.discord.util.RateLimitException;

/**
 * @author zyuiop
 */
public class DiscordEventHandler {
	@EventSubscriber
	public void onMessage(MessageReceivedEvent event) throws RateLimitException, DiscordException, MissingPermissionsException {
		IMessage message = event.getMessage();
		if (message.getContent().startsWith("!")) {
			String[] data = message.getContent().split(" ");
			if (data[0].length() == 1) {
				return;
			}
			DiscordCommand command = CommandRegistry.getCommand(data[0].substring(1));
			if (command != null) {
				try {
					command.run(message);
				} catch (Exception e) {
					message.getChannel().sendMessage("Erreur pendant l'exécution de la commande : " + e.getClass().getName());
					e.printStackTrace();
				}
			}
		}
	}
}
