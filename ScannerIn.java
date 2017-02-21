/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */


import java.io.*;
import java.util.*;

public class ScannerIn extends Token {

    static ArrayList<Token> tokens = new ArrayList<Token>();
    static int currToken = 0;
    static String builder = "";
    static int state = 0;

    String[] keywords = {"begin", "end", "start", "stop", "iff", "loop", "void", "var", "int", "call", "return", "scan", "print", "program"};
    String[] relationalOps = {"=", "<", ">", "=!=", ">=>", "<=<"};
    String[] otherOps = {"==", ":", "+", "-", "*", "/", "&", "%"};
    String[] delimeters = {"(", ")", ",", ".", "{", "}", "[", "]"};

    static int[][] fsaTable = {
        //       Let ^ Dig ^  =  ^  !  ^  >  ^  <  ^  :  ^  +  ^  -  ^  *  ^  /  ^  &  ^  %  ^  ,  ^  .  ^  (  ^  )  ^  {  ^  }  ^  ; ^  [ ^  ] ^ ws
        /* q0 */ {1,    2,    3,   -1,    7,    10,   13,   14,   15,   16,  17,    18,   19,   20,   21,   22,  23,    24,   25,   26,  27, 28,   0},
        /* q1 */ {1, 1, 1001, 1001, 1001, 1001, 1001, 1001, 1001, 1001, 1001, 1001, 1001, 1001, 1001, 1001, 1001, 1001, 1001, 1001, 1001, 1001, 1001},
        /* q2 */ {1002, 2, 1002, 1002, 1002, 1002, 1002, 1002, 1002, 1002, 1002, 1002, 1002, 1002, 1002, 1002, 1002, 1002, 1002, 1002, 1002, 1002, 1002},
        /* q3 */ {1003, 1003, 4, 5, 1003, 1003, 1003, 1003, 1003, 1003, 1003, 1003, 1003, 1003, 1003, 1003, 1003, 1003, 1003, 1003, 1003, 1003, 1003},
        /* q4 */ {1005, 1005, 1005, 1005, 1005, 1005, 1005, 1005, 1005, 1005, 1005, 1005, 1005, 1005, 1005, 1005, 1005, 1005, 1005, 1005, 1005, 1005, 1005},
        /* q5 */ {-1, -1, 6, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1},
        /* q6 */ {1004, 1004, 1004, 1004, 1004, 1004, 1004, 1004, 1004, 1004, 1004, 1004, 1004, 1004, 1004, 1004, 1004, 1004, 1004, 1004, 1004, 1004, 1004},
        /* q7 */ {2001, 2001, 8, 2001, 2001, 2001, 2001, 2001, 2001, 2001, 2001, 2001, 2001, 2001, 2001, 2001, 2001, 2001, 2001, 2001, 2001, 2001, 2001},
        /* q8 */ {-1, -1, -1, -1, 9, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1},
        /* q9 */ {2002, 2002, 2002, 2002, 2002, 2002, 2002, 2002, 2002, 2002, 2002, 2002, 2002, 2002, 2002, 2002, 2002, 2002, 2002, 2002, 2002, 2002, 2002},
        /* q10 */ {2003, 2003, 11, 2003, 2003, 2003, 2003, 2003, 2003, 2003, 2003, 2003, 2003, 2003, 2003, 2003, 2003, 2003, 2003, 2003, 2003, 2003, 2003},
        /* q11 */ {-1, -1, -1, -1, -1, 12, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1},
        /* q12 */ {2004, 2004, 2004, 2004, 2004, 2004, 2004, 2004, 2004, 2004, 2004, 2004, 2004, 2004, 2004, 2004, 2004, 2004, 2004, 2004, 2004, 2004, 2004},
        /* q13 */ {3001, 3001, 3001, 3001, 3001, 3001, 3001, 3001, 3001, 3001, 3001, 3001, 3001, 3001, 3001, 3001, 3001, 3001, 3001, 3001, 3001, 3001, 3001},
        /* q14 */ {3002, 3002, 3002, 3002, 3002, 3002, 3002, 3002, 3002, 3002, 3002, 3002, 3002, 3002, 3002, 3002, 3002, 3002, 3002, 3002, 3002, 3002, 3002},
        /* q15 */ {3003, 3003, 3003, 3003, 3003, 3003, 3003, 3003, 3003, 3003, 3003, 3003, 3003, 3003, 3003, 3003, 3003, 3003, 3003, 3003, 3003, 3003, 3003},
        /* q16 */ {3004, 3004, 3004, 3004, 3004, 3004, 3004, 3004, 3004, 3004, 3004, 3004, 3004, 3004, 3004, 3004, 3004, 3004, 3004, 3004, 3004, 3004, 3004},
        /* q17 */ {3005, 3005, 3005, 3005, 3005, 3005, 3005, 3005, 3005, 3005, 3005, 3005, 3005, 3005, 3005, 3005, 3005, 3005, 3005, 3005, 3005, 3005, 3005},
        /* q18 */ {3006, 3006, 3006, 3006, 3006, 3006, 3006, 3006, 3006, 3006, 3006, 3006, 3006, 3006, 3006, 3006, 3006, 3006, 3006, 3006, 3006, 3006, 3006},
        /* q19 */ {3007, 3007, 3007, 3007, 3007, 3007, 3007, 3007, 3007, 3007, 3007, 3007, 3007, 3007, 3007, 3007, 3007, 3007, 3007, 3007, 3007, 3007, 3007},
        /* q20 */ {3008, 3008, 3008, 3008, 3008, 3008, 3008, 3008, 3008, 3008, 3008, 3008, 3008, 3008, 3008, 3008, 3008, 3008, 3008, 3008, 3008, 3008, 3008},
        /* q21 */ {3009, 3009, 3009, 3009, 3009, 3009, 3009, 3009, 3009, 3009, 3009, 3009, 3009, 3009, 3009, 3009, 3009, 3009, 3009, 3009, 3009, 3009, 3009},
        /* q22 */ {3010, 3010, 3010, 3010, 3010, 3010, 3010, 3010, 3010, 3010, 3010, 3010, 3010, 3010, 3010, 3010, 3010, 3010, 3010, 3010, 3010, 3010, 3010},
        /* q23 */ {3011, 3011, 3011, 3011, 3011, 3011, 3011, 3011, 3011, 3011, 3011, 3011, 3011, 3011, 3011, 3011, 3011, 3011, 3011, 3011, 3011, 3011, 3011},
        /* q24 */ {3012, 3012, 3012, 3012, 3012, 3012, 3012, 3012, 3012, 3012, 3012, 3012, 3012, 3012, 3012, 3012, 3012, 3012, 3012, 3012, 3012, 3012, 3012},
        /* q25 */ {3013, 3013, 3013, 3013, 3013, 3013, 3013, 3013, 3013, 3013, 3013, 3013, 3013, 3013, 3013, 3013, 3013, 3013, 3013, 3013, 3013, 3013, 3013},
        /* q26 */ {3014, 3014, 3014, 3014, 3014, 3014, 3014, 3014, 3014, 3014, 3014, 3014, 3014, 3014, 3014, 3014, 3014, 3014, 3014, 3014, 3014, 3014, 3014},
        /* q27 */ {3015, 3015, 3015, 3015, 3015, 3015, 3015, 3015, 3015, 3015, 3015, 3015, 3015, 3015, 3015, 3015, 3015, 3015, 3015, 3015, 3015, 3015, 3015},
        /* q28*/ {3016, 3016, 3016, 3016, 3016, 3016, 3016, 3016, 3016, 3016, 3016, 3016, 3016, 3016, 3016, 3016, 3016, 3016, 3016, 3016, 3016, 3016, 3016}
    };

