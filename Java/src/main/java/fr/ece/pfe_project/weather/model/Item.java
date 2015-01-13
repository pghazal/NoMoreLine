package fr.ece.pfe_project.weather.model;

import java.util.Date;

import org.w3c.dom.Element;

import fr.ece.pfe_project.weather.util.DateUtils;

public class Item {
	String title;
	String link;
	String description;
	String guid;
	Date pubDate;
	float latitude;
	float longitude;
	Condtition condtition;
	Forecast forecastOne;
	Forecast forecastTwo;

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

	public String getGuid() {
		return guid;
	}

	public void setGuid(String guid) {
		this.guid = guid;
	}

	public Date getPubDate() {
		return pubDate;
	}

	public void setPubDate(Date pubDate) {
		this.pubDate = pubDate;
	}

	public float getLatitude() {
		return latitude;
	}

	public void setLatitude(float latitude) {
		this.latitude = latitude;
	}

	public float getLongitude() {
		return longitude;
	}

	public void setLongitude(float longitude) {
		this.longitude = longitude;
	}

	public Condtition getCondtition() {
		return condtition;
	}

	public void setCondtition(Condtition condtition) {
		this.condtition = condtition;
	}

	public Forecast getForecastOne() {
		return forecastOne;
	}

	public void setForecastOne(Forecast forecastOne) {
		this.forecastOne = forecastOne;
	}

	public Forecast getForecastTwo() {
		return forecastTwo;
	}

	public void setForecastTwo(Forecast forecastTwo) {
		this.forecastTwo = forecastTwo;
	}

	private void parseDate(Element elem) {
		title = elem.getElementsByTagName("title").item(0).getTextContent();
		link = elem.getElementsByTagName("link").item(0).getTextContent();
		description = elem.getElementsByTagName("description").item(0)
				.getTextContent();
		guid = elem.getElementsByTagName("guid").item(0).getTextContent();
		pubDate = DateUtils.parseDate(elem.getElementsByTagName("pubDate")
				.item(0).getTextContent());
		latitude = Float.parseFloat(elem.getElementsByTagName("geo:lat")
				.item(0).getTextContent());
		longitude = Float.parseFloat(elem.getElementsByTagName("geo:long")
				.item(0).getTextContent());

	}

	public Item(Element element) {
		parseDate(element);
	}

	@Override
	public String toString() {
		return "Item [title=" + title + ", pubDate=" + pubDate + ", latitude="
				+ latitude + ", longitude=" + longitude + "]";
	}

}
