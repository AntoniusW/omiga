// $ANTLR 3.4 /home/as/incman/gerald_weidinger_thesis/WOC - Wings of Change/antlr/woc.g 2012-04-23 13:05:58

package parser.antlr;

import java.util.ArrayList;


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





    // $ANTLR start "woc_program"
    // /home/as/incman/gerald_weidinger_thesis/WOC - Wings of Change/antlr/woc.g:110:1: woc_program : ( rule_or_fact )* ;
    public final void woc_program() throws RecognitionException {

        asp_rules = new ArrayList<ParserRule>();
        asp_facts = new ArrayList<ParserAtom>(); 
        try {
            // /home/as/incman/gerald_weidinger_thesis/WOC - Wings of Change/antlr/woc.g:114:2: ( ( rule_or_fact )* )
            // /home/as/incman/gerald_weidinger_thesis/WOC - Wings of Change/antlr/woc.g:114:4: ( rule_or_fact )*
            {
            // /home/as/incman/gerald_weidinger_thesis/WOC - Wings of Change/antlr/woc.g:114:4: ( rule_or_fact )*
            loop1:
            do {
                int alt1=2;
                int LA1_0 = input.LA(1);

                if ( (LA1_0==ID) ) {
                    alt1=1;
                }


                switch (alt1) {
            	case 1 :
            	    // /home/as/incman/gerald_weidinger_thesis/WOC - Wings of Change/antlr/woc.g:114:4: rule_or_fact
            	    {
            	    pushFollow(FOLLOW_rule_or_fact_in_woc_program33);
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
    // /home/as/incman/gerald_weidinger_thesis/WOC - Wings of Change/antlr/woc.g:117:1: rule_or_fact : atom ( ':-' body[r] '.' | '.' ) ;
    public final void rule_or_fact() throws RecognitionException {
        ParserAtom atom1 =null;


        try {
            // /home/as/incman/gerald_weidinger_thesis/WOC - Wings of Change/antlr/woc.g:118:2: ( atom ( ':-' body[r] '.' | '.' ) )
            // /home/as/incman/gerald_weidinger_thesis/WOC - Wings of Change/antlr/woc.g:118:4: atom ( ':-' body[r] '.' | '.' )
            {
            pushFollow(FOLLOW_atom_in_rule_or_fact46);
            atom1=atom();

            state._fsp--;


            // /home/as/incman/gerald_weidinger_thesis/WOC - Wings of Change/antlr/woc.g:118:9: ( ':-' body[r] '.' | '.' )
            int alt2=2;
            int LA2_0 = input.LA(1);

            if ( (LA2_0==26) ) {
                alt2=1;
            }
            else if ( (LA2_0==23) ) {
                alt2=2;
            }
            else {
                NoViableAltException nvae =
                    new NoViableAltException("", 2, 0, input);

                throw nvae;

            }
            switch (alt2) {
                case 1 :
                    // /home/as/incman/gerald_weidinger_thesis/WOC - Wings of Change/antlr/woc.g:118:10: ':-' body[r] '.'
                    {
                    match(input,26,FOLLOW_26_in_rule_or_fact49); 

                    ParserRule r = new ParserRule();

                    pushFollow(FOLLOW_body_in_rule_or_fact53);
                    body(r);

                    state._fsp--;


                    match(input,23,FOLLOW_23_in_rule_or_fact56); 

                    r.head=atom1; asp_rules.add(r);

                    }
                    break;
                case 2 :
                    // /home/as/incman/gerald_weidinger_thesis/WOC - Wings of Change/antlr/woc.g:119:4: '.'
                    {
                    match(input,23,FOLLOW_23_in_rule_or_fact63); 

                    asp_facts.add(atom1);

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
    // /home/as/incman/gerald_weidinger_thesis/WOC - Wings of Change/antlr/woc.g:122:1: body[ParserRule r] : literal[$r] ( ',' literal[$r] )* ;
    public final void body(ParserRule r) throws RecognitionException {
        try {
            // /home/as/incman/gerald_weidinger_thesis/WOC - Wings of Change/antlr/woc.g:123:2: ( literal[$r] ( ',' literal[$r] )* )
            // /home/as/incman/gerald_weidinger_thesis/WOC - Wings of Change/antlr/woc.g:123:4: literal[$r] ( ',' literal[$r] )*
            {
            pushFollow(FOLLOW_literal_in_body79);
            literal(r);

            state._fsp--;


            // /home/as/incman/gerald_weidinger_thesis/WOC - Wings of Change/antlr/woc.g:123:16: ( ',' literal[$r] )*
            loop3:
            do {
                int alt3=2;
                int LA3_0 = input.LA(1);

                if ( (LA3_0==21) ) {
                    alt3=1;
                }


                switch (alt3) {
            	case 1 :
            	    // /home/as/incman/gerald_weidinger_thesis/WOC - Wings of Change/antlr/woc.g:123:17: ',' literal[$r]
            	    {
            	    match(input,21,FOLLOW_21_in_body83); 

            	    pushFollow(FOLLOW_literal_in_body85);
            	    literal(r);

            	    state._fsp--;


            	    }
            	    break;

            	default :
            	    break loop3;
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
    // /home/as/incman/gerald_weidinger_thesis/WOC - Wings of Change/antlr/woc.g:126:1: literal[ParserRule r] : (a1= atom | 'not' a2= atom | operation );
    public final void literal(ParserRule r) throws RecognitionException {
        ParserAtom a1 =null;

        ParserAtom a2 =null;

        ParserOperation operation2 =null;


        try {
            // /home/as/incman/gerald_weidinger_thesis/WOC - Wings of Change/antlr/woc.g:127:2: (a1= atom | 'not' a2= atom | operation )
            int alt4=3;
            switch ( input.LA(1) ) {
            case ID:
                {
                alt4=1;
                }
                break;
            case 28:
                {
                alt4=2;
                }
                break;
            case VAR:
                {
                alt4=3;
                }
                break;
            default:
                NoViableAltException nvae =
                    new NoViableAltException("", 4, 0, input);

                throw nvae;

            }

            switch (alt4) {
                case 1 :
                    // /home/as/incman/gerald_weidinger_thesis/WOC - Wings of Change/antlr/woc.g:127:4: a1= atom
                    {
                    pushFollow(FOLLOW_atom_in_literal103);
                    a1=atom();

                    state._fsp--;


                    r.posAtoms.add(a1);

                    }
                    break;
                case 2 :
                    // /home/as/incman/gerald_weidinger_thesis/WOC - Wings of Change/antlr/woc.g:128:4: 'not' a2= atom
                    {
                    match(input,28,FOLLOW_28_in_literal110); 

                    pushFollow(FOLLOW_atom_in_literal114);
                    a2=atom();

                    state._fsp--;


                    r.negAtoms.add(a2);

                    }
                    break;
                case 3 :
                    // /home/as/incman/gerald_weidinger_thesis/WOC - Wings of Change/antlr/woc.g:129:4: operation
                    {
                    pushFollow(FOLLOW_operation_in_literal121);
                    operation2=operation();

                    state._fsp--;


                    r.operations.add(operation2);

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
    // /home/as/incman/gerald_weidinger_thesis/WOC - Wings of Change/antlr/woc.g:132:1: operation returns [ParserOperation op] : (v1= VAR COMP e1= expression |v2= VAR 'is' e2= expression );
    public final ParserOperation operation() throws RecognitionException {
        ParserOperation op = null;


        Token v1=null;
        Token v2=null;
        ParserOperation e1 =null;

        ParserOperation e2 =null;


        try {
            // /home/as/incman/gerald_weidinger_thesis/WOC - Wings of Change/antlr/woc.g:133:2: (v1= VAR COMP e1= expression |v2= VAR 'is' e2= expression )
            int alt5=2;
            int LA5_0 = input.LA(1);

            if ( (LA5_0==VAR) ) {
                int LA5_1 = input.LA(2);

                if ( (LA5_1==COMP) ) {
                    alt5=1;
                }
                else if ( (LA5_1==27) ) {
                    alt5=2;
                }
                else {
                    NoViableAltException nvae =
                        new NoViableAltException("", 5, 1, input);

                    throw nvae;

                }
            }
            else {
                NoViableAltException nvae =
                    new NoViableAltException("", 5, 0, input);

                throw nvae;

            }
            switch (alt5) {
                case 1 :
                    // /home/as/incman/gerald_weidinger_thesis/WOC - Wings of Change/antlr/woc.g:133:4: v1= VAR COMP e1= expression
                    {
                    v1=(Token)match(input,VAR,FOLLOW_VAR_in_operation140); 

                    match(input,COMP,FOLLOW_COMP_in_operation142); 

                    pushFollow(FOLLOW_expression_in_operation146);
                    e1=expression();

                    state._fsp--;


                    op =new ParserComparison(new OpVar((v1!=null?v1.getText():null)),e1);

                    }
                    break;
                case 2 :
                    // /home/as/incman/gerald_weidinger_thesis/WOC - Wings of Change/antlr/woc.g:134:4: v2= VAR 'is' e2= expression
                    {
                    v2=(Token)match(input,VAR,FOLLOW_VAR_in_operation155); 

                    match(input,27,FOLLOW_27_in_operation157); 

                    pushFollow(FOLLOW_expression_in_operation161);
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
    // /home/as/incman/gerald_weidinger_thesis/WOC - Wings of Change/antlr/woc.g:137:1: expression returns [ParserOperation op] : l= mult_expression ( ( ( '+' | '-' ) r= mult_expression ) |) ;
    public final ParserOperation expression() throws RecognitionException {
        ParserOperation op = null;


        ParserOperation l =null;

        ParserOperation r =null;


        try {
            // /home/as/incman/gerald_weidinger_thesis/WOC - Wings of Change/antlr/woc.g:138:2: (l= mult_expression ( ( ( '+' | '-' ) r= mult_expression ) |) )
            // /home/as/incman/gerald_weidinger_thesis/WOC - Wings of Change/antlr/woc.g:138:4: l= mult_expression ( ( ( '+' | '-' ) r= mult_expression ) |)
            {
            pushFollow(FOLLOW_mult_expression_in_expression180);
            l=mult_expression();

            state._fsp--;


            // /home/as/incman/gerald_weidinger_thesis/WOC - Wings of Change/antlr/woc.g:138:22: ( ( ( '+' | '-' ) r= mult_expression ) |)
            int alt7=2;
            int LA7_0 = input.LA(1);

            if ( (LA7_0==20||LA7_0==22) ) {
                alt7=1;
            }
            else if ( (LA7_0==18||LA7_0==21||LA7_0==23) ) {
                alt7=2;
            }
            else {
                NoViableAltException nvae =
                    new NoViableAltException("", 7, 0, input);

                throw nvae;

            }
            switch (alt7) {
                case 1 :
                    // /home/as/incman/gerald_weidinger_thesis/WOC - Wings of Change/antlr/woc.g:138:23: ( ( '+' | '-' ) r= mult_expression )
                    {
                    // /home/as/incman/gerald_weidinger_thesis/WOC - Wings of Change/antlr/woc.g:138:23: ( ( '+' | '-' ) r= mult_expression )
                    // /home/as/incman/gerald_weidinger_thesis/WOC - Wings of Change/antlr/woc.g:138:24: ( '+' | '-' ) r= mult_expression
                    {
                    // /home/as/incman/gerald_weidinger_thesis/WOC - Wings of Change/antlr/woc.g:138:24: ( '+' | '-' )
                    int alt6=2;
                    int LA6_0 = input.LA(1);

                    if ( (LA6_0==20) ) {
                        alt6=1;
                    }
                    else if ( (LA6_0==22) ) {
                        alt6=2;
                    }
                    else {
                        NoViableAltException nvae =
                            new NoViableAltException("", 6, 0, input);

                        throw nvae;

                    }
                    switch (alt6) {
                        case 1 :
                            // /home/as/incman/gerald_weidinger_thesis/WOC - Wings of Change/antlr/woc.g:138:25: '+'
                            {
                            match(input,20,FOLLOW_20_in_expression185); 

                            op =new OpPlus();

                            }
                            break;
                        case 2 :
                            // /home/as/incman/gerald_weidinger_thesis/WOC - Wings of Change/antlr/woc.g:138:50: '-'
                            {
                            match(input,22,FOLLOW_22_in_expression190); 

                            op =new OpMinus();

                            }
                            break;

                    }


                    pushFollow(FOLLOW_mult_expression_in_expression198);
                    r=mult_expression();

                    state._fsp--;


                     ((BinOp)op).left=l; ((BinOp)op).right=r; 

                    }


                    }
                    break;
                case 2 :
                    // /home/as/incman/gerald_weidinger_thesis/WOC - Wings of Change/antlr/woc.g:140:4: 
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
    // /home/as/incman/gerald_weidinger_thesis/WOC - Wings of Change/antlr/woc.g:143:1: mult_expression returns [ParserOperation op] : l= primary_expression ( ( ( '*' | '/' ) r= primary_expression ) |) ;
    public final ParserOperation mult_expression() throws RecognitionException {
        ParserOperation op = null;


        ParserOperation l =null;

        ParserOperation r =null;


        try {
            // /home/as/incman/gerald_weidinger_thesis/WOC - Wings of Change/antlr/woc.g:144:2: (l= primary_expression ( ( ( '*' | '/' ) r= primary_expression ) |) )
            // /home/as/incman/gerald_weidinger_thesis/WOC - Wings of Change/antlr/woc.g:144:4: l= primary_expression ( ( ( '*' | '/' ) r= primary_expression ) |)
            {
            pushFollow(FOLLOW_primary_expression_in_mult_expression224);
            l=primary_expression();

            state._fsp--;


            // /home/as/incman/gerald_weidinger_thesis/WOC - Wings of Change/antlr/woc.g:144:25: ( ( ( '*' | '/' ) r= primary_expression ) |)
            int alt9=2;
            int LA9_0 = input.LA(1);

            if ( (LA9_0==19||LA9_0==24) ) {
                alt9=1;
            }
            else if ( (LA9_0==18||(LA9_0 >= 20 && LA9_0 <= 23)) ) {
                alt9=2;
            }
            else {
                NoViableAltException nvae =
                    new NoViableAltException("", 9, 0, input);

                throw nvae;

            }
            switch (alt9) {
                case 1 :
                    // /home/as/incman/gerald_weidinger_thesis/WOC - Wings of Change/antlr/woc.g:144:26: ( ( '*' | '/' ) r= primary_expression )
                    {
                    // /home/as/incman/gerald_weidinger_thesis/WOC - Wings of Change/antlr/woc.g:144:26: ( ( '*' | '/' ) r= primary_expression )
                    // /home/as/incman/gerald_weidinger_thesis/WOC - Wings of Change/antlr/woc.g:144:27: ( '*' | '/' ) r= primary_expression
                    {
                    // /home/as/incman/gerald_weidinger_thesis/WOC - Wings of Change/antlr/woc.g:144:27: ( '*' | '/' )
                    int alt8=2;
                    int LA8_0 = input.LA(1);

                    if ( (LA8_0==19) ) {
                        alt8=1;
                    }
                    else if ( (LA8_0==24) ) {
                        alt8=2;
                    }
                    else {
                        NoViableAltException nvae =
                            new NoViableAltException("", 8, 0, input);

                        throw nvae;

                    }
                    switch (alt8) {
                        case 1 :
                            // /home/as/incman/gerald_weidinger_thesis/WOC - Wings of Change/antlr/woc.g:144:28: '*'
                            {
                            match(input,19,FOLLOW_19_in_mult_expression229); 

                            op =new OpMult(); 

                            }
                            break;
                        case 2 :
                            // /home/as/incman/gerald_weidinger_thesis/WOC - Wings of Change/antlr/woc.g:144:54: '/'
                            {
                            match(input,24,FOLLOW_24_in_mult_expression234); 

                            op =new OpDiv();

                            }
                            break;

                    }


                    pushFollow(FOLLOW_primary_expression_in_mult_expression244);
                    r=primary_expression();

                    state._fsp--;


                     ((BinOp)op).left=l; ((BinOp)op).right=r; 

                    }


                    }
                    break;
                case 2 :
                    // /home/as/incman/gerald_weidinger_thesis/WOC - Wings of Change/antlr/woc.g:146:4: 
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
    // /home/as/incman/gerald_weidinger_thesis/WOC - Wings of Change/antlr/woc.g:149:1: primary_expression returns [ParserOperation op] : ( INT | VAR | '(' expression ')' );
    public final ParserOperation primary_expression() throws RecognitionException {
        ParserOperation op = null;


        Token INT3=null;
        Token VAR4=null;
        ParserOperation expression5 =null;


        try {
            // /home/as/incman/gerald_weidinger_thesis/WOC - Wings of Change/antlr/woc.g:150:2: ( INT | VAR | '(' expression ')' )
            int alt10=3;
            switch ( input.LA(1) ) {
            case INT:
                {
                alt10=1;
                }
                break;
            case VAR:
                {
                alt10=2;
                }
                break;
            case 17:
                {
                alt10=3;
                }
                break;
            default:
                NoViableAltException nvae =
                    new NoViableAltException("", 10, 0, input);

                throw nvae;

            }

            switch (alt10) {
                case 1 :
                    // /home/as/incman/gerald_weidinger_thesis/WOC - Wings of Change/antlr/woc.g:150:4: INT
                    {
                    INT3=(Token)match(input,INT,FOLLOW_INT_in_primary_expression268); 

                    op =new OpInt((INT3!=null?INT3.getText():null));

                    }
                    break;
                case 2 :
                    // /home/as/incman/gerald_weidinger_thesis/WOC - Wings of Change/antlr/woc.g:151:4: VAR
                    {
                    VAR4=(Token)match(input,VAR,FOLLOW_VAR_in_primary_expression275); 

                    op =new OpVar((VAR4!=null?VAR4.getText():null));

                    }
                    break;
                case 3 :
                    // /home/as/incman/gerald_weidinger_thesis/WOC - Wings of Change/antlr/woc.g:152:4: '(' expression ')'
                    {
                    match(input,17,FOLLOW_17_in_primary_expression282); 

                    pushFollow(FOLLOW_expression_in_primary_expression284);
                    expression5=expression();

                    state._fsp--;


                    match(input,18,FOLLOW_18_in_primary_expression286); 

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
    // /home/as/incman/gerald_weidinger_thesis/WOC - Wings of Change/antlr/woc.g:155:1: atom returns [ParserAtom at] : (ctx= ID ':' )? name= ID ( '(' termlist ')' )? ;
    public final ParserAtom atom() throws RecognitionException {
        ParserAtom at = null;


        Token ctx=null;
        Token name=null;
        ArrayList<ParserTerm> termlist6 =null;


         at = new ParserAtom(); 
        try {
            // /home/as/incman/gerald_weidinger_thesis/WOC - Wings of Change/antlr/woc.g:157:2: ( (ctx= ID ':' )? name= ID ( '(' termlist ')' )? )
            // /home/as/incman/gerald_weidinger_thesis/WOC - Wings of Change/antlr/woc.g:157:4: (ctx= ID ':' )? name= ID ( '(' termlist ')' )?
            {
            // /home/as/incman/gerald_weidinger_thesis/WOC - Wings of Change/antlr/woc.g:157:4: (ctx= ID ':' )?
            int alt11=2;
            int LA11_0 = input.LA(1);

            if ( (LA11_0==ID) ) {
                int LA11_1 = input.LA(2);

                if ( (LA11_1==25) ) {
                    alt11=1;
                }
            }
            switch (alt11) {
                case 1 :
                    // /home/as/incman/gerald_weidinger_thesis/WOC - Wings of Change/antlr/woc.g:157:5: ctx= ID ':'
                    {
                    ctx=(Token)match(input,ID,FOLLOW_ID_in_atom310); 

                    match(input,25,FOLLOW_25_in_atom312); 

                    at.context_id=(ctx!=null?ctx.getText():null);

                    }
                    break;

            }


            name=(Token)match(input,ID,FOLLOW_ID_in_atom323); 

            at.name=(name!=null?name.getText():null);

            // /home/as/incman/gerald_weidinger_thesis/WOC - Wings of Change/antlr/woc.g:158:34: ( '(' termlist ')' )?
            int alt12=2;
            int LA12_0 = input.LA(1);

            if ( (LA12_0==17) ) {
                alt12=1;
            }
            switch (alt12) {
                case 1 :
                    // /home/as/incman/gerald_weidinger_thesis/WOC - Wings of Change/antlr/woc.g:158:36: '(' termlist ')'
                    {
                    match(input,17,FOLLOW_17_in_atom329); 

                    pushFollow(FOLLOW_termlist_in_atom331);
                    termlist6=termlist();

                    state._fsp--;


                    match(input,18,FOLLOW_18_in_atom333); 

                    at.termlist=termlist6;

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
        return at;
    }
    // $ANTLR end "atom"



    // $ANTLR start "termlist"
    // /home/as/incman/gerald_weidinger_thesis/WOC - Wings of Change/antlr/woc.g:161:1: termlist returns [ArrayList<ParserTerm> termlist] : t1= term ( ',' tn= term )* ;
    public final ArrayList<ParserTerm> termlist() throws RecognitionException {
        ArrayList<ParserTerm> termlist = null;


        ParserTerm t1 =null;

        ParserTerm tn =null;


         termlist = new ArrayList<ParserTerm>();
        try {
            // /home/as/incman/gerald_weidinger_thesis/WOC - Wings of Change/antlr/woc.g:163:2: (t1= term ( ',' tn= term )* )
            // /home/as/incman/gerald_weidinger_thesis/WOC - Wings of Change/antlr/woc.g:163:4: t1= term ( ',' tn= term )*
            {
            pushFollow(FOLLOW_term_in_termlist358);
            t1=term();

            state._fsp--;


            termlist.add(t1);

            // /home/as/incman/gerald_weidinger_thesis/WOC - Wings of Change/antlr/woc.g:164:3: ( ',' tn= term )*
            loop13:
            do {
                int alt13=2;
                int LA13_0 = input.LA(1);

                if ( (LA13_0==21) ) {
                    alt13=1;
                }


                switch (alt13) {
            	case 1 :
            	    // /home/as/incman/gerald_weidinger_thesis/WOC - Wings of Change/antlr/woc.g:164:4: ',' tn= term
            	    {
            	    match(input,21,FOLLOW_21_in_termlist365); 

            	    pushFollow(FOLLOW_term_in_termlist369);
            	    tn=term();

            	    state._fsp--;


            	    termlist.add(tn);

            	    }
            	    break;

            	default :
            	    break loop13;
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
    // /home/as/incman/gerald_weidinger_thesis/WOC - Wings of Change/antlr/woc.g:167:1: term returns [ParserTerm term] : ( constant | VAR | function );
    public final ParserTerm term() throws RecognitionException {
        ParserTerm term = null;


        Token VAR8=null;
        wocParser.constant_return constant7 =null;

        ParserFunction function9 =null;


        try {
            // /home/as/incman/gerald_weidinger_thesis/WOC - Wings of Change/antlr/woc.g:168:2: ( constant | VAR | function )
            int alt14=3;
            switch ( input.LA(1) ) {
            case ID:
                {
                int LA14_1 = input.LA(2);

                if ( (LA14_1==17) ) {
                    alt14=3;
                }
                else if ( (LA14_1==18||LA14_1==21) ) {
                    alt14=1;
                }
                else {
                    NoViableAltException nvae =
                        new NoViableAltException("", 14, 1, input);

                    throw nvae;

                }
                }
                break;
            case VAR:
                {
                alt14=2;
                }
                break;
            case INT:
                {
                alt14=1;
                }
                break;
            default:
                NoViableAltException nvae =
                    new NoViableAltException("", 14, 0, input);

                throw nvae;

            }

            switch (alt14) {
                case 1 :
                    // /home/as/incman/gerald_weidinger_thesis/WOC - Wings of Change/antlr/woc.g:168:4: constant
                    {
                    pushFollow(FOLLOW_constant_in_term388);
                    constant7=constant();

                    state._fsp--;


                    term=new ParserConstant((constant7!=null?input.toString(constant7.start,constant7.stop):null),true);

                    }
                    break;
                case 2 :
                    // /home/as/incman/gerald_weidinger_thesis/WOC - Wings of Change/antlr/woc.g:169:4: VAR
                    {
                    VAR8=(Token)match(input,VAR,FOLLOW_VAR_in_term395); 

                    term=new ParserVariable((VAR8!=null?VAR8.getText():null));

                    }
                    break;
                case 3 :
                    // /home/as/incman/gerald_weidinger_thesis/WOC - Wings of Change/antlr/woc.g:170:4: function
                    {
                    pushFollow(FOLLOW_function_in_term402);
                    function9=function();

                    state._fsp--;


                    term=function9;

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



    // $ANTLR start "function"
    // /home/as/incman/gerald_weidinger_thesis/WOC - Wings of Change/antlr/woc.g:173:1: function returns [ParserFunction func] : ID '(' termlist ')' ;
    public final ParserFunction function() throws RecognitionException {
        ParserFunction func = null;


        Token ID10=null;
        ArrayList<ParserTerm> termlist11 =null;


        try {
            // /home/as/incman/gerald_weidinger_thesis/WOC - Wings of Change/antlr/woc.g:174:2: ( ID '(' termlist ')' )
            // /home/as/incman/gerald_weidinger_thesis/WOC - Wings of Change/antlr/woc.g:174:4: ID '(' termlist ')'
            {
            ID10=(Token)match(input,ID,FOLLOW_ID_in_function419); 

            match(input,17,FOLLOW_17_in_function421); 

            pushFollow(FOLLOW_termlist_in_function423);
            termlist11=termlist();

            state._fsp--;


            match(input,18,FOLLOW_18_in_function425); 

            func=new ParserFunction((ID10!=null?ID10.getText():null),false,termlist11.size(),termlist11);

            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }

        finally {
        	// do for sure before leaving
        }
        return func;
    }
    // $ANTLR end "function"


    public static class constant_return extends ParserRuleReturnScope {
    };


    // $ANTLR start "constant"
    // /home/as/incman/gerald_weidinger_thesis/WOC - Wings of Change/antlr/woc.g:177:1: constant : ( ID | INT );
    public final wocParser.constant_return constant() throws RecognitionException {
        wocParser.constant_return retval = new wocParser.constant_return();
        retval.start = input.LT(1);


        try {
            // /home/as/incman/gerald_weidinger_thesis/WOC - Wings of Change/antlr/woc.g:177:9: ( ID | INT )
            // /home/as/incman/gerald_weidinger_thesis/WOC - Wings of Change/antlr/woc.g:
            {
            if ( (input.LA(1) >= ID && input.LA(1) <= INT) ) {
                input.consume();
                state.errorRecovery=false;
            }
            else {
                MismatchedSetException mse = new MismatchedSetException(null,input);
                throw mse;
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
    // $ANTLR end "constant"

    // Delegated rules


 

    public static final BitSet FOLLOW_rule_or_fact_in_woc_program33 = new BitSet(new long[]{0x0000000000000402L});
    public static final BitSet FOLLOW_atom_in_rule_or_fact46 = new BitSet(new long[]{0x0000000004800000L});
    public static final BitSet FOLLOW_26_in_rule_or_fact49 = new BitSet(new long[]{0x0000000010008400L});
    public static final BitSet FOLLOW_body_in_rule_or_fact53 = new BitSet(new long[]{0x0000000000800000L});
    public static final BitSet FOLLOW_23_in_rule_or_fact56 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_23_in_rule_or_fact63 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_literal_in_body79 = new BitSet(new long[]{0x0000000000200002L});
    public static final BitSet FOLLOW_21_in_body83 = new BitSet(new long[]{0x0000000010008400L});
    public static final BitSet FOLLOW_literal_in_body85 = new BitSet(new long[]{0x0000000000200002L});
    public static final BitSet FOLLOW_atom_in_literal103 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_28_in_literal110 = new BitSet(new long[]{0x0000000000000400L});
    public static final BitSet FOLLOW_atom_in_literal114 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_operation_in_literal121 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_VAR_in_operation140 = new BitSet(new long[]{0x0000000000000040L});
    public static final BitSet FOLLOW_COMP_in_operation142 = new BitSet(new long[]{0x0000000000028800L});
    public static final BitSet FOLLOW_expression_in_operation146 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_VAR_in_operation155 = new BitSet(new long[]{0x0000000008000000L});
    public static final BitSet FOLLOW_27_in_operation157 = new BitSet(new long[]{0x0000000000028800L});
    public static final BitSet FOLLOW_expression_in_operation161 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_mult_expression_in_expression180 = new BitSet(new long[]{0x0000000000500002L});
    public static final BitSet FOLLOW_20_in_expression185 = new BitSet(new long[]{0x0000000000028800L});
    public static final BitSet FOLLOW_22_in_expression190 = new BitSet(new long[]{0x0000000000028800L});
    public static final BitSet FOLLOW_mult_expression_in_expression198 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_primary_expression_in_mult_expression224 = new BitSet(new long[]{0x0000000001080002L});
    public static final BitSet FOLLOW_19_in_mult_expression229 = new BitSet(new long[]{0x0000000000028800L});
    public static final BitSet FOLLOW_24_in_mult_expression234 = new BitSet(new long[]{0x0000000000028800L});
    public static final BitSet FOLLOW_primary_expression_in_mult_expression244 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_INT_in_primary_expression268 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_VAR_in_primary_expression275 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_17_in_primary_expression282 = new BitSet(new long[]{0x0000000000028800L});
    public static final BitSet FOLLOW_expression_in_primary_expression284 = new BitSet(new long[]{0x0000000000040000L});
    public static final BitSet FOLLOW_18_in_primary_expression286 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_ID_in_atom310 = new BitSet(new long[]{0x0000000002000000L});
    public static final BitSet FOLLOW_25_in_atom312 = new BitSet(new long[]{0x0000000000000400L});
    public static final BitSet FOLLOW_ID_in_atom323 = new BitSet(new long[]{0x0000000000020002L});
    public static final BitSet FOLLOW_17_in_atom329 = new BitSet(new long[]{0x0000000000008C00L});
    public static final BitSet FOLLOW_termlist_in_atom331 = new BitSet(new long[]{0x0000000000040000L});
    public static final BitSet FOLLOW_18_in_atom333 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_term_in_termlist358 = new BitSet(new long[]{0x0000000000200002L});
    public static final BitSet FOLLOW_21_in_termlist365 = new BitSet(new long[]{0x0000000000008C00L});
    public static final BitSet FOLLOW_term_in_termlist369 = new BitSet(new long[]{0x0000000000200002L});
    public static final BitSet FOLLOW_constant_in_term388 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_VAR_in_term395 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_function_in_term402 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_ID_in_function419 = new BitSet(new long[]{0x0000000000020000L});
    public static final BitSet FOLLOW_17_in_function421 = new BitSet(new long[]{0x0000000000008C00L});
    public static final BitSet FOLLOW_termlist_in_function423 = new BitSet(new long[]{0x0000000000040000L});
    public static final BitSet FOLLOW_18_in_function425 = new BitSet(new long[]{0x0000000000000002L});

}