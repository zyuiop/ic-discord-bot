package net.zyuiop.discordbot.commands;

import net.zyuiop.discordbot.DiscordBot;
import org.jsoup.Jsoup;
import sx.blah.discord.handle.obj.IMessage;

/**
 * @author zyuiop
 */
public class DanbooruCommand extends DiscordCommand {
	private final String URL = "http://danbooru.donmai.us/posts/random";

	public DanbooruCommand() {
		super("danbooru", "affiche une image aléatoire danbooru (NSFW)");
	}

	@Override
	public void run(IMessage message) throws Exception {
		String img = Jsoup.connect(URL).get().getElementById("image").attr("src");
		DiscordBot.sendMessage(message.getChannel(), "http://danbooru.donmai.us" + img);
	}
}
