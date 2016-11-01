package net.zyuiop.discordbot.commands;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;
import net.zyuiop.discordbot.DiscordBot;
import org.apache.commons.lang3.StringUtils;
import sx.blah.discord.handle.obj.IChannel;
import sx.blah.discord.handle.obj.IMessage;

/**
 * @author zyuiop
 */
public class CwgCommand extends DiscordCommand {
	private final static Set<String> wholeChannels = new HashSet<>();
	private final static Map<String, String> currentQuestions = new HashMap<>();
	private static File workDir;


	public CwgCommand(File workDir) {
		super("cwg", "discuter avec le Credule Wise Guy");
		CwgCommand.workDir = workDir;
	}

	@Override
	public void run(IMessage message) throws Exception {
		// Réplique de question
		new File(workDir, "Pose-moi une question").createNewFile();

		//Boucle de conversation
		//Enregistrement de la réplique de l'utilisateur
		String[] args = message.getContent().split(" ");
		if (args.length < 2) {
			DiscordBot.sendMessage(message.getChannel(), "Aucun message entré !");
			return;
		}

		if (args[1].equalsIgnoreCase("!list")) {
			StringBuilder sb = new StringBuilder("Liste des questions : ```");
			for (File f : workDir.listFiles()) {
				if (sb.length() + f.getName().length() + 5 > 2000) {
					DiscordBot.sendMessage(message.getAuthor().getOrCreatePMChannel(), sb.append("```").toString());
					sb = new StringBuilder("```");
				}

				sb.append(f.getName()).append("\n");
			}

			DiscordBot.sendMessage(message.getAuthor().getOrCreatePMChannel(), sb.append("```").toString());
			return;
		}

		if (args[1].equalsIgnoreCase("!channel")) {
			if (wholeChannels.remove(message.getChannel().getID())) {
				DiscordBot.sendMessage(message.getAuthor().getOrCreatePMChannel(), "**Les messages du channel ne sont plus envoyés au CWG**");
			} else {
				wholeChannels.add(message.getChannel().getID());
				DiscordBot.sendMessage(message.getAuthor().getOrCreatePMChannel(), "**Les messages du channel sont désormais envoyés au CWG**");
			}
			return;
		}

		String reply = StringUtils.join(Arrays.copyOfRange(args, 1, args.length), " ");
		reply = StringUtils.capitalize(reply);

		run(reply, message.getChannel());
	}

	public static void run(String reply, IChannel channel) throws Exception {

		if (!reply.contains("?")) {
			String currentQuestion = currentQuestions.get(channel.getID());

			if (currentQuestion != null) {
				// Enregistrement de la réponse si elle est la réponse à la question précédente
				try {
					FileWriter answerFileWriter = new FileWriter(new File(workDir, currentQuestion), true);
					answerFileWriter.append(reply + "\n");
					answerFileWriter.flush();
					answerFileWriter.close();
				} catch (Exception except1) {
					System.out.println("Exception : " + except1.toString());
					except1.printStackTrace();
				}
			}

			File[] questionFiles = workDir.listFiles();
			Random questionRandom = new Random();
			File questionSelected = questionFiles[questionRandom.nextInt(questionFiles.length)];
			currentQuestion = questionSelected.getName();
			currentQuestions.put(channel.getID(), currentQuestion);
			DiscordBot.sendMessage(channel, currentQuestion);


		} else {
			// La phrase est identifiée comme question ; l'IA y répond

			//on oublie la question précédente venant de l'IA pour éviter qu'une réplique tardive soit considérée comme réponse
			currentQuestions.remove(channel.getID());
			File questionFile = new File(workDir, reply);

			if (questionFile.exists()) {
				InputStream ips = new FileInputStream(questionFile);
				InputStreamReader ipsr = new InputStreamReader(ips);
				BufferedReader br = new BufferedReader(ipsr);
				List<String> lines = new ArrayList<>();
				String line;

				while ((line = br.readLine()) != null) {
					if (line.length() > 0) { lines.add(line); }
				}

				br.close();

				if (lines.size() > 0) {

					Random lineRandom = new Random();
					String lineSelected = lines.get(lineRandom.nextInt(lines.size()));
					DiscordBot.sendMessage(channel, lineSelected);

				} else {
					DiscordBot.sendMessage(channel, "Je ne sais pas");
				}
			} else {
				DiscordBot.sendMessage(channel, "Je ne sais pas");
				questionFile.createNewFile();
			}
		}
	}

	public static boolean isChannelWide(IChannel channel) {
		return wholeChannels.contains(channel.getID());
	}

}
