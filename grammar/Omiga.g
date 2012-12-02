grammar Omiga;

@header{
package parser.antlr;

import java.util.ArrayList;

import Entity.*;
import Interfaces.Term;
import Interfaces.OperandI;
import Enumeration.OP;
import Exceptions.*;
}

@lexer::header{
package parser.antlr;
import Enumeration.OP;
}

@members{
private ContextASPMCSRewriting context;
int rule_or_fact_counter;

public void setContext(ContextASPMCSRewriting context) {
	this.context=context;
	rule_or_fact_counter=0;
}

}

woc_program throws RuleNotSafeException, FactSizeException
	:	rule_or_fact*
	;

rule_or_fact throws RuleNotSafeException, FactSizeException
@init{ boolean hasHead = false; }
	:	(fact{hasHead=true;})?
		(':-' {Rule r = new Rule();} body[r] '.' {if(hasHead ) {
				Term[] termarr=(Term[])$fact.terms.toArray(new Term[$fact.terms.size()]);
				Atom head=Atom.getAtom($fact.name,$fact.terms.size(),termarr);
				r.setHead(head); }
				context.addRule(r);}
	|	'.' {	// TODO: throw better exception if there is no head, currently throws NullPointerException
			context.addFact2IN(Predicate.getPredicate($fact.name,$fact.terms.size()),
				Instance.getInstance($fact.terms.toArray(new Term[$fact.terms.size()])));})
	{ //System.out.println("Parsing rule_or_fact No.: "+rule_or_fact_counter++);
	}
	;
	
fact 	returns[String name, ArrayList<Term> terms]
	:	//(ctx=ID ':' {context_id=$ctx.text;})?	// parsing atoms with context identifier
		ID {$name=$ID.text; }
		( '(' termlist ')' {$terms=$termlist.termlist;})?
	;
	
body[Rule r]
	:	literal[$r] (',' literal[$r])*
	;
	
literal[Rule r]
	:	a1=atom {$r.addAtomPlus($a1.at);}
	|	'not' a2=atom {$r.addAtomMinus($a2.at);}
	|	operation { $r.addOperator($operation.op); }
	;
	
operation returns[Operator op]
	:	v1=VAR comp_sym e1=expression {$op=new Operator(Variable.getVariable($v1.text),$e1.op,$comp_sym.openu);}
	|	v2=VAR 'is' e2=expression {$op=new Operator(Variable.getVariable($v2.text),$e2.op,OP.ASSIGN);}
	;
	
expression returns[OperandI op]
@init{ OP openu=null; }
	:	l=mult_expression ((('+' {openu=OP.PLUS;}| '-'{openu=OP.MINUS;})
		r=mult_expression { $op=new Operator($l.op,$r.op,openu); })
	|	{$op=$l.op;})
	;
	
mult_expression returns[OperandI op]
@init{ OP openu=null; }
	:	l=primary_expression ((('*' {openu=OP.TIMES; }| '/' {openu=OP.DIVIDE;} )
		r=primary_expression { $op=new Operator($l.op,$r.op,openu); })
	|	{$op=$l.op;})
	;
	
primary_expression returns[OperandI op]
	:	INT {$op=Constant.getConstant($INT.text);}
	|	VAR {$op=Variable.getVariable($VAR.text);}
	|	'(' expression ')' {$op=$expression.op;}
	;
	
atom	returns[Atom at]
@init{ String context_id=null; String atom_name; Term[] terms = new Term[0];}
	:	(ctx=ID ':' {context_id=$ctx.text;})?	// parsing atoms with context identifier
		name=ID {if(context_id==null) { atom_name=$name.text; } else {atom_name=context_id+":"+$name.text;} }
		( '(' termlist ')' {terms=(Term[])$termlist.termlist.toArray(new Term[$termlist.termlist.size()]);})?
		{$at=Atom.getAtom(atom_name, terms.length, terms);
		if(context_id !=null) {$at.getPredicate().setNodeId(context_id); context.registerFactFromOutside($at.getPredicate());
				}}
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
	|	('-')? INT {$constnt=Constant.getConstant($INT.text);}
	;

comp_sym returns[OP openu]
	:	'<' {$openu=OP.LESS;}
	|	'<=' {$openu=OP.LESS_EQ;}
	|	'>=' {$openu=OP.GREATER_EQ;}
	|	'>' {$openu=OP.GREATER;}
	|	'=' {$openu=OP.EQUAL;}
	|	'!=' {$openu=OP.NOTEQUAL;}
	;
	

ID  :	('a'..'z') ('a'..'z'|'A'..'Z'|'0'..'9'|'_')*
    ;
    
VAR	:	('A'..'Z'|'_') ('a'..'z'|'A'..'Z'|'0'..'9'|'_')*
	;

INT :	'0'..'9'+
    ;

/*FLOAT
    :   ('0'..'9')+ '.' ('0'..'9')* EXPONENT? // make it * instead of + then 3. parses as rule-and or float.
    |   '.' ('0'..'9')+ EXPONENT?
    |   ('0'..'9')+ EXPONENT
    ;
    */

COMMENT
    :   '//' ~('\n'|'\r')* '\r'? '\n' {skip();}
    |	'%' ~('\n'|'\r')* '\r'? '\n' {skip();}
    |   '/*' ( options {greedy=false;} : . )* '*/' {skip();}
    ;
    

WS  :   ( ' '
        | '\t'
        | '\r'
        | '\n'
        ) {skip();}
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
