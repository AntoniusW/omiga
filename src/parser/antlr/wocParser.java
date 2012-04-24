// $ANTLR 3.4 /home/as/incman/gerald_weidinger_thesis/WOC - Wings of Change/antlr/woc.g 2012-04-24 05:12:43

package parser.antlr;

import java.util.ArrayList;

import Entity.*;
import Interfaces.Term;
import Exceptions.*;


import org.antlr.runtime.*;
import java.util.Stack;
import java.util.List;
import java.util.ArrayList;

@SuppressWarnings({"all", "warnings", "unchecked"})
public class wocParser extends Parser {
    public static final String[] tokenNames = new String[] {
        "<invalid>", "<EOR>", "<DOWN>", "<UP>", "CHAR", "COMMENT", "COMP", "ESC_SEQ", "EXPONENT", "HEX_DIGIT", "ID", "INT", "OCTAL_ESC", "STRING", "UNICODE_ESC", "VAR", "WS", "'('", "')'", "'*'", "'+'", "','", "'-'", "'.'", "'/'", "':'", "':-'", "'is'", "'not'"
    };

    public static final int EOF=-1;
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
    public static final int CHAR=4;
    public static final int COMMENT=5;
    public static final int COMP=6;
    public static final int ESC_SEQ=7;
    public static final int EXPONENT=8;
    public static final int HEX_DIGIT=9;
    public static final int ID=10;
    public static final int INT=11;
    public static final int OCTAL_ESC=12;
    public static final int STRING=13;
    public static final int UNICODE_ESC=14;
    public static final int VAR=15;
    public static final int WS=16;

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
    public String getGrammarFileName() { return "/home/as/incman/gerald_weidinger_thesis/WOC - Wings of Change/antlr/woc.g"; }



    public class ParserTerm{
    	public ParserTerm(String name, boolean isGround) {
    		this.name=name;
    		this.isGround=isGround;
    	}
    	public boolean isGround;
    	public String name;
    }
    public class ParserFunction extends ParserTerm {
    	public ParserFunction(String name, boolean isGround, int arity, ArrayList<ParserTerm> termlist) {
    		super(name,isGround);
    		this.arity=arity;
    		this.termlist=termlist;
    	}
    	public int arity;
    	public ArrayList<ParserTerm> termlist; 
    }
    public class ParserConstant extends ParserTerm {
    	public ParserConstant(String name, boolean isGround) {
    		super(name,isGround);
    	}
    }
    public class ParserVariable extends ParserTerm {
    	public ParserVariable(String name) {
    		super(name,false);
    	}
    }
    	
    public class ParserOperation {}

    public class ParserAssign extends ParserOperation {
    	public ParserAssign(OpVar var, ParserOperation val) {
    		this.var=var;
    		this.val=val;
    	}
    	OpVar var;
    	ParserOperation val;
    }

    public class ParserComparison extends ParserOperation {
    	public ParserComparison(OpVar var, ParserOperation val) {
    		this.var=var;
    		this.val=val;
    	}
    	OpVar var;
    	ParserOperation val;
    }

    public class BinOp extends ParserOperation {
    	ParserOperation left;
    	ParserOperation right;
    }
    public class OpPlus extends BinOp { }
    public class OpMinus extends BinOp { }
    public class OpMult extends BinOp { }
    public class OpDiv extends BinOp { }
    public class OpInt extends ParserOperation {
    	public OpInt(String value) {
    		this.value=Integer.parseInt(value);
    	}
    	public int value;
    }
    public class OpVar extends ParserOperation {
    	public OpVar(String name) {
    		var = new ParserVariable(name);
    	}
    	public ParserVariable var;
    }

    	
    public class ParserAtom{
    	public String name;
    	public int arity;
    	public String context_id;
    	public ArrayList<ParserTerm> termlist;
    }
    	
    public class ParserRule{
    	public ParserRule() {
    		posAtoms=new ArrayList<ParserAtom>();
    		negAtoms=new ArrayList<ParserAtom>();
    		operations=new ArrayList<ParserOperation>();
    	}
    	public ParserAtom head;
    	public ArrayList<ParserAtom> posAtoms;
    	public ArrayList<ParserAtom> negAtoms;
    	public ArrayList<ParserOperation> operations;
    }
    	
