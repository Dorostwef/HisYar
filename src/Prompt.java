import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import java.net.http.HttpResponse;

public class Prompt {
    private String content;
    private HttpResponse<String> answer;
    private String type;
    private String model;

    public void query (String type, String model) {
        this.type = type;
        this.model = model;
        HttpCall httpCall = new HttpCall("http://localhost:11434/api/" + type, "{\"model\": \"" + model + "\", \"prompt\": \"" + content + "\"}");
        answer = httpCall.get("", "", "local");
    }

    public Prompt(String content) {
        this.content = content;
        answer = null;
    }

    public String getContent() {
        return content;
    }

    public HttpResponse<String> getAnswer() {
        return answer;
    }

    public void Print () {
        System.out.println(getString());
    }

    public String getString () {
        String all = "";
        if (answer == null) {
            all = "The prompt is not queried yet";
        } else {
            int responseCode = answer.statusCode();
            if (responseCode != 200) {
                all = ("There was an error. Status code " + responseCode);
            } else {
                String body = answer.body();
                String[] responses = body.split("\n");
                for (String response : responses) {
                    if (type.equals("embeddings")) {
                        JSONArray jsonArray = new JSONArray();
                        jsonArray = (JSONArray) ((JSONObject)JSONUtils.stringToJSON(response)).get("embedding");
                        for (int i = 0; i < jsonArray.size(); i++) {
                            all += (jsonArray.get(i));
                            if (i != jsonArray.size() - 1) {
                                all += ",";
                            }
                        }
                    }
                    if (type.equals("generate")) {
                        all += ((JSONObject)JSONUtils.stringToJSON(response)).get("response");
                    }
                }
            }
        }
        return all;
    }
}
