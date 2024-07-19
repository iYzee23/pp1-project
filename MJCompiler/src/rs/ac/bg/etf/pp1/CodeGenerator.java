package rs.ac.bg.etf.pp1;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map.Entry;
import java.util.Stack;

import rs.ac.bg.etf.pp1.ast.*;
import rs.etf.pp1.mj.runtime.Code;
import rs.etf.pp1.symboltable.concepts.Obj;
import rs.etf.pp1.symboltable.concepts.Struct;

public class CodeGenerator extends VisitorAdaptor {

	int mainPc;
	int globData;
	
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
	
	// NewType() processing
	Obj latestNew = null;
	HashMap<Obj, HashMap<Integer, Obj>> methodNews = new HashMap<>();
	
	public void handleNewExpr(SyntaxNode expr) {
		SyntaxNode parent = expr.getParent();
		
		if (parent instanceof CondFactYes || parent instanceof StmtPrintYes || parent instanceof StmtPrintNo) {
			latestNew = null;
		}
		else if (parent instanceof ActParsTempSingle) {
			Obj methObj = currCalledMethod.get(currCalledMethod.size() - 1);
			HashMap<Integer, Obj> currHM = methodNews.get(methObj);
			if (currHM == null) methodNews.put(methObj, currHM = new HashMap<>());
			currHM.put(0, latestNew);
			latestNew = null;
		}
		else if (parent instanceof ActParsTempList) {
			int cnt = 0;
			while (parent instanceof ActParsTempList) {
				++cnt;
				parent = parent.getParent();
			}
			
			Obj methObj = currCalledMethod.get(currCalledMethod.size() - 1);
			HashMap<Integer, Obj> currHM = methodNews.get(methObj);
			if (currHM == null) methodNews.put(methObj, currHM = new HashMap<>());
			currHM.put(methObj.getLevel() - cnt, latestNew);
			latestNew = null;
		}
		else if (parent instanceof OpChoiceExpr) {
			Obj currDsg = currDesignator.get(currDesignator.size() - 1); 
			if (currDsg != null && currDsg.getKind() == Obj.Fld) {
				/*
				Codee.put(Codee.dup2);
				Codee.put(Codee.pop);
				Codee.load(currDsg);
				Codee.put(Codee.dup_x2);
				Codee.put(Codee.pop);
				*/
				Codee.put(Codee.dup);
				Codee.load(currDsg);
				Codee.put(Codee.dup_x1);
				Codee.put(Codee.pop);
			}
			else if (currDsg != null && currDsg.getKind() == Obj.Elem) {
				Codee.put(Codee.dup2);
			}
		}
	}
	
	// Condition processing
	
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

	// ForLoop processing
	
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
	
	// TVFS initialization
	
	public void initTvfs() {
		Collection<Obj> symbs = Tabb.programScope.values();
		for (Obj uType: symbs) {
			if (uType.getKind() != Obj.Type) continue;
			
			uType.setAdr(globData);
			Collection<Obj> locals = uType.getLocalSymbols(); 
			for (Obj local: locals) {
				if (local.getKind() != Obj.Meth) continue;
				
				// load name
				String name = local.getName();
				for (char ch: name.toCharArray()) {
					Codee.loadConst(ch);
					Codee.put(Codee.putstatic);
					Codee.put2(globData++);
				}
				
				// load -1 for name end
				Codee.loadConst(-1);
				Codee.put(Codee.putstatic);
				Codee.put2(globData++);
				
				// load method address
				Codee.loadConst(local.getAdr());
				Codee.put(Codee.putstatic);
				Codee.put2(globData++);
			}
			
			// load -2 for TVF end
			Codee.loadConst(-2);
			Codee.put(Codee.putstatic);
			Codee.put2(globData++);
		}
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
		
		
		if (methObj.getName().equals("main")) {
			initTvfs();
			Codee.mainPc = Codee.pc;
		}
		methObj.setAdr(Codee.pc);
		
		Codee.put(Codee.enter);
		Codee.put(methObj.getLevel());
		Codee.put(methObj.getLocalSymbols().size());
		
		HashMap<Integer, Obj> currHM = methodNews.get(methObj);
		if (currHM != null) {
			for (Entry<Integer, Obj> entry: currHM.entrySet()) {
				Integer param = entry.getKey();
				Obj mObj = entry.getValue();
				
				if (0 <= param && param <= 3) {
					Codee.put(Codee.load_n + param);
				}
				else {
					Codee.put(Codee.load); 
					Codee.put(param);
				}
				Codee.loadConst(mObj.getAdr());
				Codee.put(Codee.putfield);
				Codee.put2(0);
			}
			
			methodNews.remove(methObj);
		}
		
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
		Obj methObj = currCalledMethod.get(currCalledMethod.size() - 1);
		
		if (methObj.getFpPos() > 0) {
			Codee.put(Codee.getfield);
			Codee.put2(0);

			String name = methObj.getName();
			Codee.put(Codee.invokevirtual);
			for(char ch: name.toCharArray()) {
				Codee.put4(ch);
			}
			Codee.put4(-1);
			
			Obj dsgObj = ((DesignatorStmtFirst)opChoice.getParent()).getDesignator().obj;
			if (!dsgObj.getType().equals(Tabb.noType)) {
				Codee.put(Codee.pop);
			}
		}
		else {
			Codee.put(Codee.call);
			Codee.put2(methObj.getAdr() - Codee.pc);
		}
		
		currCalledMethod.remove(currCalledMethod.size() - 1);
	}
	