    public ArrayList<ParserRule> asp_rules;
    public ArrayList<ParserAtom> asp_facts;

    private ContextASP context;

    public void setContext(ContextASP context) {
    	this.context=context;
    }




    // $ANTLR start "woc_program"
    // /home/as/incman/gerald_weidinger_thesis/WOC - Wings of Change/antlr/woc.g:119:1: woc_program : ( rule_or_fact )* ;
    public final void woc_program() throws RuleNotSafeException, RecognitionException, FactSizeException {

        asp_rules = new ArrayList<ParserRule>();
        asp_facts = new ArrayList<ParserAtom>(); 
        try {
            // /home/as/incman/gerald_weidinger_thesis/WOC - Wings of Change/antlr/woc.g:123:2: ( ( rule_or_fact )* )
            // /home/as/incman/gerald_weidinger_thesis/WOC - Wings of Change/antlr/woc.g:123:4: ( rule_or_fact )*
            {
            // /home/as/incman/gerald_weidinger_thesis/WOC - Wings of Change/antlr/woc.g:123:4: ( rule_or_fact )*
            loop1:
            do {
                int alt1=2;
                int LA1_0 = input.LA(1);

                if ( (LA1_0==ID||LA1_0==23||LA1_0==26) ) {
                    alt1=1;
                }


                switch (alt1) {
            	case 1 :
            	    // /home/as/incman/gerald_weidinger_thesis/WOC - Wings of Change/antlr/woc.g:123:4: rule_or_fact
            	    {
            	    pushFollow(FOLLOW_rule_or_fact_in_woc_program40);
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
    // /home/as/incman/gerald_weidinger_thesis/WOC - Wings of Change/antlr/woc.g:126:1: rule_or_fact : ( atom )? ( ':-' body[r] '.' | '.' ) ;
    public final void rule_or_fact() throws RuleNotSafeException, RecognitionException, FactSizeException {
        Atom atom1 =null;


         Atom head=null; 
        try {
            // /home/as/incman/gerald_weidinger_thesis/WOC - Wings of Change/antlr/woc.g:128:2: ( ( atom )? ( ':-' body[r] '.' | '.' ) )
            // /home/as/incman/gerald_weidinger_thesis/WOC - Wings of Change/antlr/woc.g:128:4: ( atom )? ( ':-' body[r] '.' | '.' )
            {
            // /home/as/incman/gerald_weidinger_thesis/WOC - Wings of Change/antlr/woc.g:128:4: ( atom )?
            int alt2=2;
            int LA2_0 = input.LA(1);

            if ( (LA2_0==ID) ) {
                alt2=1;
            }
            switch (alt2) {
                case 1 :
                    // /home/as/incman/gerald_weidinger_thesis/WOC - Wings of Change/antlr/woc.g:128:4: atom
                    {
                    pushFollow(FOLLOW_atom_in_rule_or_fact64);
                    atom1=atom();

                    state._fsp--;


                    }
                    break;

            }


            head=atom1;

            // /home/as/incman/gerald_weidinger_thesis/WOC - Wings of Change/antlr/woc.g:129:3: ( ':-' body[r] '.' | '.' )
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
                    // /home/as/incman/gerald_weidinger_thesis/WOC - Wings of Change/antlr/woc.g:129:4: ':-' body[r] '.'
                    {
                    match(input,26,FOLLOW_26_in_rule_or_fact72); 

                    Rule r = new Rule();

                    pushFollow(FOLLOW_body_in_rule_or_fact76);
                    body(r);

                    state._fsp--;


                    match(input,23,FOLLOW_23_in_rule_or_fact79); 

                    if(head != null) r.setHead(head); context.addRule(r);

                    }
                    break;
                case 2 :
                    // /home/as/incman/gerald_weidinger_thesis/WOC - Wings of Change/antlr/woc.g:130:4: '.'
                    {
                    match(input,23,FOLLOW_23_in_rule_or_fact86); 

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
    // /home/as/incman/gerald_weidinger_thesis/WOC - Wings of Change/antlr/woc.g:134:1: body[Rule r] : literal[$r] ( ',' literal[$r] )* ;
    public final void body(Rule r) throws RecognitionException {
        try {
            // /home/as/incman/gerald_weidinger_thesis/WOC - Wings of Change/antlr/woc.g:135:2: ( literal[$r] ( ',' literal[$r] )* )
            // /home/as/incman/gerald_weidinger_thesis/WOC - Wings of Change/antlr/woc.g:135:4: literal[$r] ( ',' literal[$r] )*
            {
            pushFollow(FOLLOW_literal_in_body102);
            literal(r);

            state._fsp--;


            // /home/as/incman/gerald_weidinger_thesis/WOC - Wings of Change/antlr/woc.g:135:16: ( ',' literal[$r] )*
            loop4:
            do {
                int alt4=2;
                int LA4_0 = input.LA(1);

                if ( (LA4_0==21) ) {
                    alt4=1;
                }


                switch (alt4) {
            	case 1 :
            	    // /home/as/incman/gerald_weidinger_thesis/WOC - Wings of Change/antlr/woc.g:135:17: ',' literal[$r]
            	    {
            	    match(input,21,FOLLOW_21_in_body106); 

            	    pushFollow(FOLLOW_literal_in_body108);
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
    // /home/as/incman/gerald_weidinger_thesis/WOC - Wings of Change/antlr/woc.g:138:1: literal[Rule r] : (a1= atom | 'not' a2= atom | operation );
    public final void literal(Rule r) throws RecognitionException {
        Atom a1 =null;

        Atom a2 =null;

        ParserOperation operation2 =null;


        try {
            // /home/as/incman/gerald_weidinger_thesis/WOC - Wings of Change/antlr/woc.g:139:2: (a1= atom | 'not' a2= atom | operation )
            int alt5=3;
            switch ( input.LA(1) ) {
            case ID:
                {
                alt5=1;
                }
                break;
            case 28:
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
                    // /home/as/incman/gerald_weidinger_thesis/WOC - Wings of Change/antlr/woc.g:139:4: a1= atom
                    {
                    pushFollow(FOLLOW_atom_in_literal126);
                    a1=atom();

                    state._fsp--;


                    r.addAtomPlus(a1);

                    }
                    break;
                case 2 :
                    // /home/as/incman/gerald_weidinger_thesis/WOC - Wings of Change/antlr/woc.g:140:4: 'not' a2= atom
                    {
                    match(input,28,FOLLOW_28_in_literal133); 

                    pushFollow(FOLLOW_atom_in_literal137);
                    a2=atom();

                    state._fsp--;


                    r.addAtomMinus(a2);

                    }
                    break;
                case 3 :
                    // /home/as/incman/gerald_weidinger_thesis/WOC - Wings of Change/antlr/woc.g:141:4: operation
                    {
                    pushFollow(FOLLOW_operation_in_literal144);
                    operation2=operation();

                    state._fsp--;


                    //r.operations.add(operation2); TODO
                    			

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
    // /home/as/incman/gerald_weidinger_thesis/WOC - Wings of Change/antlr/woc.g:145:1: operation returns [ParserOperation op] : (v1= VAR COMP e1= expression |v2= VAR 'is' e2= expression );
    public final ParserOperation operation() throws RecognitionException {
        ParserOperation op = null;


        Token v1=null;
        Token v2=null;
        ParserOperation e1 =null;

        ParserOperation e2 =null;


        try {
            // /home/as/incman/gerald_weidinger_thesis/WOC - Wings of Change/antlr/woc.g:146:2: (v1= VAR COMP e1= expression |v2= VAR 'is' e2= expression )
            int alt6=2;
            int LA6_0 = input.LA(1);

            if ( (LA6_0==VAR) ) {
                int LA6_1 = input.LA(2);

                if ( (LA6_1==COMP) ) {
                    alt6=1;
                }
                else if ( (LA6_1==27) ) {
                    alt6=2;
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
                    // /home/as/incman/gerald_weidinger_thesis/WOC - Wings of Change/antlr/woc.g:146:4: v1= VAR COMP e1= expression
                    {
                    v1=(Token)match(input,VAR,FOLLOW_VAR_in_operation163); 

                    match(input,COMP,FOLLOW_COMP_in_operation165); 

                    pushFollow(FOLLOW_expression_in_operation169);
                    e1=expression();

                    state._fsp--;


                    op =new ParserComparison(new OpVar((v1!=null?v1.getText():null)),e1);

                    }
                    break;
                case 2 :
                    // /home/as/incman/gerald_weidinger_thesis/WOC - Wings of Change/antlr/woc.g:147:4: v2= VAR 'is' e2= expression
                    {
                    v2=(Token)match(input,VAR,FOLLOW_VAR_in_operation178); 

                    match(input,27,FOLLOW_27_in_operation180); 

                    pushFollow(FOLLOW_expression_in_operation184);
                    e2=expression();

                    state._fsp--;


                    op =new ParserAssign(new OpVar((v2!=null?v2.getText():null)),e2);

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
    // /home/as/incman/gerald_weidinger_thesis/WOC - Wings of Change/antlr/woc.g:150:1: expression returns [ParserOperation op] : l= mult_expression ( ( ( '+' | '-' ) r= mult_expression ) |) ;
    public final ParserOperation expression() throws RecognitionException {
        ParserOperation op = null;


        ParserOperation l =null;

        ParserOperation r =null;


        try {
            // /home/as/incman/gerald_weidinger_thesis/WOC - Wings of Change/antlr/woc.g:151:2: (l= mult_expression ( ( ( '+' | '-' ) r= mult_expression ) |) )
            // /home/as/incman/gerald_weidinger_thesis/WOC - Wings of Change/antlr/woc.g:151:4: l= mult_expression ( ( ( '+' | '-' ) r= mult_expression ) |)
            {
            pushFollow(FOLLOW_mult_expression_in_expression203);
            l=mult_expression();

            state._fsp--;


            // /home/as/incman/gerald_weidinger_thesis/WOC - Wings of Change/antlr/woc.g:151:22: ( ( ( '+' | '-' ) r= mult_expression ) |)
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
                    // /home/as/incman/gerald_weidinger_thesis/WOC - Wings of Change/antlr/woc.g:151:23: ( ( '+' | '-' ) r= mult_expression )
                    {
                    // /home/as/incman/gerald_weidinger_thesis/WOC - Wings of Change/antlr/woc.g:151:23: ( ( '+' | '-' ) r= mult_expression )
                    // /home/as/incman/gerald_weidinger_thesis/WOC - Wings of Change/antlr/woc.g:151:24: ( '+' | '-' ) r= mult_expression
                    {
                    // /home/as/incman/gerald_weidinger_thesis/WOC - Wings of Change/antlr/woc.g:151:24: ( '+' | '-' )
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
                            // /home/as/incman/gerald_weidinger_thesis/WOC - Wings of Change/antlr/woc.g:151:25: '+'
                            {
                            match(input,20,FOLLOW_20_in_expression208); 

                            op =new OpPlus();

                            }
                            break;
                        case 2 :
                            // /home/as/incman/gerald_weidinger_thesis/WOC - Wings of Change/antlr/woc.g:151:50: '-'
                            {
                            match(input,22,FOLLOW_22_in_expression213); 

                            op =new OpMinus();

                            }
                            break;

                    }


                    pushFollow(FOLLOW_mult_expression_in_expression221);
                    r=mult_expression();

                    state._fsp--;


                     ((BinOp)op).left=l; ((BinOp)op).right=r; 

                    }


                    }
                    break;
                case 2 :
                    // /home/as/incman/gerald_weidinger_thesis/WOC - Wings of Change/antlr/woc.g:153:4: 
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
    // /home/as/incman/gerald_weidinger_thesis/WOC - Wings of Change/antlr/woc.g:156:1: mult_expression returns [ParserOperation op] : l= primary_expression ( ( ( '*' | '/' ) r= primary_expression ) |) ;
    public final ParserOperation mult_expression() throws RecognitionException {
        ParserOperation op = null;


        ParserOperation l =null;

        ParserOperation r =null;


        try {
            // /home/as/incman/gerald_weidinger_thesis/WOC - Wings of Change/antlr/woc.g:157:2: (l= primary_expression ( ( ( '*' | '/' ) r= primary_expression ) |) )
            // /home/as/incman/gerald_weidinger_thesis/WOC - Wings of Change/antlr/woc.g:157:4: l= primary_expression ( ( ( '*' | '/' ) r= primary_expression ) |)
            {
            pushFollow(FOLLOW_primary_expression_in_mult_expression247);
            l=primary_expression();

            state._fsp--;


            // /home/as/incman/gerald_weidinger_thesis/WOC - Wings of Change/antlr/woc.g:157:25: ( ( ( '*' | '/' ) r= primary_expression ) |)
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
                    // /home/as/incman/gerald_weidinger_thesis/WOC - Wings of Change/antlr/woc.g:157:26: ( ( '*' | '/' ) r= primary_expression )
                    {
                    // /home/as/incman/gerald_weidinger_thesis/WOC - Wings of Change/antlr/woc.g:157:26: ( ( '*' | '/' ) r= primary_expression )
                    // /home/as/incman/gerald_weidinger_thesis/WOC - Wings of Change/antlr/woc.g:157:27: ( '*' | '/' ) r= primary_expression
                    {
                    // /home/as/incman/gerald_weidinger_thesis/WOC - Wings of Change/antlr/woc.g:157:27: ( '*' | '/' )
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
                            // /home/as/incman/gerald_weidinger_thesis/WOC - Wings of Change/antlr/woc.g:157:28: '*'
                            {
                            match(input,19,FOLLOW_19_in_mult_expression252); 

                            op =new OpMult(); 

                            }
                            break;
                        case 2 :
                            // /home/as/incman/gerald_weidinger_thesis/WOC - Wings of Change/antlr/woc.g:157:54: '/'
                            {
                            match(input,24,FOLLOW_24_in_mult_expression257); 

                            op =new OpDiv();

                            }
                            break;

                    }


                    pushFollow(FOLLOW_primary_expression_in_mult_expression267);
                    r=primary_expression();

                    state._fsp--;


                     ((BinOp)op).left=l; ((BinOp)op).right=r; 

                    }


                    }
                    break;
                case 2 :
                    // /home/as/incman/gerald_weidinger_thesis/WOC - Wings of Change/antlr/woc.g:159:4: 
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
    // /home/as/incman/gerald_weidinger_thesis/WOC - Wings of Change/antlr/woc.g:162:1: primary_expression returns [ParserOperation op] : ( INT | VAR | '(' expression ')' );
    public final ParserOperation primary_expression() throws RecognitionException {
        ParserOperation op = null;


        Token INT3=null;
        Token VAR4=null;
        ParserOperation expression5 =null;


        try {
            // /home/as/incman/gerald_weidinger_thesis/WOC - Wings of Change/antlr/woc.g:163:2: ( INT | VAR | '(' expression ')' )
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
                    // /home/as/incman/gerald_weidinger_thesis/WOC - Wings of Change/antlr/woc.g:163:4: INT
                    {
                    INT3=(Token)match(input,INT,FOLLOW_INT_in_primary_expression291); 

                    op =new OpInt((INT3!=null?INT3.getText():null));

                    }
                    break;
                case 2 :
                    // /home/as/incman/gerald_weidinger_thesis/WOC - Wings of Change/antlr/woc.g:164:4: VAR
                    {
                    VAR4=(Token)match(input,VAR,FOLLOW_VAR_in_primary_expression298); 

                    op =new OpVar((VAR4!=null?VAR4.getText():null));

                    }
                    break;
                case 3 :
                    // /home/as/incman/gerald_weidinger_thesis/WOC - Wings of Change/antlr/woc.g:165:4: '(' expression ')'
                    {
                    match(input,17,FOLLOW_17_in_primary_expression305); 

                    pushFollow(FOLLOW_expression_in_primary_expression307);
                    expression5=expression();

                    state._fsp--;


                    match(input,18,FOLLOW_18_in_primary_expression309); 

                    op =expression5;

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
    // /home/as/incman/gerald_weidinger_thesis/WOC - Wings of Change/antlr/woc.g:168:1: atom returns [Atom at] : (ctx= ID ':' )? name= ID ( '(' termlist ')' )? ;
    public final Atom atom() throws RecognitionException {
        Atom at = null;


        Token ctx=null;
        Token name=null;
        ArrayList<Term> termlist6 =null;


         String context_id=null; String atom_name; Term[] terms = new Term[0];
        try {
            // /home/as/incman/gerald_weidinger_thesis/WOC - Wings of Change/antlr/woc.g:170:2: ( (ctx= ID ':' )? name= ID ( '(' termlist ')' )? )
            // /home/as/incman/gerald_weidinger_thesis/WOC - Wings of Change/antlr/woc.g:170:4: (ctx= ID ':' )? name= ID ( '(' termlist ')' )?
            {
            // /home/as/incman/gerald_weidinger_thesis/WOC - Wings of Change/antlr/woc.g:170:4: (ctx= ID ':' )?
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
                    // /home/as/incman/gerald_weidinger_thesis/WOC - Wings of Change/antlr/woc.g:170:5: ctx= ID ':'
                    {
                    ctx=(Token)match(input,ID,FOLLOW_ID_in_atom333); 

                    match(input,25,FOLLOW_25_in_atom335); 

                    context_id=(ctx!=null?ctx.getText():null);

                    }
                    break;

            }


            name=(Token)match(input,ID,FOLLOW_ID_in_atom346); 

            atom_name=(name!=null?name.getText():null);

            // /home/as/incman/gerald_weidinger_thesis/WOC - Wings of Change/antlr/woc.g:172:3: ( '(' termlist ')' )?
            int alt13=2;
            int LA13_0 = input.LA(1);

            if ( (LA13_0==17) ) {
                alt13=1;
            }
            switch (alt13) {
                case 1 :
                    // /home/as/incman/gerald_weidinger_thesis/WOC - Wings of Change/antlr/woc.g:172:5: '(' termlist ')'
                    {
                    match(input,17,FOLLOW_17_in_atom354); 

                    pushFollow(FOLLOW_termlist_in_atom356);
                    termlist6=termlist();

                    state._fsp--;


                    match(input,18,FOLLOW_18_in_atom358); 

                    terms=(Term[])termlist6.toArray(new Term[termlist6.size()]);

                    }
                    break;

            }


            at =Atom.getAtom(atom_name, terms.length, terms);

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
    // /home/as/incman/gerald_weidinger_thesis/WOC - Wings of Change/antlr/woc.g:176:1: termlist returns [ArrayList<Term> termlist] : t1= term ( ',' tn= term )* ;
    public final ArrayList<Term> termlist() throws RecognitionException {
        ArrayList<Term> termlist = null;


        Term t1 =null;

        Term tn =null;


         termlist = new ArrayList<Term>();
        try {
            // /home/as/incman/gerald_weidinger_thesis/WOC - Wings of Change/antlr/woc.g:178:2: (t1= term ( ',' tn= term )* )
            // /home/as/incman/gerald_weidinger_thesis/WOC - Wings of Change/antlr/woc.g:178:4: t1= term ( ',' tn= term )*
            {
            pushFollow(FOLLOW_term_in_termlist387);
            t1=term();

            state._fsp--;


            termlist.add(t1);

            // /home/as/incman/gerald_weidinger_thesis/WOC - Wings of Change/antlr/woc.g:179:3: ( ',' tn= term )*
            loop14:
            do {
                int alt14=2;
                int LA14_0 = input.LA(1);

                if ( (LA14_0==21) ) {
                    alt14=1;
                }


                switch (alt14) {
            	case 1 :
            	    // /home/as/incman/gerald_weidinger_thesis/WOC - Wings of Change/antlr/woc.g:179:4: ',' tn= term
            	    {
            	    match(input,21,FOLLOW_21_in_termlist394); 

            	    pushFollow(FOLLOW_term_in_termlist398);
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
    // /home/as/incman/gerald_weidinger_thesis/WOC - Wings of Change/antlr/woc.g:182:1: term returns [Term term] : ( constant | VAR | function );
    public final Term term() throws RecognitionException {
        Term term = null;


        Token VAR8=null;
        Constant constant7 =null;

        wocParser.function_return function9 =null;


        try {
            // /home/as/incman/gerald_weidinger_thesis/WOC - Wings of Change/antlr/woc.g:183:2: ( constant | VAR | function )
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
                    // /home/as/incman/gerald_weidinger_thesis/WOC - Wings of Change/antlr/woc.g:183:4: constant
                    {
                    pushFollow(FOLLOW_constant_in_term417);
                    constant7=constant();

                    state._fsp--;


                    term =constant7;

                    }
                    break;
                case 2 :
                    // /home/as/incman/gerald_weidinger_thesis/WOC - Wings of Change/antlr/woc.g:184:4: VAR
                    {
                    VAR8=(Token)match(input,VAR,FOLLOW_VAR_in_term424); 

                    term =Variable.getVariable((VAR8!=null?VAR8.getText():null));

                    }
                    break;
                case 3 :
                    // /home/as/incman/gerald_weidinger_thesis/WOC - Wings of Change/antlr/woc.g:185:4: function
                    {
                    pushFollow(FOLLOW_function_in_term431);
                    function9=function();

                    state._fsp--;


                    term =FuncTerm.getFuncTerm((function9!=null?function9.name:null),(function9!=null?function9.terms:null));

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
    // /home/as/incman/gerald_weidinger_thesis/WOC - Wings of Change/antlr/woc.g:188:1: function returns [String name, ArrayList<Term> terms] : ID '(' termlist ')' ;
    public final wocParser.function_return function() throws RecognitionException {
        wocParser.function_return retval = new wocParser.function_return();
        retval.start = input.LT(1);


        Token ID10=null;
        ArrayList<Term> termlist11 =null;


        try {
            // /home/as/incman/gerald_weidinger_thesis/WOC - Wings of Change/antlr/woc.g:189:2: ( ID '(' termlist ')' )
            // /home/as/incman/gerald_weidinger_thesis/WOC - Wings of Change/antlr/woc.g:189:4: ID '(' termlist ')'
            {
            ID10=(Token)match(input,ID,FOLLOW_ID_in_function448); 

            match(input,17,FOLLOW_17_in_function450); 

            pushFollow(FOLLOW_termlist_in_function452);
            termlist11=termlist();

            state._fsp--;


            match(input,18,FOLLOW_18_in_function454); 

            retval.name =(ID10!=null?ID10.getText():null); retval.terms =termlist11;

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
    // /home/as/incman/gerald_weidinger_thesis/WOC - Wings of Change/antlr/woc.g:192:1: constant returns [Constant constnt] : ( ID | INT );
    public final Constant constant() throws RecognitionException {
        Constant constnt = null;


        Token ID12=null;
        Token INT13=null;

        try {
            // /home/as/incman/gerald_weidinger_thesis/WOC - Wings of Change/antlr/woc.g:193:2: ( ID | INT )
            int alt16=2;
            int LA16_0 = input.LA(1);

            if ( (LA16_0==ID) ) {
                alt16=1;
            }
            else if ( (LA16_0==INT) ) {
                alt16=2;
            }
            else {
                NoViableAltException nvae =
                    new NoViableAltException("", 16, 0, input);

                throw nvae;

            }
            switch (alt16) {
                case 1 :
                    // /home/as/incman/gerald_weidinger_thesis/WOC - Wings of Change/antlr/woc.g:193:4: ID
                    {
                    ID12=(Token)match(input,ID,FOLLOW_ID_in_constant471); 

                    constnt =Constant.getConstant((ID12!=null?ID12.getText():null));

                    }
                    break;
                case 2 :
                    // /home/as/incman/gerald_weidinger_thesis/WOC - Wings of Change/antlr/woc.g:194:4: INT
                    {
                    INT13=(Token)match(input,INT,FOLLOW_INT_in_constant478); 

                    constnt =Constant.getConstant((INT13!=null?INT13.getText():null));

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

    // Delegated rules


 

    public static final BitSet FOLLOW_rule_or_fact_in_woc_program40 = new BitSet(new long[]{0x0000000004800402L});
    public static final BitSet FOLLOW_atom_in_rule_or_fact64 = new BitSet(new long[]{0x0000000004800000L});
    public static final BitSet FOLLOW_26_in_rule_or_fact72 = new BitSet(new long[]{0x0000000010008400L});
    public static final BitSet FOLLOW_body_in_rule_or_fact76 = new BitSet(new long[]{0x0000000000800000L});
    public static final BitSet FOLLOW_23_in_rule_or_fact79 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_23_in_rule_or_fact86 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_literal_in_body102 = new BitSet(new long[]{0x0000000000200002L});
    public static final BitSet FOLLOW_21_in_body106 = new BitSet(new long[]{0x0000000010008400L});
    public static final BitSet FOLLOW_literal_in_body108 = new BitSet(new long[]{0x0000000000200002L});
    public static final BitSet FOLLOW_atom_in_literal126 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_28_in_literal133 = new BitSet(new long[]{0x0000000000000400L});
    public static final BitSet FOLLOW_atom_in_literal137 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_operation_in_literal144 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_VAR_in_operation163 = new BitSet(new long[]{0x0000000000000040L});
    public static final BitSet FOLLOW_COMP_in_operation165 = new BitSet(new long[]{0x0000000000028800L});
    public static final BitSet FOLLOW_expression_in_operation169 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_VAR_in_operation178 = new BitSet(new long[]{0x0000000008000000L});
    public static final BitSet FOLLOW_27_in_operation180 = new BitSet(new long[]{0x0000000000028800L});
    public static final BitSet FOLLOW_expression_in_operation184 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_mult_expression_in_expression203 = new BitSet(new long[]{0x0000000000500002L});
    public static final BitSet FOLLOW_20_in_expression208 = new BitSet(new long[]{0x0000000000028800L});
    public static final BitSet FOLLOW_22_in_expression213 = new BitSet(new long[]{0x0000000000028800L});
    public static final BitSet FOLLOW_mult_expression_in_expression221 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_primary_expression_in_mult_expression247 = new BitSet(new long[]{0x0000000001080002L});
    public static final BitSet FOLLOW_19_in_mult_expression252 = new BitSet(new long[]{0x0000000000028800L});
    public static final BitSet FOLLOW_24_in_mult_expression257 = new BitSet(new long[]{0x0000000000028800L});
    public static final BitSet FOLLOW_primary_expression_in_mult_expression267 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_INT_in_primary_expression291 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_VAR_in_primary_expression298 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_17_in_primary_expression305 = new BitSet(new long[]{0x0000000000028800L});
    public static final BitSet FOLLOW_expression_in_primary_expression307 = new BitSet(new long[]{0x0000000000040000L});
    public static final BitSet FOLLOW_18_in_primary_expression309 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_ID_in_atom333 = new BitSet(new long[]{0x0000000002000000L});
    public static final BitSet FOLLOW_25_in_atom335 = new BitSet(new long[]{0x0000000000000400L});
    public static final BitSet FOLLOW_ID_in_atom346 = new BitSet(new long[]{0x0000000000020002L});
    public static final BitSet FOLLOW_17_in_atom354 = new BitSet(new long[]{0x0000000000008C00L});
    public static final BitSet FOLLOW_termlist_in_atom356 = new BitSet(new long[]{0x0000000000040000L});
    public static final BitSet FOLLOW_18_in_atom358 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_term_in_termlist387 = new BitSet(new long[]{0x0000000000200002L});
    public static final BitSet FOLLOW_21_in_termlist394 = new BitSet(new long[]{0x0000000000008C00L});
    public static final BitSet FOLLOW_term_in_termlist398 = new BitSet(new long[]{0x0000000000200002L});
    public static final BitSet FOLLOW_constant_in_term417 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_VAR_in_term424 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_function_in_term431 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_ID_in_function448 = new BitSet(new long[]{0x0000000000020000L});
    public static final BitSet FOLLOW_17_in_function450 = new BitSet(new long[]{0x0000000000008C00L});
    public static final BitSet FOLLOW_termlist_in_function452 = new BitSet(new long[]{0x0000000000040000L});
    public static final BitSet FOLLOW_18_in_function454 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_ID_in_constant471 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_INT_in_constant478 = new BitSet(new long[]{0x0000000000000002L});

}