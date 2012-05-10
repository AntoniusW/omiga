// $ANTLR 3.4 /home/staff/aweinz/svnrepo/gerald_weidinger_thesis/WOC - Wings of Change/antlr/woc.g 2012-05-10 16:16:21

package parser.antlr;

import java.util.ArrayList;

import Entity.*;
import Interfaces.Term;
import Interfaces.OperandI;
import Enumeration.OP;
import Exceptions.*;


import org.antlr.runtime.*;
import java.util.Stack;
import java.util.List;
import java.util.ArrayList;

@SuppressWarnings({"all", "warnings", "unchecked"})
public class wocParser extends Parser {
    public static final String[] tokenNames = new String[] {
        "<invalid>", "<EOR>", "<DOWN>", "<UP>", "CHAR", "COMMENT", "ESC_SEQ", "EXPONENT", "HEX_DIGIT", "ID", "INT", "OCTAL_ESC", "STRING", "UNICODE_ESC", "VAR", "WS", "'!='", "'('", "')'", "'*'", "'+'", "','", "'-'", "'.'", "'/'", "':'", "':-'", "'<'", "'<='", "'='", "'>'", "'>='", "'is'", "'not'"
    };

    public static final int EOF=-1;
    public static final int T__16=16;
    public static final int T__17=17;
    public static final int T__18=18;
    public static final int T__19=19;
    public static final int T__20=20;
    public static final int T__21=21;
    public static final int T__22=22;
    public static final int T__23=23;
    public static final int T__24=24;
    public static final int T__25=25;
    public static final int T__26=26;
    public static final int T__27=27;
    public static final int T__28=28;
    public static final int T__29=29;
    public static final int T__30=30;
    public static final int T__31=31;
    public static final int T__32=32;
    public static final int T__33=33;
    public static final int CHAR=4;
    public static final int COMMENT=5;
    public static final int ESC_SEQ=6;
    public static final int EXPONENT=7;
    public static final int HEX_DIGIT=8;
    public static final int ID=9;
    public static final int INT=10;
    public static final int OCTAL_ESC=11;
    public static final int STRING=12;
    public static final int UNICODE_ESC=13;
    public static final int VAR=14;
    public static final int WS=15;

    // delegates
    public Parser[] getDelegates() {
        return new Parser[] {};
    }

    // delegators


    public wocParser(TokenStream input) {
        this(input, new RecognizerSharedState());
    }
    public wocParser(TokenStream input, RecognizerSharedState state) {
        super(input, state);
    }

    public String[] getTokenNames() { return wocParser.tokenNames; }
    public String getGrammarFileName() { return "/home/staff/aweinz/svnrepo/gerald_weidinger_thesis/WOC - Wings of Change/antlr/woc.g"; }


    private ContextASP context;

    public void setContext(ContextASP context) {
    	this.context=context;
    }




