import java.io.File;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class GetEmbeddings {
    static int minLine = 1, maxLine = 1, jump = 1;

    public static void main(String[] args) {
        int count = 0;
        try {
            File knolwedgeFile = new File("api.txt");
            File csvFile = new File("api.csv");
            csvFile.createNewFile();
            ArrayList<String> lines = new ArrayList<>();
            FileWriter csvWriter = new FileWriter("api.csv");
            Scanner scanner = new Scanner(knolwedgeFile);
            while (scanner.hasNextLine()) {
                String line = scanner.nextLine();
                lines.add(line);
            }
            for (int cnt = minLine; cnt <= maxLine; cnt++) {
                for (int i = 0; i <= lines.size() - cnt; i += jump) {
                    String event = "";
                    for (int j = i; j < i + cnt; j++) {
                        event += lines.get(j);
                        event += " ";
                    }
                    event = event.replace("\\", "\\\\");
                    event = event.replace("\"", "\\\"");
                    Prompt eventPrompt = new Prompt(event);
                    eventPrompt.query("embeddings", Main.embeddingModel);
                    count++;
                    if (count % 100 == 0)
                        System.out.println(count + " finished");
                    if (eventPrompt.getString().contains("T")) {
                        System.out.println(event);
                        System.out.println(eventPrompt.getString());
                    } else {
                        csvWriter.write(ApiGet.inLine(event));
                        csvWriter.write("\n");
                        csvWriter.write(eventPrompt.getString());
                        csvWriter.write("\n");
                    }
                }
            }
            scanner.close();
            csvWriter.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        System.out.println("all " + count + " finished");
    }
}
