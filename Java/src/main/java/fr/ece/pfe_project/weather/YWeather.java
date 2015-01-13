package fr.ece.pfe_project.weather;

import java.io.IOException;

import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.xml.sax.SAXException;

import fr.ece.pfe_project.weather.model.Astronomy;
import fr.ece.pfe_project.weather.model.Atmosphere;
import fr.ece.pfe_project.weather.model.Condtition;
import fr.ece.pfe_project.weather.model.Forecast;
import fr.ece.pfe_project.weather.model.Image;
import fr.ece.pfe_project.weather.model.Item;
import fr.ece.pfe_project.weather.model.Location;
import fr.ece.pfe_project.weather.model.Units;
import fr.ece.pfe_project.weather.model.WeatherInfo;
import fr.ece.pfe_project.weather.model.Wind;
import fr.ece.pfe_project.weather.util.WOEIDUtils;


/*
 * @author Guendouz Mohamed
 */
public class YWeather {

	/*
	 * YWeather : Java Wrapper for the Yahoo Weather API
	 */

	public static final String YAHOO_WEATHER_API = "http://weather.yahooapis.com/forecastrss?w=";
	public static final String YAHOO_WEATHER_ERROR = "Yahoo! Weather - Error";

	/**
	 * @param doc
	 *            :
	 * @return : WeatherInfo object , null if error
	 */
	private WeatherInfo parseWeatherInfo(Document doc) {
		WeatherInfo result = new WeatherInfo();
		Node titleNode = doc.getElementsByTagName("title").item(0);
		if (titleNode.getTextContent().equals(YAHOO_WEATHER_ERROR))
			return null;

		result.setTitle(titleNode.getTextContent());
		result.setLink(doc.getElementsByTagName("link").item(0)
				.getTextContent());

		Element location = (Element) doc.getElementsByTagName(
				"yweather:location").item(0);
		result.setLocation(new Location(location));
		Element units = (Element) doc.getElementsByTagName("yweather:units")
				.item(0);
		result.setUnits(new Units(units));
		Element wind = (Element) doc.getElementsByTagName("yweather:wind")
				.item(0);
		result.setWind(new Wind(wind));
		Element atmosphere = (Element) doc.getElementsByTagName(
				"yweather:atmosphere").item(0);
		result.setAtmosphere(new Atmosphere(atmosphere));
		Element astronomy = (Element) doc.getElementsByTagName(
				"yweather:astronomy").item(0);
		result.setAstronomy(new Astronomy(astronomy));
		Element image = (Element) doc.getElementsByTagName("image").item(0);
		result.setImage(new Image(image));
		Element item = (Element) doc.getElementsByTagName("item").item(0);
		result.setItem(new Item(item));
		Element condition = (Element) doc.getElementsByTagName(
				"yweather:condition").item(0);
		result.getItem().setCondtition(new Condtition(condition));
		Element forecastOne = (Element) doc.getElementsByTagName(
				"yweather:forecast").item(0);
		result.getItem().setForecastOne(new Forecast(forecastOne));
		Element forecastTwo = (Element) doc.getElementsByTagName(
				"yweather:forecast").item(1);
		result.getItem().setForecastTwo(new Forecast(forecastTwo));

		return result;
	}

	public WeatherInfo getWeatherForWOEID(String woeid, String tempUnit) {
		WeatherInfo result = null;

		String queryURL = YAHOO_WEATHER_API + woeid;
		if (tempUnit == Units.TEMP_C)
			queryURL += "&u=c";

		queryURL.replaceAll(" ", "%20");
		try {
			URL url = new URL(queryURL);
			URLConnection response = url.openConnection();

			DocumentBuilderFactory factory = DocumentBuilderFactory
					.newInstance();
			DocumentBuilder builder = factory.newDocumentBuilder();

			Document doc = builder.parse(response.getInputStream());
			result = parseWeatherInfo(doc);

		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ParserConfigurationException e) {
			e.printStackTrace();
		} catch (SAXException e) {
			e.printStackTrace();
		}

		return result;

	}

	public WeatherInfo getWeatherForWOEID(String woeid) {
		return getWeatherForWOEID(woeid, Units.TEMP_F);

	}

	public WeatherInfo getWeatherForPlace(String place, String tempUnit) {
		WOEIDUtils woeidUtils = new WOEIDUtils();
		String woeid = woeidUtils.getLocationWOEID(place);
		if (woeid == WOEIDUtils.WOEID_ERROR)
			return null;
		return getWeatherForWOEID(woeid, tempUnit);
	}

	public WeatherInfo getWeatherForPlace(String place) {
		WOEIDUtils woeidUtils = new WOEIDUtils();
		String woeid = woeidUtils.getLocationWOEID(place);
		if (woeid == WOEIDUtils.WOEID_ERROR)
			return null;
		return getWeatherForWOEID(woeid, Units.TEMP_F);
	}
}
