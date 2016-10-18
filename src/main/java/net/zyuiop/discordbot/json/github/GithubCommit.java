package net.zyuiop.discordbot.json.github;

/**
 * @author zyuiop
 */
public class GithubCommit {
	private String message;
	private String html_url;
	private String sha;
	private Author author;
	private Author committer;

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public String getHtml_url() {
		return html_url;
	}

	public void setHtml_url(String html_url) {
		this.html_url = html_url;
	}

	public String getSha() {
		return sha;
	}

	public void setSha(String sha) {
		this.sha = sha;
	}

	public Author getAuthor() {
		return author;
	}

	public void setAuthor(Author author) {
		this.author = author;
	}

	public Author getCommitter() {
		return committer;
	}

	public void setCommitter(Author committer) {
		this.committer = committer;
	}

	public static class Author {
		private String name;
		private String email;
		private String date;

		public String getName() {
			return name;
		}

		public void setName(String name) {
			this.name = name;
		}

		public String getEmail() {
			return email;
		}

		public void setEmail(String email) {
			this.email = email;
		}

		public String getDate() {
			return date;
		}

		public void setDate(String date) {
			this.date = date;
		}
	}
}
