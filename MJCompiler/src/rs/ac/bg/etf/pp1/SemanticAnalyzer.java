package rs.ac.bg.etf.pp1;

import java.util.ArrayList;
import org.apache.log4j.Logger;
import rs.ac.bg.etf.pp1.ast.*;
import rs.etf.pp1.symboltable.concepts.*;

// potential problem with built-in equals method
// there can be infinite recursion
// fix this on defense of the project, if needed

// report_info is commented out because it's ugly
// if needed, uncomment it

public class SemanticAnalyzer extends VisitorAdaptor {
	
	Logger log = Logger.getLogger(getClass());
	NewVisitor nv = null;
	
	public void report_error(String message, SyntaxNode info) {
		errorDetected = true;
		StringBuilder msg = new StringBuilder(message);
		int line = (info == null) ? 0: info.getLine();
		if (line != 0)
			msg.append (" ~~~ Line: ").append(line);
		log.error(msg.toString());
	}

	public void report_info(String message, SyntaxNode info) {
		/*
		StringBuilder msg = new StringBuilder(message); 
		int line = (info == null) ? 0: info.getLine();
		if (line != 0)
			msg.append (" ~~~ Line: ").append(line);
		log.info(msg.toString());
		*/
	}
	
	public boolean getErrorDetected() {
		return errorDetected;
	}
	
	// general processing
	String currNamespace = "";
	Struct currType = null;
	Obj currMethod = null;
	int nVars = 0;
	
	// classes processing
	Obj currClass = null;
	Struct superClassType = null;
	Obj pOverrideObj = null;
	boolean staticClassActive = false;
	
	// designator processing
	ArrayList<Obj> currDesignator = new ArrayList<>();
	ArrayList<ArrayList<String>> designatorParts = new ArrayList<>();
	ArrayList<Boolean> classDesignator = new ArrayList<>();
	
	// easy designator statement processing
	static final int ASSIGNMENT = 1;
	static final int INC_DEC = 2;
	static final int FUNC_CALL = 3;
	static final int FUNC_CALL_NO_ARG = 4;
	int designatorStatementKind = NOT_USED;
	Struct currAssignmentExpr = null;
	
	// hard designator statement processsing
	ArrayList<Obj> dsgStmtParts = new ArrayList<>();
	
	// act pars processing
	ArrayList<Integer> actParsCount = new ArrayList<>();
	ArrayList<ArrayList<Struct>> actParsParts = new ArrayList<>();
	
	// general flags
	boolean returnFound = false;
	boolean errorDetected = false;
	boolean arrayActive = false;
	boolean matrixActive = false;
	boolean labelCreationActive = false;
	
	// factor & term flags
	boolean hasMulopFactor = false;
	boolean mulopFactorError = false;
	boolean hasAddopTerm = false;
	boolean addopTermError = false;
	boolean isNewFactorClass = false;
	boolean isNewFactorMatrix = false;
	
	// relation operators processing
	static final int NOT_USED = 0;
	static final int EQ_NOTEQ = 1;
	static final int OTHERS = 2;
	int relopKind = NOT_USED;
	int forCnt = 0;
	
	// list comprehension processing
	Obj listComprObj = null;
	boolean listComprActive = false;
	
	// Program
	
	public void visit(ProgNamet progName) {
		progName.obj = Tabb.insert(Obj.Prog, progName.getProgName(), Tabb.noType);
		
		currDesignator.add(null);
		designatorParts.add(new ArrayList<String>());
		classDesignator.add(false);
		actParsCount.add(0);
		actParsParts.add(new ArrayList<Struct>());
		
		Tabb.openScope();
		Tabb.programScope = Tabb.currentScope();
	}
	
	public void visit(Programt program) {
		Obj mainObj = Tabb.find("main");
		if (mainObj == Tabb.noObj || mainObj.getKind() != Obj.Meth || mainObj.getType() != Tabb.noType || mainObj.getLevel() != 0) {
			report_error("Method 'void main() { ...}' not found", null);
		}
		
		currDesignator.remove(currDesignator.size() - 1);
		designatorParts.remove(designatorParts.size() - 1);
		classDesignator.remove(classDesignator.size() - 1);
		actParsCount.remove(actParsCount.size() - 1);
		actParsParts.remove(actParsParts.size() - 1);
		
		nVars = Tabb.getAndUpdateNumGlobStat();
		Tabb.chainLocalSymbols(program.getProgName().obj);
		Tabb.closeScope();
		// Tabb.programScope = null;
	}
	
	// Namespace
	
	public void visit(NamespaceNamet namespaceName) {
		String name = namespaceName.getNamespaceName();
		Obj nspObj = Tabb.insert(Objj.Namesp, name, Tabb.noType);
		
		if (nspObj != Tabb.noObj && nspObj.getKind() != Objj.Namesp) {
			report_error("Object with the name of namespace already declared: " + name, namespaceName);
			return;
		}
		
		currNamespace = namespaceName.getNamespaceName() + "::";
	}
	
	public void visit(Namespacet namespace) {
		currNamespace = "";
	}
	
	// ConstDecl
	
	public void visit(ConstDeclt constDecl) {
		currType = null;
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
	}
	
	// VarDecl
	
	public void visit(VarDeclt varDecl) {
		currType = null;
	}
	
	public void visit(VarPartYes varPart) {
		String localName = varPart.getVarName();
		String fullName = currNamespace + localName;
		
		Obj varObj = null;
		if (currMethod != null) {
			varObj = Tabb.findLocal(localName);
		}
		else if (currClass != null) {
			varObj = Tabb.findExactStatic(currClass.getName() + "::" + localName);
			if (varObj == Tabb.noObj) varObj = Tabb.findLocal(localName); 
		}
		else {
			varObj = Tabb.find(fullName);
		}
		
		if (varObj != Tabb.noObj) {
			report_error("[Static] variable (array) already declared: " + fullName, varPart);
			return;
		}
		
		Struct arrType = new Struct(Struct.Array, currType);
		if (currMethod != null) Tabb.insert(Obj.Var, localName, arrType);
		else if (currClass != null && staticClassActive) {
			String statName = currClass.getName() + "::" + localName;
			Tabb.insertStatic(Objj.Stat, statName, arrType);
		}
		else if (currClass != null) Tabb.insert(Obj.Fld, localName, arrType);
		else Tabb.insert(Obj.Var, fullName, arrType);
	}
	
