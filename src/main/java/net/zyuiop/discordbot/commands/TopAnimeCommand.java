package net.zyuiop.discordbot.commands;

import java.io.IOException;
import java.net.URLEncoder;
import java.util.List;
import java.util.stream.Collectors;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import net.zyuiop.discordbot.DiscordBot;
import net.zyuiop.discordbot.json.mal.TopAnime;
import org.apache.commons.lang3.StringUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import sx.blah.discord.handle.obj.IMessage;

/**
 * @author zyuiop
 */
public class TopAnimeCommand extends DiscordCommand {
	public TopAnimeCommand() {
		super("topanime", "affiche la liste d'une personne sur MaL");
		addAlias("animelist");
	}

	@Override
	public void run(IMessage message) throws Exception {
		String[] parts = message.getContent().split(" ");
		if (parts.length < 2) {
			DiscordBot.sendMessage(message.getChannel(), "Utilisation : !topanime <pseudo> [nombre=10]");
			return;
		}

		String name = parts[1];
		int number = (parts.length < 3 ? 10 : Integer.parseInt(parts[2]));
		if (number < 1 || number > 500) { number = 10; }

		String url = "https://myanimelist.net/animelist/" + URLEncoder.encode(name, "UTF-8") + "?status=2&order=4&order2=0";
		try {
			Document doc = Jsoup.connect(url).get();
			String data = doc.getElementsByTag("table").get(0).attr("data-items");

			TypeToken<List<TopAnime>> typeToken = new TypeToken<List<TopAnime>>() {};
			List<TopAnime> topAnimes = new Gson().fromJson(data, typeToken.getType());

			int max = Math.min(number, topAnimes.size());
			StringBuilder builder = new StringBuilder("**Top " + max + " animes of " + name + "** : \n");

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

	private String getGenres(Document document) {
		Element element = extractTypes(document, "genres").get(0);
		List<String> elts = element.children().stream().filter(e -> e.tagName().equalsIgnoreCase("a")).map(Element::text).collect(Collectors.toList());
		return StringUtils.join(elts, ", ");
	}

	private String getRating(Document document) {
		Element element = extractTypes(document, "rating").get(0);
		return element.text();
	}

	private List<Element> extractTypes(Document document, String property) {
		// table / tbody / tr / td / div
		Element elt = document.body().getElementById("content").child(0).child(0).child(0).child(0).child(0);
		Elements elts = elt.getElementsByTag("div");

		return elts.stream().filter(e -> {
			if (e.children().size() > 0) {
				Elements darkText = e.getElementsByClass("dark_text");
				if (darkText.size() > 0) {
					return darkText.get(0).text().toLowerCase().equals(property + ":");
				}
			}
			return false;
		}).collect(Collectors.toList());
	}
}
