package net.zyuiop.discordbot.commands;

import java.io.InputStreamReader;
import java.net.URL;
import com.google.gson.Gson;
import net.zyuiop.discordbot.DiscordBot;
import net.zyuiop.discordbot.json.github.GithubCommit;
import net.zyuiop.discordbot.json.github.GithubReference;
import sx.blah.discord.handle.obj.IMessage;

/**
 * @author zyuiop
 */
public class GitCommand extends DiscordCommand {
	public GitCommand() {
		super("git", "affiche le dernier commit du bot");
	}

	@Override
	public void run(IMessage message) throws Exception {
		URL urlObject = new URL("https://api.github.com/repos/zyuiop/ic-discord-bot/git/refs/heads/master");
		GithubReference ref = new Gson().fromJson(new InputStreamReader(urlObject.openStream()), GithubReference.class);

		String sha = ref.getRef();
		URL commitUrl = new URL("https://api.github.com/repos/zyuiop/ic-discord-bot/git/commits/" + sha);
		GithubCommit commit = new Gson().fromJson(new InputStreamReader(commitUrl.openStream()), GithubCommit.class);

		StringBuilder msg = new StringBuilder("**Dernier commit sur le bot : **");
		msg.append("\n```").append(commit.getSha()).append(" : ").append(commit.getMessage());
		msg.append("\nAuthor : ").append(commit.getCommitter().getName()).append(" [").append(commit.getCommitter().getEmail()).append("]");
		msg.append("\nDate : ").append(commit.getCommitter().getDate());
		msg.append("```More details : ").append(commit.getHtml_url());

		DiscordBot.sendMessage(message.getChannel(), msg.toString());
	}
}
