import java.io.File;
import java.io.IOException;

public class MachineLearning {
    public static void main(String[] args) {
        NaiveBayes bayes = new NaiveBayes();
        for(int i = 0;i < args.length; i++) {
            File file = new File(args[i]);
            try {
                if(i != args.length-1) bayes.train(file);
                else {
                    for (String key : bayes.words.keySet()) {
                        bayes.words.get(key).calculateProbability(bayes.totalSpamCount, bayes.totalHamCount);
                    }
                    bayes.test(file);
                }
            }catch (IOException e){
                System.out.println("AN ERROR HAS OCCURRED");
            }
        }
    }
}
