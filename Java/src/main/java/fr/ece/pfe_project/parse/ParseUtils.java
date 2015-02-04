package fr.ece.pfe_project.parse;

import java.util.ArrayList;
import java.util.List;
import org.parse4j.Parse;
import org.parse4j.ParsePush;

/**
 *
 * @author pierreghazal
 */
public class ParseUtils {

    private final static String APP_ID = "5eqyL9xBQhuIm9oWva2vyiEU0dMmJCNNcJ8e66EC";
    private final static String API_ID = "gLAe5oNy5DuEG20YLrDy1vXqvNO7uXNzbWqTgjlI";

    public static void initialize() {
        Parse.initialize(APP_ID, API_ID);
    }

    public static void sendPush(String pos) {

        List<String> channels = new ArrayList<String>();
        channels.add("");

        ParsePush push = new ParsePush();
        push.sendInBackground("Alerte : Création de file d'attente détectée en position : " + pos, channels);
    }
}
