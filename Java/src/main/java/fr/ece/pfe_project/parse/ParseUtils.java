package fr.ece.pfe_project.parse;

import java.util.logging.Level;
import java.util.logging.Logger;
import org.parse4j.ParseException;
import org.parse4j.ParsePush;

/**
 *
 * @author pierreghazal
 */
public class ParseUtils {

    public static void sendPush(String pos) {
        try {
            ParsePush push = new ParsePush();
            push.setChannel("nomoreline");
            push.setMessage("Alerte : Création de file d'attente détectée en position : " + pos);
            push.send();
        } catch (ParseException ex) {
            Logger.getLogger(ParseUtils.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
