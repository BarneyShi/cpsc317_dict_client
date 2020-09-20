package cs317.a1;

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
            if(res.contains("151")){
                // Print @ dict name
                String[] arr = res.trim().split("( |\t)+");
                arr = Arrays.copyOfRange(arr, 2, arr.length);
                res = Arrays.toString(arr).replace("[", "");
                res = res.replace("]", "");
                res = "@ " + res;
            }

            // No definition then seek matches
            if(res.contains("552")) {
                res = "***No definition found***";
                String setDict = search.dictIsSet ? search.dictSet:"*";
                String query_m = "match " + setDict + " . " + word;
                connection.printWriter.println(query_m);
                String match_res;
                while((match_res = connection.bufferedReader.readLine()) != null) {
                    if(match_res.contains("152")) continue;
                    if(match_res.contains("552")){
                        res = res + "\n" + "***No matches found***";
                        break;
                    }
                    res = res + "\n" + match_res;
                    if(match_res.contains("250")){
                        break;
                    }
                }
            }
            System.out.println(res);
            if(res.contains("250 ok")) break;
            if(res.contains("No matches")) break;
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
        String query = "match " + setDict + " . " + word;
        connection.printWriter.println(query);
        String res;
        while((res = connection.bufferedReader.readLine()) != null) {
            if(res.contains("552")) {
                res = "*****No matching word(s) found*****";
                System.out.println(res);
                break;
            }
            if(res.contains("550")) {
                continue;
            }
            if(res.contains("152")) {
                res = "> " + res;
            }
            if(res.contains("250 ok")) {
                res = "> " + res;
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
                res = "> " + res;
            }
            if(res.contains("550")) {
                continue;
            }
            if(res.contains("250 ok")) {
                res = "> " + res;
                System.out.println(res);
                break;
            }
            System.out.println(res);
        }
    }
}
