public class Searcher implements Runnable {
    private DataSet dataSet;
    private final FindData findData;
    private final String fileName;
    public Searcher(FindData findData, String fileName) {
        this.findData = findData;
        this.fileName = fileName;
    }

    @Override
    public void run() {
        dataSet = findData.getClosestK(20, fileName);
    }

    public DataSet getDataSet() {
        return dataSet;
    }
}
