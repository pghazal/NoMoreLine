package fr.ece.pfe_project.weather.util;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

/*
 * Utility Class for Yahoo WOEID calculations 
 */
public class WOEIDUtils {

	public static final String GEO_API_URL = "http://query.yahooapis.com/v1/public/yql?q=select%20*%20from%20geo.places%20where%20text=";
	public static final String GEO_API_FORMAT = "&format=xml";
	public static final String WOEID_ERROR = "WOEID_NOT_FOUND";

	public String getLocationWOEID(String location) {
		String url = GEO_API_URL + "%22" + location + "%22" + GEO_API_FORMAT;
		url.replaceAll(" ", "%20");
		return queryGEO(url);
	}

	/*
	 * Get a WOEID for a Location (the first WOEID)
	 */
	private String queryGEO(String query) {
		String woeid = null;
		System.out.println(query);
		try {
			URL url = new URL(query);
			URLConnection result = url.openConnection();

			DocumentBuilderFactory factory = DocumentBuilderFactory
					.newInstance();
			DocumentBuilder builder = factory.newDocumentBuilder();
			Document doc = builder.parse(result.getInputStream());
			NodeList elemList = doc.getElementsByTagName("woeid");
			if (elemList.getLength() > 0) {
				woeid = elemList.item(0).getTextContent();
			} else {
				woeid = WOEID_ERROR;
			}

		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ParserConfigurationException e) {
			e.printStackTrace();
		} catch (SAXException e) {
			e.printStackTrace();
		}

		return woeid;

	}
}
