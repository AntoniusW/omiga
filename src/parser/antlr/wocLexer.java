// $ANTLR 3.4 /home/as/incman/gerald_weidinger_thesis/WOC - Wings of Change/antlr/woc.g 2012-04-23 13:05:59

package parser.antlr;


import org.antlr.runtime.*;
import java.util.Stack;
import java.util.List;
import java.util.ArrayList;

@SuppressWarnings({"all", "warnings", "unchecked"})
public class wocLexer extends Lexer {
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
    public String getGrammarFileName() { return "/home/as/incman/gerald_weidinger_thesis/WOC - Wings of Change/antlr/woc.g"; }

    // $ANTLR start "T__17"
    public final void mT__17() throws RecognitionException {
        try {
            int _type = T__17;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // /home/as/incman/gerald_weidinger_thesis/WOC - Wings of Change/antlr/woc.g:6:7: ( '(' )
            // /home/as/incman/gerald_weidinger_thesis/WOC - Wings of Change/antlr/woc.g:6:9: '('
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
            // /home/as/incman/gerald_weidinger_thesis/WOC - Wings of Change/antlr/woc.g:7:7: ( ')' )
            // /home/as/incman/gerald_weidinger_thesis/WOC - Wings of Change/antlr/woc.g:7:9: ')'
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
            // /home/as/incman/gerald_weidinger_thesis/WOC - Wings of Change/antlr/woc.g:8:7: ( '*' )
            // /home/as/incman/gerald_weidinger_thesis/WOC - Wings of Change/antlr/woc.g:8:9: '*'
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
            // /home/as/incman/gerald_weidinger_thesis/WOC - Wings of Change/antlr/woc.g:9:7: ( '+' )
            // /home/as/incman/gerald_weidinger_thesis/WOC - Wings of Change/antlr/woc.g:9:9: '+'
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
            // /home/as/incman/gerald_weidinger_thesis/WOC - Wings of Change/antlr/woc.g:10:7: ( ',' )
            // /home/as/incman/gerald_weidinger_thesis/WOC - Wings of Change/antlr/woc.g:10:9: ','
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
            // /home/as/incman/gerald_weidinger_thesis/WOC - Wings of Change/antlr/woc.g:11:7: ( '-' )
            // /home/as/incman/gerald_weidinger_thesis/WOC - Wings of Change/antlr/woc.g:11:9: '-'
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
            // /home/as/incman/gerald_weidinger_thesis/WOC - Wings of Change/antlr/woc.g:12:7: ( '.' )
            // /home/as/incman/gerald_weidinger_thesis/WOC - Wings of Change/antlr/woc.g:12:9: '.'
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
            // /home/as/incman/gerald_weidinger_thesis/WOC - Wings of Change/antlr/woc.g:13:7: ( '/' )
            // /home/as/incman/gerald_weidinger_thesis/WOC - Wings of Change/antlr/woc.g:13:9: '/'
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
            // /home/as/incman/gerald_weidinger_thesis/WOC - Wings of Change/antlr/woc.g:14:7: ( ':' )
            // /home/as/incman/gerald_weidinger_thesis/WOC - Wings of Change/antlr/woc.g:14:9: ':'
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
            // /home/as/incman/gerald_weidinger_thesis/WOC - Wings of Change/antlr/woc.g:15:7: ( ':-' )
            // /home/as/incman/gerald_weidinger_thesis/WOC - Wings of Change/antlr/woc.g:15:9: ':-'
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
            // /home/as/incman/gerald_weidinger_thesis/WOC - Wings of Change/antlr/woc.g:16:7: ( 'is' )
            // /home/as/incman/gerald_weidinger_thesis/WOC - Wings of Change/antlr/woc.g:16:9: 'is'
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
    // $ANTLR end "T__27"

    // $ANTLR start "T__28"
    public final void mT__28() throws RecognitionException {
        try {
            int _type = T__28;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // /home/as/incman/gerald_weidinger_thesis/WOC - Wings of Change/antlr/woc.g:17:7: ( 'not' )
            // /home/as/incman/gerald_weidinger_thesis/WOC - Wings of Change/antlr/woc.g:17:9: 'not'
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
    // $ANTLR end "T__28"

    // $ANTLR start "ID"
    public final void mID() throws RecognitionException {
        try {
            int _type = ID;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // /home/as/incman/gerald_weidinger_thesis/WOC - Wings of Change/antlr/woc.g:182:5: ( ( 'a' .. 'z' ) ( 'a' .. 'z' | 'A' .. 'Z' | '0' .. '9' | '_' )* )
            // /home/as/incman/gerald_weidinger_thesis/WOC - Wings of Change/antlr/woc.g:182:7: ( 'a' .. 'z' ) ( 'a' .. 'z' | 'A' .. 'Z' | '0' .. '9' | '_' )*
            {
            if ( (input.LA(1) >= 'a' && input.LA(1) <= 'z') ) {
                input.consume();
            }
            else {
                MismatchedSetException mse = new MismatchedSetException(null,input);
                recover(mse);
                throw mse;
            }


            // /home/as/incman/gerald_weidinger_thesis/WOC - Wings of Change/antlr/woc.g:182:18: ( 'a' .. 'z' | 'A' .. 'Z' | '0' .. '9' | '_' )*
            loop1:
            do {
                int alt1=2;
                int LA1_0 = input.LA(1);

                if ( ((LA1_0 >= '0' && LA1_0 <= '9')||(LA1_0 >= 'A' && LA1_0 <= 'Z')||LA1_0=='_'||(LA1_0 >= 'a' && LA1_0 <= 'z')) ) {
                    alt1=1;
                }


                switch (alt1) {
            	case 1 :
            	    // /home/as/incman/gerald_weidinger_thesis/WOC - Wings of Change/antlr/woc.g:
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
            // /home/as/incman/gerald_weidinger_thesis/WOC - Wings of Change/antlr/woc.g:185:5: ( ( 'A' .. 'Z' | '_' ) ( 'a' .. 'z' | 'A' .. 'Z' | '0' .. '9' | '_' )* )
            // /home/as/incman/gerald_weidinger_thesis/WOC - Wings of Change/antlr/woc.g:185:7: ( 'A' .. 'Z' | '_' ) ( 'a' .. 'z' | 'A' .. 'Z' | '0' .. '9' | '_' )*
            {
            if ( (input.LA(1) >= 'A' && input.LA(1) <= 'Z')||input.LA(1)=='_' ) {
                input.consume();
            }
            else {
                MismatchedSetException mse = new MismatchedSetException(null,input);
                recover(mse);
                throw mse;
            }


            // /home/as/incman/gerald_weidinger_thesis/WOC - Wings of Change/antlr/woc.g:185:22: ( 'a' .. 'z' | 'A' .. 'Z' | '0' .. '9' | '_' )*
            loop2:
            do {
                int alt2=2;
                int LA2_0 = input.LA(1);

                if ( ((LA2_0 >= '0' && LA2_0 <= '9')||(LA2_0 >= 'A' && LA2_0 <= 'Z')||LA2_0=='_'||(LA2_0 >= 'a' && LA2_0 <= 'z')) ) {
                    alt2=1;
                }


                switch (alt2) {
            	case 1 :
            	    // /home/as/incman/gerald_weidinger_thesis/WOC - Wings of Change/antlr/woc.g:
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
            // /home/as/incman/gerald_weidinger_thesis/WOC - Wings of Change/antlr/woc.g:188:5: ( ( '-' )? ( '0' .. '9' )+ )
            // /home/as/incman/gerald_weidinger_thesis/WOC - Wings of Change/antlr/woc.g:188:7: ( '-' )? ( '0' .. '9' )+
            {
            // /home/as/incman/gerald_weidinger_thesis/WOC - Wings of Change/antlr/woc.g:188:7: ( '-' )?
            int alt3=2;
            int LA3_0 = input.LA(1);

            if ( (LA3_0=='-') ) {
                alt3=1;
            }
            switch (alt3) {
                case 1 :
                    // /home/as/incman/gerald_weidinger_thesis/WOC - Wings of Change/antlr/woc.g:188:8: '-'
                    {
                    match('-'); 

                    }
                    break;

            }


            // /home/as/incman/gerald_weidinger_thesis/WOC - Wings of Change/antlr/woc.g:188:14: ( '0' .. '9' )+
            int cnt4=0;
            loop4:
            do {
                int alt4=2;
                int LA4_0 = input.LA(1);

                if ( ((LA4_0 >= '0' && LA4_0 <= '9')) ) {
                    alt4=1;
                }


                switch (alt4) {
            	case 1 :
            	    // /home/as/incman/gerald_weidinger_thesis/WOC - Wings of Change/antlr/woc.g:
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
            	    if ( cnt4 >= 1 ) break loop4;
                        EarlyExitException eee =
                            new EarlyExitException(4, input);
                        throw eee;
                }
                cnt4++;
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
            // /home/as/incman/gerald_weidinger_thesis/WOC - Wings of Change/antlr/woc.g:199:5: ( '//' (~ ( '\\n' | '\\r' ) )* ( '\\r' )? '\\n' | '%' (~ ( '\\n' | '\\r' ) )* ( '\\r' )? '\\n' | '/*' ( options {greedy=false; } : . )* '*/' )
            int alt10=3;
            int LA10_0 = input.LA(1);

            if ( (LA10_0=='/') ) {
                int LA10_1 = input.LA(2);

                if ( (LA10_1=='/') ) {
                    alt10=1;
                }
                else if ( (LA10_1=='*') ) {
                    alt10=3;
                }
                else {
                    NoViableAltException nvae =
                        new NoViableAltException("", 10, 1, input);

                    throw nvae;

                }
            }
            else if ( (LA10_0=='%') ) {
                alt10=2;
            }
            else {
                NoViableAltException nvae =
                    new NoViableAltException("", 10, 0, input);

                throw nvae;

            }
            switch (alt10) {
                case 1 :
                    // /home/as/incman/gerald_weidinger_thesis/WOC - Wings of Change/antlr/woc.g:199:9: '//' (~ ( '\\n' | '\\r' ) )* ( '\\r' )? '\\n'
                    {
                    match("//"); 



                    // /home/as/incman/gerald_weidinger_thesis/WOC - Wings of Change/antlr/woc.g:199:14: (~ ( '\\n' | '\\r' ) )*
                    loop5:
                    do {
                        int alt5=2;
                        int LA5_0 = input.LA(1);

                        if ( ((LA5_0 >= '\u0000' && LA5_0 <= '\t')||(LA5_0 >= '\u000B' && LA5_0 <= '\f')||(LA5_0 >= '\u000E' && LA5_0 <= '\uFFFF')) ) {
                            alt5=1;
                        }


                        switch (alt5) {
                    	case 1 :
                    	    // /home/as/incman/gerald_weidinger_thesis/WOC - Wings of Change/antlr/woc.g:
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
                    	    break loop5;
                        }
                    } while (true);


                    // /home/as/incman/gerald_weidinger_thesis/WOC - Wings of Change/antlr/woc.g:199:28: ( '\\r' )?
                    int alt6=2;
                    int LA6_0 = input.LA(1);

                    if ( (LA6_0=='\r') ) {
                        alt6=1;
                    }
                    switch (alt6) {
                        case 1 :
                            // /home/as/incman/gerald_weidinger_thesis/WOC - Wings of Change/antlr/woc.g:199:28: '\\r'
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
                    // /home/as/incman/gerald_weidinger_thesis/WOC - Wings of Change/antlr/woc.g:200:7: '%' (~ ( '\\n' | '\\r' ) )* ( '\\r' )? '\\n'
                    {
                    match('%'); 

                    // /home/as/incman/gerald_weidinger_thesis/WOC - Wings of Change/antlr/woc.g:200:11: (~ ( '\\n' | '\\r' ) )*
                    loop7:
                    do {
                        int alt7=2;
                        int LA7_0 = input.LA(1);

                        if ( ((LA7_0 >= '\u0000' && LA7_0 <= '\t')||(LA7_0 >= '\u000B' && LA7_0 <= '\f')||(LA7_0 >= '\u000E' && LA7_0 <= '\uFFFF')) ) {
                            alt7=1;
                        }


                        switch (alt7) {
                    	case 1 :
                    	    // /home/as/incman/gerald_weidinger_thesis/WOC - Wings of Change/antlr/woc.g:
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
                    	    break loop7;
                        }
                    } while (true);


                    // /home/as/incman/gerald_weidinger_thesis/WOC - Wings of Change/antlr/woc.g:200:25: ( '\\r' )?
                    int alt8=2;
                    int LA8_0 = input.LA(1);

                    if ( (LA8_0=='\r') ) {
                        alt8=1;
                    }
                    switch (alt8) {
                        case 1 :
                            // /home/as/incman/gerald_weidinger_thesis/WOC - Wings of Change/antlr/woc.g:200:25: '\\r'
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
                    // /home/as/incman/gerald_weidinger_thesis/WOC - Wings of Change/antlr/woc.g:201:9: '/*' ( options {greedy=false; } : . )* '*/'
                    {
                    match("/*"); 



                    // /home/as/incman/gerald_weidinger_thesis/WOC - Wings of Change/antlr/woc.g:201:14: ( options {greedy=false; } : . )*
                    loop9:
                    do {
                        int alt9=2;
                        int LA9_0 = input.LA(1);

                        if ( (LA9_0=='*') ) {
                            int LA9_1 = input.LA(2);

                            if ( (LA9_1=='/') ) {
                                alt9=2;
                            }
                            else if ( ((LA9_1 >= '\u0000' && LA9_1 <= '.')||(LA9_1 >= '0' && LA9_1 <= '\uFFFF')) ) {
                                alt9=1;
                            }


                        }
                        else if ( ((LA9_0 >= '\u0000' && LA9_0 <= ')')||(LA9_0 >= '+' && LA9_0 <= '\uFFFF')) ) {
                            alt9=1;
                        }


                        switch (alt9) {
                    	case 1 :
                    	    // /home/as/incman/gerald_weidinger_thesis/WOC - Wings of Change/antlr/woc.g:201:42: .
                    	    {
                    	    matchAny(); 

                    	    }
                    	    break;

                    	default :
                    	    break loop9;
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

    // $ANTLR start "COMP"
    public final void mCOMP() throws RecognitionException {
        try {
            int _type = COMP;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // /home/as/incman/gerald_weidinger_thesis/WOC - Wings of Change/antlr/woc.g:205:6: ( '<' | '<=' | '=>' | '>' | '=' )
            int alt11=5;
            switch ( input.LA(1) ) {
            case '<':
                {
                int LA11_1 = input.LA(2);

                if ( (LA11_1=='=') ) {
                    alt11=2;
                }
                else {
                    alt11=1;
                }
                }
                break;
            case '=':
                {
                int LA11_2 = input.LA(2);

                if ( (LA11_2=='>') ) {
                    alt11=3;
                }
                else {
                    alt11=5;
                }
                }
                break;
            case '>':
                {
                alt11=4;
                }
                break;
            default:
                NoViableAltException nvae =
                    new NoViableAltException("", 11, 0, input);

                throw nvae;

            }

            switch (alt11) {
                case 1 :
                    // /home/as/incman/gerald_weidinger_thesis/WOC - Wings of Change/antlr/woc.g:205:8: '<'
                    {
                    match('<'); 

                    }
                    break;
                case 2 :
                    // /home/as/incman/gerald_weidinger_thesis/WOC - Wings of Change/antlr/woc.g:205:14: '<='
                    {
                    match("<="); 



                    }
                    break;
                case 3 :
                    // /home/as/incman/gerald_weidinger_thesis/WOC - Wings of Change/antlr/woc.g:205:21: '=>'
                    {
                    match("=>"); 



                    }
                    break;
                case 4 :
                    // /home/as/incman/gerald_weidinger_thesis/WOC - Wings of Change/antlr/woc.g:205:28: '>'
                    {
                    match('>'); 

                    }
                    break;
                case 5 :
                    // /home/as/incman/gerald_weidinger_thesis/WOC - Wings of Change/antlr/woc.g:205:34: '='
                    {
                    match('='); 

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
    // $ANTLR end "COMP"

    // $ANTLR start "WS"
    public final void mWS() throws RecognitionException {
        try {
            int _type = WS;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // /home/as/incman/gerald_weidinger_thesis/WOC - Wings of Change/antlr/woc.g:208:5: ( ( ' ' | '\\t' | '\\r' | '\\n' ) )
            // /home/as/incman/gerald_weidinger_thesis/WOC - Wings of Change/antlr/woc.g:208:9: ( ' ' | '\\t' | '\\r' | '\\n' )
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
            // /home/as/incman/gerald_weidinger_thesis/WOC - Wings of Change/antlr/woc.g:216:5: ( '\"' ( ESC_SEQ |~ ( '\\\\' | '\"' ) )* '\"' )
            // /home/as/incman/gerald_weidinger_thesis/WOC - Wings of Change/antlr/woc.g:216:8: '\"' ( ESC_SEQ |~ ( '\\\\' | '\"' ) )* '\"'
            {
            match('\"'); 

            // /home/as/incman/gerald_weidinger_thesis/WOC - Wings of Change/antlr/woc.g:216:12: ( ESC_SEQ |~ ( '\\\\' | '\"' ) )*
            loop12:
            do {
                int alt12=3;
                int LA12_0 = input.LA(1);

                if ( (LA12_0=='\\') ) {
                    alt12=1;
                }
                else if ( ((LA12_0 >= '\u0000' && LA12_0 <= '!')||(LA12_0 >= '#' && LA12_0 <= '[')||(LA12_0 >= ']' && LA12_0 <= '\uFFFF')) ) {
                    alt12=2;
                }


                switch (alt12) {
            	case 1 :
            	    // /home/as/incman/gerald_weidinger_thesis/WOC - Wings of Change/antlr/woc.g:216:14: ESC_SEQ
            	    {
            	    mESC_SEQ(); 


            	    }
            	    break;
            	case 2 :
            	    // /home/as/incman/gerald_weidinger_thesis/WOC - Wings of Change/antlr/woc.g:216:24: ~ ( '\\\\' | '\"' )
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
            	    break loop12;
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
            // /home/as/incman/gerald_weidinger_thesis/WOC - Wings of Change/antlr/woc.g:219:5: ( '\\'' ( ESC_SEQ |~ ( '\\'' | '\\\\' ) ) '\\'' )
            // /home/as/incman/gerald_weidinger_thesis/WOC - Wings of Change/antlr/woc.g:219:8: '\\'' ( ESC_SEQ |~ ( '\\'' | '\\\\' ) ) '\\''
            {
            match('\''); 

            // /home/as/incman/gerald_weidinger_thesis/WOC - Wings of Change/antlr/woc.g:219:13: ( ESC_SEQ |~ ( '\\'' | '\\\\' ) )
            int alt13=2;
            int LA13_0 = input.LA(1);

            if ( (LA13_0=='\\') ) {
                alt13=1;
            }
            else if ( ((LA13_0 >= '\u0000' && LA13_0 <= '&')||(LA13_0 >= '(' && LA13_0 <= '[')||(LA13_0 >= ']' && LA13_0 <= '\uFFFF')) ) {
                alt13=2;
            }
            else {
                NoViableAltException nvae =
                    new NoViableAltException("", 13, 0, input);

                throw nvae;

            }
            switch (alt13) {
                case 1 :
                    // /home/as/incman/gerald_weidinger_thesis/WOC - Wings of Change/antlr/woc.g:219:15: ESC_SEQ
                    {
                    mESC_SEQ(); 


                    }
                    break;
                case 2 :
                    // /home/as/incman/gerald_weidinger_thesis/WOC - Wings of Change/antlr/woc.g:219:25: ~ ( '\\'' | '\\\\' )
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
            // /home/as/incman/gerald_weidinger_thesis/WOC - Wings of Change/antlr/woc.g:224:10: ( ( 'e' | 'E' ) ( '+' | '-' )? ( '0' .. '9' )+ )
            // /home/as/incman/gerald_weidinger_thesis/WOC - Wings of Change/antlr/woc.g:224:12: ( 'e' | 'E' ) ( '+' | '-' )? ( '0' .. '9' )+
            {
            if ( input.LA(1)=='E'||input.LA(1)=='e' ) {
                input.consume();
            }
            else {
                MismatchedSetException mse = new MismatchedSetException(null,input);
                recover(mse);
                throw mse;
            }


            // /home/as/incman/gerald_weidinger_thesis/WOC - Wings of Change/antlr/woc.g:224:22: ( '+' | '-' )?
            int alt14=2;
            int LA14_0 = input.LA(1);

            if ( (LA14_0=='+'||LA14_0=='-') ) {
                alt14=1;
            }
            switch (alt14) {
                case 1 :
                    // /home/as/incman/gerald_weidinger_thesis/WOC - Wings of Change/antlr/woc.g:
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


            // /home/as/incman/gerald_weidinger_thesis/WOC - Wings of Change/antlr/woc.g:224:33: ( '0' .. '9' )+
            int cnt15=0;
            loop15:
            do {
                int alt15=2;
                int LA15_0 = input.LA(1);

                if ( ((LA15_0 >= '0' && LA15_0 <= '9')) ) {
                    alt15=1;
                }


                switch (alt15) {
            	case 1 :
            	    // /home/as/incman/gerald_weidinger_thesis/WOC - Wings of Change/antlr/woc.g:
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
            	    if ( cnt15 >= 1 ) break loop15;
                        EarlyExitException eee =
                            new EarlyExitException(15, input);
                        throw eee;
                }
                cnt15++;
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
            // /home/as/incman/gerald_weidinger_thesis/WOC - Wings of Change/antlr/woc.g:227:11: ( ( '0' .. '9' | 'a' .. 'f' | 'A' .. 'F' ) )
            // /home/as/incman/gerald_weidinger_thesis/WOC - Wings of Change/antlr/woc.g:
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
            // /home/as/incman/gerald_weidinger_thesis/WOC - Wings of Change/antlr/woc.g:231:5: ( '\\\\' ( 'b' | 't' | 'n' | 'f' | 'r' | '\\\"' | '\\'' | '\\\\' ) | UNICODE_ESC | OCTAL_ESC )
            int alt16=3;
            int LA16_0 = input.LA(1);

            if ( (LA16_0=='\\') ) {
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
                    alt16=1;
                    }
                    break;
                case 'u':
                    {
                    alt16=2;
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
                    alt16=3;
                    }
                    break;
                default:
                    NoViableAltException nvae =
                        new NoViableAltException("", 16, 1, input);

                    throw nvae;

                }

            }
            else {
                NoViableAltException nvae =
                    new NoViableAltException("", 16, 0, input);

                throw nvae;

            }
            switch (alt16) {
                case 1 :
                    // /home/as/incman/gerald_weidinger_thesis/WOC - Wings of Change/antlr/woc.g:231:9: '\\\\' ( 'b' | 't' | 'n' | 'f' | 'r' | '\\\"' | '\\'' | '\\\\' )
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
                    // /home/as/incman/gerald_weidinger_thesis/WOC - Wings of Change/antlr/woc.g:232:9: UNICODE_ESC
                    {
                    mUNICODE_ESC(); 


                    }
                    break;
                case 3 :
                    // /home/as/incman/gerald_weidinger_thesis/WOC - Wings of Change/antlr/woc.g:233:9: OCTAL_ESC
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
            // /home/as/incman/gerald_weidinger_thesis/WOC - Wings of Change/antlr/woc.g:238:5: ( '\\\\' ( '0' .. '3' ) ( '0' .. '7' ) ( '0' .. '7' ) | '\\\\' ( '0' .. '7' ) ( '0' .. '7' ) | '\\\\' ( '0' .. '7' ) )
            int alt17=3;
            int LA17_0 = input.LA(1);

            if ( (LA17_0=='\\') ) {
                int LA17_1 = input.LA(2);

                if ( ((LA17_1 >= '0' && LA17_1 <= '3')) ) {
                    int LA17_2 = input.LA(3);

                    if ( ((LA17_2 >= '0' && LA17_2 <= '7')) ) {
                        int LA17_4 = input.LA(4);

                        if ( ((LA17_4 >= '0' && LA17_4 <= '7')) ) {
                            alt17=1;
                        }
                        else {
                            alt17=2;
                        }
                    }
                    else {
                        alt17=3;
                    }
                }
                else if ( ((LA17_1 >= '4' && LA17_1 <= '7')) ) {
                    int LA17_3 = input.LA(3);

                    if ( ((LA17_3 >= '0' && LA17_3 <= '7')) ) {
                        alt17=2;
                    }
                    else {
                        alt17=3;
                    }
                }
                else {
                    NoViableAltException nvae =
                        new NoViableAltException("", 17, 1, input);

                    throw nvae;

                }
            }
            else {
                NoViableAltException nvae =
                    new NoViableAltException("", 17, 0, input);

                throw nvae;

            }
            switch (alt17) {
                case 1 :
                    // /home/as/incman/gerald_weidinger_thesis/WOC - Wings of Change/antlr/woc.g:238:9: '\\\\' ( '0' .. '3' ) ( '0' .. '7' ) ( '0' .. '7' )
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
                    // /home/as/incman/gerald_weidinger_thesis/WOC - Wings of Change/antlr/woc.g:239:9: '\\\\' ( '0' .. '7' ) ( '0' .. '7' )
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
                    // /home/as/incman/gerald_weidinger_thesis/WOC - Wings of Change/antlr/woc.g:240:9: '\\\\' ( '0' .. '7' )
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
            // /home/as/incman/gerald_weidinger_thesis/WOC - Wings of Change/antlr/woc.g:245:5: ( '\\\\' 'u' HEX_DIGIT HEX_DIGIT HEX_DIGIT HEX_DIGIT )
            // /home/as/incman/gerald_weidinger_thesis/WOC - Wings of Change/antlr/woc.g:245:9: '\\\\' 'u' HEX_DIGIT HEX_DIGIT HEX_DIGIT HEX_DIGIT
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
        // /home/as/incman/gerald_weidinger_thesis/WOC - Wings of Change/antlr/woc.g:1:8: ( T__17 | T__18 | T__19 | T__20 | T__21 | T__22 | T__23 | T__24 | T__25 | T__26 | T__27 | T__28 | ID | VAR | INT | COMMENT | COMP | WS | STRING | CHAR )
        int alt18=20;
        switch ( input.LA(1) ) {
        case '(':
            {
            alt18=1;
            }
            break;
        case ')':
            {
            alt18=2;
            }
            break;
        case '*':
            {
            alt18=3;
            }
            break;
        case '+':
            {
            alt18=4;
            }
            break;
        case ',':
            {
            alt18=5;
            }
            break;
        case '-':
            {
            int LA18_6 = input.LA(2);

            if ( ((LA18_6 >= '0' && LA18_6 <= '9')) ) {
                alt18=15;
            }
            else {
                alt18=6;
            }
            }
            break;
        case '.':
            {
            alt18=7;
            }
            break;
        case '/':
            {
            int LA18_8 = input.LA(2);

            if ( (LA18_8=='*'||LA18_8=='/') ) {
                alt18=16;
            }
            else {
                alt18=8;
            }
            }
            break;
        case ':':
            {
            int LA18_9 = input.LA(2);

            if ( (LA18_9=='-') ) {
                alt18=10;
            }
            else {
                alt18=9;
            }
            }
            break;
        case 'i':
            {
            int LA18_10 = input.LA(2);

            if ( (LA18_10=='s') ) {
                int LA18_24 = input.LA(3);

                if ( ((LA18_24 >= '0' && LA18_24 <= '9')||(LA18_24 >= 'A' && LA18_24 <= 'Z')||LA18_24=='_'||(LA18_24 >= 'a' && LA18_24 <= 'z')) ) {
                    alt18=13;
                }
                else {
                    alt18=11;
                }
            }
            else {
                alt18=13;
            }
            }
            break;
        case 'n':
            {
            int LA18_11 = input.LA(2);

            if ( (LA18_11=='o') ) {
                int LA18_25 = input.LA(3);

                if ( (LA18_25=='t') ) {
                    int LA18_27 = input.LA(4);

                    if ( ((LA18_27 >= '0' && LA18_27 <= '9')||(LA18_27 >= 'A' && LA18_27 <= 'Z')||LA18_27=='_'||(LA18_27 >= 'a' && LA18_27 <= 'z')) ) {
                        alt18=13;
                    }
                    else {
                        alt18=12;
                    }
                }
                else {
                    alt18=13;
                }
            }
            else {
                alt18=13;
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
            alt18=13;
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
            alt18=14;
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
            alt18=15;
            }
            break;
        case '%':
            {
            alt18=16;
            }
            break;
        case '<':
        case '=':
        case '>':
            {
            alt18=17;
            }
            break;
        case '\t':
        case '\n':
        case '\r':
        case ' ':
            {
            alt18=18;
            }
            break;
        case '\"':
            {
            alt18=19;
            }
            break;
        case '\'':
            {
            alt18=20;
            }
            break;
        default:
            NoViableAltException nvae =
                new NoViableAltException("", 18, 0, input);

            throw nvae;

        }

        switch (alt18) {
            case 1 :
                // /home/as/incman/gerald_weidinger_thesis/WOC - Wings of Change/antlr/woc.g:1:10: T__17
                {
                mT__17(); 


                }
                break;
            case 2 :
                // /home/as/incman/gerald_weidinger_thesis/WOC - Wings of Change/antlr/woc.g:1:16: T__18
                {
                mT__18(); 


                }
                break;
            case 3 :
                // /home/as/incman/gerald_weidinger_thesis/WOC - Wings of Change/antlr/woc.g:1:22: T__19
                {
                mT__19(); 


                }
                break;
            case 4 :
                // /home/as/incman/gerald_weidinger_thesis/WOC - Wings of Change/antlr/woc.g:1:28: T__20
                {
                mT__20(); 


                }
                break;
            case 5 :
                // /home/as/incman/gerald_weidinger_thesis/WOC - Wings of Change/antlr/woc.g:1:34: T__21
                {
                mT__21(); 


                }
                break;
            case 6 :
                // /home/as/incman/gerald_weidinger_thesis/WOC - Wings of Change/antlr/woc.g:1:40: T__22
                {
                mT__22(); 


                }
                break;
            case 7 :
                // /home/as/incman/gerald_weidinger_thesis/WOC - Wings of Change/antlr/woc.g:1:46: T__23
                {
                mT__23(); 


                }
                break;
            case 8 :
                // /home/as/incman/gerald_weidinger_thesis/WOC - Wings of Change/antlr/woc.g:1:52: T__24
                {
                mT__24(); 


                }
                break;
            case 9 :
                // /home/as/incman/gerald_weidinger_thesis/WOC - Wings of Change/antlr/woc.g:1:58: T__25
                {
                mT__25(); 


                }
                break;
            case 10 :
                // /home/as/incman/gerald_weidinger_thesis/WOC - Wings of Change/antlr/woc.g:1:64: T__26
                {
                mT__26(); 


                }
                break;
            case 11 :
                // /home/as/incman/gerald_weidinger_thesis/WOC - Wings of Change/antlr/woc.g:1:70: T__27
                {
                mT__27(); 


                }
                break;
            case 12 :
                // /home/as/incman/gerald_weidinger_thesis/WOC - Wings of Change/antlr/woc.g:1:76: T__28
                {
                mT__28(); 


                }
                break;
            case 13 :
                // /home/as/incman/gerald_weidinger_thesis/WOC - Wings of Change/antlr/woc.g:1:82: ID
                {
                mID(); 


                }
                break;
            case 14 :
                // /home/as/incman/gerald_weidinger_thesis/WOC - Wings of Change/antlr/woc.g:1:85: VAR
                {
                mVAR(); 


                }
                break;
            case 15 :
                // /home/as/incman/gerald_weidinger_thesis/WOC - Wings of Change/antlr/woc.g:1:89: INT
                {
                mINT(); 


                }
                break;
            case 16 :
                // /home/as/incman/gerald_weidinger_thesis/WOC - Wings of Change/antlr/woc.g:1:93: COMMENT
                {
                mCOMMENT(); 


                }
                break;
            case 17 :
                // /home/as/incman/gerald_weidinger_thesis/WOC - Wings of Change/antlr/woc.g:1:101: COMP
                {
                mCOMP(); 


                }
                break;
            case 18 :
                // /home/as/incman/gerald_weidinger_thesis/WOC - Wings of Change/antlr/woc.g:1:106: WS
                {
                mWS(); 


                }
                break;
            case 19 :
                // /home/as/incman/gerald_weidinger_thesis/WOC - Wings of Change/antlr/woc.g:1:109: STRING
                {
                mSTRING(); 


                }
                break;
            case 20 :
                // /home/as/incman/gerald_weidinger_thesis/WOC - Wings of Change/antlr/woc.g:1:116: CHAR
                {
                mCHAR(); 


                }
                break;

        }

    }


 

}