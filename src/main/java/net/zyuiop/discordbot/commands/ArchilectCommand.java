package net.zyuiop.discordbot.commands;

import net.zyuiop.discordbot.DiscordBot;
import org.jsoup.Jsoup;
import sx.blah.discord.handle.obj.IMessage;

import java.util.Random;

/**
 * @author zyuiop
 */
public class ArchilectCommand extends DiscordCommand {
	private final String URL = "http://archillect.com/";
	private final int LAST = 93992;

	public ArchilectCommand() {
		super("archillect", "affiche une image aléatoire archillect");
	}

	@Override
	public void run(IMessage message) throws Exception {
		int id = new Random().nextInt(93992) + 1;
		String img = Jsoup.connect(URL + id).get().getElementById("ii").attr("src");
		DiscordBot.sendMessage(message.getChannel(), img);
	}
}
