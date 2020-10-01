package cs317.a1;
import cs317.a1.Connection;
import java.io.IOException;
import java.util.Arrays;

public class Search {
    public boolean dictIsSet;
    public String dictSet;
    public Connection connection;

    /**
     *
     * @param connection establised socket to a dict server
     */
    public Search(Connection connection) {
        dictIsSet = false;
        dictSet = "*";
        this.connection = connection;
    }

    /**
     *
     * @param search passed in established search
     * @param word  target
     * @throws IOException
     */
    public void define(Search search, String word) throws IOException {
        String query = "define " + dictSet + " " + word;
        connection.printWriter.println(query);
        String res;
        while((res= connection.bufferedReader.readLine()) != null) {
            // Print dict name before each definition
            if(res.contains("550")) continue;
            if(res.contains("150")){
                System.out.println("> DEFINE "+ dictSet + " airplane");
                res = "<-- " + res;
                System.out.println(res);
                continue;

            }
            if(res.contains("151")){
                // Print @ dict name
                System.out.println("<-- " + res);
                String[] arr = res.trim().split("( |\t)+");
                arr = Arrays.copyOfRange(arr, 2, arr.length);
                res = Arrays.toString(arr).replace("[", "");
                res = res.replace("]", "");
                res = "@ " + res;
            }

            // No definition then seek matches
            String match_res = "";
            if(res.contains("552")) {
                res = "***No definition found***";
                System.out.println(res);

                // seek matches
                String setDict = search.dictIsSet ? search.dictSet:"*";
                String query_m = "match " + setDict + " . " + word;
                connection.printWriter.println(query_m);
                while((match_res = connection.bufferedReader.readLine()) != null) {
                    if(match_res.contains("152")) {
                        match_res = "<-- " + match_res;
                        System.out.println(match_res);
                        continue;
                    }
                    if(match_res.contains("552")){
                        match_res = "***No matches found***";
                        System.out.println(match_res);
                        break;
                    }
                    if(match_res.contains("250")){
                        match_res = "<-- " + match_res;
                        System.out.println(match_res);
                        break;
                    }
                    System.out.println(match_res);
                }
                if(match_res.contains("No matches") || match_res.contains("250 ok")) break;
            }
            if(res.contains("250 ok")) {
                res = "<-- " + res;
                System.out.println(res);
                break;
            };
            if(match_res.contains("No matches")) break;
            System.out.println(res);
        }
    }

    /**
     *
     * @param search already established search() from prior query
     * @param word  word we're looking for
     * @throws IOException
     */
    public void match(Search search, String word) throws IOException {
        String setDict = search.dictIsSet ? search.dictSet:"*";
        String query = "match " + setDict + " exact " + word;
        connection.printWriter.println(query);
        String res;
        while((res = connection.bufferedReader.readLine()) != null) {
            if(res.contains("552")) {
                System.out.println(res);
                res = "*****No matching word(s) found*****";
                System.out.println(res);
                break;
            }
            if(res.contains("550")) {
                continue;
            }
            if(res.contains("152")) {
                System.out.println("> MATCH " + setDict + " exact " + word);
                res = "<-- " + res;
            }
            if(res.contains("250 ok")) {
                res = "<-- " + res;
                System.out.println(res);
                break;
            }
            System.out.println(res);

        }
    }

    /**
     *
     * @param search search ()
     * @param word  target
     * @throws IOException
     */
    public void prefixMatch(Search search, String word) throws IOException {
        String setDict = search.dictIsSet ? search.dictSet:"*";
        String query = "match " + setDict + " prefix " + word;
        connection.printWriter.println(query);
        String res;
        while((res = connection.bufferedReader.readLine()) != null) {
            if(res.contains("552")) {
                res = "***No matching word(s) found***";
                System.out.println(res);
                break;
            }
            if(res.contains("152")) {
                System.out.println("> MATCH " + dictSet + " PREFIX " + word);
                res = "<-- " + res;
            }
            if(res.contains("550")) {
                continue;
            }
            if(res.contains("250 ok")) {
                res = "<-- " + res;
                System.out.println(res);
                break;
            }
            System.out.println(res);
        }
    }
}
