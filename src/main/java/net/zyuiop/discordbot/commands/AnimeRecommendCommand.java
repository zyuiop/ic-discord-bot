package net.zyuiop.discordbot.commands;

import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLEncoder;
import java.util.Arrays;
import java.util.logging.Logger;
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
public class AnimeRecommendCommand extends DiscordCommand {
	public AnimeRecommendCommand() {
		super("recommend", "affiche les recommandations pour un anime");
	}

	@Override
	public void run(IMessage message) throws Exception {
		String[] parts = message.getContent().split(" ");
		if (parts.length < 2) {
			DiscordBot.sendMessage(message.getChannel(), "Utilisation : !recommend <titre>");
			return;
		}
		String title = StringUtils.join(Arrays.copyOfRange(parts, 1, parts.length), " ");
		String url = "https://myanimelist.net/search/prefix.json?type=anime&keyword=" + URLEncoder.encode(title, "UTF-8") + "&v=1";

		Logger.getAnonymousLogger().info(url);
		URL urlObject = new URL(url);
		AnimeListSearch search = new Gson().fromJson(new InputStreamReader(urlObject.openStream()), AnimeListSearch.class);

		if (search != null && search.getCategories() != null && search.getCategories().size() != 0) {
			AnimeListSearch.AnimeListCategory category = search.getCategories().get(0);
			if (category.getItems().size() != 0) {
				AnimeListSearch.AnimeListCategory.Anime anime = category.getItems().get(0);
				Document recommendations = Jsoup.connect(anime.getUrl() + "/userrecs").get();
				Elements recs = recommendations.body().getElementsByClass("js-scrollfix-bottom-rel").get(0).children();

				int max = Math.min(recs.size(), 8);
				int recId = 0;
				for (int i = 4; i < max; i++) {
					Element child = recs.get(i);
					if (!child.hasClass("borderClass"))
						continue;

					Element recommendation = child.child(0).child(0).child(0).child(1); // table / tbody / tr / td
					String recTitle = recommendation.child(1).child(0).child(0).text();
					String recContent = recommendation.child(2).child(0).text();
					StringBuilder mmsg = new StringBuilder("**Recommendation ").append(++recId).append(" : ").append(recTitle).append("**\n");
					mmsg.append(recContent);
					DiscordBot.sendMessageAutoSplit(message.getChannel(), mmsg.toString());

				}
				return;
			}
		}
	}
}
