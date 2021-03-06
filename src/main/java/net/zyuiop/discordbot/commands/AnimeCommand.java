package net.zyuiop.discordbot.commands;

import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLEncoder;
import java.util.Arrays;
import java.util.List;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import com.google.gson.Gson;
import net.zyuiop.discordbot.DiscordBot;
import net.zyuiop.discordbot.json.mal.AnimeListSearch;
import org.apache.commons.lang3.StringUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import sx.blah.discord.handle.obj.IMessage;

/**
 * @author zyuiop
 */
public class AnimeCommand extends DiscordCommand {
	private final String type;

	public AnimeCommand(String type) {
		super(type, "affiche la page d'un " + type + " sur MyAnimeList");
		this.type = type;
	}

	@Override
	public void run(IMessage message) throws Exception {
		String[] parts = message.getContent().split(" ");
		if (parts.length < 2) {
			DiscordBot.sendMessage(message.getChannel(), "Utilisation : !" + type + " <titre>");
			return;
		}
		String title = StringUtils.join(Arrays.copyOfRange(parts, 1, parts.length), " ");
		String url = "https://myanimelist.net/search/prefix.json?type=all&keyword=" + URLEncoder.encode(title, "UTF-8") + "&v=1";

		Logger.getAnonymousLogger().info(url);
		URL urlObject = new URL(url);
		AnimeListSearch search = new Gson().fromJson(new InputStreamReader(urlObject.openStream()), AnimeListSearch.class);

		if (search != null && search.getCategories() != null && search.getCategories().size() != 0) {
			AnimeListSearch.AnimeListCategory category = search.getCategories().get(0);
			if (category.getItems().size() != 0) {
				AnimeListSearch.AnimeListCategory.Anime anime = category.getItems().get(0);

				Document doc = Jsoup.connect(anime.getUrl()).get();
				String popularity = doc.body().getElementsByClass("popularity").get(0).child(0).text();
				String rank = doc.body().getElementsByClass("ranked").get(0).child(0).text();
				String synopsis = doc.body().getElementsByAttributeValue("itemprop", "description").get(0).text();
				synopsis = synopsis.replace("<br>", "\n");

				DiscordBot.sendMessage(message.getChannel(), "**" + anime.getName() + "**\n" +
						(type.equalsIgnoreCase("anime") ? "**Aired** : " + anime.getPayload().getAired() : "**Published** : " + anime.getPayload().getPublished()) + "\n" +
						"**Start year** : " + anime.getPayload().getStart_year() + "\n" +
						"**Status** : " + anime.getPayload().getStatus() + "\n" +
						"**Score** :  " + anime.getPayload().getScore() + "\n" +
						"**Page** : " + anime.getUrl() + "\n" +
						"**Popularity** : " + popularity  + "\n" +
						"**Ranking** : " + rank + "\n" +
						"**Genres** : " + getGenres(doc) + "\n" +
						"**Rating** : " + getRating(doc)
				);

				DiscordBot.sendMessageAutoSplit(message.getChannel(), "**Synopsis** : " + synopsis);

				System.out.println("Rating " + getRating(doc));
				return;
			}
		}

		DiscordBot.sendMessage(message.getChannel(), "Empossible de trouver ce " + type);
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
