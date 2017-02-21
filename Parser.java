/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */


/**
 *
 * @author klkni
 */
public class Parser {

    static Token tk;

    public static Node parser() {
        Node root;
        tk = ScannerIn.scan();
        root = program();
        // must get EOF token
        if (tk.tokenID == Token.TokenID.EOF_tk) {
            System.out.println("Parse OK");
        } else {
            support.error("Error! Didn't reach EOF while parsing.");
        }
        return root;
    }

    public static Node program() {
        Node p = support.getNode("<program>");
        p.child1 = vars();
        p.child2 = block();
        return p;
    }

    public static Node vars() {
        Node p = support.getNode("<vars>");
        if (tk.tokenID == Token.TokenID.VAR_tk) { // must start with vars which starts with var_tk
            p.tokens.add(tk);
            tk = ScannerIn.scan();
            if (tk.tokenID == Token.TokenID.IDENT_tk) {
                p.tokens.add(tk);
                tk = ScannerIn.scan();
                p.child1 = mvars();
                return p;
            } else {
                System.out.println("ERROR! Expected Identifier token." + tk.tokenID + ": " + tk.lineNum);
                System.exit(1);
                return null;
            }
        } else {
            return p;
        }
    }

    public static Node mvars() {
        if (tk.tokenID == Token.TokenID.COL_tk) {
            Node p = support.getNode("<mvars>");
            p.tokens.add(tk);
            tk = ScannerIn.scan();
            if (tk.tokenID == Token.TokenID.COL_tk) {
                p.tokens.add(tk);
                tk = ScannerIn.scan();
                if (tk.tokenID == Token.TokenID.IDENT_tk) {
                    p.tokens.add(tk);
                    tk = ScannerIn.scan();
                    p.child1 = mvars();
                    return p;
                } else {
                    System.out.println("ERROR! Expected Identifier token. " + tk.tokenID + ": " + tk.lineNum);
                    System.exit(1);
                    return null;
                }
            } else {
                System.out.println("ERROR! Expected colon token. " + tk.tokenID + ": " + tk.lineNum);
                System.exit(1);
                return null;
            }
        } else {
            return null;
        }
    }

    public static Node block() {
        if (tk.tokenID == Token.TokenID.BEGIN_tk) {
            Node p = support.getNode("<block>");
            p.tokens.add(tk);
            tk = ScannerIn.scan();
            p.child1 = vars();
            p.child2 = stats();
            if (tk.tokenID == Token.TokenID.END_tk) {
                p.tokens.add(tk);
                tk = ScannerIn.scan();
                return p;
            } else {
                System.out.println("ERROR!1 Expected End token. " + tk.tokenID + ": " + tk.lineNum);
                System.exit(1);
                return null;
            }
        } else {
            System.out.println("ERROR! Expected Begin token. " + tk.tokenID + ": " + tk.lineNum);
            System.exit(1);
            return null;
        }

    }

    public static Node stats() {
        Node p = support.getNode("<stats>");
        p.child1 = stat();
        p.child2 = mStat();
        return p;
    }

    public static Node stat() {
        Node p = support.getNode("<stat>");
        if (tk.tokenID == Token.TokenID.SCAN_tk) {
            p.child1 = in();
        } else if (tk.tokenID == Token.TokenID.PRINT_tk) {
            p.child1 = out();
        } else if (tk.tokenID == Token.TokenID.BEGIN_tk) {
            p.child1 = block();
        } else if (tk.tokenID == Token.TokenID.LBRAC_tk) {
            p.child1 = ifFunt();
        } else if (tk.tokenID == Token.TokenID.LOOP_tk) {
            p.child1 = loop();
        } else if (tk.tokenID == Token.TokenID.IDENT_tk) {
            p.child1 = assign();
        } else {
            System.out.println("ERROR! Unexpecetd token. " + tk.tokenID + ": " + tk.lineNum);
            System.exit(1);
        }
        return p;
    }

