package rs.ac.bg.etf.pp1;

import java_cup.runtime.Symbol;

%%

%cup
%line
%column
%xstate COMMENT
%xstate COMMENT_ALL

%{
	private Symbol new_symbol(int type) {
		return new Symbol(type, yyline+1, yycolumn);
	}
	
	private Symbol new_symbol(int type, Object value) {
		return new Symbol(type, yyline+1, yycolumn, value);
	}

%}

%eofval{
	return new_symbol(sym.EOF);
%eofval}

%%

// White characters
" " 	{ }
"\b" 	{ }
"\t" 	{ }
"\r\n" 	{ }
"\f" 	{ }


// Keywords
"program"   	{ return new_symbol(sym.PROG, yytext()); }
"break"			{ return new_symbol(sym.BREAK, yytext()); }
"class"			{ return new_symbol(sym.CLASS, yytext()); }
"else"			{ return new_symbol(sym.ELSE, yytext()); }
"const"			{ return new_symbol(sym.CONST, yytext()); }
"if"			{ return new_symbol(sym.IF, yytext()); }
"new"			{ return new_symbol(sym.NEW, yytext()); }
"print" 		{ return new_symbol(sym.PRINT, yytext()); }
"read"			{ return new_symbol(sym.READ, yytext()); }
"return" 		{ return new_symbol(sym.RETURN, yytext()); }
"void" 			{ return new_symbol(sym.VOID, yytext()); }
"extends"		{ return new_symbol(sym.EXTENDS, yytext()); }
"continue"		{ return new_symbol(sym.CONTINUE, yytext()); }
"for"			{ return new_symbol(sym.FOR, yytext()); }
"static"		{ return new_symbol(sym.STATIC, yytext()); }
"namespace"		{ return new_symbol(sym.NAMESP, yytext()); }
"range"			{ return new_symbol(sym.RANGE, yytext()); }
"in"			{ return new_symbol(sym.IN, yytext()); }


// Operators
"+" 	{ return new_symbol(sym.PLUS, yytext()); }
"-"		{ return new_symbol(sym.MINUS, yytext()); }
"*"		{ return new_symbol(sym.MUL, yytext()); }
"/"		{ return new_symbol(sym.DIV, yytext()); }
"%"		{ return new_symbol(sym.MOD, yytext()); }
"=="	{ return new_symbol(sym.EQUAL, yytext()); }
"!="	{ return new_symbol(sym.NOT_EQUAL, yytext()); }
">"		{ return new_symbol(sym.GRT, yytext()); }
">="	{ return new_symbol(sym.GRT_EQUAL, yytext()); }
"<"		{ return new_symbol(sym.LESS, yytext()); }
"<="	{ return new_symbol(sym.LESS_EQUAL, yytext()); }
"&&"	{ return new_symbol(sym.AND, yytext()); }
"||"	{ return new_symbol(sym.OR, yytext()); }
"=" 	{ return new_symbol(sym.ASSIGN, yytext()); }
"++"	{ return new_symbol(sym.INC, yytext()); }
"--"	{ return new_symbol(sym.DEC, yytext()); }
";" 	{ return new_symbol(sym.SEMI, yytext()); }
":"		{ return new_symbol(sym.COLON, yytext()); }
"::"	{ return new_symbol(sym.RESOLUTION, yytext()); }
"," 	{ return new_symbol(sym.COMMA, yytext()); }
"."		{ return new_symbol(sym.DOT, yytext()); }
"(" 	{ return new_symbol(sym.LPAREN, yytext()); }
")" 	{ return new_symbol(sym.RPAREN, yytext()); }
"["		{ return new_symbol(sym.LBRACKET, yytext()); }
"]"		{ return new_symbol(sym.RBRACKET, yytext()); }
"{" 	{ return new_symbol(sym.LBRACE, yytext()); }
"}"		{ return new_symbol(sym.RBRACE, yytext()); }
"=>"	{ return new_symbol(sym.ARROW, yytext()); }


// [Possibly bad] Tokens
// "'"[\040-\176]"'"				{ return new_symbol(sym.CHAR_CONST, yytext().charAt(1)); }
// ([a-z]|[A-Z])[a-z|A-Z|0-9|_]* 	{ return new_symbol(sym.IDENT, yytext()); }

// Tokens
'.'							{ return new_symbol(sym.CHAR_CONST, yytext().charAt(1)); }
"true"						{ return new_symbol(sym.BOOL_CONST, true); }
"false"						{ return new_symbol(sym.BOOL_CONST, false); }
[0-9]+  					{ return new_symbol(sym.NUM_CONST, Integer.valueOf(yytext())); }
([a-zA-Z])[a-zA-Z0-9_]* 	{ return new_symbol(sym.IDENT, yytext()); }


// Comments
"//" 					{ yybegin(COMMENT); }
<COMMENT> . 			{ yybegin(COMMENT); }
<COMMENT> "\r\n" 		{ yybegin(YYINITIAL); }


// Multiline comments
"/*"					{ yybegin(COMMENT_ALL); }
<COMMENT_ALL> .			{ yybegin(COMMENT_ALL); }
<COMMENT_ALL> "\r\n"	{ yybegin(COMMENT_ALL); }
<COMMENT_ALL> "*/"		{ yybegin(YYINITIAL); }


// Errors
. 	{ System.err.println("Lexical error [" + yytext() + "] in line [" + (yyline+1) + "] on position [" + yycolumn + "]."); }
