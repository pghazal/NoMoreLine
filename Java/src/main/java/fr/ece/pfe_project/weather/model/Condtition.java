package fr.ece.pfe_project.weather.model;

import java.util.Date;

import org.w3c.dom.Element;

import fr.ece.pfe_project.weather.util.DateUtils;

public class Condtition {

	String[] conditions = new String[] { "tornado", "tropical storm",
			"hurricane", "severe thunderstorms", "thunderstorms",
			"mixed rain and snow", "mixed rain and sleet",
			"mixed snow and sleet", "freezing drizzle", "drizzle",
			"freezing rain", "showers", "showers", "snow flurries",
			"light snow showers", "blowing snow", "snow", "hail", "sleet",
			"dust", "foggy", "haze", "smoky", "blustery", "windy", "cold",
			"cloudy", "mostly cloudy (night)", "mostly cloudy (day)",
			"partly cloudy (night)", "partly cloudy (day)", "clear (night)",
			"sunny", "fair (night)", "fair (day)", "mixed rain and hail",
			"hot", "isolated thunderstorms", "scattered thunderstorms",
			"scattered thunderstorms", "scattered showers", "heavy snow",
			"scattered snow showers", "heavy snow", "partly cloudy",
			"thundershowers", "snow showers", "isolated thundershowers",
			"not available", };

	public String getConditionByCode(int code) {
		return conditions[code];
	}

	String text;
	int code;
	int temp;
	Date date;
	String iconUrl;

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

	public int getCode() {
		return code;
	}

	public void setCode(int code) {
		this.code = code;
	}

	public int getTemp() {
		return temp;
	}

	public void setTemp(int temp) {
		this.temp = temp;
	}

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	public Condtition(Element element) {
		parseData(element);
	}

	public String getIconUrl() {
		return iconUrl;
	}

	public void setIconUrl(String iconUrl) {
		this.iconUrl = iconUrl;
	}

	private String getIconUrl(int code) {
		String link = "http://l.yimg.com/a/i/us/we/52/%d.gif";
		return String.format(link, code);
	}

	private void parseData(Element elem) {
		text = elem.getAttribute("text");
		code = Integer.parseInt(elem.getAttribute("code"));
		temp = Integer.parseInt(elem.getAttribute("temp"));
		date = DateUtils.parseDate(elem.getAttribute("date"));
		iconUrl = getIconUrl(code);
	}

	@Override
	public String toString() {
		return "Condtition [text=" + text + ", code=" + code + ", temp=" + temp
				+ ", date=" + date + "]";
	}

}
