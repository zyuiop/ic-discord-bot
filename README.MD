# IC Discord Bot

[![Build Status](https://travis-ci.org/zyuiop/ic-discord-bot.svg?branch=master)](https://travis-ci.org/zyuiop/ic-discord-bot)

A small bot I build to be used on the EPFL IC discord team.

# Add a command

To add a command, create a new class in the package `net.zyuiop.discordbot.commands`. The class must extend the `DiscordCommand` class. You then have to create a super constructor. The first argument is mandatory and represents the name of your command. The second argument represents the help text (displayed in !icbot command).

Then, you have to override the `run` message. It takes an IMessage as an argument. Here is the documentation of this class : [https://jitpack.io/com/github/austinv11/Discord4j/2.6.1/javadoc/sx/blah/discord/handle/obj/IMessage.html]. You can do what you want with it. Note that you can get the channel of the message, to send new messages.

You can see an example here : [https://github.com/zyuiop/ic-discord-bot/blob/master/src/main/java/net/zyuiop/discordbot/commands/AboutCommand.java]

When you have finished your command you MUST register it in the command registry : to do that you just have to create a new instance of the command, for example in the `main` method of the bot : [https://github.com/zyuiop/ic-discord-bot/blob/master/src/main/java/net/zyuiop/discordbot/DiscordBot.java#L78]. 

Finally, just push and make a pull request o/
