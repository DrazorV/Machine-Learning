import java.util.HashMap;

public class NaiveBayes {
    //token occurrency tables
    private EmailDataset trainData;
    private HashMap<Integer, Integer> spamOcurrTable;
    private HashMap<Integer, Integer> hamOcurrTable;
    private int spamTotalOcurr;
    private int hamTotalOcurr;
}