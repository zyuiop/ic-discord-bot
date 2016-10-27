package net.zyuiop.discordbot.commands;

import java.util.Arrays;
import java.util.List;
import java.util.Random;

import net.zyuiop.discordbot.DiscordBot;
import sx.blah.discord.handle.obj.IMessage;


public class CityCommand extends DiscordCommand
{
    public CityCommand()
    {
        super("city", "génère un nom de ville aléatoire");
    }

    @Override
    public void run(IMessage message) throws Exception
    {
        String[] args = message.getContent().split(" ");

        if(args.length == 1)
            DiscordBot.sendMessage(message.getChannel(), generateCityName());

        else
        {
            boolean error = false;

            switch(args[1])
            {
                case "print":
                    if(args.length != 3 || !isParsable(args[2]))
                    {
                        error = true;
                        break;
                    }
                    int n = Integer.parseInt(args[2]);
                    if(n < 1)
                    {
                        DiscordBot.sendMessage(message.getChannel(), generateInsult(message, n));
                        break;
                    }

                    String msg = new String();
                    for(int i=0; i<Math.min(n, 10); ++i)
                        msg += generateCityName() + '\n';

                    if(n > 10)
                        msg += "...";

                    DiscordBot.sendMessage(message.getChannel(), msg);
                    break;

                case "help":
                    DiscordBot.sendMessage(message.getChannel(),
                        "*Générateur de nom de ville aléatoire*\n" +
                        "*par Loris Witschard*\n\n" +
                        "**Utilisation** :\n" +
                        "`!city` : génère un nom de ville\n" +
                        "`!city print n` : génère *n* nom(s) de ville\n" +
                        "`!city help` : affiche l'aide");
                    break;

                default:
                    error = true;
                    break;
            }

            if(error)
                DiscordBot.sendMessage(message.getChannel(), "*Erreur de syntaxe.*");
        }
    }

    private String generateCityName()
    {
        boolean shortName = Math.random() < 0.96;

        String city = new String();

        while(city.length() < 4)
            city = capitalize(generateRandomWord());

        if(shortName && Math.random() < 0.02)
            city = (Math.random() < 0.5 ? "Saint-" : "Sainte-") + city;

        else if(!shortName)
            city += "-" + getRandomItem(links) + "-" + capitalize(generateRandomWord());

        return city;
    }

    private String generateRandomWord()
    {
        int wordSize = randInt(2, 6);
        boolean startsWithCons = Math.random() < 0.75;

        String word = getRandomItem(startsWithCons ? consonantsBegin : vowelsBegin);

        for(int i=1; i<wordSize; ++i)
        {
            if((i + (startsWithCons ? 1 : 0)) % 2 != 0)
                word += getRandomItem(i==wordSize-1 ? consonantsEnd : consonants);
            else
                word += getRandomItem(i==wordSize-1 ? vowelsEnd : vowels);
        }

        if(word.charAt(word.length()-1) == 'e')
            if(Math.random() < 0.33)
            {
                if(Math.random() < 0.75)
                    word = word.substring(0, word.length()-1) + "ès";
                else
                    word += "s";
            }

        int index = 0;
        for(int i=0; i<word.length()-2; ++i)
            if(word.charAt(i) == 'e' && !isVowel(word.charAt(i+1)) && isVowel(word.charAt(i+2)))
                index = i;

        if(index != 0)
            word = word.substring(0, word.length()-1) + "é";

        return word;
    }

    private static String getRandomItem(List<String> table)
    {
        return table.get(randInt(0, table.size()-1));
    }

    private static int randInt(int min, int max)
    {
        return rand.nextInt(max - min + 1) + min;
    }

    private static boolean isVowel(char c)
    {
        switch(c)
        {
            case 'a':
            case 'e':
            case 'i':
            case 'o':
            case 'u':
            case 'y':
                return true;
        }
        return false;
    }

