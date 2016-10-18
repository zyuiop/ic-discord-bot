package net.zyuiop.discordbot.commands;

import java.io.InputStreamReader;
import java.net.URL;
import java.util.List;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import net.zyuiop.discordbot.DiscordBot;
import net.zyuiop.discordbot.json.github.GithubCommit;
import sx.blah.discord.handle.obj.IMessage;

/**
 * @author zyuiop
 */
public class GitCommand extends DiscordCommand {
	public GitCommand() {
		super("git", "affiche les 5 derniers commit du bot");
	}

	@Override
	public void run(IMessage message) throws Exception {
		URL commitUrl = new URL("https://api.github.com/repos/zyuiop/ic-discord-bot/commits");
		TypeToken<List<GithubCommit>> typeToken = new TypeToken<List<GithubCommit>>() {};
		List<GithubCommit> commits = new Gson().fromJson(new InputStreamReader(commitUrl.openStream()), typeToken.getType());

		StringBuilder msgBuilder = new StringBuilder("**Derniers commits sur le bot : ** ```");

		for (int i = 0; i < 5; ++i) {
			GithubCommit commit = commits.get(i);
			StringBuilder msg = new StringBuilder();
			msg.append("\n - ").append(commit.getSha()).append(" : ").append(commit.getMessage());
			msg.append("\n\tAuthor : ").append(commit.getCommitter().getName()).append(" [").append(commit.getCommitter().getEmail()).append("]");
			msg.append("\n\tDate : ").append(commit.getCommitter().getDate());

			if (msgBuilder.length() + msg.length() >= 2000) {
				msgBuilder.append("```");
				DiscordBot.sendMessage(message.getChannel(), msgBuilder.toString());
				msgBuilder = new StringBuilder();
			}

			msgBuilder.append(msg);
		}

		msgBuilder.append("```");
		DiscordBot.sendMessage(message.getChannel(), msgBuilder.toString());
	}
}
