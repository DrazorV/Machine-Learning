import java.io.File;
import java.io.IOException;

public class MachineLearning {
    public static void main(String[] args) throws IOException {
        NaiveBayes bayes = new NaiveBayes();
        LogReg logReg = new LogReg();
        File testfolder = new File(args[1]);
        File[] folderList = new File(args[0]).listFiles();
        logReg.readStopWords();
        if (folderList != null) {
            for (File folder : folderList) logReg.makeVocab(folder);
            for (File folder : folderList) {
                if (folder != testfolder) {
                    bayes.train(folder);
                }
            }
            logReg.train();
            for (String key : bayes.words.keySet()) bayes.words.get(key).calculateProbability(bayes.totalSpamCount, bayes.totalHamCount);
            bayes.test(testfolder);
            logReg.test(testfolder);
        }
    }
}
