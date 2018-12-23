import java.io.FileNotFoundException;
import java.util.HashMap;

public class NaiveBayes {
    //token occurrency tables
    private EmailDataset trainData;
    private HashMap<Integer, Integer> spamOcurrTable;
    private HashMap<Integer, Integer> hamOcurrTable;
    private int spamTotalOcurr;
    private int hamTotalOcurr;


    private HashMap<Integer, Double> spamProbTable;
    private HashMap<Integer, Double> hamProbTable;

    private double threshold; //classification threshold

    private int dim;
    private double spamProb;
    private double hamProb;
    private int sigTokens;


    public NaiveBayes(String filename, double threshold, int sigTokens) throws FileNotFoundException {
        //read and store the data
        TFReader reader = new TFReader(filename);
        EmailDataset data = reader.read();

        //calculate the threshold
        this.threshold= threshold;//threashold(filename);


        initModel(data, sigTokens);

    }

    public NaiveBayes(EmailDataset trainData, double threshold, int sigTokens) throws FileNotFoundException{
        this.threshold = threshold;
        this.sigTokens = sigTokens;
        initModel(trainData, sigTokens);

    }

    private void initModel(EmailDataset data, int sigTokens){

        trainData = data;


        dim=trainData.getDictionaryDim();

        //get and init token dictionary tables

        Pair<HashMap<Integer, Integer>> pair = trainData.getTotalTokenOcurr();
        spamOcurrTable = pair.getFirst();
        hamOcurrTable = pair.getSecont();
        spamTotalOcurr = allTokenOcurr(spamOcurrTable);
        hamTotalOcurr = allTokenOcurr(hamOcurrTable);


        //init probability tables
        spamProbTable = new HashMap<Integer, Double>();
        hamProbTable = new HashMap<Integer, Double>();
        tableTokenProb(); //fills the two previous tables



        //init class prob
        spamProb = getClassProb("spam");
        hamProb = getClassProb("ham");

        if(sigTokens != 0){//filter the sigTokens most significative tokens
            filterSignificativeTokens(sigTokens);
            initModel(trainData, 0);
        }

    }

}