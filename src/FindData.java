import java.io.File;
import java.util.Arrays;
import java.util.Scanner;

public class FindData {
    private double[] vector;

    public FindData(double[] vector) {
        this.vector = vector;
    }

    public static double cosDistance (double[] a, double[] b) {
        double dotProduct = 0.0;
        double normA = 0.0;
        double normB = 0.0;
        for (int i = 0; i < a.length; i++) {
            dotProduct += a[i] * b[i];
            normA += Math.pow(a[i], 2);
            normB += Math.pow(b[i], 2);
        }
        return dotProduct / (Math.sqrt(normA) * Math.sqrt(normB));
    }

    public DataSet getClosestK (int k, String fileName) {
        DataSet closest = new DataSet(k, vector);
        try {
            File knolwedgeFile = new File(fileName);
            Scanner scanner = new Scanner(knolwedgeFile);
            String knowledge, knowledgeVectorString;
            while (scanner.hasNextLine()) {
                knowledge = scanner.nextLine();
                knowledgeVectorString = scanner.nextLine();
                String[] knowledgeVectorStrings = ((String[])knowledgeVectorString.split(","));
                double[] knowledgeVector = Arrays.stream(knowledgeVectorStrings).mapToDouble(Double::parseDouble).toArray();
                Knowledge newKnowledge = new Knowledge(knowledge, knowledgeVector);
                closest.add(newKnowledge);
            }
            return closest;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public double getVector(int index) {
        return vector[index];
    }
}