	public void visit(VarPartYesYes varPart) {
		String localName = varPart.getVarName();
		String fullName = currNamespace + localName;
		
		Obj varObj = null;
		if (currMethod != null) {
			varObj = Tabb.findLocal(localName);
		}
		else if (currClass != null) {
			varObj = Tabb.findExactStatic(currClass.getName() + "::" + localName);
			if (varObj == Tabb.noObj) varObj = Tabb.findLocal(localName); 
		}
		else {
			varObj = Tabb.find(fullName);
		}
		
		if (varObj != Tabb.noObj) {
			report_error("[Static] variable (matrix) already declared: " + fullName, varPart);
			return;
		}
		
		Struct arrType = new Struct(Struct.Array, currType);
		Struct matrixType = new Struct(Struct.Array, arrType);
		if (currMethod != null) Tabb.insert(Obj.Var, localName, matrixType);
		else if (currClass != null && staticClassActive) {
			String statName = currClass.getName() + "::" + localName;
			Tabb.insertStatic(Objj.Stat, statName, matrixType);
		}
		else if (currClass != null) Tabb.insert(Obj.Fld, localName, matrixType);
		else Tabb.insert(Obj.Var, fullName, matrixType);
	}
	
	public void visit(VarPartNo varPart) {
		String localName = varPart.getVarName();
		String fullName = currNamespace + localName;
		
		Obj varObj = null;
		if (currMethod != null) {
			varObj = Tabb.findLocal(localName);
		}
		else if (currClass != null) {
			varObj = Tabb.findExactStatic(currClass.getName() + "::" + localName);
			if (varObj == Tabb.noObj) varObj = Tabb.findLocal(localName);
		}
		else {
			varObj = Tabb.find(fullName);
		}
		
		if (varObj != Tabb.noObj) {
			report_error("[Static] variable already declared: " + fullName, varPart);
			return;
		}
		
		if (currMethod != null) Tabb.insert(Obj.Var, localName, currType);
		else if (currClass != null && staticClassActive) {
			String statName = currClass.getName() + "::" + localName;
			Tabb.insertStatic(Objj.Stat, statName, currType);
		}
		else if (currClass != null) Tabb.insert(Obj.Fld, localName, currType);
		else Tabb.insert(Obj.Var, fullName, currType);
	}
	
	// ClassDecl
	
	public void visit(ClassNamet className) {
		String name = currNamespace + className.getClassName();
		
		Obj classObj = Tabb.find(name);
		if (classObj != Tabb.noObj) {
			report_error("Class is already declared: " + name, className);
			return;
		}
		
		currClass = className.obj = Tabb.insert(Obj.Type, name, new Struct(Struct.Class));
		Tabb.openScope();
	}
	
	public void visit(ExtClasst extClass) {
		Struct classType = extClass.getType().struct;
		
		if (classType.getKind() != Struct.Class) {
			report_error("Superclass must be of Class type", extClass.getType());
			return;
		}
		
		Tabb.copyFromSuperclass(currClass.getType(), classType);
		currClass.getType().setElementType(classType);
		superClassType = classType;
		currType = null;
	}
	
	public void visit(ClassDeclYes classDecl) {
		Obj classObj = classDecl.getClassName().obj;
		
		Tabb.chainLocalSymbols(classObj.getType());
		Tabb.closeScope();
		currClass = null;
		superClassType = null;
	}
	
	public void visit(ClassDeclNo classDecl) {
		Obj classObj = classDecl.getClassName().obj;
		
		Tabb.chainLocalSymbols(classObj.getType());
		Tabb.closeScope();
		currClass = null;
		superClassType = null;
	}
	
	public void visit(StatVarDeclInitt classDecl) {
		staticClassActive = true;
	}
	
	// StaticInitializer
	
	public void visit(StatInitListt statInitList) {
		staticClassActive = false;
	}
	
	// MethodDecl
	
	public void visit(ReturnTypeType returnType) {
		currType = returnType.getType().struct;
	}
	
	public void visit(ReturnTypeVoid returnType) {
		currType = Tabb.noType;
	}
	
	public void visit(MethodNamet methodName) {
		String localName = methodName.getMethName();
		String fullName = currNamespace + localName;
		
		Obj methodObj = null;
		if (currClass == null) {
			methodObj = Tabb.find(fullName);
		}
		else {
			Obj scMethObj = null;
			if (superClassType != null) {
				scMethObj = superClassType.getMembersTable().searchKey(localName);
			}
			methodObj = Tabb.findLocal(localName);
			
			boolean compatibility = false;
			if (scMethObj != null) {
				Struct tmpType = currType;
				while (tmpType != null) {
					if (scMethObj.getType().equals(tmpType)) {
						compatibility = true;
						break;
					}
					tmpType = tmpType.getElemType();
				}
			}
			
			if (methodObj.getKind() == Obj.Meth && methodObj.getFpPos() == 1 && compatibility) {
				pOverrideObj = new Obj(Obj.Meth, localName, currType, 0, 1);
			}
		}
		
		if (methodObj != Tabb.noObj && pOverrideObj == null) {
			report_error("Method is already declared: " + fullName, methodName);
			// currType = null;
			// return;
		}
		
		currMethod = methodName.obj = (pOverrideObj == null ? Tabb.insert(Obj.Meth, currClass == null ? fullName : localName, currType) : pOverrideObj);
		currType = null;
		
		Tabb.openScope();
		if (currClass != null) {
			Tabb.insert(Obj.Var, "this", currClass.getType());
		}
	}
	
	public void visit(MethodDeclYes methodDecl) {
		Obj methObj = methodDecl.getMethodName().obj;
		
		if (methObj.getType() != Tabb.noType && !returnFound) {
			report_error("Non-void method must have return expression: " + methObj.getName(), methodDecl.getMethodName());
		}
		
		if (currClass != null) methObj.setFpPos(1);
		
		Tabb.chainLocalSymbols(methObj);
		Tabb.closeScope();
		
		if (pOverrideObj != null) {
			Obj currMethObj = Tabb.findLocal(pOverrideObj.getName());
			if (!Tabb.compareTwoMethods(pOverrideObj, currMethObj)) {
				report_error("Method is already declared and cannot be hidden, only overriden: " + pOverrideObj.getName(), methodDecl.getMethodName());
			}
			else {
				Tabb.overrideMethod(pOverrideObj);
			}
		}
		
		returnFound = false;
		currMethod = null;
		pOverrideObj = null;
	}
	