    // $ANTLR start "woc_program"
    // /home/staff/aweinz/svnrepo/gerald_weidinger_thesis/WOC - Wings of Change/antlr/woc.g:29:1: woc_program : ( rule_or_fact )* ;
    public final void woc_program() throws RuleNotSafeException, RecognitionException, FactSizeException {
        try {
            // /home/staff/aweinz/svnrepo/gerald_weidinger_thesis/WOC - Wings of Change/antlr/woc.g:30:2: ( ( rule_or_fact )* )
            // /home/staff/aweinz/svnrepo/gerald_weidinger_thesis/WOC - Wings of Change/antlr/woc.g:30:4: ( rule_or_fact )*
            {
            // /home/staff/aweinz/svnrepo/gerald_weidinger_thesis/WOC - Wings of Change/antlr/woc.g:30:4: ( rule_or_fact )*
            loop1:
            do {
                int alt1=2;
                int LA1_0 = input.LA(1);

                if ( (LA1_0==ID||LA1_0==23||LA1_0==26) ) {
                    alt1=1;
                }


                switch (alt1) {
            	case 1 :
            	    // /home/staff/aweinz/svnrepo/gerald_weidinger_thesis/WOC - Wings of Change/antlr/woc.g:30:4: rule_or_fact
            	    {
            	    pushFollow(FOLLOW_rule_or_fact_in_woc_program36);
            	    rule_or_fact();

            	    state._fsp--;


            	    }
            	    break;

            	default :
            	    break loop1;
                }
            } while (true);


            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }

        finally {
        	// do for sure before leaving
        }
        return ;
    }
    // $ANTLR end "woc_program"



    // $ANTLR start "rule_or_fact"
    // /home/staff/aweinz/svnrepo/gerald_weidinger_thesis/WOC - Wings of Change/antlr/woc.g:33:1: rule_or_fact : ( atom )? ( ':-' body[r] '.' | '.' ) ;
    public final void rule_or_fact() throws RuleNotSafeException, RecognitionException, FactSizeException {
        Atom atom1 =null;


         Atom head=null; 
        try {
            // /home/staff/aweinz/svnrepo/gerald_weidinger_thesis/WOC - Wings of Change/antlr/woc.g:35:2: ( ( atom )? ( ':-' body[r] '.' | '.' ) )
            // /home/staff/aweinz/svnrepo/gerald_weidinger_thesis/WOC - Wings of Change/antlr/woc.g:35:4: ( atom )? ( ':-' body[r] '.' | '.' )
            {
            // /home/staff/aweinz/svnrepo/gerald_weidinger_thesis/WOC - Wings of Change/antlr/woc.g:35:4: ( atom )?
            int alt2=2;
            int LA2_0 = input.LA(1);

            if ( (LA2_0==ID) ) {
                alt2=1;
            }
            switch (alt2) {
                case 1 :
                    // /home/staff/aweinz/svnrepo/gerald_weidinger_thesis/WOC - Wings of Change/antlr/woc.g:35:4: atom
                    {
                    pushFollow(FOLLOW_atom_in_rule_or_fact60);
                    atom1=atom();

                    state._fsp--;


                    }
                    break;

            }


            head=atom1;

            // /home/staff/aweinz/svnrepo/gerald_weidinger_thesis/WOC - Wings of Change/antlr/woc.g:36:3: ( ':-' body[r] '.' | '.' )
            int alt3=2;
            int LA3_0 = input.LA(1);

            if ( (LA3_0==26) ) {
                alt3=1;
            }
            else if ( (LA3_0==23) ) {
                alt3=2;
            }
            else {
                NoViableAltException nvae =
                    new NoViableAltException("", 3, 0, input);

                throw nvae;

            }
            switch (alt3) {
                case 1 :
                    // /home/staff/aweinz/svnrepo/gerald_weidinger_thesis/WOC - Wings of Change/antlr/woc.g:36:4: ':-' body[r] '.'
                    {
                    match(input,26,FOLLOW_26_in_rule_or_fact68); 

                    Rule r = new Rule();

                    pushFollow(FOLLOW_body_in_rule_or_fact72);
                    body(r);

                    state._fsp--;


                    match(input,23,FOLLOW_23_in_rule_or_fact75); 

                    if(head != null) r.setHead(head); context.addRule(r);

                    }
                    break;
                case 2 :
                    // /home/staff/aweinz/svnrepo/gerald_weidinger_thesis/WOC - Wings of Change/antlr/woc.g:37:4: '.'
                    {
                    match(input,23,FOLLOW_23_in_rule_or_fact82); 

                    	// TODO: throw better exception if there is no head, currently throws NullPointerException
                    			context.addFact2IN(atom1.getPredicate(),Instance.getInstance(atom1.getTerms()));

                    }
                    break;

            }


            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }

        finally {
        	// do for sure before leaving
        }
        return ;
    }
    // $ANTLR end "rule_or_fact"



    // $ANTLR start "body"
    // /home/staff/aweinz/svnrepo/gerald_weidinger_thesis/WOC - Wings of Change/antlr/woc.g:41:1: body[Rule r] : literal[$r] ( ',' literal[$r] )* ;
    public final void body(Rule r) throws RecognitionException {
        try {
            // /home/staff/aweinz/svnrepo/gerald_weidinger_thesis/WOC - Wings of Change/antlr/woc.g:42:2: ( literal[$r] ( ',' literal[$r] )* )
            // /home/staff/aweinz/svnrepo/gerald_weidinger_thesis/WOC - Wings of Change/antlr/woc.g:42:4: literal[$r] ( ',' literal[$r] )*
            {
            pushFollow(FOLLOW_literal_in_body98);
            literal(r);

            state._fsp--;


            // /home/staff/aweinz/svnrepo/gerald_weidinger_thesis/WOC - Wings of Change/antlr/woc.g:42:16: ( ',' literal[$r] )*
            loop4:
            do {
                int alt4=2;
                int LA4_0 = input.LA(1);

                if ( (LA4_0==21) ) {
                    alt4=1;
                }


                switch (alt4) {
            	case 1 :
            	    // /home/staff/aweinz/svnrepo/gerald_weidinger_thesis/WOC - Wings of Change/antlr/woc.g:42:17: ',' literal[$r]
            	    {
            	    match(input,21,FOLLOW_21_in_body102); 

            	    pushFollow(FOLLOW_literal_in_body104);
            	    literal(r);

            	    state._fsp--;


            	    }
            	    break;

            	default :
            	    break loop4;
                }
            } while (true);


            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }

        finally {
        	// do for sure before leaving
        }
        return ;
    }
    // $ANTLR end "body"



    // $ANTLR start "literal"
    // /home/staff/aweinz/svnrepo/gerald_weidinger_thesis/WOC - Wings of Change/antlr/woc.g:45:1: literal[Rule r] : (a1= atom | 'not' a2= atom | operation );
    public final void literal(Rule r) throws RecognitionException {
        Atom a1 =null;

        Atom a2 =null;

        Operator operation2 =null;


        try {
            // /home/staff/aweinz/svnrepo/gerald_weidinger_thesis/WOC - Wings of Change/antlr/woc.g:46:2: (a1= atom | 'not' a2= atom | operation )
            int alt5=3;
            switch ( input.LA(1) ) {
            case ID:
                {
                alt5=1;
                }
                break;
            case 33:
                {
                alt5=2;
                }
                break;
            case VAR:
                {
                alt5=3;
                }
                break;
            default:
                NoViableAltException nvae =
                    new NoViableAltException("", 5, 0, input);

                throw nvae;

            }

            switch (alt5) {
                case 1 :
                    // /home/staff/aweinz/svnrepo/gerald_weidinger_thesis/WOC - Wings of Change/antlr/woc.g:46:4: a1= atom
                    {
                    pushFollow(FOLLOW_atom_in_literal122);
                    a1=atom();

                    state._fsp--;


                    r.addAtomPlus(a1);

                    }
                    break;
                case 2 :
                    // /home/staff/aweinz/svnrepo/gerald_weidinger_thesis/WOC - Wings of Change/antlr/woc.g:47:4: 'not' a2= atom
                    {
                    match(input,33,FOLLOW_33_in_literal129); 

                    pushFollow(FOLLOW_atom_in_literal133);
                    a2=atom();

                    state._fsp--;


                    r.addAtomMinus(a2);

                    }
                    break;
                case 3 :
                    // /home/staff/aweinz/svnrepo/gerald_weidinger_thesis/WOC - Wings of Change/antlr/woc.g:48:4: operation
                    {
                    pushFollow(FOLLOW_operation_in_literal140);
                    operation2=operation();

                    state._fsp--;


                     r.addOperator(operation2); 

                    }
                    break;

            }
        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }

        finally {
        	// do for sure before leaving
        }
        return ;
    }
    // $ANTLR end "literal"



    // $ANTLR start "operation"
    // /home/staff/aweinz/svnrepo/gerald_weidinger_thesis/WOC - Wings of Change/antlr/woc.g:51:1: operation returns [Operator op] : (v1= VAR comp_sym e1= expression |v2= VAR 'is' e2= expression );
    public final Operator operation() throws RecognitionException {
        Operator op = null;


        Token v1=null;
        Token v2=null;
        OperandI e1 =null;

        OperandI e2 =null;

        OP comp_sym3 =null;


        try {
            // /home/staff/aweinz/svnrepo/gerald_weidinger_thesis/WOC - Wings of Change/antlr/woc.g:52:2: (v1= VAR comp_sym e1= expression |v2= VAR 'is' e2= expression )
            int alt6=2;
            int LA6_0 = input.LA(1);

            if ( (LA6_0==VAR) ) {
                int LA6_1 = input.LA(2);

                if ( (LA6_1==32) ) {
                    alt6=2;
                }
                else if ( (LA6_1==16||(LA6_1 >= 27 && LA6_1 <= 31)) ) {
                    alt6=1;
                }
                else {
                    NoViableAltException nvae =
                        new NoViableAltException("", 6, 1, input);

                    throw nvae;

                }
            }
            else {
                NoViableAltException nvae =
                    new NoViableAltException("", 6, 0, input);

                throw nvae;

            }
            switch (alt6) {
                case 1 :
                    // /home/staff/aweinz/svnrepo/gerald_weidinger_thesis/WOC - Wings of Change/antlr/woc.g:52:4: v1= VAR comp_sym e1= expression
                    {
                    v1=(Token)match(input,VAR,FOLLOW_VAR_in_operation159); 

                    pushFollow(FOLLOW_comp_sym_in_operation161);
                    comp_sym3=comp_sym();

                    state._fsp--;


                    pushFollow(FOLLOW_expression_in_operation165);
                    e1=expression();

                    state._fsp--;


                    op =new Operator(Variable.getVariable((v1!=null?v1.getText():null)),e1,comp_sym3);

                    }
                    break;
                case 2 :
                    // /home/staff/aweinz/svnrepo/gerald_weidinger_thesis/WOC - Wings of Change/antlr/woc.g:53:4: v2= VAR 'is' e2= expression
                    {
                    v2=(Token)match(input,VAR,FOLLOW_VAR_in_operation174); 

                    match(input,32,FOLLOW_32_in_operation176); 

                    pushFollow(FOLLOW_expression_in_operation180);
                    e2=expression();

                    state._fsp--;


                    op =new Operator(Variable.getVariable((v2!=null?v2.getText():null)),e2,OP.ASSIGN);

                    }
                    break;

            }
        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }

        finally {
        	// do for sure before leaving
        }
        return op;
    }
    // $ANTLR end "operation"



    // $ANTLR start "expression"
    // /home/staff/aweinz/svnrepo/gerald_weidinger_thesis/WOC - Wings of Change/antlr/woc.g:56:1: expression returns [OperandI op] : l= mult_expression ( ( ( '+' | '-' ) r= mult_expression ) |) ;
    public final OperandI expression() throws RecognitionException {
        OperandI op = null;


        OperandI l =null;

        OperandI r =null;


         OP openu=null; 
        try {
            // /home/staff/aweinz/svnrepo/gerald_weidinger_thesis/WOC - Wings of Change/antlr/woc.g:58:2: (l= mult_expression ( ( ( '+' | '-' ) r= mult_expression ) |) )
            // /home/staff/aweinz/svnrepo/gerald_weidinger_thesis/WOC - Wings of Change/antlr/woc.g:58:4: l= mult_expression ( ( ( '+' | '-' ) r= mult_expression ) |)
            {
            pushFollow(FOLLOW_mult_expression_in_expression203);
            l=mult_expression();

            state._fsp--;


            // /home/staff/aweinz/svnrepo/gerald_weidinger_thesis/WOC - Wings of Change/antlr/woc.g:58:22: ( ( ( '+' | '-' ) r= mult_expression ) |)
            int alt8=2;
            int LA8_0 = input.LA(1);

            if ( (LA8_0==20||LA8_0==22) ) {
                alt8=1;
            }
            else if ( (LA8_0==18||LA8_0==21||LA8_0==23) ) {
                alt8=2;
            }
            else {
                NoViableAltException nvae =
                    new NoViableAltException("", 8, 0, input);

                throw nvae;

            }
            switch (alt8) {
                case 1 :
                    // /home/staff/aweinz/svnrepo/gerald_weidinger_thesis/WOC - Wings of Change/antlr/woc.g:58:23: ( ( '+' | '-' ) r= mult_expression )
                    {
                    // /home/staff/aweinz/svnrepo/gerald_weidinger_thesis/WOC - Wings of Change/antlr/woc.g:58:23: ( ( '+' | '-' ) r= mult_expression )
                    // /home/staff/aweinz/svnrepo/gerald_weidinger_thesis/WOC - Wings of Change/antlr/woc.g:58:24: ( '+' | '-' ) r= mult_expression
                    {
                    // /home/staff/aweinz/svnrepo/gerald_weidinger_thesis/WOC - Wings of Change/antlr/woc.g:58:24: ( '+' | '-' )
                    int alt7=2;
                    int LA7_0 = input.LA(1);

                    if ( (LA7_0==20) ) {
                        alt7=1;
                    }
                    else if ( (LA7_0==22) ) {
                        alt7=2;
                    }
                    else {
                        NoViableAltException nvae =
                            new NoViableAltException("", 7, 0, input);

                        throw nvae;

                    }
                    switch (alt7) {
                        case 1 :
                            // /home/staff/aweinz/svnrepo/gerald_weidinger_thesis/WOC - Wings of Change/antlr/woc.g:58:25: '+'
                            {
                            match(input,20,FOLLOW_20_in_expression208); 

                            openu=OP.PLUS;

                            }
                            break;
                        case 2 :
                            // /home/staff/aweinz/svnrepo/gerald_weidinger_thesis/WOC - Wings of Change/antlr/woc.g:58:47: '-'
                            {
                            match(input,22,FOLLOW_22_in_expression213); 

                            openu=OP.MINUS;

                            }
                            break;

                    }


                    pushFollow(FOLLOW_mult_expression_in_expression221);
                    r=mult_expression();

                    state._fsp--;


                     op =new Operator(l,r,openu); 

                    }


                    }
                    break;
                case 2 :
                    // /home/staff/aweinz/svnrepo/gerald_weidinger_thesis/WOC - Wings of Change/antlr/woc.g:60:4: 
                    {
                    op =l;

                    }
                    break;

            }


            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }

        finally {
        	// do for sure before leaving
        }
        return op;
    }
    // $ANTLR end "expression"



    // $ANTLR start "mult_expression"
    // /home/staff/aweinz/svnrepo/gerald_weidinger_thesis/WOC - Wings of Change/antlr/woc.g:63:1: mult_expression returns [OperandI op] : l= primary_expression ( ( ( '*' | '/' ) r= primary_expression ) |) ;
    public final OperandI mult_expression() throws RecognitionException {
        OperandI op = null;


        OperandI l =null;

        OperandI r =null;


         OP openu=null; 
        try {
            // /home/staff/aweinz/svnrepo/gerald_weidinger_thesis/WOC - Wings of Change/antlr/woc.g:65:2: (l= primary_expression ( ( ( '*' | '/' ) r= primary_expression ) |) )
            // /home/staff/aweinz/svnrepo/gerald_weidinger_thesis/WOC - Wings of Change/antlr/woc.g:65:4: l= primary_expression ( ( ( '*' | '/' ) r= primary_expression ) |)
            {
            pushFollow(FOLLOW_primary_expression_in_mult_expression251);
            l=primary_expression();

            state._fsp--;


            // /home/staff/aweinz/svnrepo/gerald_weidinger_thesis/WOC - Wings of Change/antlr/woc.g:65:25: ( ( ( '*' | '/' ) r= primary_expression ) |)
            int alt10=2;
            int LA10_0 = input.LA(1);

            if ( (LA10_0==19||LA10_0==24) ) {
                alt10=1;
            }
            else if ( (LA10_0==18||(LA10_0 >= 20 && LA10_0 <= 23)) ) {
                alt10=2;
            }
            else {
                NoViableAltException nvae =
                    new NoViableAltException("", 10, 0, input);

                throw nvae;

            }
            switch (alt10) {
                case 1 :
                    // /home/staff/aweinz/svnrepo/gerald_weidinger_thesis/WOC - Wings of Change/antlr/woc.g:65:26: ( ( '*' | '/' ) r= primary_expression )
                    {
                    // /home/staff/aweinz/svnrepo/gerald_weidinger_thesis/WOC - Wings of Change/antlr/woc.g:65:26: ( ( '*' | '/' ) r= primary_expression )
                    // /home/staff/aweinz/svnrepo/gerald_weidinger_thesis/WOC - Wings of Change/antlr/woc.g:65:27: ( '*' | '/' ) r= primary_expression
                    {
                    // /home/staff/aweinz/svnrepo/gerald_weidinger_thesis/WOC - Wings of Change/antlr/woc.g:65:27: ( '*' | '/' )
                    int alt9=2;
                    int LA9_0 = input.LA(1);

                    if ( (LA9_0==19) ) {
                        alt9=1;
                    }
                    else if ( (LA9_0==24) ) {
                        alt9=2;
                    }
                    else {
                        NoViableAltException nvae =
                            new NoViableAltException("", 9, 0, input);

                        throw nvae;

                    }
                    switch (alt9) {
                        case 1 :
                            // /home/staff/aweinz/svnrepo/gerald_weidinger_thesis/WOC - Wings of Change/antlr/woc.g:65:28: '*'
                            {
                            match(input,19,FOLLOW_19_in_mult_expression256); 

                            openu=OP.TIMES; 

                            }
                            break;
                        case 2 :
                            // /home/staff/aweinz/svnrepo/gerald_weidinger_thesis/WOC - Wings of Change/antlr/woc.g:65:52: '/'
                            {
                            match(input,24,FOLLOW_24_in_mult_expression261); 

                            openu=OP.DIVIDE;

                            }
                            break;

                    }


                    pushFollow(FOLLOW_primary_expression_in_mult_expression271);
                    r=primary_expression();

                    state._fsp--;


                     op =new Operator(l,r,openu); 

                    }


                    }
                    break;
                case 2 :
                    // /home/staff/aweinz/svnrepo/gerald_weidinger_thesis/WOC - Wings of Change/antlr/woc.g:67:4: 
                    {
                    op =l;

                    }
                    break;

            }


            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }

        finally {
        	// do for sure before leaving
        }
        return op;
    }
    // $ANTLR end "mult_expression"



    // $ANTLR start "primary_expression"
    // /home/staff/aweinz/svnrepo/gerald_weidinger_thesis/WOC - Wings of Change/antlr/woc.g:70:1: primary_expression returns [OperandI op] : ( INT | VAR | '(' expression ')' );
    public final OperandI primary_expression() throws RecognitionException {
        OperandI op = null;


        Token INT4=null;
        Token VAR5=null;
        OperandI expression6 =null;


        try {
            // /home/staff/aweinz/svnrepo/gerald_weidinger_thesis/WOC - Wings of Change/antlr/woc.g:71:2: ( INT | VAR | '(' expression ')' )
            int alt11=3;
            switch ( input.LA(1) ) {
            case INT:
                {
                alt11=1;
                }
                break;
            case VAR:
                {
                alt11=2;
                }
                break;
            case 17:
                {
                alt11=3;
                }
                break;
            default:
                NoViableAltException nvae =
                    new NoViableAltException("", 11, 0, input);

                throw nvae;

            }

            switch (alt11) {
                case 1 :
                    // /home/staff/aweinz/svnrepo/gerald_weidinger_thesis/WOC - Wings of Change/antlr/woc.g:71:4: INT
                    {
                    INT4=(Token)match(input,INT,FOLLOW_INT_in_primary_expression295); 

                    op =Constant.getConstant((INT4!=null?INT4.getText():null));

                    }
                    break;
                case 2 :
                    // /home/staff/aweinz/svnrepo/gerald_weidinger_thesis/WOC - Wings of Change/antlr/woc.g:72:4: VAR
                    {
                    VAR5=(Token)match(input,VAR,FOLLOW_VAR_in_primary_expression302); 

                    op =Variable.getVariable((VAR5!=null?VAR5.getText():null));

                    }
                    break;
                case 3 :
                    // /home/staff/aweinz/svnrepo/gerald_weidinger_thesis/WOC - Wings of Change/antlr/woc.g:73:4: '(' expression ')'
                    {
                    match(input,17,FOLLOW_17_in_primary_expression309); 

                    pushFollow(FOLLOW_expression_in_primary_expression311);
                    expression6=expression();

                    state._fsp--;


                    match(input,18,FOLLOW_18_in_primary_expression313); 

                    op =expression6;

                    }
                    break;

            }
        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }

        finally {
        	// do for sure before leaving
        }
        return op;
    }
    // $ANTLR end "primary_expression"



    // $ANTLR start "atom"
    // /home/staff/aweinz/svnrepo/gerald_weidinger_thesis/WOC - Wings of Change/antlr/woc.g:76:1: atom returns [Atom at] : (ctx= ID ':' )? name= ID ( '(' termlist ')' )? ;
    public final Atom atom() throws RecognitionException {
        Atom at = null;


        Token ctx=null;
        Token name=null;
        ArrayList<Term> termlist7 =null;


         String context_id=null; String atom_name; Term[] terms = new Term[0];
        try {
            // /home/staff/aweinz/svnrepo/gerald_weidinger_thesis/WOC - Wings of Change/antlr/woc.g:78:2: ( (ctx= ID ':' )? name= ID ( '(' termlist ')' )? )
            // /home/staff/aweinz/svnrepo/gerald_weidinger_thesis/WOC - Wings of Change/antlr/woc.g:78:4: (ctx= ID ':' )? name= ID ( '(' termlist ')' )?
            {
            // /home/staff/aweinz/svnrepo/gerald_weidinger_thesis/WOC - Wings of Change/antlr/woc.g:78:4: (ctx= ID ':' )?
            int alt12=2;
            int LA12_0 = input.LA(1);

            if ( (LA12_0==ID) ) {
                int LA12_1 = input.LA(2);

                if ( (LA12_1==25) ) {
                    alt12=1;
                }
            }
            switch (alt12) {
                case 1 :
                    // /home/staff/aweinz/svnrepo/gerald_weidinger_thesis/WOC - Wings of Change/antlr/woc.g:78:5: ctx= ID ':'
                    {
                    ctx=(Token)match(input,ID,FOLLOW_ID_in_atom337); 

                    match(input,25,FOLLOW_25_in_atom339); 

                    context_id=(ctx!=null?ctx.getText():null);

                    }
                    break;

            }


            name=(Token)match(input,ID,FOLLOW_ID_in_atom350); 

            if(context_id==null) { atom_name=(name!=null?name.getText():null); } else {atom_name=context_id+":"+(name!=null?name.getText():null);} 

            // /home/staff/aweinz/svnrepo/gerald_weidinger_thesis/WOC - Wings of Change/antlr/woc.g:80:3: ( '(' termlist ')' )?
            int alt13=2;
            int LA13_0 = input.LA(1);

            if ( (LA13_0==17) ) {
                alt13=1;
            }
            switch (alt13) {
                case 1 :
                    // /home/staff/aweinz/svnrepo/gerald_weidinger_thesis/WOC - Wings of Change/antlr/woc.g:80:5: '(' termlist ')'
                    {
                    match(input,17,FOLLOW_17_in_atom358); 

                    pushFollow(FOLLOW_termlist_in_atom360);
                    termlist7=termlist();

                    state._fsp--;


                    match(input,18,FOLLOW_18_in_atom362); 

                    terms=(Term[])termlist7.toArray(new Term[termlist7.size()]);

                    }
                    break;

            }


            at =Atom.getAtom(atom_name, terms.length, terms); if(context_id !=null) { at.getPredicate().setNodeId(context_id); }

            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }

        finally {
        	// do for sure before leaving
        }
        return at;
    }
    // $ANTLR end "atom"



    // $ANTLR start "termlist"
    // /home/staff/aweinz/svnrepo/gerald_weidinger_thesis/WOC - Wings of Change/antlr/woc.g:84:1: termlist returns [ArrayList<Term> termlist] : t1= term ( ',' tn= term )* ;
    public final ArrayList<Term> termlist() throws RecognitionException {
        ArrayList<Term> termlist = null;


        Term t1 =null;

        Term tn =null;


         termlist = new ArrayList<Term>();
        try {
            // /home/staff/aweinz/svnrepo/gerald_weidinger_thesis/WOC - Wings of Change/antlr/woc.g:86:2: (t1= term ( ',' tn= term )* )
            // /home/staff/aweinz/svnrepo/gerald_weidinger_thesis/WOC - Wings of Change/antlr/woc.g:86:4: t1= term ( ',' tn= term )*
            {
            pushFollow(FOLLOW_term_in_termlist391);
            t1=term();

            state._fsp--;


            termlist.add(t1);

            // /home/staff/aweinz/svnrepo/gerald_weidinger_thesis/WOC - Wings of Change/antlr/woc.g:87:3: ( ',' tn= term )*
            loop14:
            do {
                int alt14=2;
                int LA14_0 = input.LA(1);

                if ( (LA14_0==21) ) {
                    alt14=1;
                }


                switch (alt14) {
            	case 1 :
            	    // /home/staff/aweinz/svnrepo/gerald_weidinger_thesis/WOC - Wings of Change/antlr/woc.g:87:4: ',' tn= term
            	    {
            	    match(input,21,FOLLOW_21_in_termlist398); 

            	    pushFollow(FOLLOW_term_in_termlist402);
            	    tn=term();

            	    state._fsp--;


            	    termlist.add(tn);

            	    }
            	    break;

            	default :
            	    break loop14;
                }
            } while (true);


            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }

        finally {
        	// do for sure before leaving
        }
        return termlist;
    }
    // $ANTLR end "termlist"



    // $ANTLR start "term"
    // /home/staff/aweinz/svnrepo/gerald_weidinger_thesis/WOC - Wings of Change/antlr/woc.g:90:1: term returns [Term term] : ( constant | VAR | function );
    public final Term term() throws RecognitionException {
        Term term = null;


        Token VAR9=null;
        Constant constant8 =null;

        wocParser.function_return function10 =null;


        try {
            // /home/staff/aweinz/svnrepo/gerald_weidinger_thesis/WOC - Wings of Change/antlr/woc.g:91:2: ( constant | VAR | function )
            int alt15=3;
            switch ( input.LA(1) ) {
            case ID:
                {
                int LA15_1 = input.LA(2);

                if ( (LA15_1==17) ) {
                    alt15=3;
                }
                else if ( (LA15_1==18||LA15_1==21) ) {
                    alt15=1;
                }
                else {
                    NoViableAltException nvae =
                        new NoViableAltException("", 15, 1, input);

                    throw nvae;

                }
                }
                break;
            case INT:
            case 22:
                {
                alt15=1;
                }
                break;
            case VAR:
                {
                alt15=2;
                }
                break;
            default:
                NoViableAltException nvae =
                    new NoViableAltException("", 15, 0, input);

                throw nvae;

            }

            switch (alt15) {
                case 1 :
                    // /home/staff/aweinz/svnrepo/gerald_weidinger_thesis/WOC - Wings of Change/antlr/woc.g:91:4: constant
                    {
                    pushFollow(FOLLOW_constant_in_term421);
                    constant8=constant();

                    state._fsp--;


                    term =constant8;

                    }
                    break;
                case 2 :
                    // /home/staff/aweinz/svnrepo/gerald_weidinger_thesis/WOC - Wings of Change/antlr/woc.g:92:4: VAR
                    {
                    VAR9=(Token)match(input,VAR,FOLLOW_VAR_in_term428); 

                    term =Variable.getVariable((VAR9!=null?VAR9.getText():null));

                    }
                    break;
                case 3 :
                    // /home/staff/aweinz/svnrepo/gerald_weidinger_thesis/WOC - Wings of Change/antlr/woc.g:93:4: function
                    {
                    pushFollow(FOLLOW_function_in_term435);
                    function10=function();

                    state._fsp--;


                    term =FuncTerm.getFuncTerm((function10!=null?function10.name:null),(function10!=null?function10.terms:null));

                    }
                    break;

            }
        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }

        finally {
        	// do for sure before leaving
        }
        return term;
    }
    // $ANTLR end "term"


    public static class function_return extends ParserRuleReturnScope {
        public String name;
        public ArrayList<Term> terms;
    };


    // $ANTLR start "function"
    // /home/staff/aweinz/svnrepo/gerald_weidinger_thesis/WOC - Wings of Change/antlr/woc.g:96:1: function returns [String name, ArrayList<Term> terms] : ID '(' termlist ')' ;
    public final wocParser.function_return function() throws RecognitionException {
        wocParser.function_return retval = new wocParser.function_return();
        retval.start = input.LT(1);


        Token ID11=null;
        ArrayList<Term> termlist12 =null;


        try {
            // /home/staff/aweinz/svnrepo/gerald_weidinger_thesis/WOC - Wings of Change/antlr/woc.g:97:2: ( ID '(' termlist ')' )
            // /home/staff/aweinz/svnrepo/gerald_weidinger_thesis/WOC - Wings of Change/antlr/woc.g:97:4: ID '(' termlist ')'
            {
            ID11=(Token)match(input,ID,FOLLOW_ID_in_function452); 

            match(input,17,FOLLOW_17_in_function454); 

            pushFollow(FOLLOW_termlist_in_function456);
            termlist12=termlist();

            state._fsp--;


            match(input,18,FOLLOW_18_in_function458); 

            retval.name =(ID11!=null?ID11.getText():null); retval.terms =termlist12;

            }

            retval.stop = input.LT(-1);


        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }

        finally {
        	// do for sure before leaving
        }
        return retval;
    }
    // $ANTLR end "function"



    // $ANTLR start "constant"
    // /home/staff/aweinz/svnrepo/gerald_weidinger_thesis/WOC - Wings of Change/antlr/woc.g:100:1: constant returns [Constant constnt] : ( ID | ( '-' )? INT );
    public final Constant constant() throws RecognitionException {
        Constant constnt = null;


        Token ID13=null;
        Token INT14=null;

        try {
            // /home/staff/aweinz/svnrepo/gerald_weidinger_thesis/WOC - Wings of Change/antlr/woc.g:101:2: ( ID | ( '-' )? INT )
            int alt17=2;
            int LA17_0 = input.LA(1);

            if ( (LA17_0==ID) ) {
                alt17=1;
            }
            else if ( (LA17_0==INT||LA17_0==22) ) {
                alt17=2;
            }
            else {
                NoViableAltException nvae =
                    new NoViableAltException("", 17, 0, input);

                throw nvae;

            }
            switch (alt17) {
                case 1 :
                    // /home/staff/aweinz/svnrepo/gerald_weidinger_thesis/WOC - Wings of Change/antlr/woc.g:101:4: ID
                    {
                    ID13=(Token)match(input,ID,FOLLOW_ID_in_constant475); 

                    constnt =Constant.getConstant((ID13!=null?ID13.getText():null));

                    }
                    break;
                case 2 :
                    // /home/staff/aweinz/svnrepo/gerald_weidinger_thesis/WOC - Wings of Change/antlr/woc.g:102:4: ( '-' )? INT
                    {
                    // /home/staff/aweinz/svnrepo/gerald_weidinger_thesis/WOC - Wings of Change/antlr/woc.g:102:4: ( '-' )?
                    int alt16=2;
                    int LA16_0 = input.LA(1);

                    if ( (LA16_0==22) ) {
                        alt16=1;
                    }
                    switch (alt16) {
                        case 1 :
                            // /home/staff/aweinz/svnrepo/gerald_weidinger_thesis/WOC - Wings of Change/antlr/woc.g:102:5: '-'
                            {
                            match(input,22,FOLLOW_22_in_constant483); 

                            }
                            break;

                    }


                    INT14=(Token)match(input,INT,FOLLOW_INT_in_constant487); 

                    constnt =Constant.getConstant((INT14!=null?INT14.getText():null));

                    }
                    break;

            }
        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }

        finally {
        	// do for sure before leaving
        }
        return constnt;
    }
    // $ANTLR end "constant"



    // $ANTLR start "comp_sym"
    // /home/staff/aweinz/svnrepo/gerald_weidinger_thesis/WOC - Wings of Change/antlr/woc.g:105:1: comp_sym returns [OP openu] : ( '<' | '<=' | '>=' | '>' | '=' | '!=' );
    public final OP comp_sym() throws RecognitionException {
        OP openu = null;


        try {
            // /home/staff/aweinz/svnrepo/gerald_weidinger_thesis/WOC - Wings of Change/antlr/woc.g:106:2: ( '<' | '<=' | '>=' | '>' | '=' | '!=' )
            int alt18=6;
            switch ( input.LA(1) ) {
            case 27:
                {
                alt18=1;
                }
                break;
            case 28:
                {
                alt18=2;
                }
                break;
            case 31:
                {
                alt18=3;
                }
                break;
            case 30:
                {
                alt18=4;
                }
                break;
            case 29:
                {
                alt18=5;
                }
                break;
            case 16:
                {
                alt18=6;
                }
                break;
            default:
                NoViableAltException nvae =
                    new NoViableAltException("", 18, 0, input);

                throw nvae;

            }

            switch (alt18) {
                case 1 :
                    // /home/staff/aweinz/svnrepo/gerald_weidinger_thesis/WOC - Wings of Change/antlr/woc.g:106:4: '<'
                    {
                    match(input,27,FOLLOW_27_in_comp_sym503); 

                    openu =OP.LESS;

                    }
                    break;
                case 2 :
                    // /home/staff/aweinz/svnrepo/gerald_weidinger_thesis/WOC - Wings of Change/antlr/woc.g:107:4: '<='
                    {
                    match(input,28,FOLLOW_28_in_comp_sym510); 

                    openu =OP.LESS_EQ;

                    }
                    break;
                case 3 :
                    // /home/staff/aweinz/svnrepo/gerald_weidinger_thesis/WOC - Wings of Change/antlr/woc.g:108:4: '>='
                    {
                    match(input,31,FOLLOW_31_in_comp_sym517); 

                    openu =OP.GREATER_EQ;

                    }
                    break;
                case 4 :
                    // /home/staff/aweinz/svnrepo/gerald_weidinger_thesis/WOC - Wings of Change/antlr/woc.g:109:4: '>'
                    {
                    match(input,30,FOLLOW_30_in_comp_sym524); 

                    openu =OP.GREATER;

                    }
                    break;
                case 5 :
                    // /home/staff/aweinz/svnrepo/gerald_weidinger_thesis/WOC - Wings of Change/antlr/woc.g:110:4: '='
                    {
                    match(input,29,FOLLOW_29_in_comp_sym531); 

                    openu =OP.EQUAL;

                    }
                    break;
                case 6 :
                    // /home/staff/aweinz/svnrepo/gerald_weidinger_thesis/WOC - Wings of Change/antlr/woc.g:111:4: '!='
                    {
                    match(input,16,FOLLOW_16_in_comp_sym538); 

                    openu =OP.NOTEQUAL;

                    }
                    break;

            }
        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }

        finally {
        	// do for sure before leaving
        }
        return openu;
    }
    // $ANTLR end "comp_sym"

    // Delegated rules


 

    public static final BitSet FOLLOW_rule_or_fact_in_woc_program36 = new BitSet(new long[]{0x0000000004800202L});
    public static final BitSet FOLLOW_atom_in_rule_or_fact60 = new BitSet(new long[]{0x0000000004800000L});
    public static final BitSet FOLLOW_26_in_rule_or_fact68 = new BitSet(new long[]{0x0000000200004200L});
    public static final BitSet FOLLOW_body_in_rule_or_fact72 = new BitSet(new long[]{0x0000000000800000L});
    public static final BitSet FOLLOW_23_in_rule_or_fact75 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_23_in_rule_or_fact82 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_literal_in_body98 = new BitSet(new long[]{0x0000000000200002L});
    public static final BitSet FOLLOW_21_in_body102 = new BitSet(new long[]{0x0000000200004200L});
    public static final BitSet FOLLOW_literal_in_body104 = new BitSet(new long[]{0x0000000000200002L});
    public static final BitSet FOLLOW_atom_in_literal122 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_33_in_literal129 = new BitSet(new long[]{0x0000000000000200L});
    public static final BitSet FOLLOW_atom_in_literal133 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_operation_in_literal140 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_VAR_in_operation159 = new BitSet(new long[]{0x00000000F8010000L});
    public static final BitSet FOLLOW_comp_sym_in_operation161 = new BitSet(new long[]{0x0000000000024400L});
    public static final BitSet FOLLOW_expression_in_operation165 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_VAR_in_operation174 = new BitSet(new long[]{0x0000000100000000L});
    public static final BitSet FOLLOW_32_in_operation176 = new BitSet(new long[]{0x0000000000024400L});
    public static final BitSet FOLLOW_expression_in_operation180 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_mult_expression_in_expression203 = new BitSet(new long[]{0x0000000000500002L});
    public static final BitSet FOLLOW_20_in_expression208 = new BitSet(new long[]{0x0000000000024400L});
    public static final BitSet FOLLOW_22_in_expression213 = new BitSet(new long[]{0x0000000000024400L});
    public static final BitSet FOLLOW_mult_expression_in_expression221 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_primary_expression_in_mult_expression251 = new BitSet(new long[]{0x0000000001080002L});
    public static final BitSet FOLLOW_19_in_mult_expression256 = new BitSet(new long[]{0x0000000000024400L});
    public static final BitSet FOLLOW_24_in_mult_expression261 = new BitSet(new long[]{0x0000000000024400L});
    public static final BitSet FOLLOW_primary_expression_in_mult_expression271 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_INT_in_primary_expression295 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_VAR_in_primary_expression302 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_17_in_primary_expression309 = new BitSet(new long[]{0x0000000000024400L});
    public static final BitSet FOLLOW_expression_in_primary_expression311 = new BitSet(new long[]{0x0000000000040000L});
    public static final BitSet FOLLOW_18_in_primary_expression313 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_ID_in_atom337 = new BitSet(new long[]{0x0000000002000000L});
    public static final BitSet FOLLOW_25_in_atom339 = new BitSet(new long[]{0x0000000000000200L});
    public static final BitSet FOLLOW_ID_in_atom350 = new BitSet(new long[]{0x0000000000020002L});
    public static final BitSet FOLLOW_17_in_atom358 = new BitSet(new long[]{0x0000000000404600L});
    public static final BitSet FOLLOW_termlist_in_atom360 = new BitSet(new long[]{0x0000000000040000L});
    public static final BitSet FOLLOW_18_in_atom362 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_term_in_termlist391 = new BitSet(new long[]{0x0000000000200002L});
    public static final BitSet FOLLOW_21_in_termlist398 = new BitSet(new long[]{0x0000000000404600L});
    public static final BitSet FOLLOW_term_in_termlist402 = new BitSet(new long[]{0x0000000000200002L});
    public static final BitSet FOLLOW_constant_in_term421 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_VAR_in_term428 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_function_in_term435 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_ID_in_function452 = new BitSet(new long[]{0x0000000000020000L});
    public static final BitSet FOLLOW_17_in_function454 = new BitSet(new long[]{0x0000000000404600L});
    public static final BitSet FOLLOW_termlist_in_function456 = new BitSet(new long[]{0x0000000000040000L});
    public static final BitSet FOLLOW_18_in_function458 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_ID_in_constant475 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_22_in_constant483 = new BitSet(new long[]{0x0000000000000400L});
    public static final BitSet FOLLOW_INT_in_constant487 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_27_in_comp_sym503 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_28_in_comp_sym510 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_31_in_comp_sym517 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_30_in_comp_sym524 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_29_in_comp_sym531 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_16_in_comp_sym538 = new BitSet(new long[]{0x0000000000000002L});

}