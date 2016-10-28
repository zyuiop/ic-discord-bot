package net.zyuiop.discordbot.commands;

import net.zyuiop.discordbot.DiscordBot;
import net.zyuiop.discordbot.Helpers;
import sx.blah.discord.handle.obj.IMessage;

import java.io.File;
import java.lang.reflect.Array;
import java.text.NumberFormat;
import java.util.*;

/**
 * Created by loris on 28.10.16.
 */

public class InsultCommand extends DiscordCommand
{
    private List<String> nouns = new ArrayList<String>();
    private List<String> intro = Arrays.asList(
        "Espèce de", "Sale", "Bande de", "Tête de", "Pauvre", "Connard de",
        "Saloperie de", "Saleté de", "Putain de", "Sous-merde de", "Couillon de",
        "Stupide", "Abruti de", "Enfoiré de"
    );

    public InsultCommand() throws Exception
    {
        super("insult", "génère une insulte aléatoire");

        String filename = "rsrc/noms.txt";

        File file = new File(filename);
        if(!file.exists())
        {
            System.out.println("Error: could not open file *" + filename + "*.");
            return;
        }

        Scanner fileStream = new Scanner(file);

        while(fileStream.hasNextLine())
            nouns.add(fileStream.nextLine());

        System.out.println("Successfully loaded " + nouns.size() + " nouns.");
    }

    @Override
    public void run(IMessage message) throws Exception
    {
        String[] args = message.getContent().split(" ");

        if(args.length == 1)
            DiscordBot.sendMessage(message.getChannel(), generateInsult());

        else
        {
            boolean error = false;

            switch(args[1])
            {
                case "count":
                    if(args.length != 2)
                    {
                        error = true;
                        break;
                    }
                    DiscordBot.sendMessage(message.getChannel(), "Il y a " +
                        NumberFormat.getNumberInstance(Locale.FRENCH).format(
                            intro.size()*nouns.size()*(nouns.size()-1)) +
                        " combinaisons d'insultes possibles (sans les villes).");
                    break;

                case "city":
                    if(args.length != 2)
                    {
                        error = true;
                        break;
                    }
                    String insult = generateInsult();
                    insult = insult.substring(0, insult.length()-1);

                    DiscordBot.sendMessage(message.getChannel(),
                        insult + "à " + CityCommand.generateCityName() + " !");
                    break;

                case "to":
                    if(args.length != 3 || !args[2].matches("<@[\\d]*>"))
                    {
                        error = true;
                        break;
                    }
                    DiscordBot.sendMessage(message.getChannel(), args[2] + " " + generateInsult());
                    break;

                case "help":
                    DiscordBot.sendMessage(message.getChannel(),
                        "*Générateur d'insulte*\n" +
                        "*par Loris Witschard*\n\n" +
                        "**Utilisation** :\n" +
                        "`!insult` : génère une insulte\n" +
                        "`!insult to @user` : insulte l'utilisateur *@user*\n" +
                        "`!insult city` : insulte dans une ville aléatoire\n" +
                        "`!insult count` : affiche le nombre d'insultes possibles\n" +
                        "`!insult help` : affiche l'aide");
                    break;

                default:
                    error = true;
                    break;
            }

            if(error)
                DiscordBot.sendMessage(message.getChannel(), "*Erreur de syntaxe.*");
        }
    }

    private String generateInsult()
    {
        String insult = Helpers.getRandomItem(intro);

        String w1 = Helpers.getRandomItem(nouns);
        if(Helpers.isVowel(w1.charAt(0)) && insult.substring(insult.length()-2, insult.length()).equals("de"))
            insult = insult.substring(0, insult.length()-1) + "'" + w1;
        else
            insult += " " + w1;

        String w2 = Helpers.getRandomItem(nouns);
        insult += (Helpers.isVowel(w2.charAt(0)) ? " d'" : " de ") + w2 + " !";

        return insult;
    }
}