	public void visit(MethodDeclNo methodDecl) {
		Obj methObj = methodDecl.getMethodName().obj;
		if (methObj.getType() != Tabb.noType && !returnFound) {
			report_error("Non-void method must have return expression: " + methObj.getName(), methodDecl.getMethodName());
		}
		
		if (currClass != null) methObj.setFpPos(1);
		
		Tabb.chainLocalSymbols(methObj);
		Tabb.closeScope();
		
		if (pOverrideObj != null) {
			Obj currMethObj = Tabb.findLocal(pOverrideObj.getName());
			if (!Tabb.compareTwoMethods(pOverrideObj, currMethObj)) {
				report_error("Method is already declared and cannot be hidden, only overriden: " + pOverrideObj.getName(), methodDecl.getMethodName());
			}
			else {
				Tabb.overrideMethod(pOverrideObj);
			}
		}
		
		returnFound = false;
		currMethod = null;
		pOverrideObj = null;
	}
	
	// FormPars
	
	public void visit(BracketsOptYesYes bracketsOpt) {
		matrixActive = true;
	}
	
	public void visit(BracketsOptYes bracketsOpt) {
		arrayActive = true;
	}
	
	public void visit(FormParsList formParam) {
		String paramName = formParam.getParamName();
		
		Obj paramObj = Tabb.findLocal(paramName);
		if (paramObj != Tabb.noObj) {
			report_error("Formal parameter" + (arrayActive ? " (array) " : " ") + "is already declared: " + paramName, formParam);
			arrayActive = false;
			matrixActive = false;
			currType = null;
			return;
		}
		
		Struct newType = null;
		if (arrayActive) newType = new Struct(Struct.Array, formParam.getType().struct);
		else if (matrixActive) {
			newType = new Struct(Struct.Array, formParam.getType().struct);
			newType = new Struct(Struct.Array, newType);
		}
		else newType = formParam.getType().struct;
		
		Tabb.insert(Obj.Var, paramName, newType);
		currMethod.setLevel(currMethod.getLevel() + 1);
		arrayActive = false;
		matrixActive = false;
		currType = null;
	}
	
	public void visit(FormParsSingleYesYes formParam) {
		String paramName = formParam.getParamName();
		
		Obj paramObj = Tabb.findLocal(paramName);
		if (paramObj != Tabb.noObj) {
			report_error("Formal parameter (array) is already declared: " + paramName, formParam);
			return;
		}
		
		Struct arrType = new Struct(Struct.Array, formParam.getType().struct);
		Struct matrixType = new Struct(Struct.Array, arrType);
		Tabb.insert(Obj.Var, paramName, matrixType);
		currMethod.setLevel(currMethod.getLevel() + 1);
		currType = null;
	}
	
	public void visit(FormParsSingleYes formParam) {
		String paramName = formParam.getParamName();
		
		Obj paramObj = Tabb.findLocal(paramName);
		if (paramObj != Tabb.noObj) {
			report_error("Formal parameter (array) is already declared: " + paramName, formParam);
			return;
		}
		
		Struct arrType = new Struct(Struct.Array, formParam.getType().struct);
		Tabb.insert(Obj.Var, paramName, arrType);
		currMethod.setLevel(currMethod.getLevel() + 1);
		currType = null;
	}
	
	public void visit(FormParsSingleNo formParam) {
		String paramName = formParam.getParamName();
		
		Obj paramObj = Tabb.findLocal(paramName);
		if (paramObj != Tabb.noObj) {
			report_error("Formal parameter is already declared: " + paramName, formParam);
			return;
		}
		
		Tabb.insert(Obj.Var, paramName, formParam.getType().struct);
		currMethod.setLevel(currMethod.getLevel() + 1);
		currType = null;
	}
	
	// Type
	
	public void visit(TypeYes typeYes) {
		Obj namespaceObj = Tabb.find(typeYes.getNamespaceName());
		
		if (namespaceObj == Tabb.noObj || namespaceObj.getKind() != Objj.Namesp) {
			report_error("Can't resolve namespace: " + typeYes.getNamespaceName(), typeYes);
		}
		
		String typeName = namespaceObj.getName() + "::" + typeYes.getTypeName();
		Obj typeObj = Tabb.find(typeName);
		currType = typeYes.struct = typeObj.getType();
		
		if (typeObj == Tabb.noObj || typeObj.getKind() != Obj.Type) {
			report_error("Can't resolve type: " + typeYes.getTypeName(), typeYes);
		}
		else {
			SyntaxNode parent = typeYes.getParent();
			if (parent instanceof FactorNew && ((FactorNew)parent).getNewChoice() instanceof NewChoiceActParsNo) {
				nv = new NewVisitor(); nv.visitObjNode(typeObj);
				report_info("Usage found: " + nv.getOutput(), typeYes);
			}
		}
	}
	
	public void visit(TypeNo typeNo) {
		String typeName = typeNo.getTypeName();
		if (!typeName.equals("int") && !typeName.equals("char") && !typeName.equals("bool")) {
			typeName = currNamespace + typeName;
		}
		
		Obj typeObj = Tabb.find(typeName);
		currType = typeNo.struct = typeObj.getType();
		
		if (typeObj == Tabb.noObj || typeObj.getKind() != Obj.Type) {
			report_error("Can't resolve type: " + typeNo.getTypeName(), typeNo);
		}
		else {
			SyntaxNode parent = typeNo.getParent();
			if (parent instanceof FactorNew && ((FactorNew)parent).getNewChoice() instanceof NewChoiceActParsNo) {
				nv = new NewVisitor(); nv.visitObjNode(typeObj);
				report_info("Usage found: " + nv.getOutput(), typeNo);
			}
		}
	}
	
	// Statement
	
	public void visit(StmtBreak stmt) {
		if (forCnt < 1) {
			report_error("Break statement used out of the for loop", stmt);
		}
	}
	
	public void visit(StmtContinue stmt) {
		if (forCnt < 1) {
			report_error("Continue statement used out of the for loop", stmt);
		}
	}
	
	public void visit(StmtReturnYes stmt) {
		if (currMethod == null) {
			report_error("Return must be called inside method or global function", stmt);
			return;
		}
		/*
		else if (!currMethod.getType().equals(stmt.getExpr().struct)) {
			report_error("Type of Expr in Return must be equal to method's type", stmt.getExpr());
		}
		*/
		else if (!Tabb.firstAssignableToSecond(stmt.getExpr().struct, currMethod.getType())) {
			report_error("Type of Expr in Return must be equal to method's type", stmt.getExpr());
		}
		
		returnFound = true;
	}
	
