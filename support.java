/*
 * This class contains supporting functions for larger classes.
 * 
 * 
 */


import java.io.*;
import java.util.*;

/**
 *
 * @author klkni
 */
public class support {
    
    public static int level = 1;
    public static int labelNum = 0;


    public static void error(String s) {
        System.out.println(s);
        System.exit(1);
    }

    public static Node getNode(String label) {
        Node node = new Node();
        node.label = label;
        return node;
    }

    public static void treePrint(Node n) {
        if (n != null) {
            //print out node label and all of its non-null children
            System.out.print(n.label);

            for (Token t : n.tokens) {
                if (t != null) {
                    System.out.print("( Token- " + t.tokenID + ", " + t.instanc + ", " + t.lineNum + " )  ");
                }
            }

            System.out.println("");
            level++;

            if (n.child1 != null) {
                printSpace(level);
                treePrint(n.child1);
                level--;
            }
            if (n.child2 != null) {
                printSpace(level);
                treePrint(n.child2);
                level--;
            }
            if (n.child3 != null) {
                printSpace(level);
                treePrint(n.child3);
                level--;
            }
            if (n.child4 != null) {
                printSpace(level);
                treePrint(n.child4);
                level--;
            }

        }

    }

    public static void printSpace(int a) {
        for (int i = 0; i < a; i++) {
            System.out.print("  ");
        }
        System.out.print(level);
    }
    
    public static String makeLabel(){
        String label = "label" + labelNum;
        labelNum ++;
        return label;
    }

}
