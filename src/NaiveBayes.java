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
                if (file.getName().contains("spmsga")) type = "spam";
                else type = "ham";
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
                                totalHamCount++;
                            } else {
                                w.countSpam();
                                totalSpamCount++;
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
        BufferedWriter out = new BufferedWriter(new FileWriter("predictions.txt"));
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


    private static boolean calculateBayes(ArrayList<Word> msg){
        float probabilityOfPositiveProduct = 1.0f;
        float probabilityOfNegativeProduct = 1.0f;
        for (Word word : msg) {
            probabilityOfPositiveProduct *= word.getProbOfSpam();
            probabilityOfNegativeProduct *= (1.0f - word.getProbOfSpam());
        }
        float probOfSpam = probabilityOfPositiveProduct / (probabilityOfPositiveProduct + probabilityOfNegativeProduct);
        return probOfSpam > 0.9f;
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
                        w.setProbOfSpam(totalSpamCount/(float)(totalHamCount+totalSpamCount));
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