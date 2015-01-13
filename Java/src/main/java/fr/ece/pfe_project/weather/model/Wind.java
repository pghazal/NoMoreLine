package fr.ece.pfe_project.weather.model;

import org.w3c.dom.Element;

public class Wind {
	/*
	 * a model represent the RSS Response wind element
	 */
	int chill;
	int direction;
	float speed;

	public int getChill() {
		return chill;
	}

	public void setChill(int chill) {
		this.chill = chill;
	}

	public int getDirection() {
		return direction;
	}

	public void setDirection(int direction) {
		this.direction = direction;
	}

	public float getSpeed() {
		return speed;
	}

	public void setSpeed(float speed) {
		this.speed = speed;
	}

	public Wind(Element element) {
		parseDate(element);
	}

	private void parseDate(Element elem) {
		chill = Integer.parseInt(elem.getAttribute("chill"));
		direction = Integer.parseInt(elem.getAttribute("direction"));
		speed = Float.parseFloat(elem.getAttribute("speed"));
	}

	@Override
	public String toString() {
		return "Wind [chill=" + chill + ", direction=" + direction + ", speed="
				+ speed + "]";
	}

}
