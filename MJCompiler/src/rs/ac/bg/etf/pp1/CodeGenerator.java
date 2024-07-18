package rs.ac.bg.etf.pp1;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Stack;

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
	ArrayList<Obj> currCalledMethod = new ArrayList<>();
	
	// hard designator statement processing
	int dsgStmtCounter = 0;
	HashSet<Integer> dsgStmtSet = new HashSet<>();
	
	// condition processing
	public static class IfElseFixups {
		public static final int RPAREN = 1;
		public static final int AND = 2;
		public static final int OR = 3;
		public static final int IF = 4;
		
		ArrayList<Integer> fixupsRParen = new ArrayList<>();
		ArrayList<Integer> fixupsAndNotReady = new ArrayList<>();
		ArrayList<Integer> fixupsAnd = new ArrayList<>();
		ArrayList<Integer> fixupsOr = new ArrayList<>();
		ArrayList<Integer> fixupsIf = new ArrayList<>();
		
		public void processAndClear(int kind) {
			if (kind == RPAREN) {
				fixupsRParen.forEach(Codee::fixup);
				fixupsRParen.clear();
			}
			else if (kind == AND) {
				fixupsAnd.forEach(Codee::fixup);
				fixupsAnd.clear();
			}
			else if (kind == OR) {
				fixupsOr.forEach(Codee::fixup);
				fixupsOr.clear();
			}
			else {
				fixupsIf.forEach(Codee::fixup);
				fixupsIf.clear();
			}
		}
		
		public void transferToAnd() {
			fixupsAndNotReady.forEach(fixupsAnd::add);
			fixupsAndNotReady.clear();
		}
	}
	
	Stack<IfElseFixups> ifElseStack = new Stack<>(); 
	
	public void handleCondTermIf(SyntaxNode condTerm, int relopKind) {
		// before ")"	-->		false: jump on else/after
		// before "&&"	-->		false: jump on next (else/after or condition)
		// before "||"	-->		true: jump on then
		SyntaxNode parent = ((CondTermt)condTerm).getParent();
		
		if (parent instanceof Conditiont) {
			SyntaxNode child = ((Conditiont)parent).getCondTermList();
			
			if (child instanceof CondTermListYes) {
				// condition for "||"
				Codee.putTrueJump(relopKind, 0);
				ifElseStack.peek().fixupsOr.add(Codee.pc - 2);
			}
			else {
				// condition for ")"
				Codee.putFalseJump(relopKind, 0);
				ifElseStack.peek().fixupsRParen.add(Codee.pc - 2);
			}
		}
		else {
			SyntaxNode gParent = ((CondTermListYes)parent).getParent();
			
			if (gParent instanceof CondTermListYes) {
				// condition for "||"
				Codee.putTrueJump(relopKind, 0);
				ifElseStack.peek().fixupsOr.add(Codee.pc - 2);
			}
			else {
				// condition for ")"
				Codee.putFalseJump(relopKind, 0);
				ifElseStack.peek().fixupsRParen.add(Codee.pc - 2);
			}
		}
	}

	public void handleCondFactIf(SyntaxNode condFact, int relopKind) {
		// before ")"	-->		false: jump on else/after
		// before "&&"	-->		false: jump on next (else/after or condition)
		// before "||"	-->		true: jump on then
		SyntaxNode parent = condFact.getParent();
		
		if (parent instanceof CondTermt) {
			SyntaxNode child = ((CondTermt)parent).getCondFactList();
			
			if (child instanceof CondFactListYes) {
				// condition for "&&"
				Codee.putFalseJump(relopKind, 0);
				ifElseStack.peek().fixupsAndNotReady.add(Codee.pc - 2);
			}
			else {
				// condition for ")" or "||"
				// parent is CondTerm
				handleCondTermIf(parent, relopKind);
			}
		}
		else {
			SyntaxNode gParent = ((CondFactListYes)parent).getParent();
			
			if (gParent instanceof CondFactListYes) {
				// condition for "&&"
				Codee.putFalseJump(relopKind, 0);
				ifElseStack.peek().fixupsAndNotReady.add(Codee.pc - 2);
			}
			else {
				// condition for ")" or "||"
				// gParent is CondTerm
				handleCondTermIf(gParent, relopKind);
			}
		}
	}

	// for loop processing

	public static class ForFixups {
		boolean existsCond = false;
		int pcPost = -1;
		int pcCond = -1;
		
		public static final int CTRUE = 1;
		public static final int CFALSE = 2;
		public static final int BREAK = 3;

		ArrayList<Integer> fixupsCondTrue = new ArrayList<>();
		ArrayList<Integer> fixupsCondFalse = new ArrayList<>();
		ArrayList<Integer> fixupsBreak = new ArrayList<>();

		public void processAndClear(int kind) {
			if (kind == CTRUE) {
				fixupsCondTrue.forEach(Codee::fixup);
				fixupsCondTrue.clear();
			}
			else if (kind == CFALSE) {
				fixupsCondFalse.forEach(Codee::fixup);
				fixupsCondFalse.clear();
			}
			else {
				fixupsBreak.forEach(Codee::fixup);
				fixupsBreak.clear();
			}
		}
	}

	Stack<ForFixups> forStack = new Stack<>();
	
	public void handleCondFactFor(SyntaxNode condFact, int relopKind) {
		forStack.peek().existsCond = true;
		Codee.putFalseJump(relopKind, 0);
		forStack.peek().fixupsCondFalse.add(Codee.pc - 2);
		Codee.putTrueJump(relopKind, 0);
		forStack.peek().fixupsCondTrue.add(Codee.pc - 2);
	}
	
	// Program
	
	public void visit(ProgNamet progName) {
		designatorParts.add(new ArrayList<String>());
	}
	
	public void visit(Programt program) {
		designatorParts.remove(designatorParts.size() - 1);
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

	public void visit(StmtBreak stmt) {
		Codee.putJump(0);
		forStack.peek().fixupsBreak.add(Codee.pc - 2);
	}
	
	public void visit(StmtContinue stmt) {
		Codee.putJump(forStack.peek().pcPost);
	}
	
	public void visit(ForSymbolt forSymbol) {
		forStack.push(new ForFixups());
	}
	
	public void visit(FirstSemit semi) {
		forStack.peek().pcCond = Codee.pc;
	}
	
	public void visit(SecondSemit semi) {
		if (!forStack.peek().existsCond) {
			Codee.putJump(0);
			forStack.peek().fixupsCondTrue.add(Codee.pc - 2);
		}
		
		forStack.peek().pcPost = Codee.pc;
	}

	public void visit(RParenFort rParen) {
		Codee.putJump(forStack.peek().pcCond);
		
		// start of body branch
		forStack.peek().processAndClear(ForFixups.CTRUE);
	}
	
	public void visit(StmtForYesYesYes stmt) {
		Codee.putJump(forStack.peek().pcPost);
		
		// start of after branch
		forStack.peek().processAndClear(ForFixups.CFALSE);
		forStack.peek().processAndClear(ForFixups.BREAK);
		
		forStack.pop();
	}
	
	public void visit(StmtForYesYesNo stmt) {
		Codee.putJump(forStack.peek().pcPost);
		
		// start of after branch
		forStack.peek().processAndClear(ForFixups.CFALSE);
		forStack.peek().processAndClear(ForFixups.BREAK);
		
		forStack.pop();
	}
	
	public void visit(StmtForYesNoYes stmt) {
		Codee.putJump(forStack.peek().pcPost);
		
		// start of after branch
		forStack.peek().processAndClear(ForFixups.CFALSE);
		forStack.peek().processAndClear(ForFixups.BREAK);
		
		forStack.pop();
	}
	
	public void visit(StmtForYesNoNo stmt) {
		Codee.putJump(forStack.peek().pcPost);
		
		// start of after branch
		forStack.peek().processAndClear(ForFixups.CFALSE);
		forStack.peek().processAndClear(ForFixups.BREAK);
		
		forStack.pop();
	}
	
	public void visit(StmtForNoYesYes stmt) {
		Codee.putJump(forStack.peek().pcPost);
		
		// start of after branch
		forStack.peek().processAndClear(ForFixups.CFALSE);
		forStack.peek().processAndClear(ForFixups.BREAK);
		
		forStack.pop();
	}
	
	public void visit(StmtForNoYesNo stmt) {
		Codee.putJump(forStack.peek().pcPost);
		
		// start of after branch
		forStack.peek().processAndClear(ForFixups.CFALSE);
		forStack.peek().processAndClear(ForFixups.BREAK);
		
		forStack.pop();
	}
	
	public void visit(StmtForNoNoYes stmt) {
		Codee.putJump(forStack.peek().pcPost);
		
		// start of after branch
		forStack.peek().processAndClear(ForFixups.CFALSE);
		forStack.peek().processAndClear(ForFixups.BREAK);
		
		forStack.pop();
	}
	
	public void visit(StmtForNoNoNo stmt) {
		Codee.putJump(forStack.peek().pcPost);
		
		// start of after branch
		forStack.peek().processAndClear(ForFixups.CFALSE);
		forStack.peek().processAndClear(ForFixups.BREAK);
		
		forStack.pop();
	}

	public void visit(StmtRead stmt) {
		if (stmt.getDesignator().obj.getType().equals(Tabb.charType)) Codee.put(Codee.bread);
		else if (stmt.getDesignator().obj.getType().equals(Tabb.boolType)) Codee.put(Codee.bread);
		else Codee.put(Codee.read);
		
		Codee.handleStoreDesignator(currDesignator.get(currDesignator.size() - 1));
		currDesignator.remove(currDesignator.size() - 1);
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
		designatorParts.add(new ArrayList<String>());
	}
	
	public void visit(TRParent rParen) {
		designatorParts.remove(designatorParts.size() - 1);
	}
	
	public void visit(OpChoiceActParsYes opChoice) {
		if (!currCalledMethod.isEmpty() && currCalledMethod.get(currCalledMethod.size() - 1).getFpPos() > 0) {
			String name = currMethod.getName();
			int len = name.length();
			
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
		
		currCalledMethod.remove(currCalledMethod.size() - 1);
	}
	
	public void visit(OpChoiceActParsNo opChoice) {
		if (!currCalledMethod.isEmpty() && currCalledMethod.get(currCalledMethod.size() - 1).getFpPos() > 0) {
			String name = currMethod.getName();
			int len = name.length();
			
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
		
		currCalledMethod.remove(currCalledMethod.size() - 1);
	}
	
	public void visit(OpChoiceExpr opChoice) {
		Codee.handleStoreDesignator(currDesignator.get(currDesignator.size() - 1));
		currDesignator.remove(currDesignator.size() - 1);
	}
	
	public void visit(OpChoiceInc opChoice) {
		Codee.loadConst(1);
		Codee.put(Codee.add);
		Codee.handleStoreDesignator(currDesignator.get(currDesignator.size() - 1));
		currDesignator.remove(currDesignator.size() - 1);
	}
	
	public void visit(OpChoiceDec opChoice) {
		Codee.loadConst(1);
		Codee.put(Codee.sub);
		Codee.handleStoreDesignator(currDesignator.get(currDesignator.size() - 1));
		currDesignator.remove(currDesignator.size() - 1);
	}
	
	public void visit(DesignatorListYesYes dsgList) {
		dsgStmtSet.add(dsgStmtCounter++);
	}
	
	public void visit(DesignatorListYesNo dsgList) {
		++dsgStmtCounter;
	}
	
	public void visit(DesignatorStmtSecond dsgStmt) {
		// handle errors
		int lenLeft = dsgStmt.getDesignator().obj.getFpPos();
		int lenRight = dsgStmt.getDesignator1().obj.getFpPos();
		Struct dsgLeftType = dsgStmt.getDesignator().obj.getType().getElemType();
		Struct dsgRightType = dsgStmt.getDesignator1().obj.getType().getElemType();
		int rightInstr = (!dsgRightType.equals(Tabb.charType) && !dsgRightType.equals(Tabb.boolType)) ? Codee.aload : Codee.baload;
		int leftInstr = (!dsgLeftType.equals(Tabb.charType) && !dsgLeftType.equals(Tabb.boolType)) ? Codee.astore : Codee.bastore;
		
		if ((lenRight - dsgStmtCounter <= 0) || (lenLeft - lenRight + dsgStmtCounter < 0)) {
			Codee.put(Codee.trap);
			Codee.put(1);
			return;
		}
		
		// handle array assignment
		int cnt = -1;
		while (lenRight >= dsgStmtCounter + (++cnt) + 1) {
			if (lenRight > dsgStmtCounter + cnt + 1) Codee.put(Codee.dup2);
			else Codee.put(Codee.dup_x1);
			Codee.loadConst(dsgStmtCounter + cnt);
			Codee.loadConst(cnt);
			Codee.put(Codee.dup_x2);
			Codee.put(Codee.pop);
			Codee.put(rightInstr);
			Codee.put(leftInstr);
		}
		currDesignator.remove(currDesignator.size() - 1);
		
		// handle variables assignment
		while (--dsgStmtCounter >= 0) {
			if (dsgStmtSet.contains(dsgStmtCounter)) {
				if (dsgStmtSet.size() > 1) Codee.put(Codee.dup_x1);
				Codee.loadConst(dsgStmtCounter);
				Codee.put(rightInstr);
				Codee.handleStoreDesignator(currDesignator.get(currDesignator.size() - 1));
				dsgStmtSet.remove(dsgStmtCounter);
				currDesignator.remove(currDesignator.size() - 1);
			}
		}
		
		dsgStmtCounter = 0;
	}
	
	// Condition
	
	public void visit(IfSymbolt ifSymbol) {
		ifElseStack.push(new IfElseFixups());
	}
	
	public void visit(Conditiont condition) {
		// start of then branch
		ifElseStack.peek().transferToAnd();
		ifElseStack.peek().processAndClear(IfElseFixups.OR);
	}
	
	public void visit(ElseSymbolt elseSymbol) {
		Codee.putJump(0);
		ifElseStack.peek().fixupsIf.add(Codee.pc - 2);
		
		// start of else branch
		ifElseStack.peek().processAndClear(IfElseFixups.RPAREN);
		ifElseStack.peek().processAndClear(IfElseFixups.AND);
	}
	
	public void visit(StmtIfElse stmt) {
		// start of after branch
		ifElseStack.peek().processAndClear(IfElseFixups.RPAREN);
		ifElseStack.peek().processAndClear(IfElseFixups.AND);
		ifElseStack.peek().processAndClear(IfElseFixups.IF);
		ifElseStack.pop();
	}
	
	// CondTerm
	
	public void visit(CondTermt condTerm) {
		ifElseStack.peek().processAndClear(IfElseFixups.AND);
		ifElseStack.peek().transferToAnd();
	}
	
	// CondFact

	public void visit(CondFactYes condFact) {
		SyntaxNode relopNode = condFact.getRelop();
		int relopKind = -1;
		
		if (relopNode instanceof RelopEqual) relopKind = Codee.eq;
		else if (relopNode instanceof RelopNotEqual) relopKind = Codee.ne;
		else if (relopNode instanceof RelopGrt) relopKind = Codee.gt;
		else if (relopNode instanceof RelopGrtEqual) relopKind = Codee.ge;
		else if (relopNode instanceof RelopLess) relopKind = Codee.lt;
		else relopKind = Codee.le;
		
		SyntaxNode parent = condFact.getParent();
		boolean ind = (parent instanceof StmtForYesYesYes) 
				|| (parent instanceof StmtForYesYesNo) 
				|| (parent instanceof StmtForYesNoYes) 
				|| (parent instanceof StmtForYesNoNo);
		
		if (ind) handleCondFactFor(condFact, relopKind);
		else handleCondFactIf(condFact, relopKind);
	}
	
	public void visit(CondFactNo condFact) {
		Codee.loadConst(1);
		int relopKind = Codee.eq;
		
		SyntaxNode parent = condFact.getParent();
		boolean ind = (parent instanceof StmtForYesYesYes) 
				|| (parent instanceof StmtForYesYesNo) 
				|| (parent instanceof StmtForYesNoYes) 
				|| (parent instanceof StmtForYesNoNo);
		
		if (ind) handleCondFactFor(condFact, relopKind);
		else handleCondFactIf(condFact, relopKind);
	}

	// Expr
	
	public void visit(ExprYes expr) {
		boolean cond1 = !currCalledMethod.isEmpty() && currCalledMethod.get(currCalledMethod.size() - 1).getFpPos() > 0;
		boolean cond2 = (expr.getParent() instanceof ActParsTempList) || (expr.getParent() instanceof ActParsTempSingle);
		if (cond1 && cond2) {
			Codee.put(Codee.dup_x1);
			Codee.put(Codee.pop);
		}
	}
	
	public void visit(ExprNo expr) {
		boolean cond1 = !currCalledMethod.isEmpty() && currCalledMethod.get(currCalledMethod.size() - 1).getFpPos() > 0;
		boolean cond2 = (expr.getParent() instanceof ActParsTempList) || (expr.getParent() instanceof ActParsTempSingle);
		if (cond1 && cond2) {
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
		if (!currCalledMethod.isEmpty() && currCalledMethod.get(currCalledMethod.size() - 1).getFpPos() > 0) {
			String name = currMethod.getName();
			int len = name.length();
			
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
		
		currCalledMethod.remove(currCalledMethod.size() - 1);
	}
	
	public void visit(FactorDesignatorSecond factor) {
		if (!currCalledMethod.isEmpty() && currCalledMethod.get(currCalledMethod.size() - 1).getFpPos() > 0) {
			String name = currMethod.getName();
			int len = name.length();
			
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
		
		currCalledMethod.remove(currCalledMethod.size() - 1);
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
	
	public void visit(FactorNew factor) {
		Struct type = factor.getType().struct;
		SyntaxNode newChoiceNode = factor.getNewChoice();
		
		if (newChoiceNode instanceof NewChoiceExpr) {
			Codee.put(Codee.newarray);
			if (!type.equals(Tabb.charType) && !type.equals(Tabb.boolType)) Codee.put(1);
			else Codee.put(0);
		}
		else {
			Codee.put(Codee.new_);
			Codee.put2(4 * (type.getNumberOfFields() + 1));
		}
	}
	
	// Designator
	
	public void visit(TLBrackett lBracket) {
		designatorParts.add(new ArrayList<String>());
	}
	
	public void visit(TRBrackett rBracket) {
		designatorParts.remove(designatorParts.size() - 1);
	}
	
	public void visit(DesignatorPartsIdent designator) {
		designatorParts.get(designatorParts.size() - 1).add(designator.getDsgName());
	}
	
	public void visit(DesignatorPartsExpr designator) {
		designatorParts.get(designatorParts.size() - 1).add("[]");
	}
	
	public void visit(DesignatorYes designator) {
		Obj nspObj = Tabb.find(designator.getNspName());
		String fullName = nspObj.getName() + "::" + designator.getDsgName();
		Obj initObj = Tabb.find(fullName);
		
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
			currDesignator.add(null);
			if (designatorParts.get(designatorParts.size() - 1).isEmpty()) {
				if (dsgObj.getKind() == Obj.Fld) Codee.put(Codee.load_n);
				currDesignator.set(currDesignator.size() - 1, dsgObj);
			}
			else {
				Codee.handleLoadDesignator(dsgObj, currCalledMethod, true);
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
						dsgObj = Tabb.findLocsClass(elemType, elem);
						elemType = dsgObj.getType();
					}
				
					if (i == len - 1) {
						if (dsgObj.getKind() == Obj.Fld) Codee.put(Codee.load_n);
						currDesignator.set(currDesignator.size() - 1, dsgObj);
					}
					else {
						Codee.handleLoadDesignator(dsgObj, currCalledMethod, false);
					}
				}
			}
		}
		if (!storeDsg || bothDsg) {
			Obj dsgObj = initObj;
			Struct elemType = initType;
			Codee.handleLoadDesignator(dsgObj, currCalledMethod, true);
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
					dsgObj = Tabb.findLocsClass(elemType, elem);
					elemType = dsgObj.getType();
				}
				Codee.handleLoadDesignator(dsgObj, currCalledMethod, false);
			}
		}
		
		designatorParts.get(designatorParts.size() - 1).clear();
	}
	
	public void visit(DesignatorNo designator) {
		String localName = designator.getDsgName();
		String fullName = currNamespace + localName;
		
		Obj initObj = Tabb.findLocsMethod(currMethod, localName);
		if (currClass != null) {
			if (initObj == Tabb.noObj) initObj = Tabb.findLocsClass(currClass.getType(), localName);
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
			currDesignator.add(null);
			if (designatorParts.get(designatorParts.size() - 1).isEmpty()) {
				if (dsgObj.getKind() == Obj.Fld) Codee.put(Codee.load_n);
				currDesignator.set(currDesignator.size() - 1, dsgObj);
			}
			else {
				Codee.handleLoadDesignator(dsgObj, currCalledMethod, true);
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
						dsgObj = Tabb.findLocsClass(elemType, elem);
						elemType = dsgObj.getType();
					}
				
					if (i == len - 1) {
						if (dsgObj.getKind() == Obj.Fld) Codee.put(Codee.load_n);
						currDesignator.set(currDesignator.size() - 1, dsgObj);
					}
					else {
						Codee.handleLoadDesignator(dsgObj, currCalledMethod, false);
					}
				}
			}
		}
		if (!storeDsg || bothDsg) {
			Obj dsgObj = initObj;
			Struct elemType = initType;
			Codee.handleLoadDesignator(dsgObj, currCalledMethod, true);
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
					dsgObj = Tabb.findLocsClass(elemType, elem);
					elemType = dsgObj.getType();
				}
				Codee.handleLoadDesignator(dsgObj, currCalledMethod, false);
			}
		}
		
		designatorParts.get(designatorParts.size() - 1).clear();
	}

}
