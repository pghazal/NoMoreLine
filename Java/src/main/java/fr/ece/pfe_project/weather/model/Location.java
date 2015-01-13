package fr.ece.pfe_project.weather.model;

import org.w3c.dom.Element;

public class Location {
	String city;
	String region;
	String country;

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getRegion() {
		return region;
	}

	public void setRegion(String region) {
		this.region = region;
	}

	public String getCountry() {
		return country;
	}

	public void setCountry(String country) {
		this.country = country;
	}

	public Location(Element element) {
		parseDate(element);
	}

	private void parseDate(Element elem) {
		city = elem.getAttribute("city");
		region = elem.getAttribute("region");
		country = elem.getAttribute("country");
	}

	@Override
	public String toString() {
		return "Location [city=" + city + ", region=" + region + ", country="
				+ country + "]";
	}

}
