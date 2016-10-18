package net.zyuiop.discordbot.commands;

import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Calendar;
import java.util.GregorianCalendar;
import com.stanfy.gsonxml.GsonXmlBuilder;
import net.zyuiop.discordbot.DiscordBot;
import net.zyuiop.discordbot.menus.MenuList;
import net.zyuiop.discordbot.menus.Rss;
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
		if (message.getContent().toLowerCase().endsWith("night") || hour > 16) {
			url = nightUrl;
			type = "soir";
		} else {
			url = noonUrl;
			type = "midi";
		}

		Rss rss = new GsonXmlBuilder().create().fromXml(new InputStreamReader(url.openStream()), Rss.class);
		if (rss != null && rss.getChannel() != null) {
			StringBuilder msgBuilder = new StringBuilder("**Offre de restauration du " + type + "**");
			for (MenuList.Item item : rss.getChannel().getItems()) {
				String append = "\n- *" + item.getTitle() + "* : \n" + item.getDescription();

				if (msgBuilder.toString().length() + append.length() >= 2000) {
					DiscordBot.sendMessage(message.getChannel(), msgBuilder.toString());
					msgBuilder = new StringBuilder();
				}

				msgBuilder.append(append);
			}

			DiscordBot.sendMessage(message.getChannel(), msgBuilder.toString());
		}
	}
}
