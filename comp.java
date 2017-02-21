/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */


import java.io.*;
import java.util.*;

/**
 *
 * @author klkni
 */
public class comp {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws FileNotFoundException, IOException {

        String file;
        String filename = "";
        Scanner sc;
        if (args.length == 0) {
            //no filename so read in ints from keyboard and set filename to out
            System.out.println("Please enter fs16 code.Press enter twice once completed.");
            sc = new Scanner(System.in);
            filename = "out.fs16";
            file = "out";
            PrintWriter out = new PrintWriter(new FileWriter(filename));
            while (sc.hasNextLine()) {
                String line = sc.nextLine();
                if (line.equals("")) {
                    out.close();
                    break;
                } else {
                    out.println(line);
                }
            }
            sc.close();
        } else {
            //get filename from stdin and read in ints from file
            file = args[0];
            filename = args[0] + ".fs16";
        }
        //call test scanner to filter 
        //open file and checks for unknown chars
        int lineNum = 1;
        int isValid = 1;

        BufferedReader reader = new BufferedReader(
                new InputStreamReader(
                        new FileInputStream(filename)));
        String line;

        while ((line = reader.readLine()) != null) { //read in one entire line,
            String newLine = "";
            //check for unknown chars and comments
            for (int i = 0; i < line.length(); i++) {
                if (line.charAt(i) == '@') {//don't include comments
                    while (i < line.length() && !Character.isSpaceChar(line.charAt(i)) && line.charAt(i) != '\n') {
                        i++;
                    }

                } else if (Character.isLetterOrDigit(line.charAt(i)) || ScannerIn.isAcceptableChar(line.charAt(i)) || Character.isSpaceChar(line.charAt(i))) {
                    newLine = newLine + line.charAt(i);

                } else {
                    System.out.println("Error at line:" + lineNum + "." + line.charAt(i) + " is not a character in alphabet.");
                    System.exit(1);
                }

            }
            if (newLine.equals("")) {
                //do nothing, don't want to send empty string

            } else {
                //call scannerHelper function to sort through FSA table
                ScannerIn.scanHelper(newLine, lineNum);
            }
            lineNum++;
        }
        System.out.println("P1 & P2: \n");
        Node root = Parser.parser();
        support.treePrint(root);
        System.out.println("P3 & P4:");
        SS.local(root);

        try {
            PrintWriter writer = new PrintWriter(file + ".asm", "UTF-8");
            codeGen.local(writer, root);
        } catch (IOException e) {
            System.out.println("IT didn't work.");
        }

    }

}
