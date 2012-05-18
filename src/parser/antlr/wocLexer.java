// $ANTLR 3.4 /home/staff/aweinz/svnrepo/gerald_weidinger_thesis/WOC - Wings of Change/antlr/woc.g 2012-05-18 14:02:54

package parser.antlr;
import Enumeration.OP;


import org.antlr.runtime.*;
import java.util.Stack;
import java.util.List;
import java.util.ArrayList;

@SuppressWarnings({"all", "warnings", "unchecked"})
public class wocLexer extends Lexer {
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
    // delegators
    public Lexer[] getDelegates() {
        return new Lexer[] {};
    }

    public wocLexer() {} 
    public wocLexer(CharStream input) {
        this(input, new RecognizerSharedState());
    }
    public wocLexer(CharStream input, RecognizerSharedState state) {
        super(input,state);
    }
    public String getGrammarFileName() { return "/home/staff/aweinz/svnrepo/gerald_weidinger_thesis/WOC - Wings of Change/antlr/woc.g"; }

    // $ANTLR start "T__16"
    public final void mT__16() throws RecognitionException {
        try {
            int _type = T__16;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // /home/staff/aweinz/svnrepo/gerald_weidinger_thesis/WOC - Wings of Change/antlr/woc.g:7:7: ( '!=' )
            // /home/staff/aweinz/svnrepo/gerald_weidinger_thesis/WOC - Wings of Change/antlr/woc.g:7:9: '!='
            {
            match("!="); 



            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        	// do for sure before leaving
        }
    }
    // $ANTLR end "T__16"

    // $ANTLR start "T__17"
    public final void mT__17() throws RecognitionException {
        try {
            int _type = T__17;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // /home/staff/aweinz/svnrepo/gerald_weidinger_thesis/WOC - Wings of Change/antlr/woc.g:8:7: ( '(' )
            // /home/staff/aweinz/svnrepo/gerald_weidinger_thesis/WOC - Wings of Change/antlr/woc.g:8:9: '('
            {
            match('('); 

            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        	// do for sure before leaving
        }
    }
    // $ANTLR end "T__17"

    // $ANTLR start "T__18"
    public final void mT__18() throws RecognitionException {
        try {
            int _type = T__18;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // /home/staff/aweinz/svnrepo/gerald_weidinger_thesis/WOC - Wings of Change/antlr/woc.g:9:7: ( ')' )
            // /home/staff/aweinz/svnrepo/gerald_weidinger_thesis/WOC - Wings of Change/antlr/woc.g:9:9: ')'
            {
            match(')'); 

            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        	// do for sure before leaving
        }
    }
    // $ANTLR end "T__18"

    // $ANTLR start "T__19"
    public final void mT__19() throws RecognitionException {
        try {
            int _type = T__19;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // /home/staff/aweinz/svnrepo/gerald_weidinger_thesis/WOC - Wings of Change/antlr/woc.g:10:7: ( '*' )
            // /home/staff/aweinz/svnrepo/gerald_weidinger_thesis/WOC - Wings of Change/antlr/woc.g:10:9: '*'
            {
            match('*'); 

            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        	// do for sure before leaving
        }
    }
    // $ANTLR end "T__19"

    // $ANTLR start "T__20"
    public final void mT__20() throws RecognitionException {
        try {
            int _type = T__20;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // /home/staff/aweinz/svnrepo/gerald_weidinger_thesis/WOC - Wings of Change/antlr/woc.g:11:7: ( '+' )
            // /home/staff/aweinz/svnrepo/gerald_weidinger_thesis/WOC - Wings of Change/antlr/woc.g:11:9: '+'
            {
            match('+'); 

            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        	// do for sure before leaving
        }
    }
    // $ANTLR end "T__20"

    // $ANTLR start "T__21"
    public final void mT__21() throws RecognitionException {
        try {
            int _type = T__21;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // /home/staff/aweinz/svnrepo/gerald_weidinger_thesis/WOC - Wings of Change/antlr/woc.g:12:7: ( ',' )
            // /home/staff/aweinz/svnrepo/gerald_weidinger_thesis/WOC - Wings of Change/antlr/woc.g:12:9: ','
            {
            match(','); 

            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        	// do for sure before leaving
        }
    }
    // $ANTLR end "T__21"

    // $ANTLR start "T__22"
    public final void mT__22() throws RecognitionException {
        try {
            int _type = T__22;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // /home/staff/aweinz/svnrepo/gerald_weidinger_thesis/WOC - Wings of Change/antlr/woc.g:13:7: ( '-' )
            // /home/staff/aweinz/svnrepo/gerald_weidinger_thesis/WOC - Wings of Change/antlr/woc.g:13:9: '-'
            {
            match('-'); 

            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        	// do for sure before leaving
        }
    }
    // $ANTLR end "T__22"

    // $ANTLR start "T__23"
    public final void mT__23() throws RecognitionException {
        try {
            int _type = T__23;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // /home/staff/aweinz/svnrepo/gerald_weidinger_thesis/WOC - Wings of Change/antlr/woc.g:14:7: ( '.' )
            // /home/staff/aweinz/svnrepo/gerald_weidinger_thesis/WOC - Wings of Change/antlr/woc.g:14:9: '.'
            {
            match('.'); 

            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        	// do for sure before leaving
        }
    }
    // $ANTLR end "T__23"

    // $ANTLR start "T__24"
    public final void mT__24() throws RecognitionException {
        try {
            int _type = T__24;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // /home/staff/aweinz/svnrepo/gerald_weidinger_thesis/WOC - Wings of Change/antlr/woc.g:15:7: ( '/' )
            // /home/staff/aweinz/svnrepo/gerald_weidinger_thesis/WOC - Wings of Change/antlr/woc.g:15:9: '/'
            {
            match('/'); 

            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        	// do for sure before leaving
        }
    }
    // $ANTLR end "T__24"

    // $ANTLR start "T__25"
    public final void mT__25() throws RecognitionException {
        try {
            int _type = T__25;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // /home/staff/aweinz/svnrepo/gerald_weidinger_thesis/WOC - Wings of Change/antlr/woc.g:16:7: ( ':' )
            // /home/staff/aweinz/svnrepo/gerald_weidinger_thesis/WOC - Wings of Change/antlr/woc.g:16:9: ':'
            {
            match(':'); 

            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        	// do for sure before leaving
        }
    }
    // $ANTLR end "T__25"

    // $ANTLR start "T__26"
    public final void mT__26() throws RecognitionException {
        try {
            int _type = T__26;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // /home/staff/aweinz/svnrepo/gerald_weidinger_thesis/WOC - Wings of Change/antlr/woc.g:17:7: ( ':-' )
            // /home/staff/aweinz/svnrepo/gerald_weidinger_thesis/WOC - Wings of Change/antlr/woc.g:17:9: ':-'
            {
            match(":-"); 



            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        	// do for sure before leaving
        }
    }
    // $ANTLR end "T__26"

    // $ANTLR start "T__27"
    public final void mT__27() throws RecognitionException {
        try {
            int _type = T__27;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // /home/staff/aweinz/svnrepo/gerald_weidinger_thesis/WOC - Wings of Change/antlr/woc.g:18:7: ( '<' )
            // /home/staff/aweinz/svnrepo/gerald_weidinger_thesis/WOC - Wings of Change/antlr/woc.g:18:9: '<'
            {
            match('<'); 

            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        	// do for sure before leaving
        }
    }
    // $ANTLR end "T__27"

    // $ANTLR start "T__28"
    public final void mT__28() throws RecognitionException {
        try {
            int _type = T__28;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // /home/staff/aweinz/svnrepo/gerald_weidinger_thesis/WOC - Wings of Change/antlr/woc.g:19:7: ( '<=' )
            // /home/staff/aweinz/svnrepo/gerald_weidinger_thesis/WOC - Wings of Change/antlr/woc.g:19:9: '<='
            {
            match("<="); 



            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        	// do for sure before leaving
        }
    }
    // $ANTLR end "T__28"

    // $ANTLR start "T__29"
    public final void mT__29() throws RecognitionException {
        try {
            int _type = T__29;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // /home/staff/aweinz/svnrepo/gerald_weidinger_thesis/WOC - Wings of Change/antlr/woc.g:20:7: ( '=' )
            // /home/staff/aweinz/svnrepo/gerald_weidinger_thesis/WOC - Wings of Change/antlr/woc.g:20:9: '='
            {
            match('='); 

            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        	// do for sure before leaving
        }
    }
    // $ANTLR end "T__29"

    // $ANTLR start "T__30"
    public final void mT__30() throws RecognitionException {
        try {
            int _type = T__30;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // /home/staff/aweinz/svnrepo/gerald_weidinger_thesis/WOC - Wings of Change/antlr/woc.g:21:7: ( '>' )
            // /home/staff/aweinz/svnrepo/gerald_weidinger_thesis/WOC - Wings of Change/antlr/woc.g:21:9: '>'
            {
            match('>'); 

            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        	// do for sure before leaving
        }
    }
    // $ANTLR end "T__30"

    // $ANTLR start "T__31"
    public final void mT__31() throws RecognitionException {
        try {
            int _type = T__31;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // /home/staff/aweinz/svnrepo/gerald_weidinger_thesis/WOC - Wings of Change/antlr/woc.g:22:7: ( '>=' )
            // /home/staff/aweinz/svnrepo/gerald_weidinger_thesis/WOC - Wings of Change/antlr/woc.g:22:9: '>='
            {
            match(">="); 



            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        	// do for sure before leaving
        }
    }
    // $ANTLR end "T__31"

    // $ANTLR start "T__32"
    public final void mT__32() throws RecognitionException {
        try {
            int _type = T__32;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // /home/staff/aweinz/svnrepo/gerald_weidinger_thesis/WOC - Wings of Change/antlr/woc.g:23:7: ( 'is' )
            // /home/staff/aweinz/svnrepo/gerald_weidinger_thesis/WOC - Wings of Change/antlr/woc.g:23:9: 'is'
            {
            match("is"); 



            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        	// do for sure before leaving
        }
    }
    // $ANTLR end "T__32"

    // $ANTLR start "T__33"
    public final void mT__33() throws RecognitionException {
        try {
            int _type = T__33;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // /home/staff/aweinz/svnrepo/gerald_weidinger_thesis/WOC - Wings of Change/antlr/woc.g:24:7: ( 'not' )
            // /home/staff/aweinz/svnrepo/gerald_weidinger_thesis/WOC - Wings of Change/antlr/woc.g:24:9: 'not'
            {
            match("not"); 



            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        	// do for sure before leaving
        }
    }
    // $ANTLR end "T__33"

    // $ANTLR start "ID"
    public final void mID() throws RecognitionException {
        try {
            int _type = ID;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // /home/staff/aweinz/svnrepo/gerald_weidinger_thesis/WOC - Wings of Change/antlr/woc.g:132:5: ( ( 'a' .. 'z' ) ( 'a' .. 'z' | 'A' .. 'Z' | '0' .. '9' | '_' )* )
            // /home/staff/aweinz/svnrepo/gerald_weidinger_thesis/WOC - Wings of Change/antlr/woc.g:132:7: ( 'a' .. 'z' ) ( 'a' .. 'z' | 'A' .. 'Z' | '0' .. '9' | '_' )*
            {
            if ( (input.LA(1) >= 'a' && input.LA(1) <= 'z') ) {
                input.consume();
            }
            else {
                MismatchedSetException mse = new MismatchedSetException(null,input);
                recover(mse);
                throw mse;
            }


            // /home/staff/aweinz/svnrepo/gerald_weidinger_thesis/WOC - Wings of Change/antlr/woc.g:132:18: ( 'a' .. 'z' | 'A' .. 'Z' | '0' .. '9' | '_' )*
            loop1:
            do {
                int alt1=2;
                int LA1_0 = input.LA(1);

                if ( ((LA1_0 >= '0' && LA1_0 <= '9')||(LA1_0 >= 'A' && LA1_0 <= 'Z')||LA1_0=='_'||(LA1_0 >= 'a' && LA1_0 <= 'z')) ) {
                    alt1=1;
                }


                switch (alt1) {
            	case 1 :
            	    // /home/staff/aweinz/svnrepo/gerald_weidinger_thesis/WOC - Wings of Change/antlr/woc.g:
            	    {
            	    if ( (input.LA(1) >= '0' && input.LA(1) <= '9')||(input.LA(1) >= 'A' && input.LA(1) <= 'Z')||input.LA(1)=='_'||(input.LA(1) >= 'a' && input.LA(1) <= 'z') ) {
            	        input.consume();
            	    }
            	    else {
            	        MismatchedSetException mse = new MismatchedSetException(null,input);
            	        recover(mse);
            	        throw mse;
            	    }


            	    }
            	    break;

            	default :
            	    break loop1;
                }
            } while (true);


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        	// do for sure before leaving
        }
    }
    // $ANTLR end "ID"

    // $ANTLR start "VAR"
    public final void mVAR() throws RecognitionException {
        try {
            int _type = VAR;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // /home/staff/aweinz/svnrepo/gerald_weidinger_thesis/WOC - Wings of Change/antlr/woc.g:135:5: ( ( 'A' .. 'Z' | '_' ) ( 'a' .. 'z' | 'A' .. 'Z' | '0' .. '9' | '_' )* )
            // /home/staff/aweinz/svnrepo/gerald_weidinger_thesis/WOC - Wings of Change/antlr/woc.g:135:7: ( 'A' .. 'Z' | '_' ) ( 'a' .. 'z' | 'A' .. 'Z' | '0' .. '9' | '_' )*
            {
            if ( (input.LA(1) >= 'A' && input.LA(1) <= 'Z')||input.LA(1)=='_' ) {
                input.consume();
            }
            else {
                MismatchedSetException mse = new MismatchedSetException(null,input);
                recover(mse);
                throw mse;
            }


            // /home/staff/aweinz/svnrepo/gerald_weidinger_thesis/WOC - Wings of Change/antlr/woc.g:135:22: ( 'a' .. 'z' | 'A' .. 'Z' | '0' .. '9' | '_' )*
            loop2:
            do {
                int alt2=2;
                int LA2_0 = input.LA(1);

                if ( ((LA2_0 >= '0' && LA2_0 <= '9')||(LA2_0 >= 'A' && LA2_0 <= 'Z')||LA2_0=='_'||(LA2_0 >= 'a' && LA2_0 <= 'z')) ) {
                    alt2=1;
                }


                switch (alt2) {
            	case 1 :
            	    // /home/staff/aweinz/svnrepo/gerald_weidinger_thesis/WOC - Wings of Change/antlr/woc.g:
            	    {
            	    if ( (input.LA(1) >= '0' && input.LA(1) <= '9')||(input.LA(1) >= 'A' && input.LA(1) <= 'Z')||input.LA(1)=='_'||(input.LA(1) >= 'a' && input.LA(1) <= 'z') ) {
            	        input.consume();
            	    }
            	    else {
            	        MismatchedSetException mse = new MismatchedSetException(null,input);
            	        recover(mse);
            	        throw mse;
            	    }


            	    }
            	    break;

            	default :
            	    break loop2;
                }
            } while (true);


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        	// do for sure before leaving
        }
    }
    // $ANTLR end "VAR"

    // $ANTLR start "INT"
    public final void mINT() throws RecognitionException {
        try {
            int _type = INT;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // /home/staff/aweinz/svnrepo/gerald_weidinger_thesis/WOC - Wings of Change/antlr/woc.g:138:5: ( ( '0' .. '9' )+ )
            // /home/staff/aweinz/svnrepo/gerald_weidinger_thesis/WOC - Wings of Change/antlr/woc.g:138:7: ( '0' .. '9' )+
            {
            // /home/staff/aweinz/svnrepo/gerald_weidinger_thesis/WOC - Wings of Change/antlr/woc.g:138:7: ( '0' .. '9' )+
            int cnt3=0;
            loop3:
            do {
                int alt3=2;
                int LA3_0 = input.LA(1);

                if ( ((LA3_0 >= '0' && LA3_0 <= '9')) ) {
                    alt3=1;
                }


                switch (alt3) {
            	case 1 :
            	    // /home/staff/aweinz/svnrepo/gerald_weidinger_thesis/WOC - Wings of Change/antlr/woc.g:
            	    {
            	    if ( (input.LA(1) >= '0' && input.LA(1) <= '9') ) {
            	        input.consume();
            	    }
            	    else {
            	        MismatchedSetException mse = new MismatchedSetException(null,input);
            	        recover(mse);
            	        throw mse;
            	    }


            	    }
            	    break;

            	default :
            	    if ( cnt3 >= 1 ) break loop3;
                        EarlyExitException eee =
                            new EarlyExitException(3, input);
                        throw eee;
                }
                cnt3++;
            } while (true);


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        	// do for sure before leaving
        }
    }
    // $ANTLR end "INT"

    // $ANTLR start "COMMENT"
    public final void mCOMMENT() throws RecognitionException {
        try {
            int _type = COMMENT;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // /home/staff/aweinz/svnrepo/gerald_weidinger_thesis/WOC - Wings of Change/antlr/woc.g:149:5: ( '//' (~ ( '\\n' | '\\r' ) )* ( '\\r' )? '\\n' | '%' (~ ( '\\n' | '\\r' ) )* ( '\\r' )? '\\n' | '/*' ( options {greedy=false; } : . )* '*/' )
            int alt9=3;
            int LA9_0 = input.LA(1);

            if ( (LA9_0=='/') ) {
                int LA9_1 = input.LA(2);

                if ( (LA9_1=='/') ) {
                    alt9=1;
                }
                else if ( (LA9_1=='*') ) {
                    alt9=3;
                }
                else {
                    NoViableAltException nvae =
                        new NoViableAltException("", 9, 1, input);

                    throw nvae;

                }
            }
            else if ( (LA9_0=='%') ) {
                alt9=2;
            }
            else {
                NoViableAltException nvae =
                    new NoViableAltException("", 9, 0, input);

                throw nvae;

            }
            switch (alt9) {
                case 1 :
                    // /home/staff/aweinz/svnrepo/gerald_weidinger_thesis/WOC - Wings of Change/antlr/woc.g:149:9: '//' (~ ( '\\n' | '\\r' ) )* ( '\\r' )? '\\n'
                    {
                    match("//"); 



                    // /home/staff/aweinz/svnrepo/gerald_weidinger_thesis/WOC - Wings of Change/antlr/woc.g:149:14: (~ ( '\\n' | '\\r' ) )*
                    loop4:
                    do {
                        int alt4=2;
                        int LA4_0 = input.LA(1);

                        if ( ((LA4_0 >= '\u0000' && LA4_0 <= '\t')||(LA4_0 >= '\u000B' && LA4_0 <= '\f')||(LA4_0 >= '\u000E' && LA4_0 <= '\uFFFF')) ) {
                            alt4=1;
                        }


                        switch (alt4) {
                    	case 1 :
                    	    // /home/staff/aweinz/svnrepo/gerald_weidinger_thesis/WOC - Wings of Change/antlr/woc.g:
                    	    {
                    	    if ( (input.LA(1) >= '\u0000' && input.LA(1) <= '\t')||(input.LA(1) >= '\u000B' && input.LA(1) <= '\f')||(input.LA(1) >= '\u000E' && input.LA(1) <= '\uFFFF') ) {
                    	        input.consume();
                    	    }
                    	    else {
                    	        MismatchedSetException mse = new MismatchedSetException(null,input);
                    	        recover(mse);
                    	        throw mse;
                    	    }


                    	    }
                    	    break;

                    	default :
                    	    break loop4;
                        }
                    } while (true);


                    // /home/staff/aweinz/svnrepo/gerald_weidinger_thesis/WOC - Wings of Change/antlr/woc.g:149:28: ( '\\r' )?
                    int alt5=2;
                    int LA5_0 = input.LA(1);

                    if ( (LA5_0=='\r') ) {
                        alt5=1;
                    }
                    switch (alt5) {
                        case 1 :
                            // /home/staff/aweinz/svnrepo/gerald_weidinger_thesis/WOC - Wings of Change/antlr/woc.g:149:28: '\\r'
                            {
                            match('\r'); 

                            }
                            break;

                    }


                    match('\n'); 

                    _channel=HIDDEN;

                    }
                    break;
                case 2 :
                    // /home/staff/aweinz/svnrepo/gerald_weidinger_thesis/WOC - Wings of Change/antlr/woc.g:150:7: '%' (~ ( '\\n' | '\\r' ) )* ( '\\r' )? '\\n'
                    {
                    match('%'); 

                    // /home/staff/aweinz/svnrepo/gerald_weidinger_thesis/WOC - Wings of Change/antlr/woc.g:150:11: (~ ( '\\n' | '\\r' ) )*
                    loop6:
                    do {
                        int alt6=2;
                        int LA6_0 = input.LA(1);

                        if ( ((LA6_0 >= '\u0000' && LA6_0 <= '\t')||(LA6_0 >= '\u000B' && LA6_0 <= '\f')||(LA6_0 >= '\u000E' && LA6_0 <= '\uFFFF')) ) {
                            alt6=1;
                        }


                        switch (alt6) {
                    	case 1 :
                    	    // /home/staff/aweinz/svnrepo/gerald_weidinger_thesis/WOC - Wings of Change/antlr/woc.g:
                    	    {
                    	    if ( (input.LA(1) >= '\u0000' && input.LA(1) <= '\t')||(input.LA(1) >= '\u000B' && input.LA(1) <= '\f')||(input.LA(1) >= '\u000E' && input.LA(1) <= '\uFFFF') ) {
                    	        input.consume();
                    	    }
                    	    else {
                    	        MismatchedSetException mse = new MismatchedSetException(null,input);
                    	        recover(mse);
                    	        throw mse;
                    	    }


                    	    }
                    	    break;

                    	default :
                    	    break loop6;
                        }
                    } while (true);


                    // /home/staff/aweinz/svnrepo/gerald_weidinger_thesis/WOC - Wings of Change/antlr/woc.g:150:25: ( '\\r' )?
                    int alt7=2;
                    int LA7_0 = input.LA(1);

                    if ( (LA7_0=='\r') ) {
                        alt7=1;
                    }
                    switch (alt7) {
                        case 1 :
                            // /home/staff/aweinz/svnrepo/gerald_weidinger_thesis/WOC - Wings of Change/antlr/woc.g:150:25: '\\r'
                            {
                            match('\r'); 

                            }
                            break;

                    }


                    match('\n'); 

                    _channel=HIDDEN;

                    }
                    break;
                case 3 :
                    // /home/staff/aweinz/svnrepo/gerald_weidinger_thesis/WOC - Wings of Change/antlr/woc.g:151:9: '/*' ( options {greedy=false; } : . )* '*/'
                    {
                    match("/*"); 



                    // /home/staff/aweinz/svnrepo/gerald_weidinger_thesis/WOC - Wings of Change/antlr/woc.g:151:14: ( options {greedy=false; } : . )*
                    loop8:
                    do {
                        int alt8=2;
                        int LA8_0 = input.LA(1);

                        if ( (LA8_0=='*') ) {
                            int LA8_1 = input.LA(2);

                            if ( (LA8_1=='/') ) {
                                alt8=2;
                            }
                            else if ( ((LA8_1 >= '\u0000' && LA8_1 <= '.')||(LA8_1 >= '0' && LA8_1 <= '\uFFFF')) ) {
                                alt8=1;
                            }


                        }
                        else if ( ((LA8_0 >= '\u0000' && LA8_0 <= ')')||(LA8_0 >= '+' && LA8_0 <= '\uFFFF')) ) {
                            alt8=1;
                        }


                        switch (alt8) {
                    	case 1 :
                    	    // /home/staff/aweinz/svnrepo/gerald_weidinger_thesis/WOC - Wings of Change/antlr/woc.g:151:42: .
                    	    {
                    	    matchAny(); 

                    	    }
                    	    break;

                    	default :
                    	    break loop8;
                        }
                    } while (true);


                    match("*/"); 



                    _channel=HIDDEN;

                    }
                    break;

            }
            state.type = _type;
            state.channel = _channel;
        }
        finally {
        	// do for sure before leaving
        }
    }
    // $ANTLR end "COMMENT"

    // $ANTLR start "WS"
    public final void mWS() throws RecognitionException {
        try {
            int _type = WS;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // /home/staff/aweinz/svnrepo/gerald_weidinger_thesis/WOC - Wings of Change/antlr/woc.g:155:5: ( ( ' ' | '\\t' | '\\r' | '\\n' ) )
            // /home/staff/aweinz/svnrepo/gerald_weidinger_thesis/WOC - Wings of Change/antlr/woc.g:155:9: ( ' ' | '\\t' | '\\r' | '\\n' )
            {
            if ( (input.LA(1) >= '\t' && input.LA(1) <= '\n')||input.LA(1)=='\r'||input.LA(1)==' ' ) {
                input.consume();
            }
            else {
                MismatchedSetException mse = new MismatchedSetException(null,input);
                recover(mse);
                throw mse;
            }


            _channel=HIDDEN;

            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        	// do for sure before leaving
        }
    }
    // $ANTLR end "WS"

    // $ANTLR start "STRING"
    public final void mSTRING() throws RecognitionException {
        try {
            int _type = STRING;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // /home/staff/aweinz/svnrepo/gerald_weidinger_thesis/WOC - Wings of Change/antlr/woc.g:163:5: ( '\"' ( ESC_SEQ |~ ( '\\\\' | '\"' ) )* '\"' )
            // /home/staff/aweinz/svnrepo/gerald_weidinger_thesis/WOC - Wings of Change/antlr/woc.g:163:8: '\"' ( ESC_SEQ |~ ( '\\\\' | '\"' ) )* '\"'
            {
            match('\"'); 

            // /home/staff/aweinz/svnrepo/gerald_weidinger_thesis/WOC - Wings of Change/antlr/woc.g:163:12: ( ESC_SEQ |~ ( '\\\\' | '\"' ) )*
            loop10:
            do {
                int alt10=3;
                int LA10_0 = input.LA(1);

                if ( (LA10_0=='\\') ) {
                    alt10=1;
                }
                else if ( ((LA10_0 >= '\u0000' && LA10_0 <= '!')||(LA10_0 >= '#' && LA10_0 <= '[')||(LA10_0 >= ']' && LA10_0 <= '\uFFFF')) ) {
                    alt10=2;
                }


                switch (alt10) {
            	case 1 :
            	    // /home/staff/aweinz/svnrepo/gerald_weidinger_thesis/WOC - Wings of Change/antlr/woc.g:163:14: ESC_SEQ
            	    {
            	    mESC_SEQ(); 


            	    }
            	    break;
            	case 2 :
            	    // /home/staff/aweinz/svnrepo/gerald_weidinger_thesis/WOC - Wings of Change/antlr/woc.g:163:24: ~ ( '\\\\' | '\"' )
            	    {
            	    if ( (input.LA(1) >= '\u0000' && input.LA(1) <= '!')||(input.LA(1) >= '#' && input.LA(1) <= '[')||(input.LA(1) >= ']' && input.LA(1) <= '\uFFFF') ) {
            	        input.consume();
            	    }
            	    else {
            	        MismatchedSetException mse = new MismatchedSetException(null,input);
            	        recover(mse);
            	        throw mse;
            	    }


            	    }
            	    break;

            	default :
            	    break loop10;
                }
            } while (true);


            match('\"'); 

            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        	// do for sure before leaving
        }
    }
    // $ANTLR end "STRING"

    // $ANTLR start "CHAR"
    public final void mCHAR() throws RecognitionException {
        try {
            int _type = CHAR;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // /home/staff/aweinz/svnrepo/gerald_weidinger_thesis/WOC - Wings of Change/antlr/woc.g:166:5: ( '\\'' ( ESC_SEQ |~ ( '\\'' | '\\\\' ) ) '\\'' )
            // /home/staff/aweinz/svnrepo/gerald_weidinger_thesis/WOC - Wings of Change/antlr/woc.g:166:8: '\\'' ( ESC_SEQ |~ ( '\\'' | '\\\\' ) ) '\\''
            {
            match('\''); 

            // /home/staff/aweinz/svnrepo/gerald_weidinger_thesis/WOC - Wings of Change/antlr/woc.g:166:13: ( ESC_SEQ |~ ( '\\'' | '\\\\' ) )
            int alt11=2;
            int LA11_0 = input.LA(1);

            if ( (LA11_0=='\\') ) {
                alt11=1;
            }
            else if ( ((LA11_0 >= '\u0000' && LA11_0 <= '&')||(LA11_0 >= '(' && LA11_0 <= '[')||(LA11_0 >= ']' && LA11_0 <= '\uFFFF')) ) {
                alt11=2;
            }
            else {
                NoViableAltException nvae =
                    new NoViableAltException("", 11, 0, input);

                throw nvae;

            }
            switch (alt11) {
                case 1 :
                    // /home/staff/aweinz/svnrepo/gerald_weidinger_thesis/WOC - Wings of Change/antlr/woc.g:166:15: ESC_SEQ
                    {
                    mESC_SEQ(); 


                    }
                    break;
                case 2 :
                    // /home/staff/aweinz/svnrepo/gerald_weidinger_thesis/WOC - Wings of Change/antlr/woc.g:166:25: ~ ( '\\'' | '\\\\' )
                    {
                    if ( (input.LA(1) >= '\u0000' && input.LA(1) <= '&')||(input.LA(1) >= '(' && input.LA(1) <= '[')||(input.LA(1) >= ']' && input.LA(1) <= '\uFFFF') ) {
                        input.consume();
                    }
                    else {
                        MismatchedSetException mse = new MismatchedSetException(null,input);
                        recover(mse);
                        throw mse;
                    }


                    }
                    break;

            }


            match('\''); 

            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        	// do for sure before leaving
        }
    }
    // $ANTLR end "CHAR"

    // $ANTLR start "EXPONENT"
    public final void mEXPONENT() throws RecognitionException {
        try {
            // /home/staff/aweinz/svnrepo/gerald_weidinger_thesis/WOC - Wings of Change/antlr/woc.g:171:10: ( ( 'e' | 'E' ) ( '+' | '-' )? ( '0' .. '9' )+ )
            // /home/staff/aweinz/svnrepo/gerald_weidinger_thesis/WOC - Wings of Change/antlr/woc.g:171:12: ( 'e' | 'E' ) ( '+' | '-' )? ( '0' .. '9' )+
            {
            if ( input.LA(1)=='E'||input.LA(1)=='e' ) {
                input.consume();
            }
            else {
                MismatchedSetException mse = new MismatchedSetException(null,input);
                recover(mse);
                throw mse;
            }


            // /home/staff/aweinz/svnrepo/gerald_weidinger_thesis/WOC - Wings of Change/antlr/woc.g:171:22: ( '+' | '-' )?
            int alt12=2;
            int LA12_0 = input.LA(1);

            if ( (LA12_0=='+'||LA12_0=='-') ) {
                alt12=1;
            }
            switch (alt12) {
                case 1 :
                    // /home/staff/aweinz/svnrepo/gerald_weidinger_thesis/WOC - Wings of Change/antlr/woc.g:
                    {
                    if ( input.LA(1)=='+'||input.LA(1)=='-' ) {
                        input.consume();
                    }
                    else {
                        MismatchedSetException mse = new MismatchedSetException(null,input);
                        recover(mse);
                        throw mse;
                    }


                    }
                    break;

            }


            // /home/staff/aweinz/svnrepo/gerald_weidinger_thesis/WOC - Wings of Change/antlr/woc.g:171:33: ( '0' .. '9' )+
            int cnt13=0;
            loop13:
            do {
                int alt13=2;
                int LA13_0 = input.LA(1);

                if ( ((LA13_0 >= '0' && LA13_0 <= '9')) ) {
                    alt13=1;
                }


                switch (alt13) {
            	case 1 :
            	    // /home/staff/aweinz/svnrepo/gerald_weidinger_thesis/WOC - Wings of Change/antlr/woc.g:
            	    {
            	    if ( (input.LA(1) >= '0' && input.LA(1) <= '9') ) {
            	        input.consume();
            	    }
            	    else {
            	        MismatchedSetException mse = new MismatchedSetException(null,input);
            	        recover(mse);
            	        throw mse;
            	    }


            	    }
            	    break;

            	default :
            	    if ( cnt13 >= 1 ) break loop13;
                        EarlyExitException eee =
                            new EarlyExitException(13, input);
                        throw eee;
                }
                cnt13++;
            } while (true);


            }


        }
        finally {
        	// do for sure before leaving
        }
    }
    // $ANTLR end "EXPONENT"

    // $ANTLR start "HEX_DIGIT"
    public final void mHEX_DIGIT() throws RecognitionException {
        try {
            // /home/staff/aweinz/svnrepo/gerald_weidinger_thesis/WOC - Wings of Change/antlr/woc.g:174:11: ( ( '0' .. '9' | 'a' .. 'f' | 'A' .. 'F' ) )
            // /home/staff/aweinz/svnrepo/gerald_weidinger_thesis/WOC - Wings of Change/antlr/woc.g:
            {
            if ( (input.LA(1) >= '0' && input.LA(1) <= '9')||(input.LA(1) >= 'A' && input.LA(1) <= 'F')||(input.LA(1) >= 'a' && input.LA(1) <= 'f') ) {
                input.consume();
            }
            else {
                MismatchedSetException mse = new MismatchedSetException(null,input);
                recover(mse);
                throw mse;
            }


            }


        }
        finally {
        	// do for sure before leaving
        }
    }
    // $ANTLR end "HEX_DIGIT"

    // $ANTLR start "ESC_SEQ"
    public final void mESC_SEQ() throws RecognitionException {
        try {
            // /home/staff/aweinz/svnrepo/gerald_weidinger_thesis/WOC - Wings of Change/antlr/woc.g:178:5: ( '\\\\' ( 'b' | 't' | 'n' | 'f' | 'r' | '\\\"' | '\\'' | '\\\\' ) | UNICODE_ESC | OCTAL_ESC )
            int alt14=3;
            int LA14_0 = input.LA(1);

            if ( (LA14_0=='\\') ) {
                switch ( input.LA(2) ) {
                case '\"':
                case '\'':
                case '\\':
                case 'b':
                case 'f':
                case 'n':
                case 'r':
                case 't':
                    {
                    alt14=1;
                    }
                    break;
                case 'u':
                    {
                    alt14=2;
                    }
                    break;
                case '0':
                case '1':
                case '2':
                case '3':
                case '4':
                case '5':
                case '6':
                case '7':
                    {
                    alt14=3;
                    }
                    break;
                default:
                    NoViableAltException nvae =
                        new NoViableAltException("", 14, 1, input);

                    throw nvae;

                }

            }
            else {
                NoViableAltException nvae =
                    new NoViableAltException("", 14, 0, input);

                throw nvae;

            }
            switch (alt14) {
                case 1 :
                    // /home/staff/aweinz/svnrepo/gerald_weidinger_thesis/WOC - Wings of Change/antlr/woc.g:178:9: '\\\\' ( 'b' | 't' | 'n' | 'f' | 'r' | '\\\"' | '\\'' | '\\\\' )
                    {
                    match('\\'); 

                    if ( input.LA(1)=='\"'||input.LA(1)=='\''||input.LA(1)=='\\'||input.LA(1)=='b'||input.LA(1)=='f'||input.LA(1)=='n'||input.LA(1)=='r'||input.LA(1)=='t' ) {
                        input.consume();
                    }
                    else {
                        MismatchedSetException mse = new MismatchedSetException(null,input);
                        recover(mse);
                        throw mse;
                    }


                    }
                    break;
                case 2 :
                    // /home/staff/aweinz/svnrepo/gerald_weidinger_thesis/WOC - Wings of Change/antlr/woc.g:179:9: UNICODE_ESC
                    {
                    mUNICODE_ESC(); 


                    }
                    break;
                case 3 :
                    // /home/staff/aweinz/svnrepo/gerald_weidinger_thesis/WOC - Wings of Change/antlr/woc.g:180:9: OCTAL_ESC
                    {
                    mOCTAL_ESC(); 


                    }
                    break;

            }

        }
        finally {
        	// do for sure before leaving
        }
    }
    // $ANTLR end "ESC_SEQ"

    // $ANTLR start "OCTAL_ESC"
    public final void mOCTAL_ESC() throws RecognitionException {
        try {
            // /home/staff/aweinz/svnrepo/gerald_weidinger_thesis/WOC - Wings of Change/antlr/woc.g:185:5: ( '\\\\' ( '0' .. '3' ) ( '0' .. '7' ) ( '0' .. '7' ) | '\\\\' ( '0' .. '7' ) ( '0' .. '7' ) | '\\\\' ( '0' .. '7' ) )
            int alt15=3;
            int LA15_0 = input.LA(1);

            if ( (LA15_0=='\\') ) {
                int LA15_1 = input.LA(2);

                if ( ((LA15_1 >= '0' && LA15_1 <= '3')) ) {
                    int LA15_2 = input.LA(3);

                    if ( ((LA15_2 >= '0' && LA15_2 <= '7')) ) {
                        int LA15_4 = input.LA(4);

                        if ( ((LA15_4 >= '0' && LA15_4 <= '7')) ) {
                            alt15=1;
                        }
                        else {
                            alt15=2;
                        }
                    }
                    else {
                        alt15=3;
                    }
                }
                else if ( ((LA15_1 >= '4' && LA15_1 <= '7')) ) {
                    int LA15_3 = input.LA(3);

                    if ( ((LA15_3 >= '0' && LA15_3 <= '7')) ) {
                        alt15=2;
                    }
                    else {
                        alt15=3;
                    }
                }
                else {
                    NoViableAltException nvae =
                        new NoViableAltException("", 15, 1, input);

                    throw nvae;

                }
            }
            else {
                NoViableAltException nvae =
                    new NoViableAltException("", 15, 0, input);

                throw nvae;

            }
            switch (alt15) {
                case 1 :
                    // /home/staff/aweinz/svnrepo/gerald_weidinger_thesis/WOC - Wings of Change/antlr/woc.g:185:9: '\\\\' ( '0' .. '3' ) ( '0' .. '7' ) ( '0' .. '7' )
                    {
                    match('\\'); 

                    if ( (input.LA(1) >= '0' && input.LA(1) <= '3') ) {
                        input.consume();
                    }
                    else {
                        MismatchedSetException mse = new MismatchedSetException(null,input);
                        recover(mse);
                        throw mse;
                    }


                    if ( (input.LA(1) >= '0' && input.LA(1) <= '7') ) {
                        input.consume();
                    }
                    else {
                        MismatchedSetException mse = new MismatchedSetException(null,input);
                        recover(mse);
                        throw mse;
                    }


                    if ( (input.LA(1) >= '0' && input.LA(1) <= '7') ) {
                        input.consume();
                    }
                    else {
                        MismatchedSetException mse = new MismatchedSetException(null,input);
                        recover(mse);
                        throw mse;
                    }


                    }
                    break;
                case 2 :
                    // /home/staff/aweinz/svnrepo/gerald_weidinger_thesis/WOC - Wings of Change/antlr/woc.g:186:9: '\\\\' ( '0' .. '7' ) ( '0' .. '7' )
                    {
                    match('\\'); 

                    if ( (input.LA(1) >= '0' && input.LA(1) <= '7') ) {
                        input.consume();
                    }
                    else {
                        MismatchedSetException mse = new MismatchedSetException(null,input);
                        recover(mse);
                        throw mse;
                    }


                    if ( (input.LA(1) >= '0' && input.LA(1) <= '7') ) {
                        input.consume();
                    }
                    else {
                        MismatchedSetException mse = new MismatchedSetException(null,input);
                        recover(mse);
                        throw mse;
                    }


                    }
                    break;
                case 3 :
                    // /home/staff/aweinz/svnrepo/gerald_weidinger_thesis/WOC - Wings of Change/antlr/woc.g:187:9: '\\\\' ( '0' .. '7' )
                    {
                    match('\\'); 

                    if ( (input.LA(1) >= '0' && input.LA(1) <= '7') ) {
                        input.consume();
                    }
                    else {
                        MismatchedSetException mse = new MismatchedSetException(null,input);
                        recover(mse);
                        throw mse;
                    }


                    }
                    break;

            }

        }
        finally {
        	// do for sure before leaving
        }
    }
    // $ANTLR end "OCTAL_ESC"

    // $ANTLR start "UNICODE_ESC"
    public final void mUNICODE_ESC() throws RecognitionException {
        try {
            // /home/staff/aweinz/svnrepo/gerald_weidinger_thesis/WOC - Wings of Change/antlr/woc.g:192:5: ( '\\\\' 'u' HEX_DIGIT HEX_DIGIT HEX_DIGIT HEX_DIGIT )
            // /home/staff/aweinz/svnrepo/gerald_weidinger_thesis/WOC - Wings of Change/antlr/woc.g:192:9: '\\\\' 'u' HEX_DIGIT HEX_DIGIT HEX_DIGIT HEX_DIGIT
            {
            match('\\'); 

            match('u'); 

            mHEX_DIGIT(); 


            mHEX_DIGIT(); 


            mHEX_DIGIT(); 


            mHEX_DIGIT(); 


            }


        }
        finally {
        	// do for sure before leaving
        }
    }
    // $ANTLR end "UNICODE_ESC"

    public void mTokens() throws RecognitionException {
        // /home/staff/aweinz/svnrepo/gerald_weidinger_thesis/WOC - Wings of Change/antlr/woc.g:1:8: ( T__16 | T__17 | T__18 | T__19 | T__20 | T__21 | T__22 | T__23 | T__24 | T__25 | T__26 | T__27 | T__28 | T__29 | T__30 | T__31 | T__32 | T__33 | ID | VAR | INT | COMMENT | WS | STRING | CHAR )
        int alt16=25;
        switch ( input.LA(1) ) {
        case '!':
            {
            alt16=1;
            }
            break;
        case '(':
            {
            alt16=2;
            }
            break;
        case ')':
            {
            alt16=3;
            }
            break;
        case '*':
            {
            alt16=4;
            }
            break;
        case '+':
            {
            alt16=5;
            }
            break;
        case ',':
            {
            alt16=6;
            }
            break;
        case '-':
            {
            alt16=7;
            }
            break;
        case '.':
            {
            alt16=8;
            }
            break;
        case '/':
            {
            int LA16_9 = input.LA(2);

            if ( (LA16_9=='*'||LA16_9=='/') ) {
                alt16=22;
            }
            else {
                alt16=9;
            }
            }
            break;
        case ':':
            {
            int LA16_10 = input.LA(2);

            if ( (LA16_10=='-') ) {
                alt16=11;
            }
            else {
                alt16=10;
            }
            }
            break;
        case '<':
            {
            int LA16_11 = input.LA(2);

            if ( (LA16_11=='=') ) {
                alt16=13;
            }
            else {
                alt16=12;
            }
            }
            break;
        case '=':
            {
            alt16=14;
            }
            break;
        case '>':
            {
            int LA16_13 = input.LA(2);

            if ( (LA16_13=='=') ) {
                alt16=16;
            }
            else {
                alt16=15;
            }
            }
            break;
        case 'i':
            {
            int LA16_14 = input.LA(2);

            if ( (LA16_14=='s') ) {
                int LA16_30 = input.LA(3);

                if ( ((LA16_30 >= '0' && LA16_30 <= '9')||(LA16_30 >= 'A' && LA16_30 <= 'Z')||LA16_30=='_'||(LA16_30 >= 'a' && LA16_30 <= 'z')) ) {
                    alt16=19;
                }
                else {
                    alt16=17;
                }
            }
            else {
                alt16=19;
            }
            }
            break;
        case 'n':
            {
            int LA16_15 = input.LA(2);

            if ( (LA16_15=='o') ) {
                int LA16_31 = input.LA(3);

                if ( (LA16_31=='t') ) {
                    int LA16_33 = input.LA(4);

                    if ( ((LA16_33 >= '0' && LA16_33 <= '9')||(LA16_33 >= 'A' && LA16_33 <= 'Z')||LA16_33=='_'||(LA16_33 >= 'a' && LA16_33 <= 'z')) ) {
                        alt16=19;
                    }
                    else {
                        alt16=18;
                    }
                }
                else {
                    alt16=19;
                }
            }
            else {
                alt16=19;
            }
            }
            break;
        case 'a':
        case 'b':
        case 'c':
        case 'd':
        case 'e':
        case 'f':
        case 'g':
        case 'h':
        case 'j':
        case 'k':
        case 'l':
        case 'm':
        case 'o':
        case 'p':
        case 'q':
        case 'r':
        case 's':
        case 't':
        case 'u':
        case 'v':
        case 'w':
        case 'x':
        case 'y':
        case 'z':
            {
            alt16=19;
            }
            break;
        case 'A':
        case 'B':
        case 'C':
        case 'D':
        case 'E':
        case 'F':
        case 'G':
        case 'H':
        case 'I':
        case 'J':
        case 'K':
        case 'L':
        case 'M':
        case 'N':
        case 'O':
        case 'P':
        case 'Q':
        case 'R':
        case 'S':
        case 'T':
        case 'U':
        case 'V':
        case 'W':
        case 'X':
        case 'Y':
        case 'Z':
        case '_':
            {
            alt16=20;
            }
            break;
        case '0':
        case '1':
        case '2':
        case '3':
        case '4':
        case '5':
        case '6':
        case '7':
        case '8':
        case '9':
            {
            alt16=21;
            }
            break;
        case '%':
            {
            alt16=22;
            }
            break;
        case '\t':
        case '\n':
        case '\r':
        case ' ':
            {
            alt16=23;
            }
            break;
        case '\"':
            {
            alt16=24;
            }
            break;
        case '\'':
            {
            alt16=25;
            }
            break;
        default:
            NoViableAltException nvae =
                new NoViableAltException("", 16, 0, input);

            throw nvae;

        }

        switch (alt16) {
            case 1 :
                // /home/staff/aweinz/svnrepo/gerald_weidinger_thesis/WOC - Wings of Change/antlr/woc.g:1:10: T__16
                {
                mT__16(); 


                }
                break;
            case 2 :
                // /home/staff/aweinz/svnrepo/gerald_weidinger_thesis/WOC - Wings of Change/antlr/woc.g:1:16: T__17
                {
                mT__17(); 


                }
                break;
            case 3 :
                // /home/staff/aweinz/svnrepo/gerald_weidinger_thesis/WOC - Wings of Change/antlr/woc.g:1:22: T__18
                {
                mT__18(); 


                }
                break;
            case 4 :
                // /home/staff/aweinz/svnrepo/gerald_weidinger_thesis/WOC - Wings of Change/antlr/woc.g:1:28: T__19
                {
                mT__19(); 


                }
                break;
            case 5 :
                // /home/staff/aweinz/svnrepo/gerald_weidinger_thesis/WOC - Wings of Change/antlr/woc.g:1:34: T__20
                {
                mT__20(); 


                }
                break;
            case 6 :
                // /home/staff/aweinz/svnrepo/gerald_weidinger_thesis/WOC - Wings of Change/antlr/woc.g:1:40: T__21
                {
                mT__21(); 


                }
                break;
            case 7 :
                // /home/staff/aweinz/svnrepo/gerald_weidinger_thesis/WOC - Wings of Change/antlr/woc.g:1:46: T__22
                {
                mT__22(); 


                }
                break;
            case 8 :
                // /home/staff/aweinz/svnrepo/gerald_weidinger_thesis/WOC - Wings of Change/antlr/woc.g:1:52: T__23
                {
                mT__23(); 


                }
                break;
            case 9 :
                // /home/staff/aweinz/svnrepo/gerald_weidinger_thesis/WOC - Wings of Change/antlr/woc.g:1:58: T__24
                {
                mT__24(); 


                }
                break;
            case 10 :
                // /home/staff/aweinz/svnrepo/gerald_weidinger_thesis/WOC - Wings of Change/antlr/woc.g:1:64: T__25
                {
                mT__25(); 


                }
                break;
            case 11 :
                // /home/staff/aweinz/svnrepo/gerald_weidinger_thesis/WOC - Wings of Change/antlr/woc.g:1:70: T__26
                {
                mT__26(); 


                }
                break;
            case 12 :
                // /home/staff/aweinz/svnrepo/gerald_weidinger_thesis/WOC - Wings of Change/antlr/woc.g:1:76: T__27
                {
                mT__27(); 


                }
                break;
            case 13 :
                // /home/staff/aweinz/svnrepo/gerald_weidinger_thesis/WOC - Wings of Change/antlr/woc.g:1:82: T__28
                {
                mT__28(); 


                }
                break;
            case 14 :
                // /home/staff/aweinz/svnrepo/gerald_weidinger_thesis/WOC - Wings of Change/antlr/woc.g:1:88: T__29
                {
                mT__29(); 


                }
                break;
            case 15 :
                // /home/staff/aweinz/svnrepo/gerald_weidinger_thesis/WOC - Wings of Change/antlr/woc.g:1:94: T__30
                {
                mT__30(); 


                }
                break;
            case 16 :
                // /home/staff/aweinz/svnrepo/gerald_weidinger_thesis/WOC - Wings of Change/antlr/woc.g:1:100: T__31
                {
                mT__31(); 


                }
                break;
            case 17 :
                // /home/staff/aweinz/svnrepo/gerald_weidinger_thesis/WOC - Wings of Change/antlr/woc.g:1:106: T__32
                {
                mT__32(); 


                }
                break;
            case 18 :
                // /home/staff/aweinz/svnrepo/gerald_weidinger_thesis/WOC - Wings of Change/antlr/woc.g:1:112: T__33
                {
                mT__33(); 


                }
                break;
            case 19 :
                // /home/staff/aweinz/svnrepo/gerald_weidinger_thesis/WOC - Wings of Change/antlr/woc.g:1:118: ID
                {
                mID(); 


                }
                break;
            case 20 :
                // /home/staff/aweinz/svnrepo/gerald_weidinger_thesis/WOC - Wings of Change/antlr/woc.g:1:121: VAR
                {
                mVAR(); 


                }
                break;
            case 21 :
                // /home/staff/aweinz/svnrepo/gerald_weidinger_thesis/WOC - Wings of Change/antlr/woc.g:1:125: INT
                {
                mINT(); 


                }
                break;
            case 22 :
                // /home/staff/aweinz/svnrepo/gerald_weidinger_thesis/WOC - Wings of Change/antlr/woc.g:1:129: COMMENT
                {
                mCOMMENT(); 


                }
                break;
            case 23 :
                // /home/staff/aweinz/svnrepo/gerald_weidinger_thesis/WOC - Wings of Change/antlr/woc.g:1:137: WS
                {
                mWS(); 


                }
                break;
            case 24 :
                // /home/staff/aweinz/svnrepo/gerald_weidinger_thesis/WOC - Wings of Change/antlr/woc.g:1:140: STRING
                {
                mSTRING(); 


                }
                break;
            case 25 :
                // /home/staff/aweinz/svnrepo/gerald_weidinger_thesis/WOC - Wings of Change/antlr/woc.g:1:147: CHAR
                {
                mCHAR(); 


                }
                break;

        }

    }


 

}