package net.zyuiop.discordbot.commands;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import sx.blah.discord.handle.obj.IMessage;

/**
 * @author zyuiop
 */
public class SystemCommand extends DiscordCommand {
	public SystemCommand() {
		super("system", "affiche des infos système");
	}

	@Override
	public void run(IMessage message) throws Exception {
		Process process = Runtime.getRuntime().exec("uname -a");
		process.waitFor();
		InputStream stream = process.getInputStream();
		BufferedReader br = new BufferedReader(new InputStreamReader(stream));
		StringBuilder sb = new StringBuilder();
		String line;
		while ((line = br.readLine()) != null) { sb.append(line).append("\n"); }

		System.out.println(sb);
		message.getChannel().sendMessage("The bot is running on a `" + sb + "` server");
	}
}