	public void visit(StmtReturnNo stmt) {
		if (currMethod == null) {
			report_error("Return must be called inside method or global function", stmt);
			return;
		}
		else if (currMethod.getType() != Tabb.noType) {
			report_error("Return without Expr must be called inside Void method or global function", stmt);
		}
		
		returnFound = true;
	}
	
	public void visit(StmtRead stmt) {
		Obj dsgObj = stmt.getDesignator().obj;
		
		if (dsgObj.getKind() != Obj.Var && dsgObj.getKind() != Objj.Stat && dsgObj.getKind() != Obj.Elem && dsgObj.getKind() != Obj.Fld) {
			report_error("Designator inside parens must be Variable, Array element or Class field: " + dsgObj.getName(), stmt.getDesignator());
		}
		else if (dsgObj.getKind() == Obj.Elem && dsgObj.getType().getKind() == Struct.Array) {
			report_error("Designator inside parens must be Matrix element: " + dsgObj.getName(), stmt.getDesignator());
		}
		else if (!dsgObj.getType().equals(Tabb.intType) && !dsgObj.getType().equals(Tabb.charType) && !dsgObj.getType().equals(Tabb.boolType)) {
			report_error("Type of Designator inside parens must be int, char or bool: " + dsgObj.getName(), stmt.getDesignator());
		}
		
		currDesignator.set(currDesignator.size() - 1, null);
		classDesignator.set(classDesignator.size() - 1, false);
	}
	
	public void visit(StmtPrintYes stmt) {
		Struct exprType = stmt.getExpr().struct;
		
		if (!exprType.equals(Tabb.intType) && !exprType.equals(Tabb.charType) && !exprType.equals(Tabb.boolType) && exprType.getKind() != Struct.Array) {
			report_error("Type of Expr inside parens must be int, char or bool, or Array", stmt.getExpr());
		}
	}
	
	public void visit(StmtPrintNo stmt) {
		Struct exprType = stmt.getExpr().struct;
		
		if (!exprType.equals(Tabb.intType) && !exprType.equals(Tabb.charType) && !exprType.equals(Tabb.boolType)  && exprType.getKind() != Struct.Array) {
			report_error("Type of Expr inside parens must be int, char or bool, or Array", stmt.getExpr());
		}
	}
	
	public void visit(ForSymbolt forSymbol) {
		++forCnt;
	}
	
	public void visit(StmtForYesYesYes stmt) {
		--forCnt;
	}
	
	public void visit(StmtForYesYesNo stmt) {
		--forCnt;
	}
	
	public void visit(StmtForYesNoYes stmt) {
		--forCnt;
	}
	
	public void visit(StmtForYesNoNo stmt) {
		--forCnt;
	}
	
	public void visit(StmtForNoYesYes stmt) {
		--forCnt;
	}
	
	public void visit(StmtForNoYesNo stmt) {
		--forCnt;
	}
	
	public void visit(StmtForNoNoYes stmt) {
		--forCnt;
	}
	
	public void visit(StmtForNoNoNo stmt) {
		--forCnt;
	}
	
	// DesignatorStatement
	
	public void visit(TLParent lParen) {
		currDesignator.add(null);
		designatorParts.add(new ArrayList<String>());
		classDesignator.add(false);
		actParsCount.add(0);
		actParsParts.add(new ArrayList<Struct>());
	}
	
	public void visit(TRParent rParen) {
		currDesignator.remove(currDesignator.size() - 1);
		designatorParts.remove(designatorParts.size() - 1);
		classDesignator.remove(classDesignator.size() - 1);
		actParsCount.remove(actParsCount.size() - 1);
		actParsParts.remove(actParsParts.size() - 1);
	}
	
	public void visit(OpChoiceExpr opChoice) {
		designatorStatementKind = ASSIGNMENT;
		currAssignmentExpr = opChoice.getExpr().struct;
	}
	
	public void visit(OpChoiceActParsYes opChoice) {
		designatorStatementKind = FUNC_CALL;
	}
	
	public void visit(OpChoiceActParsNo opChoice) {
		designatorStatementKind = FUNC_CALL_NO_ARG;
	}
	
	public void visit(OpChoiceInc opChoice) {
		designatorStatementKind = INC_DEC;
	}
	
	public void visit(OpChoiceDec opChoice) {
		designatorStatementKind = INC_DEC;
	}
	
	public void visit(DesignatorStmtFirst dsgStmt) {
		Obj dsgObj = dsgStmt.getDesignator().obj;
		
		 if (designatorStatementKind == ASSIGNMENT) {
			 if (dsgObj.getKind() != Obj.Var && dsgObj.getKind() != Objj.Stat && dsgObj.getKind() != Obj.Elem && dsgObj.getKind() != Obj.Fld) {
				 report_error("Designator must be Var, Array's elem or Field: " + dsgObj.getName(), dsgStmt.getDesignator());
			 }
			 else if (dsgObj.getKind() == Obj.Elem && dsgObj.getType().getKind() == Struct.Array) {
				 report_error("Designator must be matrix'element, cannot be array: " + dsgObj.getName(), dsgStmt.getDesignator());
			 }
			 else if (!Tabb.firstAssignableToSecond(currAssignmentExpr, dsgObj.getType())) {
				 report_error("Expr must be assignable to Designator: " + dsgObj.getName(), dsgStmt.getDesignator());
			 }
		 }
		 else if (designatorStatementKind == INC_DEC) {
			 if (dsgObj.getKind() != Obj.Var && dsgObj.getKind() != Objj.Stat && dsgObj.getKind() != Obj.Elem && dsgObj.getKind() != Obj.Fld) {
				 report_error("Designator must be Var, Array's elem or Field: " + dsgObj.getName(), dsgStmt.getDesignator());
			 }
			 else if (dsgObj.getKind() == Obj.Elem && dsgObj.getType().getKind() == Struct.Array) {
				 report_error("Designator must be matrix'element, cannot be array: " + dsgObj.getName(), dsgStmt.getDesignator());
			 }
			 else if (!dsgObj.getType().equals(Tabb.intType)) {
				 report_error("Designator must be type of Integer: " + dsgObj.getName(), dsgStmt.getDesignator());
			 }
		 }
		 else {
			 if (dsgObj.getKind() != Obj.Meth) {
				 report_error("Designator must be non-static class method or global function: " + dsgObj.getName(), dsgStmt.getDesignator());
			 }
			 else if (designatorStatementKind == FUNC_CALL_NO_ARG && dsgObj.getLevel() != (classDesignator.get(classDesignator.size() - 1) ? 1 : 0)) {
					 report_error("Number of formal and actual parameters must be the same: " + dsgObj.getName(), dsgStmt.getDesignator());
			 }
		 }
		 
		 designatorStatementKind = NOT_USED;
		 currAssignmentExpr = null;
		 currDesignator.set(currDesignator.size() - 1, null);
		 classDesignator.set(classDesignator.size() - 1, false);
	}
	
