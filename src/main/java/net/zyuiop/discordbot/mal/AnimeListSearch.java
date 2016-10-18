package net.zyuiop.discordbot.mal;

import java.util.List;

/**
 * @author zyuiop
 */
public class AnimeListSearch {
	private List<AnimeListCategory> categories;

	public AnimeListSearch() {
	}

	public List<AnimeListCategory> getCategories() {
		return categories;
	}

	public void setCategories(List<AnimeListCategory> categories) {
		this.categories = categories;
	}

	public static class AnimeListCategory {
		private String type;
		private List<Anime> items;

		public AnimeListCategory() {
		}

		public String getType() {
			return type;
		}

		public void setType(String type) {
			this.type = type;
		}

		public List<Anime> getItems() {
			return items;
		}

		public void setItems(List<Anime> items) {
			this.items = items;
		}

		public static class Anime {
			private int id;
			private String name;
			private String url;
			private String image_url;
			private String thumbnail_url;
			private Payload payload;
			private String es_score;

			public Anime() {
			}

			public int getId() {
				return id;
			}

			public void setId(int id) {
				this.id = id;
			}

			public String getName() {
				return name;
			}

			public void setName(String name) {
				this.name = name;
			}

			public String getUrl() {
				return url;
			}

			public void setUrl(String url) {
				this.url = url;
			}

			public String getImage_url() {
				return image_url;
			}

			public void setImage_url(String image_url) {
				this.image_url = image_url;
			}

			public String getThumbnail_url() {
				return thumbnail_url;
			}

			public void setThumbnail_url(String thumbnail_url) {
				this.thumbnail_url = thumbnail_url;
			}

			public Payload getPayload() {
				return payload;
			}

			public void setPayload(Payload payload) {
				this.payload = payload;
			}

			public String getEs_score() {
				return es_score;
			}

			public void setEs_score(String es_score) {
				this.es_score = es_score;
			}

			public static class Payload {
				private String media_type;
				private String start_year;
				private String aired;
				private String score;
				private String status;

				public Payload() {
				}

				public String getMedia_type() {
					return media_type;
				}

				public void setMedia_type(String media_type) {
					this.media_type = media_type;
				}

				public String getStart_year() {
					return start_year;
				}

				public void setStart_year(String start_year) {
					this.start_year = start_year;
				}

				public String getAired() {
					return aired;
				}

				public void setAired(String aired) {
					this.aired = aired;
				}

				public String getScore() {
					return score;
				}

				public void setScore(String score) {
					this.score = score;
				}

				public String getStatus() {
					return status;
				}

				public void setStatus(String status) {
					this.status = status;
				}
			}
		}
	}
}
