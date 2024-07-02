package rs.ac.bg.etf.pp1;

import java.util.ArrayList;

import org.apache.log4j.Logger;
import rs.ac.bg.etf.pp1.ast.*;
import rs.etf.pp1.symboltable.concepts.*;

// TODO: ocistiti currDesignator gde je potrebno [DesignatorStatement]
// kod lakog DesignatorStatement: cim se prepozna DesignatorStatement
// kod teskog DesignatorStatement: razmisliti kako ovo treba da se handluje

// TODO: videti gde sam sve "uposlio" promenljive iz klase, pa ih i "otposliti"

public class SemanticAnalyzer extends VisitorAdaptor {
	
	Logger log = Logger.getLogger(getClass());
	
	public void report_error(String message, SyntaxNode info) {
		errorDetected = true;
		StringBuilder msg = new StringBuilder(message);
		int line = (info == null) ? 0: info.getLine();
		if (line != 0)
			msg.append (" ~~~ Line: ").append(line);
		log.error(msg.toString());
	}

	public void report_info(String message, SyntaxNode info) {
		StringBuilder msg = new StringBuilder(message); 
		int line = (info == null) ? 0: info.getLine();
		if (line != 0)
			msg.append (" ~~~ Line: ").append(line);
		log.info(msg.toString());
	}
	
	public boolean getErrorDetected() {
		return errorDetected;
	}
	
	String currNamespace = "";
	Struct currType = null;
	Obj currClass = null;
	Obj currMethod = null;
	
	ArrayList<String> designatorParts = new ArrayList<>();
	Obj currDesignator = null;
	ArrayList<Struct> actParsParts = new ArrayList<>();
	int actParsCount = 0;
	
	boolean returnFound = false;
	boolean errorDetected = false;
	boolean arrayActive = false;
	
	boolean hasMulopFactor = false;
	boolean mulopFactorError = false;
	boolean hasAddopTerm = false;
	boolean addopTermError = false;
	boolean isNewFactorClass = false;
	
	// 0 --> not used
	// 1 --> equal, not equal
	// 2 --> others
	int relopKind = 0;
	
	// Program
	
	public void visit(ProgNamet progName) {
		progName.obj = Tabb.insert(Obj.Prog, progName.getProgName(), Tabb.noType);
		Tabb.openScope();
		Tabb.programScope = Tabb.currentScope();
	}
	
	public void visit(Programt program) {
		Obj mainObj = Tabb.find("main");
		if (mainObj == Tabb.noObj || mainObj.getKind() != Obj.Meth || mainObj.getType() != Tabb.noType || mainObj.getLevel() != 0) {
			report_error("Method 'void main() { ...}' not found", null);
		}
		
		Tabb.chainLocalSymbols(program.getProgName().obj);
		Tabb.closeScope();
		Tabb.programScope = null;
	}
	
	// Namespace
	
	public void visit(NamespaceNamet namespaceName) {
		// we allow multiple declarations of namespace with same name
		Tabb.insert(Objj.Namesp, namespaceName.getNamespaceName(), Tabb.noType);
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
		// report_info("Constant successfully created: " + constName, constChar);
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
		// report_info("Constant successfully created: " + constName, constBool);
	}
	
	// VarDecl
	
	public void visit(VarDeclt varDecl) {
		currType = null;
	}
	
	public void visit(VarPartYes varPart) {
		String varName = currNamespace + varPart.getVarName();
		
		Obj varObj = null;
		if (currMethod == null) varObj = Tabb.find(varName);
		else varObj = Tabb.findLocal(varName);
		
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
		else varObj = Tabb.findLocal(varName);
		
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
		
		// treba proveriti da li je pronadjen return unutra, ako metoda nije void
	}
	
	public void visit(MethodDeclNo methodDecl) {
		Tabb.chainLocalSymbols(methodDecl.getMethodName().obj);
		Tabb.closeScope();
		
		// treba proveriti da li je pronadjen return unutra, ako metoda nije void		
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
		
		Obj paramObj = Tabb.findLocal(paramName);
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
	}
	
	// Statement
	
	// DesignatorStatement
	
	// ActPars
	
	public void visit(ActParsTempSingle actPars) {
		++actParsCount;
		actParsParts.add(actPars.getExpr().struct);
	}
	
