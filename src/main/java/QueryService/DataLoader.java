package QueryService;

import org.apache.log4j.Logger;

import java.io.*;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Created by sajit.m on 9/24/2016.
 */
public class DataLoader {
    private static Logger log = Logger.getLogger(DataLoader.class);

    private static Map<Integer, String> idDocMap = new HashMap<>();
    private static Map<Integer, Double> idDocScoreMap = new HashMap<>();
    private static Map<String, Set<Integer>> tokenIdMap = new HashMap<>();
    private static final String SUMMARY = "review/summary";
    private static final String TEXT = "review/text";
    private static final String SCORE = "review/score";

    public static void init() {
        ClassLoader loader = DataLoader.class.getClassLoader();
        try {
            log.info("Started Loading data...");
            BufferedReader br = new BufferedReader(new FileReader(loader.getResource("foods-sample.txt").getFile()));
            String sCurrentLine;
            int idCounter = 1;
            StringBuilder doc = new StringBuilder();
            StringBuilder text = new StringBuilder();
            double score = 0.0;
            while ((sCurrentLine = br.readLine()) != null) {
                if (sCurrentLine.length() == 0) {
                    updateMaps(idCounter, doc.toString(), text.toString(), score);
                    idCounter++;
                    clearBuffers(doc, text);
                } else {
                    doc.append(sCurrentLine+"\n");
                    if (sCurrentLine.contains(SCORE)) {
                        try {
                            score = Double.parseDouble(getValue(sCurrentLine));
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    } else if (sCurrentLine.contains(SUMMARY) || sCurrentLine.contains(TEXT)) {
                        text.append(" ");
                        text.append(getValue(sCurrentLine));
                    }
                }
            }
            br.close();
            log.info("Data successfully loaded");
        } catch (IOException e) {
            log.error("Error loading the data",e);
        }
    }

    private static String getValue(String s) {
        return s.split(":")[1].trim();
    }

    private static void clearBuffers(StringBuilder review, StringBuilder text) {
        review.setLength(0);
        review.trimToSize();
        text.setLength(0);
        text.trimToSize();
    }

    private static void updateMaps(int id, String review, String text, double score) {
        idDocMap.put(id, review);
        idDocScoreMap.put(id, score);
        String[] tokens = text.split(" ");
        for (int i = 0; i < tokens.length; i++) {
            if (tokens[i].length() > 0) {
                String token = tokens[i].toLowerCase();
                token =token.replaceAll("[^a-z0-9]", "");
                if(token.length()==0){
                    continue;
                }
                if (!tokenIdMap.containsKey(token)) {
                    tokenIdMap.put(token, new HashSet<>());
                }
                tokenIdMap.get(token).add(id);
            }
        }
    }

    public static Set<Integer> getDocsForToken(String token) {
        return tokenIdMap.get(token);
    }

    public static double getScoreForDoc(int docId) {
        return idDocScoreMap.get(docId);
    }

    public static String getDocForId(int docId) {
        return idDocMap.get(docId);
    }

    public static void main(String[] args) {
        init();
        getQueryFile();
//        String[] tokens = new String[]{"dog", "process", "food", "quality"};
//        List<String> result = new QueryExecutor().getResult(tokens);

    }


    // Code for generating  the sample file
    public static void getSample() {
        ClassLoader loader = DataLoader.class.getClassLoader();
        try {
            BufferedReader br = new BufferedReader(new FileReader(loader.getResource("foods.txt").getFile()));
            File file = new File("C:\\Users\\sajit.m\\kredex\\src\\main\\resources\\foods-sample.txt");
            file.createNewFile();
            BufferedWriter bw = new BufferedWriter(new FileWriter(file.getAbsoluteFile(), false));
            String sCurrentLine;
            boolean toAdd = isToAdd();
            while ((sCurrentLine = br.readLine()) != null) {
                if (sCurrentLine.length() == 0) {
                    if (toAdd) {
                        bw.write("\n");
                    }
                    toAdd = isToAdd();
                } else {
                    if (toAdd) {
                        bw.write(sCurrentLine + "\n");
                    }
                }
            }
            bw.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static boolean isToAdd() {
        Random random = new Random();
        int n = random.nextInt(10);
        return n == 0;
    }

    /** Code to generate query file
     *
     */
    private static void getQueryFile(){
        List<String> keys= tokenIdMap.keySet().stream().collect(Collectors.toList());
        int numKeys = keys.size();
        int total=100000;
        int maxSize=10;
        try{
            File file = new File("C:\\Users\\sajit.m\\kredex\\src\\main\\resources\\query-sample.csv");
            file.createNewFile();
            BufferedWriter bw = new BufferedWriter(new FileWriter(file.getAbsoluteFile(), false));
            for(int i=0;i<total;i++){
                int keysReq = new Random().nextInt(maxSize)+1;
                for(int j=0;j<keysReq;j++){
                    int idx = new Random().nextInt(numKeys);
                    bw.write(keys.get(idx));
                    if(j!=keysReq-1){
                        bw.write("%2C");
                    }
                }
                bw.write("\n");
            }
            bw.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
