import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;

class NaiveBayes {
    HashMap<String, Word> words = new HashMap<>();
    int totalSpamCount = 0;
    int totalHamCount = 0;

    void train(File directoryPath) throws IOException {
        File[] files = directoryPath.listFiles((dir, name) -> name.endsWith(".txt"));
        if (files != null) {
            for (File file : files) {
                String type;
                if (file.getName().contains("spmsga")){
                    type = "spam";
                    totalSpamCount++;
                }
                else{
                    type = "ham";
                    totalHamCount++;
                }
                BufferedReader in = new BufferedReader(new java.io.FileReader(file));
                String line = in.readLine();
                while (line != null) {
                    if (!line.equals("")) {
                        for (String word : line.split(" ")) {
                            word = word.replaceAll("\\W", "");
                            word = word.toLowerCase();
                            Word w;
                            if (words.containsKey(word)) {
                                w = words.get(word);
                            } else {
                                w = new Word(word);
                                words.put(word, w);
                            }
                            if (type.equals("ham")) {
                                w.countHam();

                            } else {
                                w.countSpam();
                            }
                        }
                    }
                    line = in.readLine();
                }
                in.close();
            }
        }else{
            System.out.println("Something went wrong. Please check the path of the directory!");
        }
    }

    void test(File directoryPath) throws IOException{
        BufferedWriter out = new BufferedWriter(new FileWriter("BayesPredictions.txt"));
        File[] files = directoryPath.listFiles((dir, name) -> name.endsWith(".txt"));
        if (files != null) {
            for (File file : files) {
                ArrayList<Word> msg = makeWordList(file);
                boolean isSpam = calculateBayes(msg);
                if (isSpam) out.write(file.getName()+" spam");
                else out.write(file.getName()+" ham");
                out.newLine();
            }
            out.close();
        }
    }


    private boolean calculateBayes(ArrayList<Word> msg){
        int total= totalHamCount + totalSpamCount;
        float p_ham = totalHamCount / (float) total;

        float p_spam = totalSpamCount/ (float) total;
        float probabilityOfPositiveProduct = 0.0f;
        float probabilityOfNegativeProduct = 0.0f;
        for (Word word : msg) {

            probabilityOfPositiveProduct += Math.log(word.getProbOfSpam()  );
            probabilityOfNegativeProduct += Math.log(1 - word.getProbOfSpam() );
        }

        probabilityOfPositiveProduct = Math.abs((float) (probabilityOfPositiveProduct + Math.log(p_ham)));
        probabilityOfNegativeProduct = Math.abs((float) (probabilityOfNegativeProduct + Math.log(p_spam)));

        return probabilityOfNegativeProduct > probabilityOfPositiveProduct ;
    }


    private ArrayList<Word> makeWordList(File file) throws IOException {
        BufferedReader in = new BufferedReader(new java.io.FileReader(file));
        String line = in.readLine();
        ArrayList<Word> wordList = new ArrayList<>();
        while (line != null) {
            if (!line.equals("")) {
                for (String word : line.split(" ")) {
                    word = word.replaceAll("\\W", "");
                    word = word.toLowerCase();
                    Word w;
                    if (words.containsKey(word)) {
                        w = words.get(word);
                    } else {
                        w = new Word(word);
                        w.setProbOfSpam(0.4f);
                    }
                    wordList.add(w);
                }
            }
            line = in.readLine();
        }
        in.close();
        return wordList;
    }
}