	public void visit(DesignatorListYesYes dsgList) {
		dsgStmtParts.add(dsgList.getDesignator().obj);
	}
	
	public void visit(DesignatorListYesNo dsgList) {
		dsgStmtParts.add(null);
	}
	
	public void visit(DesignatorStmtSecond dsgStmt) {
		Obj lDsgObj = dsgStmt.getDesignator().obj;
		Obj rDsgObj = dsgStmt.getDesignator1().obj;
		
		if (rDsgObj.getType().getKind() != Struct.Array) {
			report_error("Designator on the right must be kind of Array: " + rDsgObj.getName(), dsgStmt.getDesignator1());
		}
		else if (lDsgObj.getType().getKind() != Struct.Array) {
			report_error("Designator after the * must be kind of Array: " + lDsgObj.getName(), dsgStmt.getDesignator());
		}
		/*
		else if (!lDsgObj.getType().getElemType().assignableTo(rDsgObj.getType().getElemType())) {
			report_error("Designator after the * must be assignable to the Designator on the right: " + lDsgObj.getName(), dsgStmt.getDesignator());
		}
		*/
		else if (!Tabb.firstAssignableToSecond(rDsgObj.getType().getElemType(), lDsgObj.getType().getElemType())) {
			report_error("Designator on the right must be assignable to the Designator after the *: " + rDsgObj.getName(), dsgStmt.getDesignator1());
		}
		else {
			for (Obj elem: dsgStmtParts) {
				if (elem != null) {
					if (elem.getKind() != Obj.Var && elem.getKind() != Objj.Stat && elem.getKind() != Obj.Elem && elem.getKind() != Obj.Fld) {
						report_error("Designator before * must be either Var, Array's elem or Field in class: " + elem.getName(), dsgStmt);
					}
					/*
					if (!rDsgObj.getType().getElemType().assignableTo(elem.getType())) {
						report_error("Designator on the right must be assignable to each Designator before *: " + elem.getName(), dsgStmt);
					}
					*/
					if (!Tabb.firstAssignableToSecond(rDsgObj.getType().getElemType(), elem.getType())) {
						report_error("Designator on the right must be assignable to each Designator before *: " + elem.getName(), dsgStmt);
					}
				}
			}
		}
		
		currDesignator.set(currDesignator.size() - 1, null);
		classDesignator.set(classDesignator.size() - 1, false);
		dsgStmtParts.clear();
	}
	
	public void visit(LCFort forSymbol) {
		currDesignator.set(currDesignator.size() - 1, null);
		classDesignator.set(classDesignator.size() - 1, false);
		listComprActive = true;
	}
	
	public void visit(LCInt inSymbol) {
		listComprActive = false;
	}
	
	public void visit(LCIft ifSymbol) {
		currDesignator.set(currDesignator.size() - 1, null);
		classDesignator.set(classDesignator.size() - 1, false);
		listComprActive = true;
	}
	
	public void visit(DesignatorStmtThirdYes dsgStmt) {
		Obj firstDsg = dsgStmt.getDesignator().obj;
		Obj secondDsg = dsgStmt.getDesignator1().obj;
		
		if (firstDsg.getType().getKind() != Struct.Array || secondDsg.getType().getKind() != Struct.Array ) {
			report_error("Both designators in list comprehensions must be Arrays with same element type", dsgStmt);
		}
		else if (!firstDsg.getType().getElemType().equals(secondDsg.getType().getElemType())) {
			report_error("Both designators in list comprehensions must be Arrays with same element type", dsgStmt);
		}
		
		Struct exprType = dsgStmt.getExpr().struct;
		Struct arrType = dsgStmt.getDesignator1().obj.getType().getElemType();
		
		if (!exprType.compatibleWith(arrType)) {
			report_error("Expr type must be compatible with second Array's element type", dsgStmt);
		}
		
		listComprActive = false;
		listComprObj = null;
	}
	
	public void visit(DesignatorStmtThirdNo dsgStmt) {
		Obj firstDsg = dsgStmt.getDesignator().obj;
		Obj secondDsg = dsgStmt.getDesignator1().obj;
		
		if (firstDsg.getType().getKind() != Struct.Array || secondDsg.getType().getKind() != Struct.Array ) {
			report_error("Both designators in list comprehensions must be Arrays with same element type", dsgStmt);
		}
		else if (!firstDsg.getType().getElemType().equals(secondDsg.getType().getElemType())) {
			report_error("Both designators in list comprehensions must be Arrays with same element type", dsgStmt);
		}
		
		Struct exprType = dsgStmt.getExpr().struct;
		Struct arrType = dsgStmt.getDesignator1().obj.getType().getElemType();
		
		if (!exprType.compatibleWith(arrType)) {
			report_error("Expr type must be compatible with second Array's element type", dsgStmt);
		}
		
		listComprActive = false;
		listComprObj = null;
	}
	
	// ActPars
	
	public void visit(ActParsTempSingle actPars) {
		int cInd = actParsCount.size() - 1;
		int pInd = actParsParts.size() - 1;
		actParsCount.set(cInd, actParsCount.get(cInd) + 1);
		actParsParts.get(pInd).add(actPars.getExpr().struct);
	}
	
	public void visit(ActParsTempList actPars) {
		int cInd = actParsCount.size() - 1;
		int pInd = actParsParts.size() - 1;
		actParsCount.set(cInd, actParsCount.get(cInd) + 1);
		actParsParts.get(pInd).add(actPars.getExpr().struct);
	}
	
