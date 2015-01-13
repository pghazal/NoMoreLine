package fr.ece.pfe_project.weather.model;

import java.util.Date;

import org.w3c.dom.Element;

import fr.ece.pfe_project.weather.util.DateUtils;

public class Forecast {
	String day;
	Date date;
	int low;
	int high;
	String text;
	int code;

	public String getDay() {
		return day;
	}

	public void setDay(String day) {
		this.day = day;
	}

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	public int getLow() {
		return low;
	}

	public void setLow(int low) {
		this.low = low;
	}

	public int getHigh() {
		return high;
	}

	public void setHigh(int high) {
		this.high = high;
	}

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

	public Forecast(Element element) {
		parseDate(element);
	}

	private void parseDate(Element elem) {
		day = elem.getAttribute("day");
		date = DateUtils.parseForecastDate(elem.getAttribute("date"));
		low = Integer.parseInt(elem.getAttribute("low"));
		high = Integer.parseInt(elem.getAttribute("high"));
		text = elem.getAttribute("text");
		code = Integer.parseInt(elem.getAttribute("code"));

	}

	@Override
	public String toString() {
		return "Forecast [day=" + day + ", date=" + date + ", low=" + low
				+ ", high=" + high + ", text=" + text + ", code=" + code + "]";
	}

}
