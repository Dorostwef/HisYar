import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import java.net.http.HttpResponse;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Scanner;

public class Main {
    static public String embeddingModel = "nomic-embed-text";
    static public String llmModel = "llama3.2:3b";
    static public String yourApiKey = "YOUR_API_KEY"; // replace with your Api-ninjas key! (Create in : https://www.api-ninjas.com/).

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        System.out.print("Enter your prompt: ");
        String input = scanner.nextLine();
        Prompt prompt = new Prompt(input);
        prompt.query("embeddings", embeddingModel);
        String knowledgeVectorString = prompt.getString();
        String[] knowledgeVectorStrings = (knowledgeVectorString.split(","));
        double[] vector = Arrays.stream(knowledgeVectorStrings).mapToDouble(Double::parseDouble).toArray();
        FindData findData = new FindData(vector);
        DataSet knowledges, bookKnowledges;
        Searcher searcher = new Searcher(findData, "api.csv");
        Searcher book1Searcher = new Searcher(findData, "book1.csv");
        Searcher book2Searcher = new Searcher(findData, "book2.csv");
        Thread thread = new Thread(searcher);
        thread.setPriority(Thread.NORM_PRIORITY);
        Thread book1Thread = new Thread(book1Searcher);
        book1Thread.setPriority(Thread.MIN_PRIORITY);
        Thread book2Thread = new Thread(book2Searcher);
        book2Thread.setPriority(Thread.MAX_PRIORITY);
        try {
            thread.start();
            book1Thread.start();
            book2Thread.start();
            thread.join();
            book1Thread.join();
            book2Thread.join();
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        knowledges = searcher.getDataSet();
        bookKnowledges = book1Searcher.getDataSet();
        DataSet book2Knowledges = book2Searcher.getDataSet();
        knowledges.setK(35);
        for (Knowledge knowledge : bookKnowledges.getDatas()) {
            knowledges.add(knowledge);
        }
        for (Knowledge knowledge : book2Knowledges.getDatas()) {
            knowledges.add(knowledge);
        }
        if (!yourApiKey.equals("YOUR_API_KEY")) {
            Prompt subjectPrompt = new Prompt("Write important keywords in this text each one in a line alone without saying anything else and without indexing and at most 6 words : \\\"" + input + "\\\"");
            subjectPrompt.query("generate", llmModel);
            //subjectPrompt.Print();

            String keywords = subjectPrompt.getString();
            String[] allKeywords = keywords.split("\n|\t| |.|,");
            HashSet<String> datas = new HashSet<>();
            for (String keyword : allKeywords) {
                if (keyword.isEmpty())
                    continue;
                keyword = keyword.replace(" ", "+");
                HttpCall httpCall = new HttpCall("https://api.api-ninjas.com/v1/historicalevents?text=" + keyword, "");
                HttpResponse<String> apiResponse = httpCall.get("X-Api-Key", yourApiKey, "web");
                JSONArray knowledgesArray = (JSONArray) JSONUtils.stringToJSON(apiResponse.body());
                for (Object knowledge : knowledgesArray) {
                    JSONObject jsonKnowledge = (JSONObject) knowledge;
                    int year = Integer.parseInt((String) jsonKnowledge.get("year"));
                    String yearName;
                    if (year < 0) {
                        yearName = -year + " BC";
                    } else {
                        yearName = year + " AD";
                    }
                    String stringKnowledge = jsonKnowledge.get("event") + " happend in year " + yearName + " and month " + jsonKnowledge.get("month") + " and day " + jsonKnowledge.get("day");
                    stringKnowledge = stringKnowledge.replace("\n", "\\n");
                    stringKnowledge = stringKnowledge.replace("\"", "\\\"");
                    datas.add(stringKnowledge);
                }
            }
            for (String stringKnowledge : datas) {
                Knowledge event = new Knowledge(stringKnowledge, Knowledge.getEmbed(stringKnowledge));
                knowledges.add(event);
            }
        }

        StringBuilder stringBuilder = new StringBuilder();
        for (Knowledge knowledge : knowledges.getDatas()) {
//            System.out.println(knowledge.getEvent());
//            System.out.println(knowledge.getDistance());
            stringBuilder.append(knowledge.getEvent());
            stringBuilder.append("\\n");
        }
        String promptString = stringBuilder.toString();
        promptString += "Answer the following sentence using only the knowledge above and pretend like you knew the knowledge already : ";
        promptString += input;
        Prompt finalPrompt = new Prompt(promptString);
        finalPrompt.query("generate", llmModel);
        finalPrompt.Print();
    }
}