    public static void scanHelper(String line, int lineNum) {

        //convery chars to state 
        ArrayList list = new ArrayList();
        state = 0;
        int value = 0;
        char ch;

        //covert to correct state  and get token
        for (int i = 0; i < line.length(); i++) {
            ch = line.charAt(i);
            i = findLocation(ch, value, line, lineNum, i);
        }
        //we call one more time to set very last token
        findLocation('0', 22, line, lineNum, 0);
    }

    public static int findLocation(char ch, int value, String line, int lineNum, int i) {
        Token token = new Token();

        if (Character.isSpaceChar(ch) || value == 22) {//check for end of string and set to ws char
            value = 22;
        } else if (Character.isLetter(ch)) {
            value = 0;
        } else if (Character.isDigit(ch)) {
            value = 1;
        } else {
            switch (ch) {
                case '=':
                    value = 2;
                    break;
                case '!':
                    value = 3;
                    break;
                case '>':
                    value = 4;
                    break;
                case '<':
                    value = 5;
                    break;
                case ':':
                    value = 6;
                    break;
                case '+':
                    value = 7;
                    break;
                case '-':
                    value = 8;
                    break;
                case '*':
                    value = 9;
                    break;
                case '/':
                    value = 10;
                    break;
                case '&':
                    value = 11;
                    break;
                case '%':
                    value = 12;
                    break;
                case ',':
                    value = 13;
                    break;
                case '.':
                    value = 14;
                    break;
                case '(':
                    value = 15;
                    break;
                case ')':
                    value = 16;
                    break;
                case '{':
                    value = 17;
                    break;
                case '}':
                    value = 18;
                    break;
                case ';':
                    value = 19;
                    break;
                case '[':
                    value = 20;
                    break;
                case ']':
                    value = 21;
                    break;
            }
        }
        //find current location in table
        int location = fsaTable[state][value];
        if (location == -1) {//error state
            System.out.println("ERORR: NOT A TOKEN ON LINE:" + lineNum);
            System.out.println("Current char: " + ch);
            System.exit(1);
        }
        if (location <= 28) { //possible next state
            state = location;
            
            if (value != 22) { //don't want to add whitespace to token
                builder = builder + ch;
            }
        } else {
            //go through possible ones then reset state to 0 and go back one letter
            switch (location) {
                case 1001:
                    token.tokenID = getTokenID(builder);
                    token.instanc = builder;
                    token.lineNum = lineNum;
                    tokens.add(token);
                    break;
                case 1002:
                    token.tokenID = TokenID.NUM_tk;
                    token.instanc = builder;
                    token.lineNum = lineNum;
                    tokens.add(token);
                    break;
                case 1003:
                    token.tokenID = TokenID.EQ_tk;
                    token.instanc = builder;
                    token.lineNum = lineNum;
                    tokens.add(token);
                    break;
                case 1004:
                    token.tokenID = TokenID.EQNEQ_tk;
                    token.instanc = builder;
                    token.lineNum = lineNum;
                    tokens.add(token);
                    break;
                case 1005:
                    token.tokenID = TokenID.EQEQ_tk;
                    token.instanc = builder;
                    token.lineNum = lineNum;
                    tokens.add(token);
                    break;
                case 2001:
                    token.tokenID = TokenID.GTR_tk;
                    token.instanc = builder;
                    token.lineNum = lineNum;
                    tokens.add(token);
                    break;
                case 2002:
                    token.tokenID = TokenID.GTREQ_tk;
                    token.instanc = builder;
                    token.lineNum = lineNum;
                    tokens.add(token);
                    break;
                case 2003:
                    token.tokenID = TokenID.LESS_tk;
                    token.instanc = builder;
                    token.lineNum = lineNum;
                    tokens.add(token);
                    break;
                case 2004:
                    token.tokenID = TokenID.LESSEQ_tk;
                    token.instanc = builder;
                    token.lineNum = lineNum;
                    tokens.add(token);
                    break;
                case 3001:
                    
                    token.tokenID = TokenID.COL_tk;
                    token.instanc = builder;
                    token.lineNum = lineNum;
                    tokens.add(token);
                    break;
                case 3002:
                    token.tokenID = TokenID.PLUS_tk;
                    token.instanc = builder;
                    token.lineNum = lineNum;
                    tokens.add(token);
                    break;
                case 3003:
                    token.tokenID = TokenID.MINUS_tk;
                    token.instanc = builder;
                    token.lineNum = lineNum;
                    tokens.add(token);
                    break;
                case 3004:
                    token.tokenID = TokenID.MULT_tk;
                    token.instanc = builder;
                    token.lineNum = lineNum;
                    tokens.add(token);
                    break;
                case 3005:
                    token.tokenID = TokenID.DIV_tk;
                    token.instanc = builder;
                    token.lineNum = lineNum;
                    tokens.add(token);
                    break;
                case 3006:
                    token.tokenID = TokenID.AND_tk;
                    token.instanc = builder;
                    token.lineNum = lineNum;
                    tokens.add(token);
                    break;
                case 3007:
                    token.tokenID = TokenID.PERC_tk;
                    token.instanc = builder;
                    token.lineNum = lineNum;
                    tokens.add(token);
                    break;
                case 3008:
                    token.tokenID = TokenID.COMMA_tk;
                    token.instanc = builder;
                    token.lineNum = lineNum;
                    tokens.add(token);
                    break;
                case 3009:
                    token.tokenID = TokenID.PERIOD_tk;
                    token.instanc = builder;
                    token.lineNum = lineNum;
                    tokens.add(token);
                    break;
                case 3010:
                    token.tokenID = TokenID.LPAR_tk;
                    token.instanc = builder;
                    token.lineNum = lineNum;
                    tokens.add(token);
                    break;
                case 3011:
                    token.tokenID = TokenID.RPAR_tk;
                    token.instanc = builder;
                    token.lineNum = lineNum;
                    tokens.add(token);
                    break;
                case 3012:
                    token.tokenID = TokenID.LCURL_tk;
                    token.instanc = builder;
                    token.lineNum = lineNum;
                    tokens.add(token);
                    break;
                case 3013:
                    token.tokenID = TokenID.RCURL_tk;
                    token.instanc = builder;
                    token.lineNum = lineNum;
                    tokens.add(token);
                    break;
                case 3014:
                    token.tokenID = TokenID.SEMI_tk;
                    token.instanc = builder;
                    token.lineNum = lineNum;
                    tokens.add(token);
                    break;
                case 3015:
                    token.tokenID = TokenID.LBRAC_tk;
                    token.instanc = builder;
                    token.lineNum = lineNum;
                    tokens.add(token);
                    break;
                case 3016:
                    token.tokenID = TokenID.RBRAC_tk;
                    token.instanc = builder;
                    token.lineNum = lineNum;
                    tokens.add(token);
                    break;

            }
            //reset all and go back one letter
            state = 0;
            builder = "";
            i--;
        }
        return i;
    }

