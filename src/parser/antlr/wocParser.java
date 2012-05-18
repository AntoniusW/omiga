// $ANTLR 3.4 /home/staff/aweinz/svnrepo/gerald_weidinger_thesis/WOC - Wings of Change/antlr/woc.g 2012-05-18 14:02:54

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


    private ContextASPMCSRewriting context;
    int rule_or_fact_counter;

    public void setContext(ContextASPMCSRewriting context) {
    	this.context=context;
    	rule_or_fact_counter=0;
    }




    // $ANTLR start "woc_program"
    // /home/staff/aweinz/svnrepo/gerald_weidinger_thesis/WOC - Wings of Change/antlr/woc.g:31:1: woc_program : ( rule_or_fact )* ;
    public final void woc_program() throws RuleNotSafeException, RecognitionException, FactSizeException {
        try {
            // /home/staff/aweinz/svnrepo/gerald_weidinger_thesis/WOC - Wings of Change/antlr/woc.g:32:2: ( ( rule_or_fact )* )
            // /home/staff/aweinz/svnrepo/gerald_weidinger_thesis/WOC - Wings of Change/antlr/woc.g:32:4: ( rule_or_fact )*
            {
            // /home/staff/aweinz/svnrepo/gerald_weidinger_thesis/WOC - Wings of Change/antlr/woc.g:32:4: ( rule_or_fact )*
            loop1:
            do {
                int alt1=2;
                int LA1_0 = input.LA(1);

                if ( (LA1_0==ID||LA1_0==23||LA1_0==26) ) {
                    alt1=1;
                }


                switch (alt1) {
            	case 1 :
            	    // /home/staff/aweinz/svnrepo/gerald_weidinger_thesis/WOC - Wings of Change/antlr/woc.g:32:4: rule_or_fact
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
    // /home/staff/aweinz/svnrepo/gerald_weidinger_thesis/WOC - Wings of Change/antlr/woc.g:35:1: rule_or_fact : ( fact )? ( ':-' body[r] '.' | '.' ) ;
    public final void rule_or_fact() throws RuleNotSafeException, RecognitionException, FactSizeException {
        wocParser.fact_return fact1 =null;


         boolean hasHead = false; 
        try {
            // /home/staff/aweinz/svnrepo/gerald_weidinger_thesis/WOC - Wings of Change/antlr/woc.g:37:2: ( ( fact )? ( ':-' body[r] '.' | '.' ) )
            // /home/staff/aweinz/svnrepo/gerald_weidinger_thesis/WOC - Wings of Change/antlr/woc.g:37:4: ( fact )? ( ':-' body[r] '.' | '.' )
            {
            // /home/staff/aweinz/svnrepo/gerald_weidinger_thesis/WOC - Wings of Change/antlr/woc.g:37:4: ( fact )?
            int alt2=2;
            int LA2_0 = input.LA(1);

            if ( (LA2_0==ID) ) {
                alt2=1;
            }
            switch (alt2) {
                case 1 :
                    // /home/staff/aweinz/svnrepo/gerald_weidinger_thesis/WOC - Wings of Change/antlr/woc.g:37:4: fact
                    {
                    pushFollow(FOLLOW_fact_in_rule_or_fact59);
                    fact1=fact();

                    state._fsp--;


                    }
                    break;

            }


            hasHead=true;

            // /home/staff/aweinz/svnrepo/gerald_weidinger_thesis/WOC - Wings of Change/antlr/woc.g:38:3: ( ':-' body[r] '.' | '.' )
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
                    // /home/staff/aweinz/svnrepo/gerald_weidinger_thesis/WOC - Wings of Change/antlr/woc.g:38:4: ':-' body[r] '.'
                    {
                    match(input,26,FOLLOW_26_in_rule_or_fact67); 

                    Rule r = new Rule();

                    pushFollow(FOLLOW_body_in_rule_or_fact71);
                    body(r);

                    state._fsp--;


                    match(input,23,FOLLOW_23_in_rule_or_fact74); 

                    if(hasHead ) {
                    				Term[] termarr=(Term[])(fact1!=null?fact1.terms:null).toArray(new Term[(fact1!=null?fact1.terms:null).size()]);
                    				Atom head=Atom.getAtom((fact1!=null?fact1.name:null),(fact1!=null?fact1.terms:null).size(),termarr);
                    				r.setHead(head); }
                    				context.addRule(r);

                    }
                    break;
                case 2 :
                    // /home/staff/aweinz/svnrepo/gerald_weidinger_thesis/WOC - Wings of Change/antlr/woc.g:43:4: '.'
                    {
                    match(input,23,FOLLOW_23_in_rule_or_fact81); 

                    	// TODO: throw better exception if there is no head, currently throws NullPointerException
                    			context.addFact2IN(Predicate.getPredicate((fact1!=null?fact1.name:null),(fact1!=null?fact1.terms:null).size()),
                    				Instance.getInstance((fact1!=null?fact1.terms:null).toArray(new Term[(fact1!=null?fact1.terms:null).size()])));

                    }
                    break;

            }


             //System.out.println("Parsing rule_or_fact No.: "+rule_or_fact_counter++);
            	

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


    public static class fact_return extends ParserRuleReturnScope {
        public String name;
        public ArrayList<Term> terms;
    };


    // $ANTLR start "fact"
    // /home/staff/aweinz/svnrepo/gerald_weidinger_thesis/WOC - Wings of Change/antlr/woc.g:50:1: fact returns [String name, ArrayList<Term> terms] : ID ( '(' termlist ')' )? ;
    public final wocParser.fact_return fact() throws RecognitionException {
        wocParser.fact_return retval = new wocParser.fact_return();
        retval.start = input.LT(1);


        Token ID2=null;
        ArrayList<Term> termlist3 =null;


        try {
            // /home/staff/aweinz/svnrepo/gerald_weidinger_thesis/WOC - Wings of Change/antlr/woc.g:51:2: ( ID ( '(' termlist ')' )? )
            // /home/staff/aweinz/svnrepo/gerald_weidinger_thesis/WOC - Wings of Change/antlr/woc.g:52:3: ID ( '(' termlist ')' )?
            {
            ID2=(Token)match(input,ID,FOLLOW_ID_in_fact106); 

            retval.name =(ID2!=null?ID2.getText():null); 

            // /home/staff/aweinz/svnrepo/gerald_weidinger_thesis/WOC - Wings of Change/antlr/woc.g:53:3: ( '(' termlist ')' )?
            int alt4=2;
            int LA4_0 = input.LA(1);

            if ( (LA4_0==17) ) {
                alt4=1;
            }
            switch (alt4) {
                case 1 :
                    // /home/staff/aweinz/svnrepo/gerald_weidinger_thesis/WOC - Wings of Change/antlr/woc.g:53:5: '(' termlist ')'
                    {
                    match(input,17,FOLLOW_17_in_fact114); 

                    pushFollow(FOLLOW_termlist_in_fact116);
                    termlist3=termlist();

                    state._fsp--;


                    match(input,18,FOLLOW_18_in_fact118); 

                    retval.terms =termlist3;

                    }
                    break;

            }


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
    // $ANTLR end "fact"



    // $ANTLR start "body"
    // /home/staff/aweinz/svnrepo/gerald_weidinger_thesis/WOC - Wings of Change/antlr/woc.g:56:1: body[Rule r] : literal[$r] ( ',' literal[$r] )* ;
    public final void body(Rule r) throws RecognitionException {
        try {
            // /home/staff/aweinz/svnrepo/gerald_weidinger_thesis/WOC - Wings of Change/antlr/woc.g:57:2: ( literal[$r] ( ',' literal[$r] )* )
            // /home/staff/aweinz/svnrepo/gerald_weidinger_thesis/WOC - Wings of Change/antlr/woc.g:57:4: literal[$r] ( ',' literal[$r] )*
            {
            pushFollow(FOLLOW_literal_in_body135);
            literal(r);

            state._fsp--;


            // /home/staff/aweinz/svnrepo/gerald_weidinger_thesis/WOC - Wings of Change/antlr/woc.g:57:16: ( ',' literal[$r] )*
            loop5:
            do {
                int alt5=2;
                int LA5_0 = input.LA(1);

                if ( (LA5_0==21) ) {
                    alt5=1;
                }


                switch (alt5) {
            	case 1 :
            	    // /home/staff/aweinz/svnrepo/gerald_weidinger_thesis/WOC - Wings of Change/antlr/woc.g:57:17: ',' literal[$r]
            	    {
            	    match(input,21,FOLLOW_21_in_body139); 

            	    pushFollow(FOLLOW_literal_in_body141);
            	    literal(r);

            	    state._fsp--;


            	    }
            	    break;

            	default :
            	    break loop5;
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
    // /home/staff/aweinz/svnrepo/gerald_weidinger_thesis/WOC - Wings of Change/antlr/woc.g:60:1: literal[Rule r] : (a1= atom | 'not' a2= atom | operation );
    public final void literal(Rule r) throws RecognitionException {
        Atom a1 =null;

        Atom a2 =null;

        Operator operation4 =null;


        try {
            // /home/staff/aweinz/svnrepo/gerald_weidinger_thesis/WOC - Wings of Change/antlr/woc.g:61:2: (a1= atom | 'not' a2= atom | operation )
            int alt6=3;
            switch ( input.LA(1) ) {
            case ID:
                {
                alt6=1;
                }
                break;
            case 33:
                {
                alt6=2;
                }
                break;
            case VAR:
                {
                alt6=3;
                }
                break;
            default:
                NoViableAltException nvae =
                    new NoViableAltException("", 6, 0, input);

                throw nvae;

            }

            switch (alt6) {
                case 1 :
                    // /home/staff/aweinz/svnrepo/gerald_weidinger_thesis/WOC - Wings of Change/antlr/woc.g:61:4: a1= atom
                    {
                    pushFollow(FOLLOW_atom_in_literal159);
                    a1=atom();

                    state._fsp--;


                    r.addAtomPlus(a1);

                    }
                    break;
                case 2 :
                    // /home/staff/aweinz/svnrepo/gerald_weidinger_thesis/WOC - Wings of Change/antlr/woc.g:62:4: 'not' a2= atom
                    {
                    match(input,33,FOLLOW_33_in_literal166); 

                    pushFollow(FOLLOW_atom_in_literal170);
                    a2=atom();

                    state._fsp--;


                    r.addAtomMinus(a2);

                    }
                    break;
                case 3 :
                    // /home/staff/aweinz/svnrepo/gerald_weidinger_thesis/WOC - Wings of Change/antlr/woc.g:63:4: operation
                    {
                    pushFollow(FOLLOW_operation_in_literal177);
                    operation4=operation();

                    state._fsp--;


                     r.addOperator(operation4); 

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
    // /home/staff/aweinz/svnrepo/gerald_weidinger_thesis/WOC - Wings of Change/antlr/woc.g:66:1: operation returns [Operator op] : (v1= VAR comp_sym e1= expression |v2= VAR 'is' e2= expression );
    public final Operator operation() throws RecognitionException {
        Operator op = null;


        Token v1=null;
        Token v2=null;
        OperandI e1 =null;

        OperandI e2 =null;

        OP comp_sym5 =null;


        try {
            // /home/staff/aweinz/svnrepo/gerald_weidinger_thesis/WOC - Wings of Change/antlr/woc.g:67:2: (v1= VAR comp_sym e1= expression |v2= VAR 'is' e2= expression )
            int alt7=2;
            int LA7_0 = input.LA(1);

            if ( (LA7_0==VAR) ) {
                int LA7_1 = input.LA(2);

                if ( (LA7_1==32) ) {
                    alt7=2;
                }
                else if ( (LA7_1==16||(LA7_1 >= 27 && LA7_1 <= 31)) ) {
                    alt7=1;
                }
                else {
                    NoViableAltException nvae =
                        new NoViableAltException("", 7, 1, input);

                    throw nvae;

                }
            }
            else {
                NoViableAltException nvae =
                    new NoViableAltException("", 7, 0, input);

                throw nvae;

            }
            switch (alt7) {
                case 1 :
                    // /home/staff/aweinz/svnrepo/gerald_weidinger_thesis/WOC - Wings of Change/antlr/woc.g:67:4: v1= VAR comp_sym e1= expression
                    {
                    v1=(Token)match(input,VAR,FOLLOW_VAR_in_operation196); 

                    pushFollow(FOLLOW_comp_sym_in_operation198);
                    comp_sym5=comp_sym();

                    state._fsp--;


                    pushFollow(FOLLOW_expression_in_operation202);
                    e1=expression();

                    state._fsp--;


                    op =new Operator(Variable.getVariable((v1!=null?v1.getText():null)),e1,comp_sym5);

                    }
                    break;
                case 2 :
                    // /home/staff/aweinz/svnrepo/gerald_weidinger_thesis/WOC - Wings of Change/antlr/woc.g:68:4: v2= VAR 'is' e2= expression
                    {
                    v2=(Token)match(input,VAR,FOLLOW_VAR_in_operation211); 

                    match(input,32,FOLLOW_32_in_operation213); 

                    pushFollow(FOLLOW_expression_in_operation217);
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
    // /home/staff/aweinz/svnrepo/gerald_weidinger_thesis/WOC - Wings of Change/antlr/woc.g:71:1: expression returns [OperandI op] : l= mult_expression ( ( ( '+' | '-' ) r= mult_expression ) |) ;
    public final OperandI expression() throws RecognitionException {
        OperandI op = null;


        OperandI l =null;

        OperandI r =null;


         OP openu=null; 
        try {
            // /home/staff/aweinz/svnrepo/gerald_weidinger_thesis/WOC - Wings of Change/antlr/woc.g:73:2: (l= mult_expression ( ( ( '+' | '-' ) r= mult_expression ) |) )
            // /home/staff/aweinz/svnrepo/gerald_weidinger_thesis/WOC - Wings of Change/antlr/woc.g:73:4: l= mult_expression ( ( ( '+' | '-' ) r= mult_expression ) |)
            {
            pushFollow(FOLLOW_mult_expression_in_expression240);
            l=mult_expression();

            state._fsp--;


            // /home/staff/aweinz/svnrepo/gerald_weidinger_thesis/WOC - Wings of Change/antlr/woc.g:73:22: ( ( ( '+' | '-' ) r= mult_expression ) |)
            int alt9=2;
            int LA9_0 = input.LA(1);

            if ( (LA9_0==20||LA9_0==22) ) {
                alt9=1;
            }
            else if ( (LA9_0==18||LA9_0==21||LA9_0==23) ) {
                alt9=2;
            }
            else {
                NoViableAltException nvae =
                    new NoViableAltException("", 9, 0, input);

                throw nvae;

            }
            switch (alt9) {
                case 1 :
                    // /home/staff/aweinz/svnrepo/gerald_weidinger_thesis/WOC - Wings of Change/antlr/woc.g:73:23: ( ( '+' | '-' ) r= mult_expression )
                    {
                    // /home/staff/aweinz/svnrepo/gerald_weidinger_thesis/WOC - Wings of Change/antlr/woc.g:73:23: ( ( '+' | '-' ) r= mult_expression )
                    // /home/staff/aweinz/svnrepo/gerald_weidinger_thesis/WOC - Wings of Change/antlr/woc.g:73:24: ( '+' | '-' ) r= mult_expression
                    {
                    // /home/staff/aweinz/svnrepo/gerald_weidinger_thesis/WOC - Wings of Change/antlr/woc.g:73:24: ( '+' | '-' )
                    int alt8=2;
                    int LA8_0 = input.LA(1);

                    if ( (LA8_0==20) ) {
                        alt8=1;
                    }
                    else if ( (LA8_0==22) ) {
                        alt8=2;
                    }
                    else {
                        NoViableAltException nvae =
                            new NoViableAltException("", 8, 0, input);

                        throw nvae;

                    }
                    switch (alt8) {
                        case 1 :
                            // /home/staff/aweinz/svnrepo/gerald_weidinger_thesis/WOC - Wings of Change/antlr/woc.g:73:25: '+'
                            {
                            match(input,20,FOLLOW_20_in_expression245); 

                            openu=OP.PLUS;

                            }
                            break;
                        case 2 :
                            // /home/staff/aweinz/svnrepo/gerald_weidinger_thesis/WOC - Wings of Change/antlr/woc.g:73:47: '-'
                            {
                            match(input,22,FOLLOW_22_in_expression250); 

                            openu=OP.MINUS;

                            }
                            break;

                    }


                    pushFollow(FOLLOW_mult_expression_in_expression258);
                    r=mult_expression();

                    state._fsp--;


                     op =new Operator(l,r,openu); 

                    }


                    }
                    break;
                case 2 :
                    // /home/staff/aweinz/svnrepo/gerald_weidinger_thesis/WOC - Wings of Change/antlr/woc.g:75:4: 
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
    // /home/staff/aweinz/svnrepo/gerald_weidinger_thesis/WOC - Wings of Change/antlr/woc.g:78:1: mult_expression returns [OperandI op] : l= primary_expression ( ( ( '*' | '/' ) r= primary_expression ) |) ;
    public final OperandI mult_expression() throws RecognitionException {
        OperandI op = null;


        OperandI l =null;

        OperandI r =null;


         OP openu=null; 
        try {
            // /home/staff/aweinz/svnrepo/gerald_weidinger_thesis/WOC - Wings of Change/antlr/woc.g:80:2: (l= primary_expression ( ( ( '*' | '/' ) r= primary_expression ) |) )
            // /home/staff/aweinz/svnrepo/gerald_weidinger_thesis/WOC - Wings of Change/antlr/woc.g:80:4: l= primary_expression ( ( ( '*' | '/' ) r= primary_expression ) |)
            {
            pushFollow(FOLLOW_primary_expression_in_mult_expression288);
            l=primary_expression();

            state._fsp--;


            // /home/staff/aweinz/svnrepo/gerald_weidinger_thesis/WOC - Wings of Change/antlr/woc.g:80:25: ( ( ( '*' | '/' ) r= primary_expression ) |)
            int alt11=2;
            int LA11_0 = input.LA(1);

            if ( (LA11_0==19||LA11_0==24) ) {
                alt11=1;
            }
            else if ( (LA11_0==18||(LA11_0 >= 20 && LA11_0 <= 23)) ) {
                alt11=2;
            }
            else {
                NoViableAltException nvae =
                    new NoViableAltException("", 11, 0, input);

                throw nvae;

            }
            switch (alt11) {
                case 1 :
                    // /home/staff/aweinz/svnrepo/gerald_weidinger_thesis/WOC - Wings of Change/antlr/woc.g:80:26: ( ( '*' | '/' ) r= primary_expression )
                    {
                    // /home/staff/aweinz/svnrepo/gerald_weidinger_thesis/WOC - Wings of Change/antlr/woc.g:80:26: ( ( '*' | '/' ) r= primary_expression )
                    // /home/staff/aweinz/svnrepo/gerald_weidinger_thesis/WOC - Wings of Change/antlr/woc.g:80:27: ( '*' | '/' ) r= primary_expression
                    {
                    // /home/staff/aweinz/svnrepo/gerald_weidinger_thesis/WOC - Wings of Change/antlr/woc.g:80:27: ( '*' | '/' )
                    int alt10=2;
                    int LA10_0 = input.LA(1);

                    if ( (LA10_0==19) ) {
                        alt10=1;
                    }
                    else if ( (LA10_0==24) ) {
                        alt10=2;
                    }
                    else {
                        NoViableAltException nvae =
                            new NoViableAltException("", 10, 0, input);

                        throw nvae;

                    }
                    switch (alt10) {
                        case 1 :
                            // /home/staff/aweinz/svnrepo/gerald_weidinger_thesis/WOC - Wings of Change/antlr/woc.g:80:28: '*'
                            {
                            match(input,19,FOLLOW_19_in_mult_expression293); 

                            openu=OP.TIMES; 

                            }
                            break;
                        case 2 :
                            // /home/staff/aweinz/svnrepo/gerald_weidinger_thesis/WOC - Wings of Change/antlr/woc.g:80:52: '/'
                            {
                            match(input,24,FOLLOW_24_in_mult_expression298); 

                            openu=OP.DIVIDE;

                            }
                            break;

                    }


                    pushFollow(FOLLOW_primary_expression_in_mult_expression308);
                    r=primary_expression();

                    state._fsp--;


                     op =new Operator(l,r,openu); 

                    }


                    }
                    break;
                case 2 :
                    // /home/staff/aweinz/svnrepo/gerald_weidinger_thesis/WOC - Wings of Change/antlr/woc.g:82:4: 
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
    // /home/staff/aweinz/svnrepo/gerald_weidinger_thesis/WOC - Wings of Change/antlr/woc.g:85:1: primary_expression returns [OperandI op] : ( INT | VAR | '(' expression ')' );
    public final OperandI primary_expression() throws RecognitionException {
        OperandI op = null;


        Token INT6=null;
        Token VAR7=null;
        OperandI expression8 =null;


        try {
            // /home/staff/aweinz/svnrepo/gerald_weidinger_thesis/WOC - Wings of Change/antlr/woc.g:86:2: ( INT | VAR | '(' expression ')' )
            int alt12=3;
            switch ( input.LA(1) ) {
            case INT:
                {
                alt12=1;
                }
                break;
            case VAR:
                {
                alt12=2;
                }
                break;
            case 17:
                {
                alt12=3;
                }
                break;
            default:
                NoViableAltException nvae =
                    new NoViableAltException("", 12, 0, input);

                throw nvae;

            }

            switch (alt12) {
                case 1 :
                    // /home/staff/aweinz/svnrepo/gerald_weidinger_thesis/WOC - Wings of Change/antlr/woc.g:86:4: INT
                    {
                    INT6=(Token)match(input,INT,FOLLOW_INT_in_primary_expression332); 

                    op =Constant.getConstant((INT6!=null?INT6.getText():null));

                    }
                    break;
                case 2 :
                    // /home/staff/aweinz/svnrepo/gerald_weidinger_thesis/WOC - Wings of Change/antlr/woc.g:87:4: VAR
                    {
                    VAR7=(Token)match(input,VAR,FOLLOW_VAR_in_primary_expression339); 

                    op =Variable.getVariable((VAR7!=null?VAR7.getText():null));

                    }
                    break;
                case 3 :
                    // /home/staff/aweinz/svnrepo/gerald_weidinger_thesis/WOC - Wings of Change/antlr/woc.g:88:4: '(' expression ')'
                    {
                    match(input,17,FOLLOW_17_in_primary_expression346); 

                    pushFollow(FOLLOW_expression_in_primary_expression348);
                    expression8=expression();

                    state._fsp--;


                    match(input,18,FOLLOW_18_in_primary_expression350); 

                    op =expression8;

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
    // /home/staff/aweinz/svnrepo/gerald_weidinger_thesis/WOC - Wings of Change/antlr/woc.g:91:1: atom returns [Atom at] : (ctx= ID ':' )? name= ID ( '(' termlist ')' )? ;
    public final Atom atom() throws RecognitionException {
        Atom at = null;


        Token ctx=null;
        Token name=null;
        ArrayList<Term> termlist9 =null;


         String context_id=null; String atom_name; Term[] terms = new Term[0];
        try {
            // /home/staff/aweinz/svnrepo/gerald_weidinger_thesis/WOC - Wings of Change/antlr/woc.g:93:2: ( (ctx= ID ':' )? name= ID ( '(' termlist ')' )? )
            // /home/staff/aweinz/svnrepo/gerald_weidinger_thesis/WOC - Wings of Change/antlr/woc.g:93:4: (ctx= ID ':' )? name= ID ( '(' termlist ')' )?
            {
            // /home/staff/aweinz/svnrepo/gerald_weidinger_thesis/WOC - Wings of Change/antlr/woc.g:93:4: (ctx= ID ':' )?
            int alt13=2;
            int LA13_0 = input.LA(1);

            if ( (LA13_0==ID) ) {
                int LA13_1 = input.LA(2);

                if ( (LA13_1==25) ) {
                    alt13=1;
                }
            }
            switch (alt13) {
                case 1 :
                    // /home/staff/aweinz/svnrepo/gerald_weidinger_thesis/WOC - Wings of Change/antlr/woc.g:93:5: ctx= ID ':'
                    {
                    ctx=(Token)match(input,ID,FOLLOW_ID_in_atom374); 

                    match(input,25,FOLLOW_25_in_atom376); 

                    context_id=(ctx!=null?ctx.getText():null);

                    }
                    break;

            }


            name=(Token)match(input,ID,FOLLOW_ID_in_atom387); 

            if(context_id==null) { atom_name=(name!=null?name.getText():null); } else {atom_name=context_id+":"+(name!=null?name.getText():null);} 

            // /home/staff/aweinz/svnrepo/gerald_weidinger_thesis/WOC - Wings of Change/antlr/woc.g:95:3: ( '(' termlist ')' )?
            int alt14=2;
            int LA14_0 = input.LA(1);

            if ( (LA14_0==17) ) {
                alt14=1;
            }
            switch (alt14) {
                case 1 :
                    // /home/staff/aweinz/svnrepo/gerald_weidinger_thesis/WOC - Wings of Change/antlr/woc.g:95:5: '(' termlist ')'
                    {
                    match(input,17,FOLLOW_17_in_atom395); 

                    pushFollow(FOLLOW_termlist_in_atom397);
                    termlist9=termlist();

                    state._fsp--;


                    match(input,18,FOLLOW_18_in_atom399); 

                    terms=(Term[])termlist9.toArray(new Term[termlist9.size()]);

                    }
                    break;

            }


            at =Atom.getAtom(atom_name, terms.length, terms);
            		if(context_id !=null) {at.getPredicate().setNodeId(context_id); context.registerFactFromOutside(at.getPredicate());
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
        return at;
    }
    // $ANTLR end "atom"



    // $ANTLR start "termlist"
    // /home/staff/aweinz/svnrepo/gerald_weidinger_thesis/WOC - Wings of Change/antlr/woc.g:101:1: termlist returns [ArrayList<Term> termlist] : t1= term ( ',' tn= term )* ;
    public final ArrayList<Term> termlist() throws RecognitionException {
        ArrayList<Term> termlist = null;


        Term t1 =null;

        Term tn =null;


         termlist = new ArrayList<Term>();
        try {
            // /home/staff/aweinz/svnrepo/gerald_weidinger_thesis/WOC - Wings of Change/antlr/woc.g:103:2: (t1= term ( ',' tn= term )* )
            // /home/staff/aweinz/svnrepo/gerald_weidinger_thesis/WOC - Wings of Change/antlr/woc.g:103:4: t1= term ( ',' tn= term )*
            {
            pushFollow(FOLLOW_term_in_termlist428);
            t1=term();

            state._fsp--;


            termlist.add(t1);

            // /home/staff/aweinz/svnrepo/gerald_weidinger_thesis/WOC - Wings of Change/antlr/woc.g:104:3: ( ',' tn= term )*
            loop15:
            do {
                int alt15=2;
                int LA15_0 = input.LA(1);

                if ( (LA15_0==21) ) {
                    alt15=1;
                }


                switch (alt15) {
            	case 1 :
            	    // /home/staff/aweinz/svnrepo/gerald_weidinger_thesis/WOC - Wings of Change/antlr/woc.g:104:4: ',' tn= term
            	    {
            	    match(input,21,FOLLOW_21_in_termlist435); 

            	    pushFollow(FOLLOW_term_in_termlist439);
            	    tn=term();

            	    state._fsp--;


            	    termlist.add(tn);

            	    }
            	    break;

            	default :
            	    break loop15;
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
    // /home/staff/aweinz/svnrepo/gerald_weidinger_thesis/WOC - Wings of Change/antlr/woc.g:107:1: term returns [Term term] : ( constant | VAR | function );
    public final Term term() throws RecognitionException {
        Term term = null;


        Token VAR11=null;
        Constant constant10 =null;

        wocParser.function_return function12 =null;


        try {
            // /home/staff/aweinz/svnrepo/gerald_weidinger_thesis/WOC - Wings of Change/antlr/woc.g:108:2: ( constant | VAR | function )
            int alt16=3;
            switch ( input.LA(1) ) {
            case ID:
                {
                int LA16_1 = input.LA(2);

                if ( (LA16_1==17) ) {
                    alt16=3;
                }
                else if ( (LA16_1==18||LA16_1==21) ) {
                    alt16=1;
                }
                else {
                    NoViableAltException nvae =
                        new NoViableAltException("", 16, 1, input);

                    throw nvae;

                }
                }
                break;
            case INT:
            case 22:
                {
                alt16=1;
                }
                break;
            case VAR:
                {
                alt16=2;
                }
                break;
            default:
                NoViableAltException nvae =
                    new NoViableAltException("", 16, 0, input);

                throw nvae;

            }

            switch (alt16) {
                case 1 :
                    // /home/staff/aweinz/svnrepo/gerald_weidinger_thesis/WOC - Wings of Change/antlr/woc.g:108:4: constant
                    {
                    pushFollow(FOLLOW_constant_in_term458);
                    constant10=constant();

                    state._fsp--;


                    term =constant10;

                    }
                    break;
                case 2 :
                    // /home/staff/aweinz/svnrepo/gerald_weidinger_thesis/WOC - Wings of Change/antlr/woc.g:109:4: VAR
                    {
                    VAR11=(Token)match(input,VAR,FOLLOW_VAR_in_term465); 

                    term =Variable.getVariable((VAR11!=null?VAR11.getText():null));

                    }
                    break;
                case 3 :
                    // /home/staff/aweinz/svnrepo/gerald_weidinger_thesis/WOC - Wings of Change/antlr/woc.g:110:4: function
                    {
                    pushFollow(FOLLOW_function_in_term472);
                    function12=function();

                    state._fsp--;


                    term =FuncTerm.getFuncTerm((function12!=null?function12.name:null),(function12!=null?function12.terms:null));

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
    // /home/staff/aweinz/svnrepo/gerald_weidinger_thesis/WOC - Wings of Change/antlr/woc.g:113:1: function returns [String name, ArrayList<Term> terms] : ID '(' termlist ')' ;
    public final wocParser.function_return function() throws RecognitionException {
        wocParser.function_return retval = new wocParser.function_return();
        retval.start = input.LT(1);


        Token ID13=null;
        ArrayList<Term> termlist14 =null;


        try {
            // /home/staff/aweinz/svnrepo/gerald_weidinger_thesis/WOC - Wings of Change/antlr/woc.g:114:2: ( ID '(' termlist ')' )
            // /home/staff/aweinz/svnrepo/gerald_weidinger_thesis/WOC - Wings of Change/antlr/woc.g:114:4: ID '(' termlist ')'
            {
            ID13=(Token)match(input,ID,FOLLOW_ID_in_function489); 

            match(input,17,FOLLOW_17_in_function491); 

            pushFollow(FOLLOW_termlist_in_function493);
            termlist14=termlist();

            state._fsp--;


            match(input,18,FOLLOW_18_in_function495); 

            retval.name =(ID13!=null?ID13.getText():null); retval.terms =termlist14;

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
    // /home/staff/aweinz/svnrepo/gerald_weidinger_thesis/WOC - Wings of Change/antlr/woc.g:117:1: constant returns [Constant constnt] : ( ID | ( '-' )? INT );
    public final Constant constant() throws RecognitionException {
        Constant constnt = null;


        Token ID15=null;
        Token INT16=null;

        try {
            // /home/staff/aweinz/svnrepo/gerald_weidinger_thesis/WOC - Wings of Change/antlr/woc.g:118:2: ( ID | ( '-' )? INT )
            int alt18=2;
            int LA18_0 = input.LA(1);

            if ( (LA18_0==ID) ) {
                alt18=1;
            }
            else if ( (LA18_0==INT||LA18_0==22) ) {
                alt18=2;
            }
            else {
                NoViableAltException nvae =
                    new NoViableAltException("", 18, 0, input);

                throw nvae;

            }
            switch (alt18) {
                case 1 :
                    // /home/staff/aweinz/svnrepo/gerald_weidinger_thesis/WOC - Wings of Change/antlr/woc.g:118:4: ID
                    {
                    ID15=(Token)match(input,ID,FOLLOW_ID_in_constant512); 

                    constnt =Constant.getConstant((ID15!=null?ID15.getText():null));

                    }
                    break;
                case 2 :
                    // /home/staff/aweinz/svnrepo/gerald_weidinger_thesis/WOC - Wings of Change/antlr/woc.g:119:4: ( '-' )? INT
                    {
                    // /home/staff/aweinz/svnrepo/gerald_weidinger_thesis/WOC - Wings of Change/antlr/woc.g:119:4: ( '-' )?
                    int alt17=2;
                    int LA17_0 = input.LA(1);

                    if ( (LA17_0==22) ) {
                        alt17=1;
                    }
                    switch (alt17) {
                        case 1 :
                            // /home/staff/aweinz/svnrepo/gerald_weidinger_thesis/WOC - Wings of Change/antlr/woc.g:119:5: '-'
                            {
                            match(input,22,FOLLOW_22_in_constant520); 

                            }
                            break;

                    }


                    INT16=(Token)match(input,INT,FOLLOW_INT_in_constant524); 

                    constnt =Constant.getConstant((INT16!=null?INT16.getText():null));

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
    // /home/staff/aweinz/svnrepo/gerald_weidinger_thesis/WOC - Wings of Change/antlr/woc.g:122:1: comp_sym returns [OP openu] : ( '<' | '<=' | '>=' | '>' | '=' | '!=' );
    public final OP comp_sym() throws RecognitionException {
        OP openu = null;


        try {
            // /home/staff/aweinz/svnrepo/gerald_weidinger_thesis/WOC - Wings of Change/antlr/woc.g:123:2: ( '<' | '<=' | '>=' | '>' | '=' | '!=' )
            int alt19=6;
            switch ( input.LA(1) ) {
            case 27:
                {
                alt19=1;
                }
                break;
            case 28:
                {
                alt19=2;
                }
                break;
            case 31:
                {
                alt19=3;
                }
                break;
            case 30:
                {
                alt19=4;
                }
                break;
            case 29:
                {
                alt19=5;
                }
                break;
            case 16:
                {
                alt19=6;
                }
                break;
            default:
                NoViableAltException nvae =
                    new NoViableAltException("", 19, 0, input);

                throw nvae;

            }

            switch (alt19) {
                case 1 :
                    // /home/staff/aweinz/svnrepo/gerald_weidinger_thesis/WOC - Wings of Change/antlr/woc.g:123:4: '<'
                    {
                    match(input,27,FOLLOW_27_in_comp_sym540); 

                    openu =OP.LESS;

                    }
                    break;
                case 2 :
                    // /home/staff/aweinz/svnrepo/gerald_weidinger_thesis/WOC - Wings of Change/antlr/woc.g:124:4: '<='
                    {
                    match(input,28,FOLLOW_28_in_comp_sym547); 

                    openu =OP.LESS_EQ;

                    }
                    break;
                case 3 :
                    // /home/staff/aweinz/svnrepo/gerald_weidinger_thesis/WOC - Wings of Change/antlr/woc.g:125:4: '>='
                    {
                    match(input,31,FOLLOW_31_in_comp_sym554); 

                    openu =OP.GREATER_EQ;

                    }
                    break;
                case 4 :
                    // /home/staff/aweinz/svnrepo/gerald_weidinger_thesis/WOC - Wings of Change/antlr/woc.g:126:4: '>'
                    {
                    match(input,30,FOLLOW_30_in_comp_sym561); 

                    openu =OP.GREATER;

                    }
                    break;
                case 5 :
                    // /home/staff/aweinz/svnrepo/gerald_weidinger_thesis/WOC - Wings of Change/antlr/woc.g:127:4: '='
                    {
                    match(input,29,FOLLOW_29_in_comp_sym568); 

                    openu =OP.EQUAL;

                    }
                    break;
                case 6 :
                    // /home/staff/aweinz/svnrepo/gerald_weidinger_thesis/WOC - Wings of Change/antlr/woc.g:128:4: '!='
                    {
                    match(input,16,FOLLOW_16_in_comp_sym575); 

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
    public static final BitSet FOLLOW_fact_in_rule_or_fact59 = new BitSet(new long[]{0x0000000004800000L});
    public static final BitSet FOLLOW_26_in_rule_or_fact67 = new BitSet(new long[]{0x0000000200004200L});
    public static final BitSet FOLLOW_body_in_rule_or_fact71 = new BitSet(new long[]{0x0000000000800000L});
    public static final BitSet FOLLOW_23_in_rule_or_fact74 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_23_in_rule_or_fact81 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_ID_in_fact106 = new BitSet(new long[]{0x0000000000020002L});
    public static final BitSet FOLLOW_17_in_fact114 = new BitSet(new long[]{0x0000000000404600L});
    public static final BitSet FOLLOW_termlist_in_fact116 = new BitSet(new long[]{0x0000000000040000L});
    public static final BitSet FOLLOW_18_in_fact118 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_literal_in_body135 = new BitSet(new long[]{0x0000000000200002L});
    public static final BitSet FOLLOW_21_in_body139 = new BitSet(new long[]{0x0000000200004200L});
    public static final BitSet FOLLOW_literal_in_body141 = new BitSet(new long[]{0x0000000000200002L});
    public static final BitSet FOLLOW_atom_in_literal159 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_33_in_literal166 = new BitSet(new long[]{0x0000000000000200L});
    public static final BitSet FOLLOW_atom_in_literal170 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_operation_in_literal177 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_VAR_in_operation196 = new BitSet(new long[]{0x00000000F8010000L});
    public static final BitSet FOLLOW_comp_sym_in_operation198 = new BitSet(new long[]{0x0000000000024400L});
    public static final BitSet FOLLOW_expression_in_operation202 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_VAR_in_operation211 = new BitSet(new long[]{0x0000000100000000L});
    public static final BitSet FOLLOW_32_in_operation213 = new BitSet(new long[]{0x0000000000024400L});
    public static final BitSet FOLLOW_expression_in_operation217 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_mult_expression_in_expression240 = new BitSet(new long[]{0x0000000000500002L});
    public static final BitSet FOLLOW_20_in_expression245 = new BitSet(new long[]{0x0000000000024400L});
    public static final BitSet FOLLOW_22_in_expression250 = new BitSet(new long[]{0x0000000000024400L});
    public static final BitSet FOLLOW_mult_expression_in_expression258 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_primary_expression_in_mult_expression288 = new BitSet(new long[]{0x0000000001080002L});
    public static final BitSet FOLLOW_19_in_mult_expression293 = new BitSet(new long[]{0x0000000000024400L});
    public static final BitSet FOLLOW_24_in_mult_expression298 = new BitSet(new long[]{0x0000000000024400L});
    public static final BitSet FOLLOW_primary_expression_in_mult_expression308 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_INT_in_primary_expression332 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_VAR_in_primary_expression339 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_17_in_primary_expression346 = new BitSet(new long[]{0x0000000000024400L});
    public static final BitSet FOLLOW_expression_in_primary_expression348 = new BitSet(new long[]{0x0000000000040000L});
    public static final BitSet FOLLOW_18_in_primary_expression350 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_ID_in_atom374 = new BitSet(new long[]{0x0000000002000000L});
    public static final BitSet FOLLOW_25_in_atom376 = new BitSet(new long[]{0x0000000000000200L});
    public static final BitSet FOLLOW_ID_in_atom387 = new BitSet(new long[]{0x0000000000020002L});
    public static final BitSet FOLLOW_17_in_atom395 = new BitSet(new long[]{0x0000000000404600L});
    public static final BitSet FOLLOW_termlist_in_atom397 = new BitSet(new long[]{0x0000000000040000L});
    public static final BitSet FOLLOW_18_in_atom399 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_term_in_termlist428 = new BitSet(new long[]{0x0000000000200002L});
    public static final BitSet FOLLOW_21_in_termlist435 = new BitSet(new long[]{0x0000000000404600L});
    public static final BitSet FOLLOW_term_in_termlist439 = new BitSet(new long[]{0x0000000000200002L});
    public static final BitSet FOLLOW_constant_in_term458 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_VAR_in_term465 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_function_in_term472 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_ID_in_function489 = new BitSet(new long[]{0x0000000000020000L});
    public static final BitSet FOLLOW_17_in_function491 = new BitSet(new long[]{0x0000000000404600L});
    public static final BitSet FOLLOW_termlist_in_function493 = new BitSet(new long[]{0x0000000000040000L});
    public static final BitSet FOLLOW_18_in_function495 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_ID_in_constant512 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_22_in_constant520 = new BitSet(new long[]{0x0000000000000400L});
    public static final BitSet FOLLOW_INT_in_constant524 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_27_in_comp_sym540 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_28_in_comp_sym547 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_31_in_comp_sym554 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_30_in_comp_sym561 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_29_in_comp_sym568 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_16_in_comp_sym575 = new BitSet(new long[]{0x0000000000000002L});

}