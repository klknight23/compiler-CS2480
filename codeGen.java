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
public class codeGen {

    public static ArrayList<Token> stack = new ArrayList<Token>();
    static PrintWriter out;
    static int temps = 0;

    public static void local(PrintWriter writer, Node root) {
        out = writer;
        Node node = root.child1;//this is global var subtree
        global(node); //add vars from global sub tree to global arraylist
        node = root.child2;//this is the block subtree
        block(node);
        //pop gloabl vars 
        int k = stack.size();
        for (int i = 0; i < k; i++) {
            pop();
        }
        printToTarget("STOP");
        addVars();
        System.out.println("Done");
        out.close();
    }

    public static void push(Token t) {
        stack.add(t);
        codeGen.printToTarget("PUSH");
        if (stack.size() == 100) {
            support.error("Stack Overflow");
        }
    }

    public static void addVars() {
        for (int i = 0; i <= temps; i++) {
            codeGen.printToTarget("t" + i + " 0");
        }
    }

    public static void pop() {
        stack.remove(stack.size() - 1);
        codeGen.printToTarget("POP");
    }

    public static int find(String s) {
        for (int i = stack.size() - 1; i >= 0; i--) {
            if (stack.get(i).instanc.equals(s)) {
                return stack.size() - 1 - i;
            }
        }
        return -1;
    }

    public static void printToTarget(String s) {
        out.println(s);
    }

    public static String makeTemp() {
        String s = "t" + temps;
        temps++;
        return s;
    }

    //this function add global vars to stack
    public static void global(Node n) {
        for (int i = 0; i < n.tokens.size(); i++) {
            if (n.tokens.get(i).tokenID == Token.TokenID.IDENT_tk) {
                push(n.tokens.get(i));
                codeGen.printToTarget(n.tokens.get(i).instanc + " " + 0);
            }
        }
        if (n.child1 != null) {
            n = n.child1;
            global(n);
        }
    }

    //adds vars to stack and increases count for block
    public static int vars(int varCount, Node n) {
        for (int i = 0; i < n.tokens.size(); i++) {
            if (n.tokens.get(i).tokenID == Token.TokenID.IDENT_tk) {
                if (varCount > 0) {
                    int k = find(n.tokens.get(i).instanc);
                    if (k == -1) {
                        //do nothing
                    } else {
                        support.error("This variable has already been defined: " + n.tokens.get(i).instanc);
                    }
                }
                push(n.tokens.get(i));
                if (find(n.tokens.get(i).instanc) == -1) {
                    codeGen.printToTarget(n.tokens.get(i).instanc + " " + 0);
                }
                varCount++;
            }

        }
        if (n.child1 != null) {
            varCount = +vars(varCount, n.child1);
        }

        return varCount;
    }

    public static void block(Node n) {
        int varCount = 0;
        //checking for vars tokens, if not null call vars()
        if (!n.child1.tokens.isEmpty()) {
            varCount = vars(varCount, n.child1);
        }
        stats(n.child2);
        //pop vars from stack
        for (int i = 0; i < varCount; i++) {
            pop();
        }

    }

    public static void stats(Node n) {
       
        stat(n.child1);
        if (n.child2 != null) {
            mstat(n.child2);
        }
    }

    public static void stat(Node n) {
       
        switch (n.child1.label) {
            case "<in>":
                in(n.child1);
                break;
            case "<out>":
                out(n.child1);
                break;
            case "<if>":
                ifFunt(n.child1);
                break;
            case "<loop>":
                loop(n.child1);
                break;
            case "<assign>":
                assign(n.child1);
                break;
            case "<block>":
                block(n.child1);
        }
    }

    public static void mstat(Node n) {
        
        if (n.child1 != null) {
            stat(n.child1);
            if (n.child2 != null) {
                mstat(n.child2);
            }
        }
    }

    public static void in(Node n) {
       
        Token p = new Token();
        String temp = makeTemp();
        for (Token t : n.tokens) {
            if (t.tokenID == Token.TokenID.IDENT_tk) {
                p = t;
            }
        }
        printToTarget("READ " + temp);
        printToTarget("LOAD " + temp);
        int k = find(p.instanc);
        printToTarget("STACKW " + k);
    }

    public static void out(Node n) {
        
        String temp = makeTemp();
        expr(n.child1);
        printToTarget("STORE " + temp);
        printToTarget("WRITE " + temp);

    }