	public void visit(ActParst actPars) {
		if (currType != null) {
			report_error("Only constructors without parameters are allowed", actPars);
		}
		else {
			int additional = (classDesignator.get(classDesignator.size() - 2) ? 1 : 0);
			int apCount = actParsCount.get(actParsCount.size() - 1);
			
			if (currDesignator.get(currDesignator.size() - 2).getKind() != Obj.Meth) {
				report_error("Recognized designator must be a method", actPars);
			}
			else if (currDesignator.get(currDesignator.size() - 2).getLevel() != (apCount + additional)) {
				report_error("Number of formal and actual parameters must be the same", actPars);
			}
			else {
				for (int i = 0; i < apCount; ++i) {
					Struct actPar = actParsParts.get(actParsParts.size() - 1).get(i);
					Struct formPar = Tabb.getFormalParam(currDesignator.get(currDesignator.size() - 2), i + additional).getType();
					/*if (!actPar.assignableTo(formPar)) {
						report_error("Actual parameters must be assignable to the formal parameters", actPars);
						break;
					}*/
					if (!Tabb.firstAssignableToSecond(actPar, formPar)) {
						report_error("Actual parameters must be assignable to the formal parameters", actPars);
						break;
					}
				}
			}
		}
		
		actParsParts.get(actParsParts.size() - 1).clear();
		actParsCount.set(actParsCount.size() - 1, 0);
	}
	
	// Condition
	
	// CondTerm
	
	// CondFact
	
	public void visit(CondFactYes condFact) {
		Struct exprType1 = condFact.getExpr().struct;
		Struct exprType2 = condFact.getExpr1().struct;
		
		if (!exprType1.compatibleWith(exprType2)) {
			report_error("Types of expressions must be compatibile", condFact.getExpr());
		}
		else if (exprType1.isRefType() && exprType2.isRefType() && relopKind != EQ_NOTEQ) {
			report_error("Relop when using Ref Types must be either Equal or Not_Equal", condFact.getExpr());
		}
		
		relopKind = NOT_USED;
	}
	
	public void visit(CondFactNo condFact) {
		Struct exprType = condFact.getExpr().struct;
		
		if (!exprType.equals(Tabb.boolType)) {
			report_error("Expression must be of bool type", condFact.getExpr());
		}
	}
	
	// Expr
	
	public void visit(AddopTermListYes expr) {
		Struct termType = expr.getTerm().struct;
		hasAddopTerm = true;
		if (!termType.equals(Tabb.intType)) addopTermError = true;
	}
	
	public void visit(ExprYes expr) {
		Struct termType = expr.getTerm().struct;
		if (!termType.equals(Tabb.intType)) addopTermError = true;
		
		if (addopTermError) {
			report_error("First term in Minus Addop Expr must be integer", expr.getTerm());
		}
		
		addopTermError = false;
		if (hasAddopTerm && !termType.equals(Tabb.intType)) addopTermError = true; 
		
		if (addopTermError) {
			report_error("All terms in Addop Expr must be integers", expr.getTerm());
		}
		
		expr.struct = termType;
		hasAddopTerm = false;
		addopTermError = false;
	}
	
	public void visit(ExprNo expr) {
		Struct termType = expr.getTerm().struct;
		if (hasAddopTerm && !termType.equals(Tabb.intType)) addopTermError = true; 
		
		if (addopTermError) {
			report_error("All terms in Addop Expr must be integers", expr.getTerm());
		}
		
		expr.struct = termType;
		hasAddopTerm = false;
		addopTermError = false;
	}
	
	// Term
	
	public void visit(MulopFactorListYes factor) {
		Struct factorType = factor.getFactor().struct;
		hasMulopFactor = true;
		if (!factorType.equals(Tabb.intType)) mulopFactorError = true;
	}
	
	public void visit(Termt term) {
		Struct factorType = term.getFactor().struct;
		if (hasMulopFactor && !factorType.equals(Tabb.intType)) mulopFactorError = true;
		
		if (mulopFactorError) {
			report_error("All factors in Mulop Term must be integers", term.getFactor());
		}
		
		term.struct = factorType;
		hasMulopFactor = false;
		mulopFactorError = false;
	}
	
	// Factor
	
	public void visit(FactorDesignatorFirst factor) {
		Obj dsgObj = factor.getDesignator().obj;
		if (dsgObj.getKind() != Obj.Meth) {
			report_error("Designator must be Nonstatic Class or Global method", factor.getDesignator());
		}
		
		factor.struct = dsgObj.getType();
		currDesignator.set(currDesignator.size() - 1, null);
		classDesignator.set(classDesignator.size() - 1, false);
	}
	
	public void visit(FactorDesignatorSecond factor) {
		Obj dsgObj = factor.getDesignator().obj;
		if (dsgObj.getKind() != Obj.Meth) {
			report_error("Designator must be Nonstatic Class or Global method", factor.getDesignator());
		}
		else if (dsgObj.getLevel() != (classDesignator.get(classDesignator.size() - 1) ? 1 : 0)) {
			report_error("Number of formal and actual parameters must be the same", factor.getDesignator());
		}
		
		factor.struct = dsgObj.getType();
		currDesignator.set(currDesignator.size() - 1, null);
		classDesignator.set(classDesignator.size() - 1, false);
	}
	
	public void visit(FactorDesignatorThird factor) {
		Obj dsgObj = factor.getDesignator().obj;
		if (dsgObj.getKind() == Obj.Type || dsgObj.getKind() == Obj.Meth) {
			report_error("Designator must be Constant or Variable", factor.getDesignator());
		}
		
		if (listComprActive && dsgObj.getKind() != Obj.Con) {
			if (listComprObj != null && !listComprObj.equals(dsgObj)) {
				report_error("You must have max one different Var in list comprehension", factor.getDesignator());
			}
			else if (listComprObj == null) {
				if (dsgObj.getKind() != Obj.Var) {
					report_error("You must have max one different Var in list comprehension", factor.getDesignator());
				}
				else {
					listComprObj = dsgObj;
				}
			}
		}
		
		factor.struct = dsgObj.getType();
		currDesignator.set(currDesignator.size() - 1, null);
		classDesignator.set(classDesignator.size() - 1, false);
	}
	
	public void visit(FactorNum factor) {
		factor.struct = Tabb.intType;
	}
	
	public void visit(FactorChar factor) {
		factor.struct = Tabb.charType;
	}
	
	public void visit(FactorBool factor) {
		factor.struct = Tabb.boolType;
	}
	
