import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;

public class EmailDataset {
    private List<EmailMessage> messages;
    private int dictionaryDim;


    public EmailDataset(List<EmailMessage> messages){
        this.messages = messages;
        dictionaryDim = calculateDictionaryDim();
    }


    public EmailDataset() {
        this.messages = new LinkedList<EmailMessage>();
    }


    public List<EmailMessage> getMessages(){
        return messages;
    }

    /**
     * This method returns a pair of HashMaps that map the tokens
     * of the tagged messages to the total number of occurrencies
     * in all the messages of the dataset
     *
     * TOKEN -> TOTAL OCURR
     *
     * First - spam token ocurrencies
     * Second - ham token ocurrencies
     *
     * @return Pair<First,Second>
     */
    public Pair<HashMap<Integer, Integer>> getTotalTokenOcurr(){

        Pair<HashMap<Integer,Integer>> pair = new Pair<HashMap<Integer,Integer>>();

        HashMap<Integer, Integer> spamTable = new HashMap<Integer, Integer>();
        HashMap<Integer, Integer> hamTable = new HashMap<Integer, Integer>();
        HashMap<Integer, Integer> currentTable = null; //to select between both tables

        for(EmailMessage m : messages){	//foreach message in the dataset
            if(m.isTagged()){			//we can only separate tagged messages
                if(m.isSpam())
                    currentTable = spamTable;
                else
                    currentTable = hamTable;

                //store the tokens in the current table
                HashMap<Integer,Integer> tokens = m.getTokens();
                for(Integer token : tokens.keySet()){
                    int currentOcurrencies = 0;
                    if(currentTable.containsKey(token))
                        currentOcurrencies = currentTable.get(token);

                    //update the current map token ocurr count
                    //sums 0 if the tokens does not exist
                    currentTable.put(token, (tokens.get(token) + currentOcurrencies));
                }//end for each token in the current message

            }//end is tagged
        }//end for each message


        //chamar ao metodo para incluir todos os keys nas duas tabelas
        pair=completeKeys(spamTable, hamTable);
        return pair;
    }


    private Pair<HashMap<Integer, Integer>>completeKeys(HashMap<Integer, Integer> spamTable,HashMap<Integer, Integer> hamTable ){

        Pair<HashMap<Integer,Integer>> pair = new Pair<HashMap<Integer,Integer>>();

        for(Integer key : spamTable.keySet()){//transverse spam table
            if (!hamTable.containsKey(key)){
                hamTable.put(key, 0);
            }
        }
        for(Integer key : hamTable.keySet()){//transverse ham table
            if(!spamTable.containsKey(key)){
                spamTable.put(key, 0);
            }
        }

        pair.setFirst(spamTable);
        pair.setSecond(hamTable);
        return pair;

    }

    public int getNumSpam(){
        int numSpam = 0;
        for(EmailMessage m: messages){
            if(m.isTagged()){
                if(m.isSpam()) numSpam++;
            }
        }
        return numSpam;
    }


    public int getNumHam(){
        int numHam = 0;
        for(EmailMessage m: messages){
            if(m.isTagged()){
                if(!m.isSpam()) numHam++;
            }
        }
        return numHam;
    }


    public int getNumMessages(){
        return this.messages.size();
    }


    public int calculateDictionaryDim(){
        HashSet<Integer> tokens = new HashSet<Integer>();
        for(EmailMessage m : messages){
            tokens.addAll(m.getTokens().keySet());
        }
        return tokens.size();
    }

    public HashSet<Integer> getDictionary(){
        HashSet<Integer> tokens = new HashSet<Integer>();
        for(EmailMessage m : messages){
            tokens.addAll(m.getTokens().keySet());
        }
        return tokens;
    }

    public int getDictionaryDim(){
        return dictionaryDim;
    }


    public EmailDataset clone(){
        List<EmailMessage> messages2 = new LinkedList<EmailMessage>();
        for(EmailMessage m: messages){
            messages2.add(m.clone());
        }
        return new EmailDataset(messages2);
    }


    public void merge(EmailDataset dataset){
        messages.addAll(dataset.getMessages());
    }


    public String toString(){
        StringBuilder sb = new StringBuilder();
        for(EmailMessage m : messages){
            sb.append(m.toString());
            sb.append('\n');
        }

        return sb.toString();
    }

    public int size(){
        return messages.size();
    }




    @Override
    public Iterator<EmailMessage> iterator() {
        return messages.iterator();
    }

    public void add(EmailMessage msg){
        messages.add(msg);
    }

    public Pair<EmailDataset> split(){
        EmailDataset ds1 = new EmailDataset();
        EmailDataset ds2 = new EmailDataset();

        for(EmailMessage m : messages)
            if(Math.random() < 0.5)
                ds1.add(m.clone());
            else
                ds2.add(m.clone());

        return new Pair<EmailDataset>(ds1, ds2);
    }


    public List<Integer> getClassifications(){
        List<Integer> classifs = new LinkedList<Integer>();
        for(EmailMessage m: messages)
            classifs.add(m.getClassification());
        return classifs;
    }

}
