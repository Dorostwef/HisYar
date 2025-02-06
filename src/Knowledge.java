import java.util.Arrays;

public class Knowledge {
    private String event;

    private final double[] vector;

    private double distance;

    public Knowledge(String event, double[] vector) {
        this.event = event;
        this.vector = vector;
    }


    public static double[] getEmbed(String event) {
        Prompt knowledgePrompt = new Prompt(event);
        knowledgePrompt.query("embeddings", Main.embeddingModel);
        String knowledgeVectorString = knowledgePrompt.getString();
        String[] knowledgeVectorStrings = (knowledgeVectorString.split(","));
        return Arrays.stream(knowledgeVectorStrings).mapToDouble(Double::parseDouble).toArray();
    }

    public String getEvent() {
        return event;
    }

    public void setEvent(String event) {
        this.event = event;
    }

    public double getDistance() {
        return distance;
    }

    public void setDistance(double[] vector) {
        distance = FindData.cosDistance(vector, this.vector);
    }
}
