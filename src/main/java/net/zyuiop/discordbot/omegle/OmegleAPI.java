package net.zyuiop.discordbot.omegle;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import net.zyuiop.discordbot.DiscordBot;
import org.json.JSONObject;
import sx.blah.discord.handle.obj.IChannel;

/**
 * @author zyuiop
 */
public class OmegleAPI {
	/**
	 * The base omegle url
	 */
	public static String BASE_URL = "http://front1.omegle.com";

	/**
	 * The URL used to start a chat
	 */
	public static URL OPEN_URL;

	/**
	 * The URL used to disconnect from a chat
	 */
	public static URL DISCONNECT_URL;

	/**
	 * The URL used to parse events
	 */
	public static URL EVENT_URL;

	/**
	 * The URL used to send messages
	 */
	public static URL SEND_URL;

	/**
	 * The URL used to change typing status
	 */
	public static URL TYPING_URL;

	private static Map<String, OmegleSession> SESSIONS = new HashMap<>();

	static {
		try {
			OPEN_URL = new URL(BASE_URL + "/start");
			DISCONNECT_URL = new URL(BASE_URL + "/disconnect");
			EVENT_URL = new URL(BASE_URL + "/events");
			SEND_URL = new URL(BASE_URL + "/send");
			TYPING_URL = new URL(BASE_URL + "/typing");
		} catch (MalformedURLException e) {
			e.printStackTrace();
		}
	}

	public static OmegleSession getSession(IChannel channel) {
		return SESSIONS.get(channel.getID());
	}

	public static OmegleSession openSession(IChannel channel) throws Exception {
		String id = channel.getID();
		if (SESSIONS.containsKey(id)) {
			DiscordBot.sendMessage(channel, "[Omegle] Fermez d'abord la session courante avec !omegle stop");
			return null;
		}

		Map<String, Object> vars = new HashMap<>();
		vars.put("rcs", "1");
		vars.put("firstevents", "0");

		URL url = new URL(OPEN_URL + "?" + HttpUtil.implode(vars));

		JSONObject resp = new JSONObject(HttpUtil.post(url, ""));

		if (!resp.has("clientID")) {
			throw new Exception("Omegle didn't return a client id!");
		}

		OmegleSession session = new OmegleSession(resp.getString("clientID"), channel);
		SESSIONS.put(id, session);
		return session;
	}

	public static void removeSession(OmegleSession omegleSession) {
		SESSIONS.remove(omegleSession.channel.getID());
	}
}