	public void visit(OpChoiceActParsNo opChoice) {
		Obj methObj = currCalledMethod.get(currCalledMethod.size() - 1);
		
		if (methObj.getFpPos() > 0) {
			Codee.put(Codee.getfield);
			Codee.put2(0);

			String name = methObj.getName();
			Codee.put(Codee.invokevirtual);
			for(char ch: name.toCharArray()) {
				Codee.put4(ch);
			}
			Codee.put4(-1);
			
			Obj dsgObj = ((DesignatorStmtFirst)opChoice.getParent()).getDesignator().obj;
			if (!dsgObj.getType().equals(Tabb.noType)) {
				Codee.put(Codee.pop);
			}
		}
		else {
			Codee.put(Codee.call);
			Codee.put2(methObj.getAdr() - Codee.pc);
		}
		
		currCalledMethod.remove(currCalledMethod.size() - 1);
	}
	
	public void visit(OpChoiceExpr opChoice) {
		Codee.handleStoreDesignator(currDesignator.get(currDesignator.size() - 1));
		
		if (latestNew != null) {
			Codee.handleNewDesignator(currDesignator.get(currDesignator.size() - 1), latestNew);
			latestNew = null;
		}
		
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
		SyntaxNode parent = expr.getParent();
		boolean cond1 = !currCalledMethod.isEmpty() && currCalledMethod.get(currCalledMethod.size() - 1).getFpPos() > 0;
		boolean cond2 = (parent instanceof ActParsTempList) || (parent instanceof ActParsTempSingle);
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
		Obj methObj = currCalledMethod.get(currCalledMethod.size() - 1); 
		
		if (methObj.getFpPos() > 0) {
			Codee.put(Codee.getfield);
			Codee.put2(0);

			String name = methObj.getName();
			Codee.put(Codee.invokevirtual);
			for(char ch: name.toCharArray()) {
				Codee.put4(ch);
			}
			Codee.put4(-1);
		}
		else {
			Codee.put(Codee.call);
			Codee.put2(methObj.getAdr() - Codee.pc);
		}
		
		currCalledMethod.remove(currCalledMethod.size() - 1);
	}
	
	public void visit(FactorDesignatorSecond factor) {
		Obj methObj = currCalledMethod.get(currCalledMethod.size() - 1); 
		
		if (methObj.getFpPos() > 0) {
			Codee.put(Codee.getfield);
			Codee.put2(0);

			String name = methObj.getName();
			Codee.put(Codee.invokevirtual);
			for(char ch: name.toCharArray()) {
				Codee.put4(ch);
			}
			Codee.put4(-1);
		}
		else {
			Codee.put(Codee.call);
			Codee.put2(methObj.getAdr() - Codee.pc);
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
			latestNew = Tabb.findObjForType(type);
			handleNewExpr(factor.getParent().getParent());
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
