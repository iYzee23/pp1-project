package rs.ac.bg.etf.pp1;

import java_cup.runtime.Symbol;

%%

%cup
%line
%column
%xstate COMMENT

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
"program"   { return new_symbol(sym.PROG, yytext());}
"print" 	{ return new_symbol(sym.PRINT, yytext()); }
"return" 	{ return new_symbol(sym.RETURN, yytext()); }
"void" 		{ return new_symbol(sym.VOID, yytext()); }


// Operators
"+" 	{ return new_symbol(sym.PLUS, yytext()); }
"=" 	{ return new_symbol(sym.EQUAL, yytext()); }
";" 	{ return new_symbol(sym.SEMI, yytext()); }
"," 	{ return new_symbol(sym.COMMA, yytext()); }
"(" 	{ return new_symbol(sym.LPAREN, yytext()); }
")" 	{ return new_symbol(sym.RPAREN, yytext()); }
"{" 	{ return new_symbol(sym.LBRACE, yytext()); }
"}"		{ return new_symbol(sym.RBRACE, yytext()); }


// Tokens
[0-9]+  						{ return new_symbol(sym.NUMBER, Integer.valueOf(yytext())); }
([a-z]|[A-Z])[a-z|A-Z|0-9|_]* 	{ return new_symbol (sym.IDENT, yytext()); }


// Comments
"//" 				{yybegin(COMMENT);}
<COMMENT> . 		{yybegin(COMMENT);}
<COMMENT> "\r\n" 	{ yybegin(YYINITIAL); }


// Errors
. 	{ System.err.println("Lexical error [" + yytext() + "] in line [" + (yyline+1) + "] on position [" + yycolumn + "]."); }










