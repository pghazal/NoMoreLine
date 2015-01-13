package fr.ece.pfe_project.weather.model;

import org.w3c.dom.Element;

public class Units {

	public static final String TEMP_F = "f";
	public static final String TEMP_C = "c";

	String temperature;
	String distance;
	String pressure;
	String speed;

	public String getTemperature() {
		return temperature;
	}

	public void setTemperature(String temperature) {
		this.temperature = temperature;
	}

	public String getDistance() {
		return distance;
	}

	public void setDistance(String distance) {
		this.distance = distance;
	}

	public String getPressure() {
		return pressure;
	}

	public void setPressure(String pressure) {
		this.pressure = pressure;
	}

	public String getSpeed() {
		return speed;
	}

	public void setSpeed(String speed) {
		this.speed = speed;
	}

	public Units(Element element) {
		parseDate(element);
	}

	private void parseDate(Element elem) {
		temperature = elem.getAttribute("temperature");
		distance = elem.getAttribute("distance");
		pressure = elem.getAttribute("pressure");
		speed = elem.getAttribute("speed");
	}

	@Override
	public String toString() {
		return "Units [temperature=" + temperature + ", distance=" + distance
				+ ", pressure=" + pressure + ", speed=" + speed + "]";
	}

}
