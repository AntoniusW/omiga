grammar woc;

@header{
package parser.antlr;

import java.util.ArrayList;

import Entity.*;
import Interfaces.Term;
import Exceptions.*;
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

private ContextASP context;

public void setContext(ContextASP context) {
	this.context=context;
}

}

woc_program throws RuleNotSafeException, FactSizeException
@init{
asp_rules = new ArrayList<ParserRule>();
asp_facts = new ArrayList<ParserAtom>(); }
	:	rule_or_fact*
	;
	
rule_or_fact throws RuleNotSafeException, FactSizeException
@init{ Atom head=null; }
	:	atom? {head=$atom.at;}
		(':-' {Rule r = new Rule();} body[r] '.' {if(head != null) r.setHead(head); context.addRule(r);}
	|	'.' {	// TODO: throw better exception if there is no head, currently throws NullPointerException
			context.addFact2IN($atom.at.getPredicate(),Instance.getInstance($atom.at.getTerms()));})
	;
	
body[Rule r]
	:	literal[$r] (',' literal[$r])*
	;
	
literal[Rule r]
	:	a1=atom {$r.addAtomPlus($a1.at);}
	|	'not' a2=atom {$r.addAtomMinus($a2.at);}
	|	operation {//$r.operations.add($operation.op); TODO
			}
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
	
atom	returns[Atom at]
@init{ String context_id=null; String atom_name; Term[] terms = new Term[0];}
	:	(ctx=ID ':' {context_id=$ctx.text;})?	// parsing atoms with context identifier
		name=ID {atom_name=$name.text;}
		( '(' termlist ')' {terms=(Term[])$termlist.termlist.toArray(new Term[$termlist.termlist.size()]);})?
		{$at=Atom.getAtom(atom_name, terms.length, terms);}
	;
	
termlist returns[ArrayList<Term> termlist]
@init{ $termlist = new ArrayList<Term>();}
	:	t1=term {$termlist.add($t1.term);}
		(',' tn=term {$termlist.add($tn.term);})*
	;
	
term	returns[Term term]
	:	constant {$term=$constant.constnt;}
	|	VAR {$term=Variable.getVariable($VAR.text);}
	|	function {$term=FuncTerm.getFuncTerm($function.name,$function.terms);}
	;
	
function returns[String name, ArrayList<Term> terms]
	:	ID '(' termlist ')' {$name=$ID.text; $terms=$termlist.termlist;}
	;
	
constant returns[Constant constnt]
	:	ID {$constnt=Constant.getConstant($ID.text);}
	|	INT {$constnt=Constant.getConstant($INT.text);}
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