	public void visit(ActParsTempList actPars) {
		++actParsCount;
		actParsParts.add(actPars.getExpr().struct);
	}
	
	public void visit(ActParst actPars) {
		if (currType != null) {
			report_error("Only constructors without parameters are allowed", actPars);
		}
		else if (currDesignator.getKind() != Obj.Meth) {
			report_error("Recognized designator must be a method", actPars);
		}
		else if (currDesignator.getLevel() != actParsCount) {
			report_error("Number of formal and actual parameters must be the same", actPars);
		}
		else {
			for (int i = 0; i < actParsCount; ++i) {
				Struct actPar = actParsParts.get(i);
				Struct formPar = Tabb.getFormalParam(currDesignator, i).getType();
				if (!actPar.assignableTo(formPar)) {
					report_error("Actual parameters must be assignable to the formal parameters", actPars);
					break;
				}
			}
		}
		
		actParsParts.clear();
		actParsCount = 0;
	}
	
	// Condition
	
	// CondTerm
	
	// CondFact
	
	public void visit(CondFactYes condFact) {
		Struct exprType1 = condFact.getExpr().struct;
		Struct exprType2 = condFact.getExpr1().struct;
		
		if (!exprType1.compatibleWith(exprType2)) {
			report_error("Types of expressions must be compatibile", condFact);
		}
		else if (exprType1.isRefType() && exprType2.isRefType() && relopKind != 1) {
			report_error("Relop when using Ref Types must be either Equal or Not_Equal", condFact);
		}
		
		relopKind = 0;
	}
	
	public void visit(CondFactNo condFact) {
		Struct exprType = condFact.getExpr().struct;
		
		if (!exprType.equals(Tabb.boolType)) {
			report_error("Expression must be of bool type", condFact);
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
			report_error("First term in Minus Addop Expr must be integer", expr);
		}
		
		addopTermError = false;
		if (hasAddopTerm && !termType.equals(Tabb.intType)) addopTermError = true; 
		
		if (addopTermError) {
			report_error("All terms in Addop Expr must be integers", expr);
		}
		
		expr.struct = termType;
		hasAddopTerm = false;
		addopTermError = false;
	}
	
	public void visit(ExprNo expr) {
		Struct termType = expr.getTerm().struct;
		if (hasAddopTerm && !termType.equals(Tabb.intType)) addopTermError = true; 
		
		if (addopTermError) {
			report_error("All terms in Addop Expr must be integers", expr);
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
			report_error("All factors in Mulop Term must be integers", term);
		}
		
		term.struct = factorType;
		hasMulopFactor = false;
		mulopFactorError = false;
	}
	
	// Factor
	
	public void visit(FactorDesignatorFirst factor) {
		Obj dsgObj = factor.getDesignator().obj;
		if (dsgObj.getKind() != Obj.Meth) {
			report_error("Designator must be Nonstatic Class or Global method", factor);
		}
		factor.struct = dsgObj.getType();
		currDesignator = null;
	}
	
	public void visit(FactorDesignatorSecond factor) {
		Obj dsgObj = factor.getDesignator().obj;
		if (dsgObj.getKind() != Obj.Meth) {
			report_error("Designator must be Nonstatic Class or Global method", factor);
		}
		factor.struct = dsgObj.getType();
		currDesignator = null;
	}
	
