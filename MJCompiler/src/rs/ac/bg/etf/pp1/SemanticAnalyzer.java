package rs.ac.bg.etf.pp1;

import org.apache.log4j.Logger;
import rs.ac.bg.etf.pp1.ast.*;
import rs.etf.pp1.symboltable.concepts.*;
import rs.etf.pp1.symboltable.visitors.*;
import rs.etf.pp1.symboltable.structure.*;

public class SemanticAnalyzer extends VisitorAdaptor {
	
	Logger log = Logger.getLogger(getClass());
	
	public static final int NAMESPACE = 7;
	
	public void report_error(String message, SyntaxNode info) {
		errorDetected = true;
		StringBuilder msg = new StringBuilder(message);
		int line = (info == null) ? 0: info.getLine();
		if (line != 0)
			msg.append (" na liniji ").append(line);
		log.error(msg.toString());
	}

	public void report_info(String message, SyntaxNode info) {
		StringBuilder msg = new StringBuilder(message); 
		int line = (info == null) ? 0: info.getLine();
		if (line != 0)
			msg.append (" na liniji ").append(line);
		log.info(msg.toString());
	}
	
	public boolean getErrorDetected() {
		return errorDetected;
	}
	
	String currNamespace = "";
	Struct currType = null;
	Obj currClass = null;
	Obj currMethod = null;
	
	boolean returnFound = false;
	boolean errorDetected = false;
	boolean arrayActive = false;
	
	// Program
	
	public void visit(ProgNamet progName) {
		progName.obj = Tabb.insert(Obj.Prog, progName.getProgName(), Tabb.noType);
		Tabb.openScope();
	}
	
	public void visit(Programt program) {
		Tabb.chainLocalSymbols(program.getProgName().obj);
		Tabb.closeScope();
		
		Obj mainObj = Tabb.find("main");
		if (mainObj == Tabb.noObj || mainObj.getKind() != Obj.Meth || mainObj.getType() != Tabb.noType || mainObj.getLevel() != 0) {
			report_error("Method 'void main() { ...}' not found", program);
		}
	}
	
	// Namespace
	
	public void visit(NamespaceNamet namespaceName) {
		// we allow multiple declarations of namespace with same name
		Tabb.insert(NAMESPACE, namespaceName.getNamespaceName(), Tabb.noType);
		currNamespace = namespaceName.getNamespaceName() + "::";
	}
	
	public void visit(Namespacet namespace) {
		currNamespace = "";
	}
	
	// ConstDecl
	
	public void visit(ConstDeclt constDecl) {
		currType = null;
	}
	
	public void visit(ConstDeclSingle constDecl) {
		currType = constDecl.getType().struct;
	}
	
	public void visit(ConstPartNum constNum) {
		String constName = currNamespace + constNum.getConstName();
		Integer constValue = constNum.getConstValue();
		
		if (!currType.equals(Tabb.intType)) {
			report_error("Incompatibile value for constant: " + constName, constNum);
			return;
		}
		
		Obj constNumObj = Tabb.find(constName);
		if (constNumObj != Tabb.noObj) {
			report_error("Constant already declared: " + constName, constNum);
			return;
		}
		
		Obj newConstNum = Tabb.insert(Obj.Con, constName, currType);
		newConstNum.setAdr(constValue);
		// report_info("Constant successfully created: " + constName, constNum);
	}
	
	public void visit(ConstPartChar constChar) {
		String constName = currNamespace + constChar.getConstName();
		Character constValue = constChar.getConstValue();
		
		if (!currType.equals(Tabb.charType)) {
			report_error("Incompatibile value for constant: " + constName, constChar);
			return;
		}
		
		Obj constCharObj = Tabb.find(constName);
		if (constCharObj != Tabb.noObj) {
			report_error("Constant already declared: " + constName, constChar);
			return;
		}
		
		Obj newConstChar = Tabb.insert(Obj.Con, constName, currType);
		newConstChar.setAdr(constValue);
		report_info("Constant successfully created: " + constName, constChar);
	}
	
	public void visit(ConstPartBool constBool) {
		String constName = currNamespace + constBool.getConstName();
		Boolean constValue = constBool.getConstValue();
		
		if (!currType.equals(Tabb.boolType)) {
			report_error("Incompatibile value for constant: " + constName, constBool);
			return;
		}
		
		Obj constBoolObj = Tabb.find(constName);
		if (constBoolObj != Tabb.noObj) {
			report_error("Constant already declared: " + constName, constBool);
			return;
		}
		
		Obj newConstBool = Tabb.insert(Obj.Con, constName, currType);
		newConstBool.setAdr(constValue ? 1 : 0);
		report_info("Constant successfully created: " + constName, constBool);
	}
	
	// VarDecl
	
	public void visit(VarDeclt varDecl) {
		currType = null;
	}
	
	public void visit(VarDeclSingle varDecl) {
		currType = varDecl.getType().struct;
	}
	
