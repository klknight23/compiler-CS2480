/*
 * 
 * 
 * 
 */

import java.util.*;

/**
 *
 * @author klkni
 */
public class SS {

    static ArrayList<Token> global1 = new ArrayList<Token>();
    static ArrayList<Token> stack1 = new ArrayList<Token>();

    public static void push(Token t) {
        stack1.add(t);
        if (stack1.size() == 100) {
            support.error("Stack Overflow");
        }
    }

    public static void pop() {
        stack1.remove(stack1.size() - 1);
    }

    public static void local(Node root) {
        Node node = root.child1;//this is global var subtree
        global(node); //add vars from global sub tree to global arraylist
        node = root.child2;//this is the block subtree
        block(node);
    }

    //this function add global vars to global array
    public static void global(Node n) {
        for (int i = 0; i < n.tokens.size(); i++) {
            if (n.tokens.get(i).tokenID == Token.TokenID.IDENT_tk) {
                global1.add(n.tokens.get(i));
            }
        }
        if (n.child1 != null) {
            n = n.child1;
            global(n);
        }
    }

    //this function checks if var is a global var if not defined in scope,
    // returns 1 if found errors if not found
    public static int checkGlobal(Token t) {
        for (int i = 0; i < global1.size(); i++) {

            if (t.instanc.equals(global1.get(i).instanc)) {
                return 1;
            }
        }
        support.error("This variable is not defined: " + t.instanc + ": " + t.lineNum);
        return -1;
    }
    public static int find(String s) {
        for (int i = stack1.size() - 1; i >= 0; i--) {
            if (stack1.get(i).instanc.equals(s)) {
                return stack1.size() - 1 - i;
            }
        }
        return -1;
    }

    ///checks if var is defined in stack already if not call checkGlobal
    public static void checkLocal(Token t) {
        int k = find(t.instanc);
        if (k == -1) {
            checkGlobal(t);
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
        if (n.child1.tokens != null) {
            varCount = vars(varCount, n.child1);
        }
        //recursivley go through tree, if  c node label == <block> call block()
        //if tokens == IDENT_tk check
        blockHelper(n.child2, varCount);
        for (int i = 0; i < varCount; i++) {
            pop();
        }
    }

    //cheks if label is block, if no checks for Ident_tk, if yes calls block
    public static void blockHelper(Node n, int varCount) {
        if (!n.label.equals("<block>")) {
            for (Token t : n.tokens) {
                if (t.tokenID == Token.TokenID.IDENT_tk) {
                    checkLocal(t);
                }
            }
            if (n.child1 != null) {
                blockHelper(n.child1, varCount);
            }
            if (n.child2 != null) {
                blockHelper(n.child2, varCount);
            }
            if (n.child3 != null) {
                blockHelper(n.child3, varCount);
            }
            if (n.child4 != null) {
                blockHelper(n.child4, varCount);
            }
        } else {
            block(n);
        }
    }

}