    public static Node in() {
        Node p = support.getNode("<in>");
        p.tokens.add(tk);
        tk = ScannerIn.scan();
        if (tk.tokenID == Token.TokenID.COL_tk) {
            tk = ScannerIn.scan();
            if (tk.tokenID == Token.TokenID.IDENT_tk) {
                p.tokens.add(tk);
                tk = ScannerIn.scan();

                if (tk.tokenID == Token.TokenID.PERIOD_tk) {
                    p.tokens.add(tk);
                    tk = ScannerIn.scan();

                    return p;
                } else {
                    System.out.println("ERROR! Expected period. " + tk.tokenID + ": " + tk.lineNum);
                    System.exit(1);

                    return null;
                }
            } else {
                System.out.println("ERROR! Expected an identifier. " + tk.tokenID + ": " + tk.lineNum);
                System.exit(1);

                return null;
            }
        } else {
            System.out.println("ERROR! Expecetd a colon. " + tk.tokenID + ": " + tk.lineNum);
            System.exit(1);

            return null;
        }

    }

    public static Node out() {
        Node p = support.getNode("<out>");
        p.tokens.add(tk);
        tk = ScannerIn.scan();

        if (tk.tokenID == Token.TokenID.LBRAC_tk) {
            tk = ScannerIn.scan();
            p.child1 = expr();
            if (tk.tokenID == Token.TokenID.RBRAC_tk) {
                tk = ScannerIn.scan();

                if (tk.tokenID == Token.TokenID.PERIOD_tk) {
                    tk = ScannerIn.scan();

                    return p;
                } else {
                    System.out.println("ERROR! Expected period. " + tk.tokenID + ": " + tk.lineNum);
                    System.exit(1);
                    return null;
                }
            } else {
                System.out.println("ERROR! Expecetd right bracket. " + tk.tokenID + ": " + tk.lineNum);
                System.exit(1);
                return null;
            }
        } else {
            System.out.println("ERROR! Expected left bracket. " + tk.tokenID + ": " + tk.lineNum);
            System.exit(1);
            return null;
        }

    }

    public static Node ifFunt() {
        Node p = support.getNode("<if>");
        p.tokens.add(tk);
        tk = ScannerIn.scan();
        p.child1 = expr();
        p.child2 = RO();
        p.child3 = expr();
        if (tk.tokenID == Token.TokenID.RBRAC_tk) {
            tk = ScannerIn.scan();

            if (tk.tokenID == Token.TokenID.IFF_tk) {
                p.tokens.add(tk);
                tk = ScannerIn.scan();
                p.child4 = block();
                return p;
            } else {
                System.out.println("ERROR! Expected an IFF. " + tk.tokenID + ": " + tk.lineNum);
                System.exit(1);
                return null;
            }
        } else {
            System.out.println("ERROR! Expecetd right bracket. " + tk.tokenID + ": " + tk.lineNum);
            System.exit(1);
            return null;
        }
    }

    public static Node loop() {
        Node p = support.getNode("<loop>");
        p.tokens.add(tk);
        tk = ScannerIn.scan();
        if (tk.tokenID == Token.TokenID.LBRAC_tk) {
            tk = ScannerIn.scan();
            p.child1 = expr();
            p.child2 = RO();
            p.child3 = expr();
            if (tk.tokenID == Token.TokenID.RBRAC_tk) {
                tk = ScannerIn.scan();
                p.child4 = block();
                return p;
            } else {
                System.out.println("ERROR! Expecetd a right bracket. " + tk.tokenID + ": " + tk.lineNum);
                System.exit(1);
                return null;
            }
        } else {
            System.out.println("ERROR! Expected a left bracket. " + tk.tokenID + ": " + tk.lineNum);
            System.exit(1);
            return null;
        }
    }

    public static Node assign() {
        Node p = support.getNode("<assign>");
        p.tokens.add(tk);
        tk = ScannerIn.scan();
        if (tk.tokenID == Token.TokenID.EQEQ_tk) {
            p.tokens.add(tk);
            tk = ScannerIn.scan();
            p.child1 = expr();
            if (tk.tokenID == Token.TokenID.PERIOD_tk) {
                tk = ScannerIn.scan();
                return p;
            } else {
                System.out.println("ERROR!1 Expected a period. " + tk.tokenID + ": " + tk.lineNum);
                System.exit(1);
                return null;
            }
        } else {
            System.out.println("ERROR!2 Expected an equal equal. " + tk.tokenID + ": " + tk.lineNum);
            System.exit(1);
            return null;
        }
    }

