package net.zyuiop.discordbot.json.github;

/**
 * @author zyuiop
 */
public class GithubReference {
	private String ref;
	private String url;
	private RefObject object;

	public String getRef() {
		return ref;
	}

	public void setRef(String ref) {
		this.ref = ref;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public RefObject getObject() {
		return object;
	}

	public void setObject(RefObject object) {
		this.object = object;
	}

	public static class RefObject {
		private String sha;
		private String type;
		private String url;

		public String getSha() {
			return sha;
		}

		public void setSha(String sha) {
			this.sha = sha;
		}

		public String getType() {
			return type;
		}

		public void setType(String type) {
			this.type = type;
		}

		public String getUrl() {
			return url;
		}

		public void setUrl(String url) {
			this.url = url;
		}
	}
}
