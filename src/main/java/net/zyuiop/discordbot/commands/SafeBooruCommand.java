package net.zyuiop.discordbot.commands;

import net.zyuiop.discordbot.DiscordBot;
import org.jsoup.Jsoup;
import sx.blah.discord.handle.obj.IMessage;

import java.util.Random;

/**
 * @author zyuiop
 */
public class SafeBooruCommand extends DiscordCommand {
	private final String URL = "http://safebooru.org/index.php?page=post&s=view&id=";

	public SafeBooruCommand() {
		super("safebooru", "affiche une image aléatoire safebooru (SFW)");
	}

	private String getImageUrl(int attempt) {
		Random random = new Random();
		int id = random.nextInt(1867943);

		try {
			return Jsoup.connect(URL + id).get().body().getElementById("image").attr("src");
		} catch (Exception e) {
			e.printStackTrace();
			if (attempt >= 20)
				return "Erreur.";
			return getImageUrl(attempt + 1);
		}
	}

	@Override
	public void run(IMessage message) throws Exception {
		DiscordBot.sendMessage(message.getChannel(), getImageUrl(0));
	}
}