	public void visit(NewChoiceExprExpr expr) {
		isNewFactorClass = false;
		isNewFactorMatrix = true;
		Struct exprType1 = expr.getExpr().struct;
		Struct exprType2 = expr.getExpr2().struct;
		if (!exprType1.equals(Tabb.intType) || !exprType2.equals(Tabb.intType)) {
			report_error("Expr between brackets must be integer", expr.getExpr());
		}
	}
	
	public void visit(NewChoiceExpr expr) {
		isNewFactorClass = false;
		isNewFactorMatrix = false;
		Struct exprType = expr.getExpr().struct;
		if (!exprType.equals(Tabb.intType)) {
			report_error("Expr between brackets must be integer", expr.getExpr());
		}
	}
	
	public void visit(NewChoiceActParsNo expr) {
		isNewFactorClass = true;
		isNewFactorMatrix = false;
	}
	
	public void visit(NewChoiceActParsYes expr) {
		isNewFactorClass = true;
		isNewFactorMatrix = false;
	}
	
	public void visit(FactorNew factor) {
		Struct factorType = factor.getType().struct;
		if (isNewFactorClass && factorType.getKind() != Struct.Class) {
			report_error("Type must be user defined type (class)", factor.getType());
		}
		
		if (!isNewFactorClass) {
			factorType = new Struct(Struct.Array, factorType);
			if (isNewFactorMatrix) factorType = new Struct(Struct.Array, factorType);
		}
		factor.struct = factorType;
		
		currType = null;
		isNewFactorClass = false;
		isNewFactorMatrix = false;
	}
	
	public void visit(FactorExpr factor) {
		Struct factorType = factor.getExpr().struct;
		factor.struct = factorType;
	}
	
	public void visit(FactorRange factor) {
		Struct exprType = factor.getExpr().struct; 
		
		if (!exprType.equals(Tabb.intType)) {
			report_error("Expr between parens must be int type", factor.getExpr());
		}
		
		Struct factorType = new Struct(Struct.Array, exprType);
		factor.struct = factorType;
	}
	
	// Designator
	
	public void visit(TLBrackett lBracket) {
		currDesignator.add(null);
		designatorParts.add(new ArrayList<String>());
		classDesignator.add(false);
		actParsCount.add(0);
		actParsParts.add(new ArrayList<Struct>());
	}
	
	public void visit(TRBrackett rBracket) {
		currDesignator.remove(currDesignator.size() - 1);
		designatorParts.remove(designatorParts.size() - 1);
		classDesignator.remove(classDesignator.size() - 1);
		actParsCount.remove(actParsCount.size() - 1);
		actParsParts.remove(actParsParts.size() - 1);
	}
	
	public void visit(DesignatorPartsIdent designator) {
		designatorParts.get(designatorParts.size() - 1).add(designator.getDsgName());
	}
	
	public void visit(DesignatorPartsExpr designator) {
		Struct exprType = designator.getExpr().struct;
		if (!exprType.equals(Tabb.intType)) {
			report_error("Expr inside brackets must be integer", designator.getExpr());
		}
		designatorParts.get(designatorParts.size() - 1).add("[]");
	}
	
	public void visitDsgYes(Designatort designator, String nspName, String dsgName) {
		Obj nspObj = Tabb.find(nspName);
		
		if (nspObj == Tabb.noObj || nspObj.getKind() != Objj.Namesp) {
			report_error("Can't resolve namespace: " + nspName, designator);
		}
		
		dsgName = nspObj.getName() + "::" + dsgName;
		Obj dsgObj = Tabb.find(dsgName);
		
		int dsgKind = dsgObj.getKind();
		if (dsgObj == Tabb.noObj || (dsgKind != Obj.Var && dsgKind != Obj.Fld && dsgKind != Objj.Stat && dsgKind != Obj.Con && dsgKind != Obj.Meth && dsgObj.getType().getKind() != Struct.Class)) {
			report_error("Can't resolve designator: " + dsgName, designator);
		}
		else if (staticClassActive) {
			if (dsgKind != Obj.Type && dsgKind != Objj.Stat) {
				report_error("In static initializer, you must refer to current class: " + dsgObj.getName(), designator);
			}
			else if (!dsgObj.getType().equals(currClass.getType())) {
				report_error("In static initializer, you must refer to current class: " + dsgObj.getName(), designator);
			}
		}
		
		Struct elemType = dsgObj.getType();
		ArrayList<String> elems = designatorParts.get(designatorParts.size() - 1); 
		
		if (elems.isEmpty() && dsgObj.getKind() == Obj.Meth) {
			if (Tabb.checkIfClassDesignator(dsgObj, currClass != null, currMethod != null))
				classDesignator.set(classDesignator.size() - 1, true);
		}
		
		for (String elem: elems) {
			if (elem.equals("[]")) {
				dsgName += "[]";
				
				if (elemType.getKind() != Struct.Array) {
					report_error("Designator before brackets must be an Array type: " + dsgName, designator);
					break;
				}
				
				elemType = elemType.getElemType();
				dsgObj = new Obj(Obj.Elem, dsgName, elemType);
			}
			else {
				dsgName += "." + elem;
				classDesignator.set(classDesignator.size() - 1, true);
				
				if (elemType.getKind() != Struct.Class) {
					report_error("Designator before dot must be a Class type: " + dsgName, designator);
					break;
				}
				
				Obj tmpObj = elemType.getMembersTable().searchKey(elem);
				if (tmpObj != null) {
					if (dsgObj.getKind() == Obj.Type) {
						report_error("Nonstatic field cannot be accessed via user defined type: " + dsgName, designator);
						break;
					}
					else {
						dsgObj = tmpObj;
					}
				}
				else {
					tmpObj = Tabb.findStatic(elem, elemType);
					if (tmpObj != Tabb.noObj) {
						if (dsgObj.getKind() != Obj.Type) {
							report_error("Static field cannot be accessed via object of user defined type: " + dsgName, designator);
							break;
						}
						else {
							dsgObj = tmpObj;
						}
					}
					else {
						report_error("Can't resolve designator: " + dsgName, designator);
						break;
					}
				}
				
				elemType = dsgObj.getType();
			}
		}
		
		designatorParts.get(designatorParts.size() - 1).clear();
		currDesignator.set(currDesignator.size() - 1, designator.obj = dsgObj);
		
		nv = new NewVisitor(); nv.visitObjNode(dsgObj);
		report_info("Usage found: " + nv.getOutput(), designator);
	}
	
