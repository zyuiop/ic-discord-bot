package net.zyuiop.discordbot.commands;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import net.zyuiop.discordbot.DiscordBot;
import org.apache.commons.lang3.StringUtils;
import sx.blah.discord.handle.obj.IMessage;

/**
 * @author zyuiop
 */
public class CwgCommand extends DiscordCommand {
	private final static Map<String, String> currentQuestions = new HashMap<>();
	private final File workDir;


	public CwgCommand(File workDir) {
		super("cwg", "discuter avec le Credule Wise Guy");
		this.workDir = workDir;
	}

	@Override
	public void run(IMessage message) throws Exception {
		// Réplique de question
		new File(workDir + "Pose-moi une question").createNewFile();

		//Boucle de conversation
		//Enregistrement de la réplique de l'utilisateur
		String[] args = message.getContent().split(" ");
		if (args.length < 2) {
			DiscordBot.sendMessage(message.getChannel(), "Aucun message entré !");
			return;
		}

		String reply = StringUtils.join(Arrays.copyOfRange(args, 1, args.length), " ");
		reply = StringUtils.capitalize(reply);

		if (!reply.contains("?")) {
			String currentQuestion = currentQuestions.get(message.getChannel().getID());

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
			currentQuestions.put(message.getChannel().getID(), currentQuestion);
			DiscordBot.sendMessage(message.getChannel(), currentQuestion);


		} else {
			// La phrase est identifiée comme question ; l'IA y répond

			//on oublie la question précédente venant de l'IA pour éviter qu'une réplique tardive soit considérée comme réponse
			currentQuestions.remove(message.getChannel().getID());
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
					DiscordBot.sendMessage(message.getChannel(), lineSelected);

				} else {
					DiscordBot.sendMessage(message.getChannel(), "Je ne sais pas");
				}
			} else {
				DiscordBot.sendMessage(message.getChannel(), "Je ne sais pas");
				questionFile.createNewFile();
			}
		}
	}

}
