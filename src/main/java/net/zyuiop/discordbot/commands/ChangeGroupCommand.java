package net.zyuiop.discordbot.commands;

import net.zyuiop.discordbot.DiscordBot;
import sx.blah.discord.handle.obj.IMessage;
import sx.blah.discord.handle.obj.IRole;
import sx.blah.discord.handle.obj.IUser;
import sx.blah.discord.util.MissingPermissionsException;

/**
 * @author zyuiop
 */
public class ChangeGroupCommand extends DiscordCommand {
	private final String roleName;
	private final String leaveRoleName;
	private IRole role;
	private IRole leaveRole;

	public ChangeGroupCommand(String role, String leaveRole) {
		super(role, "rejoint le groupe " + role + " et quitte le groupe " + leaveRole);

		this.roleName = role;
		this.leaveRoleName = leaveRole;
	}


	@Override
	public void run(IMessage message) throws Exception {
		IUser user = message.getAuthor();

		if (role == null) { role = message.getGuild().getRolesByName(roleName).get(0); }
		if (leaveRole == null) {
			leaveRole = message.getGuild().getRolesByName(leaveRoleName).get(0);
		}

		try {
			message.delete();
			user.addRole(role);
			user.getOrCreatePMChannel().sendMessage("Je vous ai ajouté dans le groupe `" + role.getName() + "` sur le Discord _" + message.getGuild().getName() + "_");
			user.removeRole(leaveRole);
		} catch (MissingPermissionsException e) {
			DiscordBot.sendMessage(message.getChannel(), "Désolé " + user.mention() + " mais je ne peux pas modifier votre groupe...");
		}
	}
}
