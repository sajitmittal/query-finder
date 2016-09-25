package QueryService;

import org.apache.log4j.Logger;

import java.util.*;


/**
 * Created by sajit.m on 9/24/2016.
 */
public class QueryExecutor {
    private static Logger log = Logger.getLogger(QueryExecutor.class);

    private Map<Integer,Integer> docScoreMap;
    private Map<Integer,Set<Integer>> scoreDocMap;
    private static final int NUM_DOCS = 20;

    public QueryExecutor(){
        docScoreMap = new HashMap<>();
        scoreDocMap = new HashMap<>();
    }

    public List<String> getResult(String[] tokens){
        getDocScores(tokens);
        arrangeDocsByScore();
        List<Integer> topDocs = getTopDocs(tokens.length);
        List<String> docsList = new LinkedList<>();
        for(int doc : topDocs){
            docsList.add(DataLoader.getDocForId(doc));
        }
        return docsList;
    }

    private void getDocScores(String[] tokens){
//        log.info("Getting scores for Doccuments");
        for(int i=0;i<tokens.length;i++){
            Set<Integer> docIds = DataLoader.getDocsForToken(tokens[i].trim().toLowerCase());
            if(docIds == null){
                continue;
            }
            for(Integer docId : docIds){
                if(!docScoreMap.containsKey(docId)){
                    docScoreMap.put(docId,0);
                }
                docScoreMap.put(docId, docScoreMap.get(docId)+1);
            }
        }
    }

    private void arrangeDocsByScore(){
//        log.info("Ordering the doccuments");
        for(Map.Entry<Integer,Integer> entry : docScoreMap.entrySet()){
            if(!scoreDocMap.containsKey(entry.getValue())){
                scoreDocMap.put(entry.getValue(),new HashSet<>());
            }
            scoreDocMap.get(entry.getValue()).add(entry.getKey());
        }
    }

    private List<Integer> getTopDocs(int k){
//        log.info("Getting the required number of Doccuments");
        int docsAdded=0;
        List<Integer> finalDocs = new LinkedList<>();
        for(int i=k;i>=0;i--){
            if(scoreDocMap.containsKey(i)){
                Set<Integer> docSet = scoreDocMap.get(i);
                finalDocs.addAll(getTopNDocs(docSet, NUM_DOCS - docsAdded));
                docsAdded = finalDocs.size();
                if(docsAdded==NUM_DOCS){
                    break;
                }
            }
        }
        return finalDocs;
    }

    private List<Integer> getTopNDocs(Set<Integer> docSet, int n) {
        List<Integer> docs= new ArrayList<>(docSet);
        Collections.sort(docs, new Comparator<Integer>() {
            @Override
            public int compare(Integer i1, Integer i2) {
                double score1 = DataLoader.getScoreForDoc(i1);
                double score2 = DataLoader.getScoreForDoc(i2);
                if(score1==score2) return 0;
                else return score1 > score2 ? -1 : 1;
            }
        });
        if(docs.size()>n) {
            return docs.subList(0, n);
        }else{
            return docs;
        }
    }
}
