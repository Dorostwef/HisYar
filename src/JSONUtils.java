import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

public class JSONUtils {
    public static Object stringToJSON (String string) {
        try {
            JSONParser parser = new JSONParser();
            return parser.parse(string);
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
    }
}
