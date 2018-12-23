import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;


public class EmailMessage implements Iterable<Integer> {
    private int classification; // 1 spam -1 ham
    private HashMap <Integer, Integer> tokens;
    private boolean tagged;


    public EmailMessage(int classification, HashMap <Integer, Integer> tokens){
        tagged = true;
        this.tokens = tokens;
        this.classification = classification;
    }


    public EmailMessage(HashMap <Integer, Integer> tokens){
        tagged = false;
        this.tokens = tokens;
        this.classification = 0;
    }


    public HashMap<Integer, Integer> getTokens(){
        return this.tokens;
    }


    public boolean isSpam(){
        return (classification == 1);
    }


    public boolean isTagged(){
        return tagged;
    }


    public int getClassification(){
        return classification;
    }


    public void classify(int c){
        if(c == 1 || c == -1){
            this.classification = c;
            this.tagged = true;
        }
    }

    public EmailMessage clone(){
        return new EmailMessage(getClassification(),new HashMap<Integer, Integer>(tokens));
    }

    public String toString(){
        return this.tokens.toString();
    }

    @Override
    public Iterator<Integer> iterator() {
        return tokens.keySet().iterator();
    }

    public boolean containsToken(int token){
        return tokens.containsKey(tokens);
    }

    public void removeToken(int token){
        tokens.remove(tokens);
    }


    public void filter(Collection<Integer> tokenFilter){
        for(Integer token : tokenFilter){
            if(tokens.containsKey(token))
                tokens.remove(token);
        }
    }
}