package net.zyuiop.discordbot;

import java.util.List;
import java.util.Random;

/**
 * Created by loris on 28.10.16.
 */
public class Helpers
{
    private static Random rand = new Random();

    public static String getRandomItem(List<String> table)
    {
        return table.get(randInt(0, table.size()-1));
    }

    public static int randInt(int min, int max)
    {
        return rand.nextInt(max - min + 1) + min;
    }

    public static boolean isVowel(char c)
    {
        switch(c)
        {
            case 'a':
            case 'e':
            case 'i':
            case 'o':
            case 'u':
            case 'y':
            case 'é':
            case 'è':
            case 'ë':
            case 'ê':
            case 'ô':
            case 'ö':
            case 'â':
            case 'à':
            case 'ä':
            case 'ï':
            case 'ü':
            case 'û':
                return true;
        }
        return false;
    }
}
