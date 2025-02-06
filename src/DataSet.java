import java.util.ArrayList;
import java.util.Collections;

public class DataSet {
    private final ArrayList<Knowledge> datas = new ArrayList<>();
    private int k;
    double[] vector;
    public DataSet(int k, double[] vector) {
        this.k = k;
        this.vector = vector;
    }
    public void add (Knowledge newKnowledge) {
        newKnowledge.setDistance(vector);
        if (datas.isEmpty()) {
            datas.add(newKnowledge);
        } else if (datas.size() < k) {
            datas.add(newKnowledge);
        } else {
            if (newKnowledge.getDistance() > datas.getLast().getDistance()) {
                datas.removeLast();
                datas.add(newKnowledge);
            } else {
                return;
            }
        }
        for (int i = datas.size() - 1; i >= 1; i--) {
            if (datas.get(i).getDistance() > datas.get(i - 1).getDistance()) {
                Collections.swap(datas, i, i - 1);
            }
        }
    }

    public Knowledge get (int index) {
        return datas.get(index);
    }

    public int getSize (){
        return datas.size();
    }

    public ArrayList<Knowledge> getDatas() {
        return datas;
    }

    public void setK(int k) {
        this.k = k;
    }
}
