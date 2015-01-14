package fr.ece.pfe_project.weather.test;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import fr.ece.pfe_project.weather.YWeather;
import fr.ece.pfe_project.weather.model.Units;
import fr.ece.pfe_project.weather.model.WeatherInfo;

public class Test {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		/*YWeather weather = new YWeather();
		WeatherInfo info = weather.getWeatherForWOEID("2442047", Units.TEMP_C);
		System.err.println(info.getItem().getForecastOne().getDay());
		info = weather.getWeatherForPlace("Lile", Units.TEMP_C);
		System.out.println(info.getItem().getCondtition().getIconUrl());*/
		 
		/*YWeather weather = new YWeather();
        WeatherInfo info = weather.getWeatherForPlace("Paris");
        // afficher la vitesse du vent
        System.out.println(info.getWind().getSpeed());
        System.out.println(info.getAtmosphere().toString());
        System.out.println(info.getUnits().toString());*/
        
        /*YWeather weather = new YWeather();
        WeatherInfo info = weather.getWeatherForPlace("Paris Orly", Units.TEMP_C);
        System.out.println(info.getItem().getCondtition().toString());
        System.out.println(info.getItem().getForecastOne().toString());*/
        
        try{
        		long start;
        		start = System.nanoTime();
        		
        		//-------------------------------------//
        		//Récupération des destinations du jour//
        		//-------------------------------------//
        		
	        	FileWriter fw = new FileWriter("voldujour",false);
	        	BufferedWriter output = new BufferedWriter(fw);
	        	//On se connecte au site et on charge le document html
	        	Document doc = Jsoup.connect("http://www.strasbourg.aeroport.fr/destinations/vols").get();
	        	//On récupère dans ce document la premiere balise ayant comme nom td et pour attribut class="center"
	        	int i = 1;
	        	int el = doc.select("td .center").size();
	        	for(i=1;i<el;i+=5){
	        	Element element = doc.select("td .center").get(i);
	        	String element1 =  element.text();
	        	String[] tab = element1.split("\\s");
	        	output.write(tab[0] + "\r\n");
	        	//System.out.println(element1);
	        	output.flush();
	        	}
	        	output.close();
	        	
	        	//----------------------------------------------------//
	        	//Récupération de la météo liée à chaque destinations//
	        	//----------------------------------------------------//
	        	
	        	String chaine = "";
	        	YWeather weather = new YWeather();
	        	InputStream ips=new FileInputStream("voldujour"); 
				InputStreamReader ipsr=new InputStreamReader(ips);
				BufferedReader br=new BufferedReader(ipsr);
				String ligne;
				FileWriter fw1 = new FileWriter("VoldujourEtMeteo",false);
				BufferedWriter output1 = new BufferedWriter(fw1);
				while ((ligne=br.readLine())!=null){
					System.out.println(ligne);
					chaine+=ligne+"\n";
					WeatherInfo info = weather.getWeatherForPlace(ligne, Units.TEMP_C);
					output1.write(ligne + "--" + info.getItem().getCondtition().getConditionByCode(info.getItem().getCondtition().getCode()) + "--" + info.getItem().getCondtition().getTemp() + "\r\n");
					output1.flush();
					int code = info.getItem().getCondtition().getCode();
					if(code == 32 || code == 33 || code == 34 || code == 36){
						if(info.getItem().getCondtition().getTemp() >=20)
						System.out.println("Il fait beau à " + ligne + " et il fait " + info.getItem().getCondtition().getTemp() + "°c, les vols vont suremennt être plein pour ces destinations");
					}
				}
				br.close();
				output1.close();
				
				//Calcul du temps d'execution du programme
				long duree = System.nanoTime() - start;
				duree = duree/1000000000;
				System.out.println(duree + "s");
        	 }
        
        	//--------------------------------------------//
        	//gestion des différentes exceptions possibles//
        	//--------------------------------------------//
        	 catch(MalformedURLException e){
        	 System.out.println(e);
        	 }
        	 catch(IOException e){
        	 System.out.println(e);
        	 }
        	catch(NumberFormatException ex){
        		System.out.println(ex);
        	}
        	catch(java.lang.ArrayIndexOutOfBoundsException exce){
        		System.out.println(exce);
        	}
	}

}
