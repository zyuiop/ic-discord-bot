package net.zyuiop.discordbot.commands;

import java.io.File;
import java.io.FilenameFilter;
import java.util.Random;
import sx.blah.discord.handle.obj.IMessage;

/**
 * @author zyuiop
 */
public class RandomMemeCommand extends DiscordCommand {
	private final File archiveDir;

	public RandomMemeCommand() {
		super("randmeme", "affiche un meme aléatoire de l'archive");
		archiveDir = new File("memesarchive");

		if (!archiveDir.exists())
			archiveDir.mkdir();
	}

	@Override
	public void run(IMessage message) throws Exception {
		Random r = new Random();
		File[] files = archiveDir.listFiles((dir, name) -> name.endsWith("jpg") || name.endsWith("png"));
		if (files == null)
			return;
		File f = files[r.nextInt(files.length)];
		message.getChannel().sendMessage("https://archive.zyuiop.net/Discord/" + f.getName());
	}
}