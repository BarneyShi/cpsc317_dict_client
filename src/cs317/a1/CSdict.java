package cs317.a1;
// You can use this file as a starting point for your dictionary client
// The file contains the code for command line parsing and it also
// illustrates how to read and partially parse the input typed by the user.
// Although your main class has to be in this file, there is no requirement that you
// use this template or hav all or your classes in this file.

import java.lang.System;
import java.io.IOException;

//
// This is an implementation of a simplified version of a command
// line dictionary client. The only argument the program takes is
// -d which turns on debugging output.
//
public class CSdict {
    static final int MAX_LEN = 255;
    static Boolean debugOn = false;

    private static final int PERMITTED_ARGUMENT_COUNT = 1;
    private static String command;
    private static String[] arguments;


    public static void main(String [] args) {
        byte cmdString[] = new byte[MAX_LEN];
        int len;
        // Verify command line arguments

        if (args.length == PERMITTED_ARGUMENT_COUNT) {
            debugOn = args[0].equals("-d");
            if (debugOn) {
                System.out.println("Debugging output enabled");
            } else {
                System.out.println("997 Invalid command line option - Only -d is allowed");
                return;
            }
        } else if (args.length > PERMITTED_ARGUMENT_COUNT) {
            System.out.println("996 Too many command line options - Only -d is allowed");
            return;
        }

        // Example code to read command line input and extract arguments.

        try {
            while(true){
                System.out.print("csdict> ");
                System.in.read(cmdString);

                // Convert the command string to ASII
                String inputString = new String(cmdString, "ASCII");
                // Split the string into words
                String[] inputs = inputString.trim().split("( |\t)+");

                // "open xx xx" inovke conenct()
                Connection connection = null;
                if(inputs[0].equals("open")) {
                    connection = new Connection(inputs[1], inputs[2]);
                    connection.connect();
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
                        search.dictSet = inputs_arr[1];
                        search.dictIsSet = true;
                    } else if(cmd.equals("define")) {
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
                        break;
                    }
                }
            }


            // Operation for connected i.e Close() and Quite()
           // while()


//            command = inputs[0].toLowerCase().trim();
//            // Remainder of the inputs is the arguments.
//            arguments = Arrays.copyOfRange(inputs, 1, inputs.length);
//
//            System.out.println("The command is: " + command);
//            len = arguments.length;
//            System.out.println("The arguments are: ");
//            for (int i = 0; i < len; i++) {
//                System.out.println("    " + arguments[i]);
//            }
//            System.out.println("Done.");


        } catch (IOException exception) {
            System.err.println("998 Input error while reading commands, terminating.");
            System.exit(-1);
        }

    }
}



