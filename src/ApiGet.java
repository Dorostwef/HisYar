import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import java.io.FileWriter;
import java.io.IOException;
import java.net.http.HttpResponse;

import static java.lang.Thread.sleep;

public class ApiGet {
    public static String inLine (String s) {
        return s.replace("\n", "\\n");
    }
    public static void main(String[] args) {
        try {
            FileWriter fileWriter = new FileWriter("api.txt");
            for (int year = -500; year <= 2025; year++) {
                HttpCall apiCall = new HttpCall("https://events.historylabs.io/year/" + year, "");

                HttpResponse<String> httpResponse;
                do {
                    httpResponse = apiCall.get("", "", "web");
                } while (httpResponse == null);
                JSONObject response = (JSONObject) JSONUtils.stringToJSON(httpResponse.body());
                Long count = (Long)response.get("totalResults");
                JSONArray events = (JSONArray) response.get("events");
                String yearName;
                if (year < 0) {
                    yearName = -year + " BC";
                } else {
                    yearName = year + " AD";
                }
                for (int i = 0; i < count; i++) {
                    JSONObject event = (JSONObject) events.get(i);
                    fileWriter.write(inLine((event.get("content")) + " happened in " + (event.get("date")) + " " + yearName));
                    fileWriter.write("\n");
                }
                if (year % 5 == 0) {
                    System.out.println(yearName + " finished");
                }
                try {
                    sleep(230);
                } catch (InterruptedException e) {
                    System.out.println(e.getMessage());
                }
            }
            fileWriter.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