	public void visit(VarPartYes varPart) {
		String varName = currNamespace + varPart.getVarName();
		
		Obj varObj = null;
		if (currMethod == null) varObj = Tabb.find(varName);
		else varObj = Tabb.currentScope.findSymbol(varName);
		
		if (varObj != Tabb.noObj) {
			report_error("Variable (array) already declared: " + varName, varPart);
			return;
		}
		
		Struct arrType = new Struct(Struct.Array, currType);
		Tabb.insert(Obj.Var, varName, arrType);
		// report_info("Variable successfully created: " + varName, varPart);
	}
	
	public void visit(VarPartNo varPart) {
		String varName = currNamespace + varPart.getVarName();
		
		Obj varObj = null;
		if (currMethod == null) varObj = Tabb.find(varName);
		else varObj = Tabb.currentScope.findSymbol(varName);
		
		if (varObj != Tabb.noObj) {
			report_error("Variable already declared: " + varName, varPart);
			return;
		}
		
		Tabb.insert(Obj.Var, varName, currType);
		// report_info("Variable successfully created: " + varName, varPart);
	}
	
	// ClassDecl
	
	// StaticInitializer
	
	// MethodDecl
	
	public void visit(MethodDeclYes methodDecl) {
		Tabb.chainLocalSymbols(methodDecl.getMethodName().obj);
		Tabb.closeScope();
	}
	
	public void visit(MethodDeclNo methodDecl) {
		Tabb.chainLocalSymbols(methodDecl.getMethodName().obj);
		Tabb.closeScope();
	}
	
	public void visit(ReturnTypeType returnType) {
		currType = returnType.getType().struct;
	}
	
	public void visit(ReturnTypeVoid returnType) {
		currType = Tabb.noType;
	}
	
	public void visit(MethodNamet methodName) {
		String name = currNamespace + methodName.getMethName();
		
		Obj methodObj = Tabb.find(name);
		if (methodObj != Tabb.noObj) {
			report_error("Method is already declared: " + name, methodName);
			currType = null;
			return;
		}
		
		currMethod = methodName.obj = Tabb.insert(Obj.Meth, name, currType);
		Tabb.openScope();
		currType = null;
	}
	
	// FormPars
	
	public void visit(BracketsOptYes bracketsOpt) {
		arrayActive = true;
	}
	
	public void visit(FormParsList formParam) {
		String paramName = formParam.getParamName();
		
		Obj paramObj = Tabb.currentScope.findSymbol(paramName);
		if (paramObj != Tabb.noObj) {
			report_error("Formal parameter" + (arrayActive ? " (array) " : " ") + "is already declared: " + paramName, formParam);
			arrayActive = false;
			return;
		}
		
		Struct newType = null;
		if (arrayActive) newType = new Struct(Struct.Array, formParam.getType().struct);
		else newType = formParam.getType().struct;
		
		Tabb.insert(Obj.Var, paramName, newType);
		currMethod.setLevel(currMethod.getLevel() + 1);
		arrayActive = false;
	}
	
	public void visit(FormParsSingleYes formParam) {
		String paramName = formParam.getParamName();
		
		Obj paramObj = Tabb.currentScope.findSymbol(paramName);
		if (paramObj != Tabb.noObj) {
			report_error("Formal parameter (array) is already declared: " + paramName, formParam);
			return;
		}
		
		Struct arrType = new Struct(Struct.Array, formParam.getType().struct);
		Tabb.insert(Obj.Var, paramName, arrType);
		currMethod.setLevel(currMethod.getLevel() + 1);
	}
	
	public void visit(FormParsSingleNo formParam) {
		String paramName = formParam.getParamName();
		
		Obj paramObj = Tabb.currentScope.findSymbol(paramName);
		if (paramObj != Tabb.noObj) {
			report_error("Formal parameter is already declared: " + paramName, formParam);
			return;
		}
		
		Tabb.insert(Obj.Var, paramName, formParam.getType().struct);
		currMethod.setLevel(currMethod.getLevel() + 1);
	}
	
	// Type
	
	public void visit(TypeYes typeYes) {
		Obj namespaceObj = Tabb.find(typeYes.getNamespaceName());
		
		if (namespaceObj == Tabb.noObj || namespaceObj.getKind() != NAMESPACE) {
			report_error("Can't resolve namespace: " + typeYes.getNamespaceName(), typeYes);
		}
		
		String typeName = namespaceObj.getName() + "::" + typeYes.getTypeName();
		Obj typeObj = Tabb.find(typeName);
		typeYes.struct = typeObj.getType();
		
		if (typeObj == Tabb.noObj || typeObj.getKind() != Obj.Type) {
			report_error("Can't resolve type: " + typeYes.getTypeName(), typeYes);
		}
	}
	
	public void visit(TypeNo typeNo) {
		String typeName = currNamespace + typeNo.getTypeName();
		Obj typeObj = Tabb.find(typeName);
		typeNo.struct = typeObj.getType();
		
		if (typeObj == Tabb.noObj || typeObj.getKind() != Obj.Type) {
			report_error("Can't resolve type: " + typeNo.getTypeName(), typeNo);
		}
	}
	
	// Statement
	
	// DesignatorStatement
	
	// ActPars
	
	// Condition
	
	// CondTerm
	
	// CondFact
	
	// Expr
	
	// Term
	
	// Factor
	
	// Designator
	
	// Label
	
	// Assignop
	
	// Relop
	
	// Addop
	
	// Mulop
	

}