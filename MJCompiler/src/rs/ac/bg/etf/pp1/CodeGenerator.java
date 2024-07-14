package rs.ac.bg.etf.pp1;

import java.util.ArrayList;

import rs.ac.bg.etf.pp1.ast.*;
import rs.etf.pp1.mj.runtime.Code;
import rs.etf.pp1.symboltable.concepts.Obj;
import rs.etf.pp1.symboltable.concepts.Struct;

public class CodeGenerator extends VisitorAdaptor {

	int mainPc;
	
	public int getMainPc() {
		return mainPc;
	}
	
	// general
	Obj currClass = null;
	Obj currMethod = null;
	String currNamespace = "";
	
	// designator processing
	ArrayList<Obj> currDesignator = new ArrayList<>();
	ArrayList<ArrayList<String>> designatorParts = new ArrayList<>();
	ArrayList<Boolean> currClassMethod = new ArrayList<>();
	
	// Program
	
	public void visit(ProgNamet progName) {
		currDesignator.add(null);
		designatorParts.add(new ArrayList<String>());
		currClassMethod.add(false);
	}
	
	public void visit(Programt program) {
		currDesignator.remove(currDesignator.size() - 1);
		designatorParts.remove(designatorParts.size() - 1);
		currClassMethod.remove(currClassMethod.size() - 1);
	}
	
	// Namespace
	
	public void visit(NamespaceNamet namespaceName) {
		currNamespace = namespaceName.getNamespaceName() + "::";
	}
	
	public void visit(Namespacet namespace) {
		currNamespace = "";
	}
	
	// ClassDecl
	
	public void visit(ClassNamet className) {
		currClass = className.obj;
	}
	
	public void visit(ClassDeclYes classDecl) {
		currClass = null;
	}
	
	public void visit(ClassDeclNo classDecl) {
		currClass = null;
	}
	
	// MethodDecl
	
	public void visit(MethodNamet methodName) {
		Obj methObj = methodName.obj;
		
		methObj.setAdr(Codee.pc);
		if (methObj.getName().equals("main")) {
			Codee.mainPc = Codee.pc;
		}
		
		Codee.put(Codee.enter);
		Codee.put(methObj.getLevel());
		Codee.put(methObj.getLocalSymbols().size());
		currMethod = methObj;
	}
	
	public void visit(MethodDeclYes methodDecl) {
		Codee.put(Codee.exit);
		Codee.put(Codee.return_);
		currMethod = null;
	}
	
	public void visit(MethodDeclNo methodDecl) {
		Codee.put(Codee.exit);
		Codee.put(Codee.return_);
		currMethod = null;
	}
	
	// Statement
	
	public void visit(StmtRead stmt) {
		if (stmt.getDesignator().obj.getType().equals(Tabb.charType)) Codee.put(Codee.bread);
		else if (stmt.getDesignator().obj.getType().equals(Tabb.boolType)) Codee.put(Codee.bread);
		else Codee.put(Codee.read);
		
		Codee.handleStoreDesignator(currDesignator.get(currDesignator.size() - 1));
		currDesignator.set(currDesignator.size() - 1, null);
	}
	
	public void visit(StmtPrintYes stmt) {
		Codee.loadConst(stmt.getValue());
		if (stmt.getExpr().struct.equals(Tabb.charType)) Codee.put(Codee.bprint);
		else Codee.put(Codee.print);
	}
	
	public void visit(StmtPrintNo stmt) {
		Codee.loadConst(4);
		if (stmt.getExpr().struct.equals(Tabb.charType)) Codee.put(Codee.bprint);
		else Codee.put(Codee.print);
	}
	
	// DesignatorStatement
	
	public void visit(TLParent lParen) {
		currDesignator.add(null);
		designatorParts.add(new ArrayList<String>());
		currClassMethod.add(false);
	}
	
	public void visit(TRParent rParen) {
		currDesignator.remove(currDesignator.size() - 1);
		designatorParts.remove(designatorParts.size() - 1);
		currClassMethod.remove(currClassMethod.size() - 1);
	}
	
	public void visit(OpChoiceActParsYes opChoice) {
		if (currClassMethod.get(currClassMethod.size() - 1)) {
			String name = currMethod.getName();
			int len = name.length();
			
			Codee.put(Codee.load_n);
			Codee.put(Codee.getfield);
			Codee.put2(0);

			Codee.put(Codee.invokevirtual);
			for(int i = 0; i < len; ++i) {
				Codee.put4(name.charAt(i));
			}
			Codee.put4(-1);
		}
		else {
			
		}
		
		currClassMethod.set(currClassMethod.size() - 1, false);
	}
	