    private static String generateInsult(IMessage message, int n)
    {
        int h = message.getTimestamp().plusMinutes(10).getHour();
        String time = (h == 0 ? "minuit" : (h < 10 ? h + "h du mat" : (h == 12 ? "midi" : h + "h")));

        return getRandomItem(Arrays.asList(
            "Je dois en afficher " + n + " ? Tu te crois malin, " + message.getAuthor() + "?",
            "Ok, je fais rien du coup ?",
            "Très bien je vais me faire foutre.",
            "Je la sentais venir cette blague.",
            ":middle_finger:",
            "Bravo " + message.getAuthor() + " ( ͡° ͜ʖ ͡°)",
            "T'as vraiment que ça à faire à " + time + " ?",
            "Apparemment, " + message.getAuthor() + " se fait chier.",
            "Tu ferais pas mieux de bosser un peu ?",
            (h < 7 ? "Tu dors pas à " + time + ", toi ?" : "Je vais bientôt aller me coucher je pense."),
            "On applaudit très fort " + message.getAuthor() + " pour son humour subtile.",
            "Connard.",
            "Ah l'enfoiré.",
            "Nique ma vie.",
            "Oui mais non."));
    }

    private static String capitalize(String str)
    {
        return Character.toUpperCase(str.charAt(0))
                + str.substring(1, str.length());
    }

    public static boolean isParsable(String input)
    {
        boolean parsable = true;
        try
        {
            Integer.parseInt(input);
        }
        catch(NumberFormatException e)
        {
            parsable = false;
        }

        return parsable;
    }

    private static Random rand = new Random();

    private List<String> consonants = Arrays.asList(
            "b", "br", "bl", "c", "ch", "cl", "cr", "d", "dr", "f", "fr", "fl",
            "g", "gg", "gl", "gh", "gu", "h", "j", "l", "ll", "m", "mb",
            "mp", "mm", "mn", "n", "nn", "nd", "nch", "ns", "nt", "p", "pp","ps", "pt", "pl", "pr", "ph", "qu",
            "r", "rr", "rm", "rn", "rt", "rb", "rp", "rs", "rc", "rqu", "rv", "s", "ss",
            "st", "sp", "sc", "sl", "sv", "sb", "sg", "t", "tt", "tr", "ts",
            "th", "v", "vl", "vr");

    private List<String> consonantsEnd = Arrays.asList(
            "bre", "ble", "c", "cle", "che", "cre", "ck", "ce", "d", "dre", "f", "fe",  "fre", "fle",
            "g", "gle", "ge", "gny", "gy", "le", "lle", "m", "mb", "me", "ne", "nd", "ny", "pe",
            "pte", "ple", "pre", "pry", "phe", "que", "r", "re", "ry", "se", "ste", "te",
            "t", "ty", "tte", "tre", "ve", "vre", "vry", "x", "fort", "bourg", "ville", "ille");

    private List<String> consonantsBegin = Arrays.asList(
            "b", "br", "bl", "c", "ch", "cl", "cr", "d", "dr", "f", "fr", "fl", "g", "gu",
            "gl", "gh", "h", "j", "l", "m", "n", "p", "pl", "pr", "ph", "qu", "r", "s",
            "st", "sp", "sc", "sl", "t", "tr", "th", "v", "vl", "vr" );

    private List<String> vowels = Arrays.asList(
            "a", "au", "ai", "e", "eu", "ei", "i", "o", "ou",  "u");

    private List<String> vowelsEnd = Arrays.asList(
            "a", "an", "ans", "aux", "au", "ais", "ay", "aix", "er", "ens", "eix", "eux",
            "ey", "ès", "ie", "in", "is", "o", "on", "ons", "ou", "oy", "ois", "oux", "us",
            "un", "y", "ord", "ort", "ard", "art");

    private List<String> vowelsBegin = Arrays.asList(
            "a", "au", "e", "eu", "o", "ou", "u");

    private List<String> links = Arrays.asList(
            "en", "sur", "près");
}
