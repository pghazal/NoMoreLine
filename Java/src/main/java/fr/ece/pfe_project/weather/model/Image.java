package fr.ece.pfe_project.weather.model;

import org.w3c.dom.Element;

public class Image {

	String title;
	String link;
	String url;
	int width;
	int height;

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

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public int getWidth() {
		return width;
	}

	public void setWidth(int width) {
		this.width = width;
	}

	public int getHeight() {
		return height;
	}

	public void setHeight(int height) {
		this.height = height;
	}

	public Image(Element element) {
		parseDate(element);
	}

	private void parseDate(Element elem) {
		title = elem.getElementsByTagName("title").item(0).getTextContent();
		link = elem.getElementsByTagName("link").item(0).getTextContent();
		url = elem.getElementsByTagName("url").item(0).getTextContent();
		width = Integer.parseInt(elem.getElementsByTagName("width").item(0)
				.getTextContent());
		height = Integer.parseInt(elem.getElementsByTagName("height").item(0)
				.getTextContent());
	}

	@Override
	public String toString() {
		return "Image [title=" + title + ", link=" + link + ", url=" + url
				+ ", width=" + width + ", height=" + height + "]";
	}

}