    public static Token scan() {
        if (currToken < tokens.size()) {
            Token t = tokens.get(currToken);
            currToken++;
            return t;
        }else{
            Token EOF = new Token();
            EOF.tokenID = TokenID.EOF_tk;
            return EOF;
        }
            
    }

    public static Boolean isAcceptableChar(char c) {
        if (c == '=' || c == '<' || c == '>' || c == '!' || c == ':' || c == '+'
                || c == '-' || c == '*' || c == '/' || c == '&' || c == '%'
                || c == '(' || c == ')' || c == ',' || c == '.' || c == '{' || c == '}'
                || c == '[' || c == ']' || c == '\n' || c == ';') {
            return true;
        } else {
            return false;
        }
    }

    public static Token.TokenID getTokenID(String s) {
        switch (s) {
            case "Int":
                return TokenID.INT_tk;
            case "Begin":
                return TokenID.BEGIN_tk;
            case "End":
                return TokenID.END_tk;
            case "Start":
                return TokenID.START_tk;
            case "Stop":
                return TokenID.STOP_tk;
            case "Iff":
                return TokenID.IFF_tk;
            case "Loop":
                return TokenID.LOOP_tk;
            case "Void":
                return TokenID.VOID_tk;
            case "Var":
                return TokenID.VAR_tk;
            case "Call":
                return TokenID.CALL_tk;
            case "Return":
                return TokenID.RETURN_tk;
            case "Scan":
                return TokenID.SCAN_tk;
            case "Print":
                return TokenID.PRINT_tk;
            case "Program":
                return TokenID.PROGRAM_tk;
            default:
                return TokenID.IDENT_tk;

        }
    }

}