	public void visit(OpChoiceActParsNo opChoice) {
		if (currClassMethod.get(currClassMethod.size() - 1)) {
			String name = currMethod.getName();
			int len = name.length();
			
			Codee.put(Codee.load_n);
			Codee.put(Codee.getfield);
			Codee.put2(0);

			Codee.put(Codee.invokevirtual);
			for(int i = 0; i < len; ++i) {
				Codee.put4(name.charAt(i));
			}
			Codee.put4(-1);
		}
		else {
			
		}
		
		currClassMethod.set(currClassMethod.size() - 1, false);
	}
	
	public void visit(OpChoiceExpr opChoice) {
		Codee.handleStoreDesignator(currDesignator.get(currDesignator.size() - 1));
		currDesignator.set(currDesignator.size() - 1, null);
	}
	
	public void visit(OpChoiceInc opChoice) {
		Codee.loadConst(1);
		Codee.put(Codee.add);
		Codee.handleStoreDesignator(currDesignator.get(currDesignator.size() - 1));
		currDesignator.set(currDesignator.size() - 1, null);
	}
	
	public void visit(OpChoiceDec opChoice) {
		Codee.loadConst(1);
		Codee.put(Codee.sub);
		Codee.handleStoreDesignator(currDesignator.get(currDesignator.size() - 1));
		currDesignator.set(currDesignator.size() - 1, null);
	}
	
	// Condition
	
	// CondTerm
	
	// CondFact
	
	// Expr
	
	public void visit(ExprYes expr) {
		if (currClass != null && currClassMethod.get(currClassMethod.size() - 1)) {
			Codee.put(Codee.dup_x1);
			Codee.put(Codee.pop);
		}
	}
	
	public void visit(ExprNo expr) {
		if (currClass != null && currClassMethod.get(currClassMethod.size() - 1)) {
			Codee.put(Codee.dup_x1);
			Codee.put(Codee.pop);
		}
	}
	
	public void visit(AddopTermListYes addopTerm) {
		SyntaxNode addopNode = addopTerm.getAddop();
		
		if (addopNode instanceof AddopPlus) Codee.put(Codee.add);
		else Codee.put(Codee.sub);
	}
	
	// Term
	
	public void visit(MulopFactorListYes mulopFactor) {
		SyntaxNode mulopNode = mulopFactor.getMulop();
		
		if (mulopNode instanceof MulopMul) Codee.put(Codee.mul);
		else if (mulopNode instanceof MulopDiv) Codee.put(Codee.div);
		else Codee.put(Codee.rem);
	}
	
	public void visit(Termt term) {
		SyntaxNode termPar = term.getParent();
		
		if (termPar instanceof ExprYes) Codee.put(Codee.neg);
	}
	
	// Factor
	
	public void visit(FactorDesignatorFirst factor) {
		if (currClassMethod.get(currClassMethod.size() - 1)) {
			String name = currMethod.getName();
			int len = name.length();
			
			Codee.put(Codee.load_n);
			Codee.put(Codee.getfield);
			Codee.put2(0);

			Codee.put(Codee.invokevirtual);
			for(int i = 0; i < len; ++i) {
				Codee.put4(name.charAt(i));
			}
			Codee.put4(-1);
		}
		else {
			
		}
		
		currClassMethod.set(currClassMethod.size() - 1, false);
	}
	
	public void visit(FactorDesignatorSecond factor) {
		if (currClassMethod.get(currClassMethod.size() - 1)) {
			String name = currMethod.getName();
			int len = name.length();
			
			Codee.put(Codee.load_n);
			Codee.put(Codee.getfield);
			Codee.put2(0);

			Codee.put(Codee.invokevirtual);
			for(int i = 0; i < len; ++i) {
				Codee.put4(name.charAt(i));
			}
			Codee.put4(-1);
		}
		else {
			
		}
		
		currClassMethod.set(currClassMethod.size() - 1, false);
	}
	
	public void visit(FactorDesignatorThird factor) {
		
	}
	
	public void visit(FactorNum factor) {
		Code.loadConst(factor.getValue());
	}
	
	public void visit(FactorChar factor) {
		Code.loadConst(factor.getValue());
	}
	
	public void visit(FactorBool factor) {
		Code.loadConst(factor.getValue() ? 1 : 0);
	}
	
	public void visit(NewChoiceExpr choice) {
		
	}
	
	public void visit(NewChoiceActParsYes choice) {
		
	}
	
	public void visit(NewChoiceActParsNo choice) {
		
	}
	
	public void visit(FactorNew factor) {
		
	}
	
	public void visit(FactorExpr factor) {
		
	}
	
	// Designator
	
	public void visit(TLBrackett lBracket) {
		currDesignator.add(null);
		designatorParts.add(new ArrayList<String>());
		currClassMethod.add(false);
	}
	
