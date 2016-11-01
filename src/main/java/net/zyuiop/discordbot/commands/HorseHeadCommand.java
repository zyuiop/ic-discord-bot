package net.zyuiop.discordbot.commands;

import net.zyuiop.discordbot.DiscordBot;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import sx.blah.discord.handle.obj.IMessage;

// Created by Saralfddin on 31.10.16.
// Edited by Loris Witschard on 01.11.16.

public class HorseHeadCommand extends DiscordCommand
{
    private String next;

    public HorseHeadCommand() throws Exception
    {
        super("hhh", "affiche un contenu de Horse Head Huffer");
        String url = "http://www.anti-joke.com/posts/5000";
        Document doc = Jsoup.connect(url).get();

        next = getNext(doc);
    }

    @Override
    public void run(IMessage message) throws Exception
    {
        Document doc = Jsoup.connect(next).get();

        DiscordBot.sendMessage(message.getChannel(), "*" + getJoke(doc) + "*\n\nScore : " + getScore(doc));
        next = getNext(doc);
    }

    private String getNext(Document doc)
    {
        return doc.select("a:contains(Random)").first().attr("abs:href");
    }

    private String getJoke(Document doc)
    {
        return doc.select("h3.content").first().text();
    }

    private String getScore(Document doc)
    {
        String score = doc.select("span.value").text();
        if(score.length() == 0)
            return "**0**";

        int value = Integer.parseInt(score);
        return value < 0 ? "**" + value + "** :thumbsdown:" : "**+" + value + "** :thumbsup:";
    }
}
