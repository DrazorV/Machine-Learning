import java.io.*;
import java.util.HashMap;
import java.util.LinkedHashSet;

class LogReg {
    private HashMap<String, Word> words = new HashMap<>();
    private LinkedHashSet<String> stopWords = new LinkedHashSet<>();
    private double lambda = 0.02;

    void makeVocab(File directoryPath) throws IOException {
        File[] files = directoryPath.listFiles((dir, name) -> name.endsWith(".txt"));
        if (files != null) {
            for (File file : files) {
                BufferedReader in = new BufferedReader(new java.io.FileReader(file));
                String line = in.readLine();
                String type;
                if (file.getName().contains("spmsga")) type = "spam";
                else type = "ham";
                while (line  != null) {
                    if (!line.equals("")) {
                        for (String word : line.split(" ")) {
                            word = word.replaceAll("\\W", "");
                            word = word.replaceAll("\\d", "");
                            word = word.toLowerCase();
                            Word w;
                            if (!stopWords.contains(word)&&!word.equals("")){
                                if(words.containsKey(word)) {
                                    w = words.get(word);
                                }
                                else {
                                    w = new Word(word);
                                    words.put(word,w);
                                }
                                if (type.equals("ham")) {
                                    w.countHam();

                                } else {
                                    w.countSpam();
                                }
                            }
                        }
                    }
                    line = in.readLine();
                }
                in.close();
            }
        }
    }

    void train(){
        for (Word word : words.values()){
            double weight = (double) word.getSpamCount()/(double)(word.getHamCount() + word.getSpamCount());
            double result = 1 / (1 + Math.exp(-weight));
            word.setResult(result);
        }
    }

    void test(File folder) throws IOException {
        BufferedWriter out = new BufferedWriter(new FileWriter("LogisticPredictions.txt"));
        File[] files = folder.listFiles((dir, name) -> name.endsWith(".txt"));
        if (files != null) {
            for(File file: files){
                HashMap<String, Integer> testcount = new HashMap<>();
                double totval = 0;
                BufferedReader br = new BufferedReader(new FileReader(file));
                String line;
                while((line = br.readLine()) != null){
                    for (String word : line.split(" ")) {
                        word = word.replaceAll("\\W", "");
                        word = word.replaceAll("\\d", "");
                        word = word.toLowerCase();
                        if(!stopWords.contains(word)&&!word.equals("")) {
                            if (testcount.containsKey(word)) testcount.put(word, testcount.get(word) + 1);
                            else testcount.put(word, 1);
                        }
                    }
                }
                for (String word : testcount.keySet()) {
                    if(words.containsKey(word)) {
                        totval += words.get(word).getResult();
                    }
                }

                double mesos = totval/testcount.keySet().size() - lambda;
                if(mesos < 0.50) out.write(file.getName()+" ham");
                else out.write(file.getName()+" spam");
                out.newLine();
            }
            out.close();
        }
    }


    void readStopWords() throws IOException {
        File file = new File("C:\\Users\\Drazor\\IdeaProjects\\Artificial Intelligence2\\src\\stopWords.txt");
        FileReader fr = new FileReader(file);
        BufferedReader br = new BufferedReader(fr);
        String line;
        while ((line = br.readLine()) != null) stopWords.add(line);
        br.close();
    }
}
