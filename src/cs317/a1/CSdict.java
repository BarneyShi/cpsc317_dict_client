package cs317.a1;
// You can use this file as a starting point for your dictionary client
// The file contains the code for command line parsing and it also
// illustrates how to read and partially parse the input typed by the user.
// Although your main class has to be in this file, there is no requirement that you
// use this template or hav all or your classes in this file.
import cs317.a1.Search;
import cs317.a1.Connection;
import java.lang.System;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

//
// This is an implementation of a simplified version of a command
// line dictionary client. The only argument the program takes is
// -d which turns on debugging output.
//
public class CSdict {
    static final int MAX_LEN = 255;
    static Boolean debugOn = false;
    static List<String> validCommands;
    static List<String> validDB;

    private static final int PERMITTED_ARGUMENT_COUNT = 1;
    private static String command;
    private static String[] arguments;


    public static void main(String [] args) {
        byte cmdString[] = new byte[MAX_LEN];
        int len;
        validCommands = Arrays.asList("open, dict", "set", "define", "match", "prefixmatch");
        validDB = new ArrayList<String>();

        // Verify command line arguments
        if (args.length == PERMITTED_ARGUMENT_COUNT) {
            debugOn = args[0].equals("-d");
            if (debugOn) {
                System.out.println("Debugging output enabled");
            } else {
                System.out.println("> 997 Invalid command line option - Only -d is allowed");
                return;
            }
        } else if (args.length > PERMITTED_ARGUMENT_COUNT) {
            System.out.println("> 996 Too many command line options - Only -d is allowed");
            return;
        }

        try {
            while(true){
                System.out.print("csdict> ");
                cmdString = new byte[MAX_LEN];
                System.in.read(cmdString);

                // Convert the command string to ASII
                String inputString = new String(cmdString, "ASCII");
                // Split the string into words
                String[] inputs = inputString.trim().split("( |\t)+");

                // "open xx xx" inovke conenct()
                Connection connection = null;
                if(inputs[0].toLowerCase().equals("open")) {
                    // 901 Error
                    if(inputs.length != 3) {
                        if(debugOn) System.out.println("> 901 Incorrect number of arguments.");
                        continue;
                    }
                    // 902 error
                    if(!isPureNums(inputs[2])) {
                        if(debugOn) System.out.println("> 902 Invalid argument.");
                        continue;
                    }
                    connection = new Connection(inputs[1], inputs[2]);
                    connection.connect();
                } else {
                    // QUIT
                    if(inputs[0].equals("quit")) {
                        System.out.println("QAQ Bye!");
                        return;
                    }
                    // Invalid command AKA not 'OPEN'
                    if(debugOn) {
                        // 903 Error
                        if(validCommands.contains(inputs[0])) {
                            System.out.println("> 903 Supplied command not expected at this time.");
                            continue;
                        }
                        if(debugOn) System.out.println("> 900 Invalid command.");
                        continue;
                    }
                }

                // Instantiate Search() so we could use later in while loop
                Search search = null;

                // Check if socket is connected
                while(connection.connected) {
                    byte command_string[] = new byte[MAX_LEN];
                    System.out.print("csdict> ");
                    System.in.read(command_string);
                    String inputs_string = new String(command_string, "ASCII");
                    String[] inputs_arr = inputs_string.trim().split("( |\t)+");
                    String cmd = inputs_arr[0].toLowerCase();
                    // ==================================================================
                    // === Invoke different action based on inputs ======================
                    // ==================================================================
                    if(cmd.equals("dict")) {
                        connection.showDB();
                    } else if(cmd.equals("set")) {
                        if(search == null) search = new Search(connection);
                        /*
                        =======================
                         Get list of valid DICT
                         */
                        getListDB(connection);
                        /*
                        =======================
                         */
                        if(validDB.contains(inputs_arr[1])) {
                            search.dictSet = inputs_arr[1];
                            search.dictIsSet = true;
                        } else if(inputs_arr.length != 2) {
                            if(debugOn) System.out.println("> 901 Incorrect number of arguments.");
                            continue;
                        } else {
                            if(debugOn) System.out.println("> 902 Invalid argument.");
                            continue;
                        }
                    } else if(cmd.equals("define")) {
                        // 901 Error
                        if(inputs_arr.length != 2) {
                            if(debugOn) System.out.println("> 901 Incorrect number of arguments.");
                            continue;
                        }
                        if(search == null) search = new Search(connection);
                        search.define(search, inputs_arr[1]);
                    } else if(cmd.equals("match")) {
                        if(search == null) search = new Search(connection);
                        search.match(search, inputs_arr[1]);
                    } else if(cmd.equals("prefixmatch")) {
                        if(search == null) search = new Search(connection);
                        search.prefixMatch(search, inputs_arr[1]);
                    } else if(cmd.equals("close")){
                        search = null;
                        connection.connected = false;
                        continue;
                    } else if(cmd.equals("quit")) {
                        connection.quit();
                        return;
                    }
                }
            }
        } catch (IOException exception) {
            if (debugOn) System.err.println("> 998 Input error while reading commands, terminating.");
            System.exit(-1);
        } catch (Exception e) {
            if(debugOn) System.out.println("> 999 Processing error. yyyy. ");
            System.exit(-1);
        }
    }

    // Static helper method checking if string is numeric
    public static boolean isPureNums(String str) {
        for (char c: str.toCharArray()) {
            if(!Character.isDigit(c)) {
                return false;
            }
        }
        return true;
    }

    // Get list of valid bd
    public static void getListDB(Connection connection) throws IOException {
        connection.printWriter.println("show db");
        String res;
        while( (res = connection.bufferedReader.readLine()) != null){
            if(res.contains("250 ok")) {
                break;
            }
            String val = res.split(" ", 2)[0];
            validDB.add(val);
        }
    }
}