	public void visitDsgNo(Designatort designator, String dsgName) {
		String localName = dsgName;
		String fullName = currNamespace + localName;
		
		Obj dsgObj = Tabb.find(localName);
		if (currClass != null && (dsgObj == Tabb.noObj || dsgObj.getLevel() == 0)) {
			Obj tmpObj = Tabb.findStatic(localName, currClass.getType());
			if (tmpObj != Tabb.noObj) dsgObj = tmpObj;
		}
		if (dsgObj == Tabb.noObj || (dsgObj.getKind() != Objj.Stat && dsgObj.getLevel() == 0)) {
			Obj tmpObj = Tabb.find(fullName);
			if (tmpObj != Tabb.noObj) dsgObj = tmpObj;
		}
		
		int dsgKind = dsgObj.getKind();
		if (dsgObj == Tabb.noObj || (dsgKind != Obj.Var && dsgKind != Obj.Fld && dsgKind != Objj.Stat && dsgKind != Obj.Con && dsgKind != Obj.Meth && dsgObj.getType().getKind() != Struct.Class)) {
			report_error("Can't resolve designator: " + fullName, designator);
		}
		else {
			SyntaxNode dsgParent = designator.getParent();
			boolean dsgInd = (dsgParent instanceof DesignatorStmtFirst)
					|| (dsgParent instanceof DesignatorListYesYes)
					|| (dsgParent instanceof DesignatorStmtSecond && ((DesignatorStmtSecond)dsgParent).getDesignator().equals(designator))
					|| (dsgParent instanceof StmtRead); 
			if (staticClassActive && dsgInd) {
				if (dsgKind != Obj.Type && dsgKind != Objj.Stat) {
					report_error("In static initializer, you must refer to current class: " + dsgObj.getName(), designator);
				}
				else if (dsgKind == Obj.Type && !dsgObj.getType().equals(currClass.getType())) {
					report_error("In static initializer, you must refer to current class: " + dsgObj.getName(), designator);
				}
			}
		}
		
		dsgName = fullName;
		Struct elemType = dsgObj.getType();
		ArrayList<String> elems = designatorParts.get(designatorParts.size() - 1);
		
		if (elems.isEmpty() && dsgObj.getKind() == Obj.Meth) {
			if (Tabb.checkIfClassDesignator(dsgObj, currClass != null, currMethod != null))
				classDesignator.set(classDesignator.size() - 1, true);
		}
		
		for (String elem: elems) {
			if (elem.equals("[]")) {
				dsgName += "[]";
				
				if (elemType.getKind() != Struct.Array) {
					report_error("Designator before brackets must be an Array type: " + dsgName, designator);
					break;
				}
				
				elemType = elemType.getElemType();
				dsgObj = new Obj(Obj.Elem, dsgName, elemType);
			}
			else {
				dsgName += "." + elem;
				classDesignator.set(classDesignator.size() - 1, true);
				
				if (elemType.getKind() != Struct.Class) {
					report_error("Designator before dot must be a Class type: " + dsgName, designator);
					break;
				}
				
				boolean ind = currClass != null && elemType.equals(currClass.getType());
				Obj tmpObj = (ind ? Tabb.findOuterLocal(elem) : elemType.getMembersTable().searchKey(elem));
				if (tmpObj == null) tmpObj = Tabb.noObj;
				
				if (tmpObj != Tabb.noObj) {
					if (dsgObj.getKind() == Obj.Type) {
						report_error("Nonstatic field cannot be accessed via user defined type: " + dsgName, designator);
						break;
					}
					else {
						dsgObj = tmpObj;
					}
				}
				else {
					tmpObj = Tabb.findStatic(elem, elemType);
					if (tmpObj != Tabb.noObj) {
						if (dsgObj.getKind() != Obj.Type) {
							report_error("Static field cannot be accessed via object of user defined type: " + dsgName, designator);
							break;
						}
						else {
							dsgObj = tmpObj;
						}
					}
					else {
						report_error("Can't resolve designator: " + dsgName, designator);
						break;
					}
				}
				
				elemType = dsgObj.getType();
			}
		}
		
		designatorParts.get(designatorParts.size() - 1).clear();
		currDesignator.set(currDesignator.size() - 1, designator.obj = dsgObj);
		
		nv = new NewVisitor(); nv.visitObjNode(dsgObj);
		report_info("Usage found: " + nv.getOutput(), designator);
	}

	public void visit(Designatort designator) {
		String nspName = "";
		String dsgName = "";
		SyntaxNode fDesignator = designator.getFirstDesignator();
		
		if (fDesignator instanceof FDesignatorYes) {
			nspName = ((FDesignatorYes)fDesignator).getNspName();
			dsgName = ((FDesignatorYes)fDesignator).getDsgName();
			visitDsgYes(designator, nspName, dsgName);
		}
		else {
			dsgName = ((FDesignatorNo)fDesignator).getDsgName();
			visitDsgNo(designator, dsgName);
		}
	}
	
	// Label
	// can be used only inside functions
	// cannot have qualified names
	
	public void visit (Labelt label) {
		String labName = label.getLabName();
		
		if (currMethod == null) {
			report_error("Labels must be declared and used only inside functions: " + labName, label);
			return;
		}
		
		Obj labObj = Tabb.findLocal(labName);
		
		if (labelCreationActive) {
			if (labObj != Tabb.noObj) {
				report_error("Label already declared: " + labName, label);
				return;
			}
			label.obj = Tabb.insert(Objj.Labl, labName, Tabb.noType);
		}
		else if (labObj == Tabb.noObj || labObj.getKind() != Objj.Labl) {
			report_error("Label is not declared: " + labName, label);
		}
	}
	
	// Relop
	
	public void visit(RelopEqual relop) {
		relopKind = EQ_NOTEQ;
	}
	
	public void visit(RelopNotEqual relop) {
		relopKind = EQ_NOTEQ;
	}
	
	public void visit(RelopGrt relop) {
		relopKind = OTHERS;
	}
	
	public void visit(RelopGrtEqual relop) {
		relopKind = OTHERS;
	}
	
	public void visit(RelopLess relop) {
		relopKind = OTHERS;
	}
	
	public void visit(RelopLessEqual relop) {
		relopKind = OTHERS;
	}

}
