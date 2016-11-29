package net.zyuiop.discordbot.data.mal;

import java.util.List;
import java.util.stream.Collectors;
import org.apache.commons.lang3.StringUtils;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

/**
 * @author zyuiop
 */
public class AnimePage {
	public static List<Element> extractTypes(Document document, String property) {
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

	public static String getGenres(Document document) {
		Element element = AnimePage.extractTypes(document, "genres").get(0);
		List<String> elts = element.children().stream().filter(e -> e.tagName().equalsIgnoreCase("a")).map(Element::text).collect(Collectors.toList());
		return StringUtils.join(elts, ", ");
	}

	public static String getRating(Document document) {
		Element element = AnimePage.extractTypes(document, "rating").get(0);
		return element.text();
	}
}
