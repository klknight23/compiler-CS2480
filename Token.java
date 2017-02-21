/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */


/**
 *
 * @author klkni
 */
public class Token {
   
    public static enum TokenID {
        IDENT_tk,
        NUM_tk,
        EQ_tk,
        EQNEQ_tk,
        EQEQ_tk,
        GTR_tk,
        GTREQ_tk,
        LESS_tk,
        LESSEQ_tk,
        COL_tk,
        PLUS_tk,
        MINUS_tk,
        MULT_tk,
        DIV_tk,
        AND_tk,
        PERC_tk,
        COMMA_tk,
        PERIOD_tk,
        LPAR_tk,
        RPAR_tk,
        LCURL_tk,
        RCURL_tk,
        SEMI_tk,
        RBRAC_tk,
        LBRAC_tk,
        BEGIN_tk,
        END_tk,
        START_tk,
        STOP_tk,
        IFF_tk,
        LOOP_tk,
        VOID_tk,
        VAR_tk,
        INT_tk,
        CALL_tk,
        RETURN_tk,
        SCAN_tk,
        PRINT_tk,
        PROGRAM_tk,
        EOF_tk;
        
    }
    String[] tokenNames = {"Identifier", "Number",
        
        
        "Begin keyword",
        "End keyword", "Start keyword", "Stop keyword", "Iff keyword",
        "Loop keyword", "Void keyword", "Var keyword", "Int keyword",
        "Call keyword", "Return keyword", "Sacn keyword", "Print keyword",
        "Program keyword"};
     
    TokenID tokenID;
    String instanc;
    int lineNum;
}