	public void visit(TRBrackett rBracket) {
		currDesignator.remove(currDesignator.size() - 1);
		designatorParts.remove(designatorParts.size() - 1);
		currClassMethod.remove(currClassMethod.size() - 1);
	}
	
	public void visit(DesignatorPartsIdent designator) {
		designatorParts.get(designatorParts.size() - 1).add(designator.getDsgName());
	}
	
	public void visit(DesignatorPartsExpr designator) {
		designatorParts.get(designatorParts.size() - 1).add("[]");
	}
	
	public void visit(DesignatorNo designator) {
		String localName = designator.getDsgName();
		String fullName = currNamespace + localName;
		
		Obj initObj = Tabb.findLocsMethod(currMethod, localName);
		if (currClass != null) {
			if (initObj == Tabb.noObj) initObj = Tabb.findLocsClass(currClass.getType(), localName);
			if (initObj.getKind() == Obj.Meth) currClassMethod.set(currClassMethod.size() - 1, true);
			if (initObj == Tabb.noObj) initObj = Tabb.findStatic(localName, currClass.getType());
		}
		if (initObj == Tabb.noObj) initObj = Tabb.find(fullName);
		if (initObj == Tabb.noObj) initObj = Tabb.find(localName);
		
		SyntaxNode dsgParent = designator.getParent();
		boolean storeDsg = (dsgParent instanceof StmtRead)
				|| (dsgParent instanceof DesignatorStmtFirst && ((DesignatorStmtFirst)dsgParent).getOpChoice() instanceof OpChoiceExpr)
				|| (dsgParent instanceof DesignatorListYesYes)
				|| (dsgParent instanceof DesignatorStmtSecond && ((DesignatorStmtSecond)dsgParent).getDesignator().equals(designator));
		boolean bothDsg = (dsgParent instanceof DesignatorStmtFirst && ((DesignatorStmtFirst)dsgParent).getOpChoice() instanceof OpChoiceInc)
				|| (dsgParent instanceof DesignatorStmtFirst && ((DesignatorStmtFirst)dsgParent).getOpChoice() instanceof OpChoiceDec); 
		
		ArrayList<String> elems = designatorParts.get(designatorParts.size() - 1);
		Struct initType = initObj.getType();
		int len = elems.size();
		
		if (storeDsg || bothDsg) {
			Obj dsgObj = initObj;
			Struct elemType = initType;
			if (designatorParts.get(designatorParts.size() - 1).isEmpty()) {
				if (dsgObj.getKind() == Obj.Fld) Codee.put(Codee.load_n);
				currDesignator.set(currDesignator.size() - 1, dsgObj);
			}
			else {
				Codee.handleLoadDesignator(dsgObj, currClassMethod.get(currClassMethod.size() - 1), true);
				for (int i = 0; i < len; ++i) {
					String elem = elems.get(i);
					if (elem.equals("[]")) {
						elemType = elemType.getElemType();
						dsgObj = new Obj(Obj.Elem, "", elemType);
					}
					else if (dsgObj.getKind() == Obj.Type) {
						dsgObj = Tabb.findExactStatic(dsgObj.getName() + elem);
						elemType = dsgObj.getType();
					}
					else {
						dsgObj = Tabb.findLocsClass(elemType, localName);
						if (dsgObj.getKind() == Obj.Meth) currClassMethod.set(currClassMethod.size() - 1, true);
						elemType = dsgObj.getType();
					}
				
					if (i == len - 1) {
						if (dsgObj.getKind() == Obj.Fld) Codee.put(Codee.load_n);
						currDesignator.set(currDesignator.size() - 1, dsgObj);
					}
					else {
						Codee.handleLoadDesignator(dsgObj, currClassMethod.get(currClassMethod.size() - 1), false);
					}
				}
			}
		}
		if (!storeDsg || bothDsg) {
			Obj dsgObj = initObj;
			Struct elemType = initType;
			Codee.handleLoadDesignator(dsgObj, currClassMethod.get(currClassMethod.size() - 1), true);
			for (String elem: elems) {
				if (elem.equals("[]")) {
					elemType = elemType.getElemType();
					dsgObj = new Obj(Obj.Elem, "", elemType);
				}
				else if (dsgObj.getKind() == Obj.Type) {
					dsgObj = Tabb.findExactStatic(dsgObj.getName() + elem);
					elemType = dsgObj.getType();
				}
				else {
					dsgObj = Tabb.findLocsClass(elemType, localName);
					if (dsgObj.getKind() == Obj.Meth) currClassMethod.set(currClassMethod.size() - 1, true);
					elemType = dsgObj.getType();
				}
				Codee.handleLoadDesignator(dsgObj, currClassMethod.get(currClassMethod.size() - 1), false);
			}
		}
		
		designatorParts.get(designatorParts.size() - 1).clear();
	}
	
}
