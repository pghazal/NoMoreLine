package fr.ece.pfe_project.weather.model;

import org.w3c.dom.Element;

public class Atmosphere {
	float humidity;
	float visibility;
	float pressure;
	int rising;

	public float getHumidity() {
		return humidity;
	}

	public void setHumidity(float humidity) {
		this.humidity = humidity;
	}

	public float getVisibility() {
		return visibility;
	}

	public void setVisibility(float visibility) {
		this.visibility = visibility;
	}

	public float getPressure() {
		return pressure;
	}

	public void setPressure(float pressure) {
		this.pressure = pressure;
	}

	public int getRising() {
		return rising;
	}

	public void setRising(int rising) {
		this.rising = rising;
	}

	public Atmosphere(Element element) {
		parseData(element);
	}

	private void parseData(Element elem) {
		humidity = Float.parseFloat(elem.getAttribute("humidity"));
		visibility = Float.parseFloat(elem.getAttribute("visibility"));
		pressure = Float.parseFloat(elem.getAttribute("pressure"));
		rising = Integer.parseInt(elem.getAttribute("rising"));
	}

	@Override
	public String toString() {
		return "Atmosphere [humidity=" + humidity + ", visibility="
				+ visibility + ", pressure=" + pressure + ", rising=" + rising
				+ "]";
	}

}
