package net.zyuiop.discordbot;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.Random;
import org.apache.commons.io.IOUtils;

// Created by Loris Witschard on 28.10.16.

public class Helpers {
	private static Random rand = new Random();

	public static String getRandomItem(List<String> table) {
		return table.get(randInt(0, table.size() - 1));
	}

	public static int randInt(int min, int max) {
		return rand.nextInt(max - min + 1) + min;
	}

	public static boolean isVowel(char c) {
		String vowels = "aäâàáæeëêèéiïîìíoöôòóœuüûùúyÿŷỳý";
		return vowels.indexOf(c) >= 0;
	}

	public static void extractFile(String name, File target) {
		if (!target.getParentFile().exists()) {
			target.getParentFile().mkdirs();
		}

		try {
			IOUtils.copy(Helpers.class.getResourceAsStream(name), new FileOutputStream(target));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
