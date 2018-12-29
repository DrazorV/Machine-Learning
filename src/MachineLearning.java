import java.io.File;
import java.io.IOException;

public class MachineLearning {
    public static void main(String[] args) throws IOException {
        NaiveBayes bayes = new NaiveBayes();
        LogReg logReg = new LogReg();
        File testfolder = new File("C:\\Users\\DrazorV\\Desktop\\bare\\part10");
        File[] folderList = new File(args[0]).listFiles();
        if (folderList != null) {
            for (File folder : folderList) {
                logReg.makeVocab(folder);
                logReg.readStopWords();
                if (!folder.getName().equals("part10")) {
                    bayes.train(folder);
                    logReg.train(folder);
                }
            }
            for (String key : bayes.words.keySet()) bayes.words.get(key).calculateProbability(bayes.totalSpamCount, bayes.totalHamCount);
            bayes.test(testfolder);
            logReg.test(testfolder);
        }
    }
}
