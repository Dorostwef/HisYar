import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import javax.xml.crypto.Data;
import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Scanner;

public class Main {
    public static final String yourApiKey = "Z+Rc3mhEisordgwx1O/Mrw==N0jhww8UFlB7nYLS"; // replace with your Api-ninjas key! (Create in : https://www.api-ninjas.com/).
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        System.out.print("Enter your prompt: ");
        String input = scanner.nextLine();
        Prompt prompt = new Prompt(input);
        prompt.query("embeddings", "nomic-embed-text");
        String knowledgeVectorString = prompt.getString();
        String[] knowledgeVectorStrings = ((String[])knowledgeVectorString.split(","));
        double[] vector = Arrays.stream(knowledgeVectorStrings).mapToDouble(Double::parseDouble).toArray();
        FindData findData = new FindData(vector);
        DataSet knowledges = null, bookKnowledges = null;
        Searcher searcher = new Searcher(findData, "api.csv");
        Searcher bookSearcher = new Searcher(findData, "book1.csv");
        Searcher book2Searcher = new Searcher(findData, "book2.csv");
        Thread thread = new Thread(searcher);
        thread.setPriority(Thread.NORM_PRIORITY);
        Thread bookThread = new Thread(bookSearcher);
        bookThread.setPriority(Thread.MAX_PRIORITY);
        Thread book2Thread = new Thread(book2Searcher);
        book2Thread.setPriority(Thread.MIN_PRIORITY);
        try {
            thread.start();
            bookThread.start();
            book2Thread.start();
            thread.join();
            bookThread.join();
            book2Thread.join();
        } catch (Exception e) {
            e.printStackTrace();
        }
        knowledges = searcher.getDataSet();
        bookKnowledges = bookSearcher.getDataSet();
        DataSet book2Knowledges = book2Searcher.getDataSet();
        knowledges.setK(35);
        for (Knowledge knowledge : bookKnowledges.getDatas()) {
            knowledges.add(knowledge);
        }
        for (Knowledge knowledge : book2Knowledges.getDatas()) {
            knowledges.add(knowledge);
        }
        Prompt subjectPrompt = new Prompt("Write important keywords in this text each one in a line alone without saying anything else and without indexing and at most 6 words : \\\"" + input + "\\\"");
        subjectPrompt.query("generate", "llama3.2:3b");
//        subjectPrompt.Print();

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
                int year = Integer.parseInt((String)jsonKnowledge.get("year"));
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

        String promptString = "";
        for (Knowledge knowledge : knowledges.getDatas()) {
//            System.out.println(knowledge.getEvent());
//            System.out.println(knowledge.getDistance());
            promptString += knowledge.getEvent();
            promptString += "\\n";
        }
        promptString += "Answer the following sentence using only the knowledge above : ";
        promptString += input;
        Prompt finalPrompt = new Prompt(promptString);
        finalPrompt.query("generate", "llama3.2:3b");
        finalPrompt.Print();
    }
}
