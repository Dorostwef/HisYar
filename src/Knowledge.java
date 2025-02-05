import java.util.Arrays;

public class Knowledge {
    private String event;

    private double[] vector;

    private double distance;

    public Knowledge(String event, double[] vector) {
        this.event = event;
        this.vector = vector;
    }

    public double[] getVector() {
        return vector;
    }

    public double getIndex(int index) {
        return vector[index];
    }

    public static double[] getEmbed(String event) {
        Prompt knowledgePrompt = new Prompt(event);
        knowledgePrompt.query("embeddings", Main.embeddingModel);
        String knowledgeVectorString = knowledgePrompt.getString();
        String[] knowledgeVectorStrings = ((String[])knowledgeVectorString.split(","));
        return Arrays.stream(knowledgeVectorStrings).mapToDouble(Double::parseDouble).toArray();
    }

    public String getEvent() {
        return event;
    }

    public void setEvent() {
        this.event = event;
    }

    public double getDistance() {
        return distance;
    }

    public void setDistance(double[] vector) {
        distance = FindData.cosDistance(vector, this.vector);
    }
}