    public static void ifFunt(Node n) {
        
        String temp1 = makeTemp();
        String temp2 = makeTemp();
        expr(n.child1);
        printToTarget("STORE " + temp1);
        expr(n.child3);
        printToTarget("STORE " + temp2);
        printToTarget("LOAD " + temp1);
        printToTarget("SUB " + temp2);
        String label = RO(n.child2);
        block(n.child4);
        printToTarget(label + ": NOOP");

    }

    public static void assign(Node n) {
        expr(n.child1);
        String temp = makeTemp();
        for (Token t : n.tokens) {
            if (t.tokenID == Token.TokenID.IDENT_tk) {
                printToTarget("STORE " + temp);
                printToTarget("LOAD " + temp);
                int k = find(t.instanc);
                printToTarget("STACKW " + k);

            }
        }
    }

    public static void loop(Node n) {
       
        String temp1 = makeTemp();
        String temp2 = makeTemp();
        String label = support.makeLabel();
        printToTarget(label + ": NOOP");
        expr(n.child1);
        printToTarget("STORE " + temp1);
        expr(n.child3);
        printToTarget("STORE " + temp2);
        printToTarget("LOAD " + temp1);
        printToTarget("SUB " + temp2);
        String labelB = RO(n.child2);
        block(n.child4);
        printToTarget("BR " + label);
        printToTarget(labelB + ": NOOP");
    }

    public static String RO(Node n) {
        String label = support.makeLabel();
        if (n.tokens.get(0).tokenID == Token.TokenID.EQ_tk) {
            printToTarget("BRNEG " + label);
            printToTarget("BRPOS " + label);
        } else if (n.tokens.get(0).tokenID == Token.TokenID.EQNEQ_tk) {
            printToTarget("BRZERO " + label);
        } else if (n.tokens.get(0).tokenID == Token.TokenID.GTR_tk) {
            printToTarget("BRZNEG " + label);
        } else if (n.tokens.get(0).tokenID == Token.TokenID.GTREQ_tk) {
            printToTarget("BRNEG " + label);
        } else if (n.tokens.get(0).tokenID == Token.TokenID.LESS_tk) {
            printToTarget("BRZPOS " + label);
        } else if (n.tokens.get(0).tokenID == Token.TokenID.LESSEQ_tk) {
            printToTarget("BRPOS " + label);
        }
        return label;
    }

    public static void expr(Node n) {
        
        if (!n.tokens.isEmpty()) {
            M(n.child1);
            String temp = makeTemp();
            printToTarget("STORE " + temp);
            expr(n.child2);
            String temp1 = makeTemp();
            printToTarget("STORE " + temp1);
            printToTarget("LOAD " + temp1);
            printToTarget("ADD " + temp);
            printToTarget("STACKW 0");//does this need to write to stack?
        } else {
            M(n.child1);
        }
    }

    public static void M(Node n) {
        
        if (!n.tokens.isEmpty()) {
            T(n.child1);
            String temp = makeTemp();
            printToTarget("STORE "+ temp);
            M(n.child2);
            String temp1 = makeTemp();
            printToTarget("STORE "+ temp1);
            printToTarget("LOAD "+ temp);
            printToTarget("SUB " + temp1);
            printToTarget("STACKW 0");//do we need to write to stack??

        } else {
            T(n.child1);
        }
    }

    public static void T(Node n) {
      
        if (!n.tokens.isEmpty()) {
           F(n.child1);
            String temp = makeTemp();
            printToTarget("STORE " + temp);
             T(n.child2);
            if (n.tokens.get(0).tokenID == Token.TokenID.MULT_tk) {
                printToTarget("MULT " + temp);
            } else {
                printToTarget("DIV " + temp);
            }
        } else {
            F(n.child1);
        }
    }
     public static void F(Node n) {
        
        if (!n.tokens.isEmpty()) {
            F(n.child1);
            printToTarget("MULT -1");
        } else {
            R(n.child1);
        }
    }
    
    public static void R(Node n) {
      
        if (!n.tokens.isEmpty()) {
            if (n.tokens.get(0).tokenID == Token.TokenID.NUM_tk) {
                printToTarget("LOAD " + n.tokens.get(0).instanc);
            } else {
                int k = find(n.tokens.get(0).instanc);
                printToTarget("STACKR " + k);
            }
        } else {
            expr(n.child1);
        }
    }

}//end