	public void visit(FactorDesignatorThird factor) {
		factor.struct = factor.getDesignator().obj.getType();
		currDesignator = null;
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
	
	public void visit(NewChoiceExpr expr) {
		isNewFactorClass = false;
		Struct exprType = expr.getExpr().struct;
		if (!exprType.equals(Tabb.intType)) {
			report_error("Expr between brackets must be integer", expr);
		}
	}
	
	public void visit(NewChoiceActParsNo expr) {
		isNewFactorClass = true;
	}
	
	public void visit(NewChoiceActParsYes expr) {
		isNewFactorClass = true;
	}
	
	public void visit(FactorNew factor) {
		Struct factorType = factor.getType().struct;
		if (isNewFactorClass && factorType.getKind() != Struct.Class) {
			report_error("Type must be user defined type (class)", factor);
		}
		
		factor.struct = factorType;
		currType = null;
	}
	
	public void visit(FactorExpr factor) {
		Struct factorType = factor.getExpr().struct;
		factor.struct = factorType;
	}
	
	// Designator
	
	public void visit(DesignatorPartsIdent designator) {
		designatorParts.add(designator.getDsgName());
	}
	
	public void visit(DesignatorPartsExpr designator) {
		Struct exprType = designator.getExpr().struct;
		if (!exprType.equals(Tabb.intType)) {
			report_error("Expr inside brackets must be integer", designator);
		}
		designatorParts.add("[]");
	}
	
	public void visit(DesignatorYes designator) {
		Obj nspObj = Tabb.find(designator.getNspName());
		
		if (nspObj == Tabb.noObj || nspObj.getKind() != Objj.Namesp) {
			report_error("Can't resolve namespace: " + designator.getNspName(), designator);
		}
		
		String dsgName = nspObj.getName() + "::" + designator.getDsgName();
		Obj dsgObj = Tabb.find(dsgName);
		
		if (dsgObj == Tabb.noObj || (dsgObj.getKind() != Obj.Var && dsgObj.getKind() != Obj.Con && dsgObj.getKind() != Obj.Meth && dsgObj.getKind() != Obj.Type)) {
			report_error("Can't resolve user defined type: " + designator.getDsgName(), designator);
		}
		
		Struct elemType = dsgObj.getType();
		for (String elem: designatorParts) {
			if (elem.equals("[]")) {
				if (elemType.getKind() != Struct.Array) {
					report_error("Designator before brackets must be an Array type", designator);
					break;
				}
				dsgName += elem;
				elemType = elemType.getElemType();
				dsgObj = new Obj(Obj.Elem, dsgName, elemType);
			}
			else {
				if (elemType.getKind() != Struct.Class) {
					report_error("Designator before dot must be a Class type", designator);
					break;
				}
				dsgName += "." + elem;
				dsgObj = elemType.getMembersTable().searchKey(elem);
				if (dsgObj == null) dsgObj = Tabb.findStatic(elem, elemType);
				elemType = dsgObj.getType();
			}
		}
		
		designatorParts.clear();
		currDesignator = designator.obj = dsgObj;
	}
	
	public void visit(DesignatorNo designator) {
		String dsgName = currNamespace + designator.getDsgName();
		Obj dsgObj = Tabb.find(dsgName);
		
		if (dsgObj == Tabb.noObj || (dsgObj.getKind() != Obj.Var && dsgObj.getKind() != Obj.Con && dsgObj.getKind() != Obj.Meth && dsgObj.getType().getKind() != Struct.Class)) {
			report_error("Can't resolve user defined type: " + designator.getDsgName(), designator);
		}
		
		Struct elemType = dsgObj.getType();
		for (String elem: designatorParts) {
			if (elem.equals("[]")) {
				if (elemType.getKind() != Struct.Array) {
					report_error("Designator before brackets must be an Array type", designator);
					break;
				}
				dsgName += elem;
				elemType = elemType.getElemType();
				dsgObj = new Obj(Obj.Elem, dsgName, elemType);
			}
			else {
				if (elemType.getKind() != Struct.Class) {
					report_error("Designator before dot must be a Class type", designator);
					break;
				}
				dsgName += "." + elem;
				dsgObj = elemType.getMembersTable().searchKey(elem);
				if (dsgObj == null) dsgObj = Tabb.findStatic(elem, elemType);
				elemType = dsgObj.getType();
			}
		}
		
		designatorParts.clear();
		currDesignator = designator.obj = dsgObj;
	}
	
	// Label
	
	public void visit (Labelt label) {
		String labName = label.getLabName();
		Obj labObj = Tabb.find(labName);
		
		if (labObj != Tabb.noObj) {
			report_error("Label already declared " + labName, label);
		}
		
		label.obj = Tabb.insert(Objj.Labl, labName, Tabb.noType);
	}
	
	// Relop
	
	public void visit(RelopEqual relop) {
		relopKind = 1;
	}
	
	public void visit(RelopNotEqual relop) {
		relopKind = 1;
	}
	
	public void visit(RelopGrt relop) {
		relopKind = 2;
	}
	
	public void visit(RelopGrtEqual relop) {
		relopKind = 2;
	}
	
	public void visit(RelopLess relop) {
		relopKind = 2;
	}
	
	public void visit(RelopLessEqual relop) {
		relopKind = 2;
	}

}
