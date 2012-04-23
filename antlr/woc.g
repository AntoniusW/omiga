grammar woc;

@header{
package parser.antlr;

import java.util.ArrayList;
}

@lexer::header{
package parser.antlr;
}

@members{

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


}

woc_program
@init{
asp_rules = new ArrayList<ParserRule>();
asp_facts = new ArrayList<ParserAtom>(); }
	:	rule_or_fact*
	;
	
rule_or_fact
	:	atom (':-' {ParserRule r = new ParserRule();} body[r] '.' {r.head=$atom.at; asp_rules.add(r);}
	|	'.' {asp_facts.add($atom.at);})
	;
	
body[ParserRule r]
	:	literal[$r] (',' literal[$r])*
	;
	
literal[ParserRule r]
	:	a1=atom {$r.posAtoms.add($a1.at);}
	|	'not' a2=atom {$r.negAtoms.add($a2.at);}
	|	operation {$r.operations.add($operation.op);}
	;
	
operation returns[ParserOperation op]
	:	v1=VAR COMP e1=expression {$op=new ParserComparison(new OpVar($v1.text),$e1.op);}
	|	v2=VAR 'is' e2=expression {$op=new ParserAssign(new OpVar($v2.text),$e2.op);}
	;
	
expression returns[ParserOperation op]
	:	l=mult_expression ((('+' {$op=new OpPlus();}| '-'{$op=new OpMinus();})
		r=mult_expression { ((BinOp)$op).left=$l.op; ((BinOp)$op).right=$r.op; })
	|	{$op=$l.op;})
	;
	
mult_expression returns[ParserOperation op]
	:	l=primary_expression ((('*' {$op=new OpMult(); }| '/' {$op=new OpDiv();} )
		r=primary_expression { ((BinOp)$op).left=$l.op; ((BinOp)$op).right=$r.op; })
	|	{$op=$l.op;})
	;
	
primary_expression returns[ParserOperation op]
	:	INT {$op=new OpInt($INT.text);}
	|	VAR {$op=new OpVar($VAR.text);}
	|	'(' expression ')' {$op=$expression.op;}
	;
	
atom	returns[ParserAtom at]
@init{ $at = new ParserAtom(); }
	:	(ctx=ID ':' {$at.context_id=$ctx.text;})?	// parsing atoms with context identifier
		name=ID {$at.name=$name.text;} ( '(' termlist ')' {$at.termlist=$termlist.termlist;})?
	;
	
termlist returns[ArrayList<ParserTerm> termlist]
@init{ $termlist = new ArrayList<ParserTerm>();}
	:	t1=term {$termlist.add($t1.term);}
		(',' tn=term {$termlist.add($tn.term);})*
	;
	
term	returns[ParserTerm term]
	:	constant {term=new ParserConstant($constant.text,true);}
	|	VAR {term=new ParserVariable($VAR.text);}
	|	function {term=$function.func;}
	;
	
function returns[ParserFunction func]
	:	ID '(' termlist ')' {func=new ParserFunction($ID.text,false,$termlist.termlist.size(),$termlist.termlist);}
	;
	
constant:	ID
	|	INT
	;
	

ID  :	('a'..'z') ('a'..'z'|'A'..'Z'|'0'..'9'|'_')*
    ;
    
VAR	:	('A'..'Z'|'_') ('a'..'z'|'A'..'Z'|'0'..'9'|'_')*
	;

INT :	('-')? '0'..'9'+
    ;

/*FLOAT
    :   ('0'..'9')+ '.' ('0'..'9')* EXPONENT? // make it * instead of + then 3. parses as rule-and or float.
    |   '.' ('0'..'9')+ EXPONENT?
    |   ('0'..'9')+ EXPONENT
    ;
    */

COMMENT
    :   '//' ~('\n'|'\r')* '\r'? '\n' {$channel=HIDDEN;}
    |	'%' ~('\n'|'\r')* '\r'? '\n' {$channel=HIDDEN;}
    |   '/*' ( options {greedy=false;} : . )* '*/' {$channel=HIDDEN;}
    ;
    
    	
COMP	:	'<' | '<=' | '=>' | '>' | '='
	;

WS  :   ( ' '
        | '\t'
        | '\r'
        | '\n'
        ) {$channel=HIDDEN;}
    ;

STRING
    :  '"' ( ESC_SEQ | ~('\\'|'"') )* '"'
    ;

CHAR:  '\'' ( ESC_SEQ | ~('\''|'\\') ) '\''
    ;

fragment
EXPONENT : ('e'|'E') ('+'|'-')? ('0'..'9')+ ;

fragment
HEX_DIGIT : ('0'..'9'|'a'..'f'|'A'..'F') ;

fragment
ESC_SEQ
    :   '\\' ('b'|'t'|'n'|'f'|'r'|'\"'|'\''|'\\')
    |   UNICODE_ESC
    |   OCTAL_ESC
    ;

fragment
OCTAL_ESC
    :   '\\' ('0'..'3') ('0'..'7') ('0'..'7')
    |   '\\' ('0'..'7') ('0'..'7')
    |   '\\' ('0'..'7')
    ;

fragment
UNICODE_ESC
    :   '\\' 'u' HEX_DIGIT HEX_DIGIT HEX_DIGIT HEX_DIGIT
    ;
