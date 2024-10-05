grammar Xprsm;

//
// ************** parser rules **************
//

parse
 :  statement_list* EOF
 ;

statement_list
  : statement STATEMENT_TERMINATOR (statement STATEMENT_TERMINATOR )*
  ;

statement
  : basepackage_statement 
  | define_primitive_statement
  | define_enum_statement
  | define_model_statement 
  | include_statement
  | project_statement
  | use_statement
  ;

// PROJECT STATEMENT
project_statement
  : K_PROJECT project_name
  ;

project_name
  : STRING_LITERAL
  ;

// BASEPACKAGE STATEMENT
basepackage_statement
  : K_BASEPACKAGE package_name
  ;

package_name
  : STRING_LITERAL
  ;


// INCLUDE STATEMENT
include_statement
  : K_INCLUDE source_name
  ;

source_name
  : STRING_LITERAL
  ;

// DEFINE PRIMITIVE STATEMENT
define_primitive_statement
  : K_DEFINE K_PRIMITIVE primitive_name K_AS datatype
  ;

primitive_name
  : CLASS_NAME
  ;

// DEFINE ENUM STATEMENT
define_enum_statement
  : K_DEFINE K_ENUM model_name optionlist
  ;

optionlist
  : K_WITH K_OPTIONS option (LIST_SEPARATOR option)*
  ;

option
  : CLASS_NAME
  ;

// DEFINE MODEL STATEMENT
define_model_statement
  : K_DEFINE K_ABSTRACT? K_MODEL model_name inherit_from? fieldlist? on_deletelist?
  ;

model_name
  : CLASS_NAME
  | STRING_LITERAL
  ;

inherit_from
  : K_INHERIT K_FROM model_name
  ;

fieldlist
  : K_WITH K_FIELDS field (LIST_SEPARATOR field)*
  ;

field
  : fieldname datatype field_constraint*
  ;

fieldname
  : ATTRIBUTE_NAME
  ;

datatype
  : datatype_name (OPENING_BRACKET datatype_limit CLOSING_BRACKET)?
  ;

datatype_name
  : CLASS_NAME
  ;

datatype_limit
  : NUMERIC_LITERAL
  ;

field_constraint
  : K_PRIMARY K_KEY
  | K_REQUIRED
  | K_MATCHES OPENING_BRACKET regex CLOSING_BRACKET
  | K_DEFAULT OPENING_BRACKET default_value CLOSING_BRACKET
  | K_TRANSIENT
  | K_UNIQUE
  ;

default_value
  : K_NULL
  | K_NOW
  | STRING_LITERAL
  | NUMERIC_LITERAL
  ;

on_deletelist
  : K_ON K_DELETE on_delete (LIST_SEPARATOR on_delete)*
  ;

on_delete
  : K_IN model_name (K_DELETE | K_SET K_NULL);

// USE STATEMENT
use_statement
  : K_USE model_name K_AS K_MAIN K_MODEL;


// COMMON RULES
number
  : NUMERIC_LITERAL
  ;

regex
  : STRING_LITERAL
  ;

//
// ************** LEXER RULES **************
//

// KEYWORDS
K_ABSTRACT: A B S T R A C T;
K_AS: A S;
K_BASEPACKAGE: B A S E P A C K A G E;
K_DEFAULT: D E F A U L T;
K_DEFINE: D E F I N E;
K_DELETE: D E L E T E;
K_ENUM: E N U M;
K_FIELDS: F I E L D S;
K_FROM: F R O M;
K_IN: I N;
K_INCLUDE: I N C L U D E;
K_INHERIT: I N H E R I T;
K_KEY: K E Y;
K_MAIN: M A I N;
K_MATCHES: M A T C H E S;
K_MODEL: M O D E L;
K_NOW: N O W;
K_NULL: N U L L;
K_ON: O N;
K_OPTIONS: O P T I O N S;
K_PRIMARY: P R I M A R Y;
K_PRIMITIVE: P R I M I T I V E;
K_PROJECT: P R O J E C T;
K_REQUIRED: R E Q U I R E D;
K_SET: S E T;
K_TRANSIENT: T R A N S I E N T;
K_USE: U S E;
K_WITH: W I T H;
K_UNIQUE: U N I Q U E;

// NAMES
CLASS_NAME
  : [A-Z] [a-zA-Z_0-9]*('<'[A-Z_] [a-zA-Z_0-9]*'>')?
  ;

ATTRIBUTE_NAME
  : [a-z] [a-zA-Z_0-9]*
  ;

OPTION_NAME
  : [A-Z][a-zA-Z_0-9]*
  ;

// GENERAL DEFINITIONS
STRING_LITERAL
 : '"' ( ~'\"' | '\"\"' )* '"'
 ;

SINGLE_LINE_COMMENT
 : ('#' | '//') ~[\r\n]* -> channel(HIDDEN)
 ;

MULTILINE_COMMENT
 : '/*' .*? ( '*/' | EOF ) -> channel(HIDDEN)
 ;

SPACES
 : [ \u000B\t\r\n] -> channel(HIDDEN)
 ;

LIST_SEPARATOR
  : ','
  ;

STATEMENT_TERMINATOR
  : ';'
  ;

OPENING_BRACKET
  : '('
  ;

CLOSING_BRACKET
  : ')'
  ;

NUMERIC_LITERAL
  : [0-9]+
  ;

UNEXPECTED_CHAR
 : .
 ;

fragment A : [A];
fragment B : [B];
fragment C : [C];
fragment D : [D];
fragment E : [E];
fragment F : [F];
fragment G : [G];
fragment H : [H];
fragment I : [I];
fragment J : [J];
fragment K : [K];
fragment L : [L];
fragment M : [M];
fragment N : [N];
fragment O : [O];
fragment P : [P];
fragment Q : [Q];
fragment R : [R];
fragment S : [S];
fragment T : [T];
fragment U : [U];
fragment V : [V];
fragment W : [W];
fragment X : [X];
fragment Y : [Y];
fragment Z : [Z];