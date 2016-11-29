package net.zyuiop.discordbot.commands;

import java.io.IOException;
import java.net.URLEncoder;
import java.util.List;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import net.zyuiop.discordbot.DiscordBot;
import net.zyuiop.discordbot.data.mal.TopAnime;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import sx.blah.discord.handle.obj.IMessage;

/**
 * @author zyuiop
 */
public class TopAnimeCommand extends DiscordCommand {
	private final String heading;
	private final int sortOrder;
	private final int status;

	public TopAnimeCommand(String command, String description, String heading, int sortOrder, String... aliases) {
		this(command, description, heading, sortOrder, 2, aliases);
	}

	public TopAnimeCommand(String command, String description, String heading, int sortOrder, int status, String... aliases) {
		super(command, description);

		for (String alias : aliases)
			addAlias(alias);

		this.heading = heading;
		this.sortOrder = sortOrder;
		this.status = status;
	}

	@Override
	public void run(IMessage message) throws Exception {
		String[] parts = message.getContent().split(" ");
		if (parts.length < 2) {
			DiscordBot.sendMessage(message.getChannel(), "Utilisation : !" + getName() + " <pseudo> [nombre=10]");
			return;
		}

		String name = parts[1];
		int number = (parts.length < 3 ? 10 : Integer.parseInt(parts[2]));
		if (number < 1 || number > 500) { number = 10; }

		String url = "https://myanimelist.net/animelist/" + URLEncoder.encode(name, "UTF-8") + "?status=2&order=4&order2=0";
		try {
			Document doc = Jsoup.connect(url).get();
			String data = doc.getElementsByTag("table").get(0).attr("data-items");

			if (data == null || data.isEmpty()) {
				DiscordBot.sendMessage(message.getChannel(), "Impossible de trouver cet utilisateur.");
				return;
			}

			TypeToken<List<TopAnime>> typeToken = new TypeToken<List<TopAnime>>() {};
			List<TopAnime> topAnimes = new Gson().fromJson(data, typeToken.getType());

			int max = Math.min(number, topAnimes.size());
			StringBuilder builder = new StringBuilder("**" + heading + " " + max + " animes of " + name + "** : \n");

			for (int i = 0; i < max; ++i) {
				TopAnime anime = topAnimes.get(i);
				StringBuilder lineBuilder = new StringBuilder("**#").append(i + 1);
				lineBuilder.append(" : ").append(anime.getAnime_title()).append("**");
				lineBuilder.append(", ").append(anime.getAnime_media_type_string()).append(" of ").append(anime.getAnime_num_episodes()).append(" episodes");
				lineBuilder.append(", score ").append(anime.getScore()).append("\n");

				if (builder.length() + lineBuilder.length() > 2000) {
					DiscordBot.sendMessage(message.getChannel(), builder.toString());
					builder = new StringBuilder();
				}
				builder.append(lineBuilder);
			}

			DiscordBot.sendMessage(message.getChannel(), builder.toString());
		} catch (IOException e) {
			DiscordBot.sendMessage(message.getChannel(), "Impossible de trouver cet utilisateur.");
		}
	}
}
