package net.zyuiop.discordbot.json.menus;

import java.util.List;
import com.google.gson.annotations.SerializedName;

/**
 * @author zyuiop
 */
public class MenuList {
	private String title;
	private String description;
	@SerializedName("item")
	private List<Item> items;

	public static class Item {
		@SerializedName("title")
		private String title;
		@SerializedName("link")
		private String link;
		@SerializedName("description")
		private String description;

		public String getTitle() {
			return title;
		}

		public void setTitle(String title) {
			this.title = title;
		}

		public String getLink() {
			return link;
		}

		public void setLink(String link) {
			this.link = link;
		}

		public String getDescription() {
			return description;
		}

		public void setDescription(String description) {
			this.description = description;
		}
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public List<Item> getItems() {
		return items;
	}

	public void setItems(List<Item> items) {
		this.items = items;
	}
}
