package net.zyuiop.discordbot.commands;

import net.zyuiop.discordbot.DiscordBot;
import org.jsoup.Jsoup;
import sx.blah.discord.handle.obj.IMessage;

/**
 * @author zyuiop
 */
public class KonachanCommand extends DiscordCommand {
	private final String URL = "http://konachan.com/post/random";

	public KonachanCommand() {
		super("konachan", "affiche une image aléatoire Konachan (NSFW)");
	}

	@Override
	public void run(IMessage message) throws Exception {
		String img = Jsoup.connect(URL).get().getElementById("image").attr("src");
		DiscordBot.sendMessage(message.getChannel(), img);
	}
}
