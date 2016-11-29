package net.zyuiop.discordbot.json.mal;

/**
 * @author zyuiop
 */
public class TopAnime {
	private int score;
	private int anime_num_episodes;
	private String anime_title;
	private String anime_media_type_string;

	public int getScore() {
		return score;
	}

	public void setScore(int score) {
		this.score = score;
	}

	public int getAnime_num_episodes() {
		return anime_num_episodes;
	}

	public void setAnime_num_episodes(int anime_num_episodes) {
		this.anime_num_episodes = anime_num_episodes;
	}

	public String getAnime_title() {
		return anime_title;
	}

	public void setAnime_title(String anime_title) {
		this.anime_title = anime_title;
	}

	public String getAnime_media_type_string() {
		return anime_media_type_string;
	}

	public void setAnime_media_type_string(String anime_media_type_string) {
		this.anime_media_type_string = anime_media_type_string;
	}
}
