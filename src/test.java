import java.net.URL;
import java.util.HashMap;
import java.util.Map;

public class test {
    public static void main(String args[]) {


        //map of dataset files
        Map<String, URL> trainingFiles = new HashMap<>();
        trainingFiles.put("English", NaiveBayesExample.class.getResource("/datasets/training.language.en.txt"));
        trainingFiles.put("French", NaiveBayesExample.class.getResource("/datasets/training.language.fr.txt"));
        trainingFiles.put("German", NaiveBayesExample.class.getResource("/datasets/training.language.de.txt"));











        //P (A | B) = (P (B | A) * P(A))/ P(B) (Naive-Bayes)
        //P (ham| you won lottery) = (P (you won lottery | ham) * P(ham))/ P (you won lottery)
        //P (you won lottery | ham) = P (you | ham) * P (won | ham) * P (lottery | ham)
        //P (you won lottery | spam) = P (you | spam) * P (won | spam) * P (lottery | spam)
        //P (you | ham) = (1 + 1)/ (7 + 15)
        //P (you | spam) = (1 + 1)/ (10 + 15)
    }
}
