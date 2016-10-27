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
        DiscordBot.sendMessage(message.getChannel(), generateCityName());
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
        return table.get(rand.nextInt(table.size()));
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

    private static String capitalize(String str)
    {
        return Character.toUpperCase(str.charAt(0))
                + str.substring(1, str.length());
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
