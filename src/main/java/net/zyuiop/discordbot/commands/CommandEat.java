package net.zyuiop.discordbot.commands;

import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Random;
import com.stanfy.gsonxml.GsonXmlBuilder;
import com.stanfy.gsonxml.XmlParserCreator;
import net.zyuiop.discordbot.DiscordBot;
import net.zyuiop.discordbot.menus.MenuList;
import net.zyuiop.discordbot.menus.Rss;
import org.apache.commons.codec.Charsets;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;
import sx.blah.discord.handle.obj.IMessage;

/**
 * @author zyuiop
 */
public class CommandEat extends DiscordCommand {
	private final URL noonUrl = new URL("https://menus.epfl.ch/cgi-bin/rssMenus");
	private final URL nightUrl = new URL("https://menus.epfl.ch/cgi-bin/rssMenus?midisoir=soir");

	public CommandEat() throws MalformedURLException {
		super("eat", "affiche les menus");
	}

	@Override
	public void run(IMessage message) throws Exception {
		Calendar calendar = new GregorianCalendar();
		int hour = calendar.get(Calendar.HOUR_OF_DAY);
		URL url;
		String type;
		if (message.getContent().toLowerCase().contains("night") || hour > 16) {
			url = nightUrl;
			type = "soir";
		} else {
			url = noonUrl;
			type = "midi";
		}

		XmlParserCreator parserCreator = () -> {
			try {
				return XmlPullParserFactory.newInstance().newPullParser();
			} catch (Exception e) {
				throw new RuntimeException(e);
			}
		};


		Rss rss = new GsonXmlBuilder().setSameNameLists(true).setXmlParserCreator(parserCreator).create().fromXml(new InputStreamReader(url.openStream()), Rss.class);
		if (rss != null && rss.getChannel() != null) {
			StringBuilder msgBuilder = new StringBuilder("**Offre de restauration du " + type + "**");
			boolean all = message.getContent().toLowerCase().contains("all");
			if (!all)
				msgBuilder.append("\nAffichage d'un menu aléatoire (!eat all pour tout voir)");

			if (all) {
				for (MenuList.Item item : rss.getChannel().getItems()) {
					String append = "\n- " + convertFromShittyIso(item.getTitle()) + " : \n\t" + convertFromShittyIso(item.getDescription());

					if (msgBuilder.toString().length() + append.length() >= 2000) {
						DiscordBot.sendMessage(message.getChannel(), msgBuilder.toString());
						msgBuilder = new StringBuilder();
					}

					msgBuilder.append(append);
				}
			} else {
				Random random = new Random();
				List<MenuList.Item> items = rss.getChannel().getItems();
				MenuList.Item item = items.get(random.nextInt(items.size()));

				String append = "\n- " + convertFromShittyIso(item.getTitle()) + " : \n\t" + convertFromShittyIso(item.getDescription());
				msgBuilder.append(append);
			}

			DiscordBot.sendMessage(message.getChannel(), msgBuilder.toString());
		}
	}

	private static String convertFromShittyIso(String str) {
		return convert(Charsets.UTF_16BE, Charsets.ISO_8859_1, str);
	}

	private static String convert(Charset source, Charset target, String str) {
		return new String(str.getBytes(source), target);
	}
}
