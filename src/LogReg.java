import java.io.*;
import java.util.HashMap;
import java.util.LinkedHashSet;


class LogReg {
    private HashMap<String, Integer> words = new HashMap<>();
    private LinkedHashSet<String> stopWords = new LinkedHashSet<>();
    private HashMap<String,Double> weights = new HashMap<>();
    private double learningRate = 0.001;
    double lambda = 0.3;

    void makeVocab(File directoryPath) throws IOException {
        File[] files = directoryPath.listFiles((dir, name) -> name.endsWith(".txt"));
        if (files != null) {
            for (File file : files) {
                String type;
                if (file.getName().contains("spmsga")) type = "spam";
                else type = "ham";
                BufferedReader in = new BufferedReader(new java.io.FileReader(file));
                String line = in.readLine();
                while (line  != null) {
                    if (!line.equals("")) {
                        for (String word : line.split(" ")) {
                            word = word.replaceAll("\\W", "");
                            word = word.toLowerCase();
                            if (!stopWords.contains(word)&&!word.equals("")){
                                if(!words.containsKey(word)) words.put(word, 0);
                            }

                        }
                    }
                    line = in.readLine();
                }
                in.close();
            }
            for(String word: words.keySet()){
                weights.put(word, 0.0);
            }
        }
    }

    private HashMap<String, Integer> countWords(File directoryPath) throws IOException {
        HashMap<String, Integer> count = new HashMap<>();
        BufferedReader in = new BufferedReader(new FileReader(directoryPath));
        String line = in.readLine();
        while (line  != null) {
            if (!line.equals("")) {
                for (String word : line.split(" ")) {
                    word = word.replaceAll("\\W", "");
                    word = word.toLowerCase();
                    if(!stopWords.contains(word)&&!word.equals("")) {
                        if (count.containsKey(word)) count.put(word, count.get(word) + 1);
                        else count.put(word, 1);
                    }
                }
            }
            line = in.readLine();
        }
        return count;
    }


    void train(File folder) throws IOException {
        double cla;
        File[] files = folder.listFiles((dir, name) -> name.endsWith(".txt"));
        if (files != null) {
            for (File file : files) {
                if (file.getName().contains("spmsga")) cla = 0.0;
                else cla = 1.0;
                HashMap<String, Integer> wordCount = countWords(file);
                double totalVal = 0.0;
                for (String word : wordCount.keySet()) totalVal += wordCount.get(word) * words.get(word);
                double predictedVal = 1.0/(1.0+Math.exp(-totalVal));
                for (String word : wordCount.keySet()) weights.put(word, weights.get(word) + learningRate * (cla - predictedVal) * wordCount.get(word));
            }
        }
        for(String word: words.keySet()){
            double modified = words.get(word) + weights.get(word) - (learningRate * lambda * words.get(word));
            weights.put(word , modified + weights.get(word));
        }
        System.out.println("a");
    }

    void test(File folder) throws IOException {
        BufferedWriter out = new BufferedWriter(new FileWriter("predictions2.txt"));
        File[] files = folder.listFiles((dir, name) -> name.endsWith(".txt"));
        if (files != null) {
            for(File file: files){
                HashMap<String, Integer> testcount = new HashMap<>();
                int totval = 0;
                BufferedReader br = new BufferedReader(new FileReader(file));
                String line;
                while((line=br.readLine())!=null){
                    line=line.toLowerCase();
                    String[] words = line.split("\\s+");
                    for (String word : words) {
                        if(!stopWords.contains(word)) {
                            if (testcount.containsKey(word)) testcount.put(word, testcount.get(word) + 1);
                            else testcount.put(word, 1);
                        }
                    }
                }
                for(String word: testcount.keySet()) if(words.get(word)!=null) totval += testcount.get(word)*weights.get(word);
                double result = 1.0/(1.0+Math.exp(-totval));
                if(result<0.5) out.write(file.getName()+" ham");
                else out.write(file.getName()+" spam");
                out.newLine();
            }
            out.close();
        }
    }

    void readStopWords() throws IOException {
        File file = new File("C:\\Users\\DrazorV\\IdeaProjects\\Artificial Intelligence2\\src\\stopWords.txt");
        FileReader fr = new FileReader(file);
        BufferedReader br = new BufferedReader(fr);
        String line;
        while ((line = br.readLine()) != null) stopWords.add(line);
        br.close();
    }
}