    public static Node mStat() {
        if (tk.tokenID == Token.TokenID.SCAN_tk || tk.tokenID == Token.TokenID.PRINT_tk || tk.tokenID == Token.TokenID.BEGIN_tk || tk.tokenID == Token.TokenID.LBRAC_tk || tk.tokenID == Token.TokenID.LOOP_tk || tk.tokenID == Token.TokenID.IDENT_tk) {
            Node p = support.getNode("<mStat>");
            p.child1 = stat();
            p.child2 = mStat();
            return p;
        } else {
            return null;
        }
    }

    public static Node expr() {
        Node p = support.getNode("<expr>");
        p.child1 = M();
        if (tk.tokenID == Token.TokenID.PLUS_tk) {
            p.tokens.add(tk);
            tk = ScannerIn.scan();
            p.child2 = expr();
            return p;
        } else {
            return p;
        }
    }

    public static Node RO() {
        if (tk.tokenID == Token.TokenID.GTREQ_tk || tk.tokenID == Token.TokenID.LESSEQ_tk || tk.tokenID == Token.TokenID.EQ_tk || tk.tokenID == Token.TokenID.GTR_tk || tk.tokenID == Token.TokenID.LESS_tk || tk.tokenID == Token.TokenID.LESSEQ_tk || tk.tokenID == Token.TokenID.EQNEQ_tk) {
            Node p = support.getNode("<RO>");
            p.tokens.add(tk);
            tk = ScannerIn.scan();
            return p;
        } else {
            System.out.println("ERROR! Unexpected Token. " + tk.tokenID + ": " + tk.lineNum);
            System.exit(1);
            return null;
        }
    }

    public static Node M() {
        Node p = support.getNode("<M>");
        p.child1 = T();
        if (tk.tokenID == Token.TokenID.MINUS_tk){
            p.tokens.add(tk);
            tk = ScannerIn.scan();
            p.child2 = M();
            return p;
        } else {
            return p;
        }
    }

    public static Node T() {
        Node p = support.getNode("<T>");
        p.child1 = F();
        if (tk.tokenID == Token.TokenID.MULT_tk) {
            p.tokens.add(tk);
            tk = ScannerIn.scan();
            p.child2 = T();
            return p;
        } else if (tk.tokenID == Token.TokenID.DIV_tk) {
            p.tokens.add(tk);
            tk = ScannerIn.scan();
            p.child2 = T();
            return p;
        } else {
            return p;
        }
    }

    public static Node F() {
        Node p = support.getNode("<F>");
        if (tk.tokenID == Token.TokenID.MINUS_tk) {
            p.tokens.add(tk);
            tk = ScannerIn.scan();
            p.child1 = F();
        } else {
            p.child1 = R();
        }
        return p;
    }

    public static Node R() {
        Node p = support.getNode("<R>");
        if (tk.tokenID == Token.TokenID.LBRAC_tk) {
            tk = ScannerIn.scan();
            p.child1 = expr();
            if (tk.tokenID == Token.TokenID.RBRAC_tk) {
                tk = ScannerIn.scan();
                return p;
            } else {
                System.out.println("ERROR! Expectd a right bracket. " + tk.tokenID + ": " + tk.lineNum);
                System.exit(1);
                return null;
            }
        } else if (tk.tokenID == Token.TokenID.IDENT_tk) {
            p.tokens.add(tk);
            tk = ScannerIn.scan();
            return p;

        } else if (tk.tokenID == Token.TokenID.NUM_tk) {
            p.tokens.add(tk);
            tk = ScannerIn.scan();
            return p;
        } else {
            System.out.println("ERROR! unexpected token. " + tk.tokenID + ": " + tk.lineNum);
            System.exit(1);
            return null;
        }
    }

}//end parser
