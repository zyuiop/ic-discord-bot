package net.zyuiop.discordbot.commands;

import net.zyuiop.discordbot.DiscordBot;
import org.jsoup.Jsoup;
import sx.blah.discord.handle.obj.IMessage;

import java.util.Random;

/**
 * @author zyuiop
 */
public class ZerochanCommand extends DiscordCommand {
	private final String URL = "http://www.zerochan.net/";

	public ZerochanCommand() {
		super("zerochan", "affiche une image aléatoire Zerochan (SFW)");
	}

	private String getImageUrl() {
		Random random = new Random();
		int id = random.nextInt();

		try {
			return Jsoup.connect(URL + id).get().body().getElementsByTag("img").first().attr("src");
		} catch (Exception e) {
			return getImageUrl();
		}
	}

	@Override
	public void run(IMessage message) throws Exception {
		DiscordBot.sendMessage(message.getChannel(), getImageUrl());
	}
}
