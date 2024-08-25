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
	static int numTemp = 6;
	
	// general
	Obj currClass = null;
	Obj currMethod = null;
	String currNamespace = "";
	
	// designator processing
	public static final int LOAD_DSG = 1;
	public static final int STORE_DSG = 2;
	public static final int BOTH_DSG = 3;
	ArrayList<Obj> currStoreDesignator = new ArrayList<>();
	ArrayList<Obj> currDsgObj = new ArrayList<>();
	ArrayList<Integer> currDsgType = new ArrayList<>();
	ArrayList<Obj> currCalledMethod = new ArrayList<>();
	
	// hard designator statement processing
	int dsgStmtCounter = 0;
	HashSet<Integer> dsgStmtSet = new HashSet<>();
	
	// NewType() processing
	Obj latestNewType = null;
	HashMap<Obj, HashMap<Integer, Obj>> methodNewTypes = new HashMap<>();
	
	public void handleNewTypeExpr(SyntaxNode expr) {
		SyntaxNode parent = expr.getParent();
		
		if (parent instanceof CondFactYes || parent instanceof StmtPrintYes || parent instanceof StmtPrintNo) {
			latestNewType = null;
		}
		else if (parent instanceof ActParsTempSingle) {
			Obj methObj = currCalledMethod.get(currCalledMethod.size() - 1);
			HashMap<Integer, Obj> currHM = methodNewTypes.get(methObj);
			if (currHM == null) methodNewTypes.put(methObj, currHM = new HashMap<>());
			currHM.put(0, latestNewType);
			latestNewType = null;
		}
		else if (parent instanceof ActParsTempList) {
			int cnt = 0;
			while (parent instanceof ActParsTempList) {
				++cnt;
				parent = parent.getParent();
			}
			
			Obj methObj = currCalledMethod.get(currCalledMethod.size() - 1);
			HashMap<Integer, Obj> currHM = methodNewTypes.get(methObj);
			if (currHM == null) methodNewTypes.put(methObj, currHM = new HashMap<>());
			currHM.put(methObj.getLevel() - cnt, latestNewType);
			latestNewType = null;
		}
		else if (parent instanceof OpChoiceExpr) {
			Obj currDsg = currStoreDesignator.get(currStoreDesignator.size() - 1); 
			if (currDsg != null && currDsg.getKind() == Obj.Fld) {
				// Codee.put(Codee.dup);
				Codee.put(Codee.dup_x1);
				Codee.put(Codee.pop);
				Codee.put(Codee.dup_x1);
				Codee.put(Codee.dup_x1);
				Codee.put(Codee.pop);
			}
			else if (currDsg != null && currDsg.getKind() == Obj.Elem) {
				// Codee.put(Codee.dup2);
				Codee.put(Codee.dup_x2);
				Codee.put(Codee.pop);
				Codee.put(Codee.dup_x2);
				Codee.put(Codee.pop);
				Codee.put(Codee.dup_x2);
				Codee.put(Codee.dup_x2);
				Codee.put(Codee.pop);
				Codee.put(Codee.dup_x1);
				Codee.put(Codee.pop);
				Codee.put(Codee.dup_x2);
				Codee.put(Codee.dup_x1);
				Codee.put(Codee.pop);
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
		
		Codee.put(Codee.dup2);
		Codee.putFalseJump(relopKind, 0);
		forStack.peek().fixupsCondFalse.add(Codee.pc - 2);
		
		Codee.put(Codee.dup2);
		Codee.putTrueJump(relopKind, 0);
		forStack.peek().fixupsCondTrue.add(Codee.pc - 2);
	}
	
	// TVFS and std methods initialization
	public static boolean tvfInitDone = false;
	public static HashMap<Integer, Obj> fixupsTvf = new HashMap<>();
	public static HashSet<Obj> visitedMethods = new HashSet<>();

	boolean newStatInit = true;
	int firstStatInit = -1;
	int lastStatFixup = -1;
	
	public void addressMethod(Obj methObj, Struct type) {
		type = type.getElemType();
		while (type != null) {
			Obj superMethObj = type.getMembersTable().searchKey(methObj.getName());
			if (visitedMethods.contains(superMethObj)) {
				methObj.setAdr(superMethObj.getAdr());
				visitedMethods.add(methObj);
				return;
			}
			type = type.getElemType();
		}
	}
	
	public void initTvfs() {
		Collection<Obj> symbs = Tabb.programScope.values();
		for (Obj uType: symbs) {
			if (uType.getKind() != Obj.Type) continue;
			
			uType.setAdr(globData);
			Collection<Obj> locals = uType.getType().getMembers(); 
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
				
				// process method if it's not visited
				if (!visitedMethods.contains(local)) {
					addressMethod(local, uType.getType());
				}
				
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
		
		for (Entry<Integer, Obj> entry: fixupsTvf.entrySet()) {
	 		Integer addr = entry.getKey();
	 		Obj tvf = entry.getValue();
	 		Codee.fixupTvf(addr, tvf);
	 	}
		
		if (firstStatInit != -1) {
			Codee.putJump(firstStatInit);
		}
		
		fixupsTvf.clear();
		visitedMethods.clear();
		tvfInitDone = true;
	}
	
	public void initStdMethods() {
		Obj ordObj = Tabb.programScope.getOuter().getLocals().searchKey("ord");
		ordObj.setAdr(Codee.pc);
		
		Codee.put(Codee.enter); Code.put(1); Codee.put(1);
		Codee.put(Codee.load_n);
		Codee.put(Codee.exit);
		Codee.put(Codee.return_);
		
		Obj chrObj = Tabb.programScope.getOuter().getLocals().searchKey("chr");
		chrObj.setAdr(Codee.pc);
		
		Codee.put(Codee.enter); Code.put(1); Codee.put(1);
		Codee.put(Codee.load_n);
		Codee.put(Codee.exit);
		Codee.put(Codee.return_);
		
		Obj lenObj = Tabb.programScope.getOuter().getLocals().searchKey("len");
		lenObj.setAdr(Codee.pc);
		
		Codee.put(Codee.enter); Code.put(1); Codee.put(1);
		Codee.put(Codee.load_n);
		Codee.put(Codee.arraylength);
		Codee.put(Codee.exit);
		Codee.put(Codee.return_);
	}
	
	// list comprehension processing
	public static class LCElem {
		boolean isOperator = false;
		int value = -1;
	}
	
	ArrayList<Object> instrList = new ArrayList<>();
	boolean listComprExprActive = false;
	boolean listComprIfActive = false;
	Obj listComprObj = null;
	
	public static int pcLCCond = -1;
	public static int fixupLCNoVar = -1;
	
	// Program
	
	public void visit(ProgNamet progName) {
		currDsgObj.add(null);
		currDsgType.add(LOAD_DSG);
		
		initStdMethods();
	}
	
	public void visit(Programt program) {
		currDsgObj.remove(currDsgObj.size() - 1);
		currDsgType.remove(currDsgType.size() - 1);
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
	
	public void visit(StaticSymbolt statSymbol) {
		if (newStatInit) {
			if (firstStatInit == -1) firstStatInit = Codee.pc;
			if (lastStatFixup != -1) Codee.fixup(lastStatFixup);
			newStatInit = false;
		}
	}
	
	public void visit(StatInitListt statInitList) {
		if (statInitList.getStatInits() instanceof StatInitsYes) {
			Codee.putJump(0);
			lastStatFixup = Codee.pc - 2;
			newStatInit = true;
		}
	}
	
	// MethodDecl

	public void visit(MethodNamet methodName) {
		Obj methObj = methodName.obj;
		visitedMethods.add(methObj);
		
		if (methObj.getName().equals("main")) {
			mainPc = Codee.pc;
			initTvfs();
			
			if (lastStatFixup != -1) {
				Codee.fixup(lastStatFixup);
				lastStatFixup = -1;
			}
		}
		methObj.setAdr(Codee.pc);
		
		Codee.put(Codee.enter);
		Codee.put(methObj.getLevel());
		Codee.put(methObj.getLocalSymbols().size());
		
		HashMap<Integer, Obj> currHM = methodNewTypes.get(methObj);
		if (currHM != null) {
			for (Entry<Integer, Obj> entry: currHM.entrySet()) {
				Integer param = entry.getKey() + (methObj.getFpPos() > 0 ? 1 : 0);
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
			
			methodNewTypes.remove(methObj);
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
		
		// dummy
		Codee.put(Codee.const_n);
	}
	
	public void visit(StmtContinue stmt) {
		if (forStack.peek().existsCond) {
			Codee.put(Codee.pop);
			Codee.put(Codee.pop);
		}
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
		if (forStack.peek().existsCond) {
			Codee.put(Codee.pop);
			Codee.put(Codee.pop);
		}
		Codee.putJump(forStack.peek().pcPost);
		
		// start of after branch
		forStack.peek().processAndClear(ForFixups.CFALSE);
		forStack.peek().processAndClear(ForFixups.BREAK);
		
		if (forStack.peek().existsCond) {
			Codee.put(Codee.pop);
			Codee.put(Codee.pop);
		}
		forStack.pop();
	}
	
	public void visit(StmtForYesYesNo stmt) {
		if (forStack.peek().existsCond) {
			Codee.put(Codee.pop);
			Codee.put(Codee.pop);
		}
		Codee.putJump(forStack.peek().pcPost);
		
		// start of after branch
		forStack.peek().processAndClear(ForFixups.CFALSE);
		forStack.peek().processAndClear(ForFixups.BREAK);
		
		if (forStack.peek().existsCond) {
			Codee.put(Codee.pop);
			Codee.put(Codee.pop);
		}
		forStack.pop();
	}
	
	public void visit(StmtForYesNoYes stmt) {
		if (forStack.peek().existsCond) {
			Codee.put(Codee.pop);
			Codee.put(Codee.pop);
		}
		Codee.putJump(forStack.peek().pcPost);
		
		// start of after branch
		forStack.peek().processAndClear(ForFixups.CFALSE);
		forStack.peek().processAndClear(ForFixups.BREAK);
		
		if (forStack.peek().existsCond) {
			Codee.put(Codee.pop);
			Codee.put(Codee.pop);
		}
		forStack.pop();
	}
	
	public void visit(StmtForYesNoNo stmt) {
		if (forStack.peek().existsCond) {
			Codee.put(Codee.pop);
			Codee.put(Codee.pop);
		}
		Codee.putJump(forStack.peek().pcPost);
		
		// start of after branch
		forStack.peek().processAndClear(ForFixups.CFALSE);
		forStack.peek().processAndClear(ForFixups.BREAK);
		
		if (forStack.peek().existsCond) {
			Codee.put(Codee.pop);
			Codee.put(Codee.pop);
		}
		forStack.pop();
	}
	
	public void visit(StmtForNoYesYes stmt) {
		if (forStack.peek().existsCond) {
			Codee.put(Codee.pop);
			Codee.put(Codee.pop);
		}
		Codee.putJump(forStack.peek().pcPost);
		
		// start of after branch
		forStack.peek().processAndClear(ForFixups.CFALSE);
		forStack.peek().processAndClear(ForFixups.BREAK);
		
		if (forStack.peek().existsCond) {
			Codee.put(Codee.pop);
			Codee.put(Codee.pop);
		}
		forStack.pop();
	}
	
	public void visit(StmtForNoYesNo stmt) {
		if (forStack.peek().existsCond) {
			Codee.put(Codee.pop);
			Codee.put(Codee.pop);
		}
		Codee.putJump(forStack.peek().pcPost);
		
		// start of after branch
		forStack.peek().processAndClear(ForFixups.CFALSE);
		forStack.peek().processAndClear(ForFixups.BREAK);
		
		if (forStack.peek().existsCond) {
			Codee.put(Codee.pop);
			Codee.put(Codee.pop);
		}
		forStack.pop();
	}
	
	public void visit(StmtForNoNoYes stmt) {
		if (forStack.peek().existsCond) {
			Codee.put(Codee.pop);
			Codee.put(Codee.pop);
		}
		Codee.putJump(forStack.peek().pcPost);
		
		// start of after branch
		forStack.peek().processAndClear(ForFixups.CFALSE);
		forStack.peek().processAndClear(ForFixups.BREAK);
		
		if (forStack.peek().existsCond) {
			Codee.put(Codee.pop);
			Codee.put(Codee.pop);
		}
		forStack.pop();
	}
	
	public void visit(StmtForNoNoNo stmt) {
		if (forStack.peek().existsCond) {
			Codee.put(Codee.pop);
			Codee.put(Codee.pop);
		}
		Codee.putJump(forStack.peek().pcPost);
		
		// start of after branch
		forStack.peek().processAndClear(ForFixups.CFALSE);
		forStack.peek().processAndClear(ForFixups.BREAK);
		
		if (forStack.peek().existsCond) {
			Codee.put(Codee.pop);
			Codee.put(Codee.pop);
		}
		forStack.pop();
	}

	public void visit(StmtRead stmt) {
		if (stmt.getDesignator().obj.getType().equals(Tabb.charType)) Codee.put(Codee.bread);
		else if (stmt.getDesignator().obj.getType().equals(Tabb.boolType)) Codee.put(Codee.bread);
		else Codee.put(Codee.read);
		
		Codee.handleStoreDesignator(currStoreDesignator.get(currStoreDesignator.size() - 1));
		currStoreDesignator.remove(currStoreDesignator.size() - 1);
	}
	
	public void visit(StmtPrintYes stmt) {
		Codee.loadConst(stmt.getValue());
		if (stmt.getExpr().struct.getKind() == Struct.Array) {
			Struct elemType = stmt.getExpr().struct.getElemType();
			if (stmt.getExpr().struct.getElemType().getKind() == Struct.Array) Codee.printMatrix(elemType.getElemType());
			else Codee.printArray(elemType);
		}
		else if (stmt.getExpr().struct.equals(Tabb.charType)) Codee.put(Codee.bprint);
		else Codee.put(Codee.print);
	}
	
	public void visit(StmtPrintNo stmt) {
		Codee.loadConst(4);
		if (stmt.getExpr().struct.getKind() == Struct.Array) {
			Struct elemType = stmt.getExpr().struct.getElemType();
			if (stmt.getExpr().struct.getElemType().getKind() == Struct.Array) Codee.printMatrix(elemType.getElemType());
			else Codee.printArray(elemType);
		}
		else if (stmt.getExpr().struct.equals(Tabb.charType)) Codee.put(Codee.bprint);
		else Codee.put(Codee.print);
	}
	
	// DesignatorStatement
	
	public void visit(TLParent lParen) {
		currDsgObj.add(null);
		currDsgType.add(LOAD_DSG);
	}
	
	public void visit(TRParent rParen) {
		currDsgObj.remove(currDsgObj.size() - 1);
		currDsgType.remove(currDsgType.size() - 1);
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
		}
		else {
			Codee.put(Codee.call);
			Codee.put2(methObj.getAdr() - Codee.pc + 1);
		}
		
		Obj dsgObj = ((DesignatorStmtFirst)opChoice.getParent()).getDesignator().obj;
		if (!dsgObj.getType().equals(Tabb.noType)) {
			Codee.put(Codee.pop);
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
		}
		else {
			Codee.put(Codee.call);
			Codee.put2(methObj.getAdr() - Codee.pc + 1);
		}
		
		Obj dsgObj = ((DesignatorStmtFirst)opChoice.getParent()).getDesignator().obj;
		if (!dsgObj.getType().equals(Tabb.noType)) {
			Codee.put(Codee.pop);
		}
		
		currCalledMethod.remove(currCalledMethod.size() - 1);
	}
	
	public void visit(OpChoiceExpr opChoice) {
		Codee.handleStoreDesignator(currStoreDesignator.get(currStoreDesignator.size() - 1));
		
		if (latestNewType != null) {
			Codee.handleNewDesignator(currStoreDesignator.get(currStoreDesignator.size() - 1), latestNewType);
			latestNewType = null;
		}
		
		currStoreDesignator.remove(currStoreDesignator.size() - 1);
	}
	
	public void visit(OpChoiceInc opChoice) {
		Codee.loadConst(1);
		Codee.put(Codee.add);
		Codee.handleStoreDesignator(currStoreDesignator.get(currStoreDesignator.size() - 1));
		currStoreDesignator.remove(currStoreDesignator.size() - 1);
	}
	
	public void visit(OpChoiceDec opChoice) {
		Codee.loadConst(1);
		Codee.put(Codee.sub);
		Codee.handleStoreDesignator(currStoreDesignator.get(currStoreDesignator.size() - 1));
		currStoreDesignator.remove(currStoreDesignator.size() - 1);
	}
	
	public void visit(DesignatorListYesYes dsgList) {
		dsgStmtSet.add(dsgStmtCounter++);
	}
	
	public void visit(DesignatorListYesNo dsgList) {
		++dsgStmtCounter;
	}
	
	public void visit(DesignatorStmtSecond dsgStmt) {
		Struct dsgLeftType = dsgStmt.getDesignator().obj.getType().getElemType();
		Struct dsgRightType = dsgStmt.getDesignator1().obj.getType().getElemType();
		int rightInstr = (!dsgRightType.equals(Tabb.charType) && !dsgRightType.equals(Tabb.boolType)) ? Codee.aload : Codee.baload;
		int leftInstr = (!dsgLeftType.equals(Tabb.charType) && !dsgLeftType.equals(Tabb.boolType)) ? Codee.astore : Codee.bastore;
		
		// handle errors
		// handle array assignment
		Codee.writeDsgStmtToCode(dsgStmtCounter, rightInstr, leftInstr);
		currStoreDesignator.remove(currStoreDesignator.size() - 1);
		
		if (dsgStmtCounter == 0) Codee.put(Codee.pop);
		while (--dsgStmtCounter >= 0) {
			if (dsgStmtSet.contains(dsgStmtCounter)) {
				if (dsgStmtSet.size() > 1) {
					boolean eInd = currStoreDesignator.get(currStoreDesignator.size() - 1).getKind() == Obj.Elem;
					boolean fInd = currStoreDesignator.get(currStoreDesignator.size() - 1).getKind() == Obj.Fld;
					if (eInd) Codee.put(Codee.dup_x2);
					else if (fInd) Codee.put(Codee.dup_x1);
					else Codee.put(Codee.dup);
				}
				Codee.loadConst(dsgStmtCounter);
				Codee.put(rightInstr);
				Codee.handleStoreDesignator(currStoreDesignator.get(currStoreDesignator.size() - 1));
				dsgStmtSet.remove(dsgStmtCounter);
				currStoreDesignator.remove(currStoreDesignator.size() - 1);
			}
		}
		
		dsgStmtCounter = 0;
	}
	
	public void visit(LCFort forSymb) {
		listComprExprActive = true;
		Codee.processLCFor();
	}
	
	public void visit(LCInt inSymb) {
		listComprExprActive = false;
	}
	
	public void visit(LCIft ifSymb) {
		ifElseStack.push(new IfElseFixups());
		listComprIfActive = true;
		Codee.processLCSecondArray(listComprObj);
	}
	
	public void visit(DesignatorStmtThirdYes dsgStmt) {
		Obj dsgObj = dsgStmt.getDesignator().obj;
		boolean isChar = dsgObj.getType().getElemType().equals(Tabb.charType);
		boolean isBool = dsgObj.getType().getElemType().equals(Tabb.boolType);
		
		// start of then branch
		// already fixed up in Condition
		Codee.processLCBody(instrList, isChar || isBool, true);
		
		// start of else branch
		ifElseStack.peek().processAndClear(IfElseFixups.RPAREN);
		ifElseStack.peek().processAndClear(IfElseFixups.AND);
		Codee.processLCElse();
		
		// start of after [if] branch
		ifElseStack.peek().processAndClear(IfElseFixups.RPAREN);
		ifElseStack.peek().processAndClear(IfElseFixups.AND);
		ifElseStack.peek().processAndClear(IfElseFixups.IF);
		ifElseStack.pop();
		Codee.processLCPost();
		
		// start of after [for] branch
		Codee.fixup(fixupLCNoVar);
		listComprIfActive = false;
		listComprObj = null;
		pcLCCond = -1;
		fixupLCNoVar = -1;
	}
	
	public void visit(DesignatorStmtThirdNo dsgStmt) {
		Obj dsgObj = dsgStmt.getDesignator().obj;
		boolean isChar = dsgObj.getType().getElemType().equals(Tabb.charType);
		boolean isBool = dsgObj.getType().getElemType().equals(Tabb.boolType);
		
		// there was no if
		Codee.processLCSecondArray(listComprObj);
		
		Codee.processLCBody(instrList, isChar || isBool, false);
		Codee.processLCPost();
		
		// start of after [for] branch
		Codee.fixup(fixupLCNoVar);
		listComprIfActive = false;
		listComprObj = null;
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
		
		if (latestNewType != null) { 
			handleNewTypeExpr(expr);
		}
	}
	
	public void visit(AddopTermListYes addopTerm) {
		SyntaxNode addopNode = addopTerm.getAddop();
		int instr = -1;
		
		if (addopNode instanceof AddopPlus) instr = Codee.add;
		else instr = Codee.sub;
		
		Codee.put(instr);
		
		if (listComprExprActive) {
			LCElem elem = new LCElem();
			elem.isOperator = true;
			elem.value = instr;
			instrList.add(elem);
		}
	}
	
	// Term
	
	public void visit(MulopFactorListYes mulopFactor) {
		SyntaxNode mulopNode = mulopFactor.getMulop();
		int instr = -1;
		
		if (mulopNode instanceof MulopMul) instr = Codee.mul;
		else if (mulopNode instanceof MulopDiv) instr = Codee.div;
		else instr = Codee.rem;
		
		Codee.put(instr);
		
		if (listComprExprActive) {
			LCElem elem = new LCElem();
			elem.isOperator = true;
			elem.value = instr;
			instrList.add(elem);
		}
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
			Codee.put2(methObj.getAdr() - Codee.pc + 1);
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
			Codee.put2(methObj.getAdr() - Codee.pc + 1);
		}
		
		currCalledMethod.remove(currCalledMethod.size() - 1);
	}
	
	public void visit(FactorDesignatorThird factor) {
		Obj dsgObj = factor.getDesignator().obj;
		boolean ind = listComprExprActive || listComprIfActive; 
		
		if (ind && dsgObj.getKind() != Obj.Con && listComprObj == null) {
			listComprObj = dsgObj;
		}
		if (listComprExprActive) {
			instrList.add(dsgObj);
		}
	}
	
	public void visit(FactorNum factor) {
		Code.loadConst(factor.getValue());
		
		if (listComprExprActive) {
			LCElem elem = new LCElem();
			elem.isOperator = false;
			elem.value = factor.getValue();
			instrList.add(elem);
		}
	}
	
	public void visit(FactorChar factor) {
		Code.loadConst(factor.getValue());
		
		if (listComprExprActive) {
			LCElem elem = new LCElem();
			elem.isOperator = false;
			elem.value = factor.getValue();
			instrList.add(elem);
		}
	}
	
	public void visit(FactorBool factor) {
		Code.loadConst(factor.getValue() ? 1 : 0);
		
		if (listComprExprActive) {
			LCElem elem = new LCElem();
			elem.isOperator = false;
			elem.value = factor.getValue() ? 1 : 0;
			instrList.add(elem);
		}
	}
	
	public void visit(FactorNew factor) {
		Struct type = factor.getType().struct;
		SyntaxNode newChoiceNode = factor.getNewChoice();
		
		if (newChoiceNode instanceof NewChoiceExpr) {
			Codee.put(Codee.newarray);
			if (!type.equals(Tabb.charType) && !type.equals(Tabb.boolType)) Codee.put(1);
			else Codee.put(0);
		}
		else if (newChoiceNode instanceof NewChoiceExprExpr) {
			boolean ind = !type.equals(Tabb.charType) && !type.equals(Tabb.boolType);
			Codee.allocateMatrix(ind);
		}
		else {
			latestNewType = Tabb.findObjForType(type);
			// handleNewExpr(factor.getParent().getParent());
			Codee.put(Codee.new_);
			Codee.put2(4 * (type.getNumberOfFields() + 1));
		}
	}
	
	public void visit(FactorRange factor) {
		Codee.processRange();
	}
	
	// Designator
	
	public void visit(TLBrackett lBracket) {
		currDsgObj.add(null);
		currDsgType.add(LOAD_DSG);
	}
	
	public void visit(TRBrackett rBracket) {
		currDsgObj.remove(currDsgObj.size() - 1);
		currDsgType.remove(currDsgType.size() - 1);
	}
	
	public void visit(FDesignatorYes fDesignator) {
		Obj nspObj = Tabb.findProgramSymbol(fDesignator.getNspName());
		String fullName = nspObj.getName() + "::" + fDesignator.getDsgName();
		Obj initObj = Tabb.findProgramSymbol(fullName);
		
		currDsgObj.set(currDsgObj.size() - 1, initObj);
		processFDesignator((Designatort)fDesignator.getParent());
	}
	
	public void visit(FDesignatorNo fDesignator) {
		String localName = fDesignator.getDsgName();
		String fullName = currNamespace + localName;
		
		Obj initObj = Tabb.noObj;
		if (currMethod != null) initObj = Tabb.findLocsMethod(currMethod, localName);
		if (currClass != null) {
			if (initObj == Tabb.noObj) initObj = Tabb.findLocsClass(currClass.getType(), localName);
			if (initObj == Tabb.noObj) initObj = Tabb.findStatic(localName, currClass.getType());
		}
		if (initObj == Tabb.noObj) initObj = Tabb.findProgramSymbol(fullName);
		if (initObj == Tabb.noObj) initObj = Tabb.findProgramSymbol(localName);
		
		currDsgObj.set(currDsgObj.size() - 1, initObj);
		processFDesignator((Designatort)fDesignator.getParent());
	}
	
	public void processFDesignator(Designatort designator) {
		SyntaxNode dsgParent = designator.getParent();
		
		boolean bothDsg = (dsgParent instanceof DesignatorStmtFirst && ((DesignatorStmtFirst)dsgParent).getOpChoice() instanceof OpChoiceInc)
				|| (dsgParent instanceof DesignatorStmtFirst && ((DesignatorStmtFirst)dsgParent).getOpChoice() instanceof OpChoiceDec);
		boolean storeDsg = (dsgParent instanceof StmtRead)
				|| (dsgParent instanceof DesignatorStmtFirst && ((DesignatorStmtFirst)dsgParent).getOpChoice() instanceof OpChoiceExpr)
				|| (dsgParent instanceof DesignatorListYesYes)
				|| (dsgParent instanceof DesignatorStmtSecond && ((DesignatorStmtSecond)dsgParent).getDesignator().equals(designator));
		
		int dsgType = (bothDsg ? BOTH_DSG : (storeDsg ? STORE_DSG : LOAD_DSG));
		currDsgType.set(currDsgType.size() - 1, dsgType);
		
		Obj dsgObj = currDsgObj.get(currDsgObj.size() - 1);
		boolean lastDsg = (designator.getDesignatorParts() instanceof DesignatorPartsNo);
		
		if (dsgType == BOTH_DSG || dsgType == STORE_DSG) {
			currStoreDesignator.add(null);
			if (lastDsg) {
				if (dsgObj.getKind() == Obj.Fld) Codee.put(Codee.load_n);
				currStoreDesignator.set(currStoreDesignator.size() - 1, dsgObj);
				if (dsgType == BOTH_DSG) Codee.handleBothDesignator(dsgObj);
				else if (dsgParent instanceof DesignatorStmtSecond && ((DesignatorStmtSecond)dsgParent).getDesignator().equals(designator)) {
					Codee.getArrLenStoreDesignator(dsgObj);
				}
				currDsgObj.set(currDsgObj.size() - 1, null);
			}
			else {
				Codee.handleLoadDesignator(dsgObj, currCalledMethod, true);
			}
		}
		else {
			Codee.handleLoadDesignator(dsgObj, currCalledMethod, true);
			if (lastDsg) {
				if (dsgParent instanceof DesignatorStmtSecond && ((DesignatorStmtSecond)dsgParent).getDesignator1().equals(designator)) {
					Codee.getArrLenLoadDesignator(dsgObj);
				}
				currDsgObj.set(currDsgObj.size() - 1, null);
			}
		}
	}
	
	public void visit(DesignatorPartsIdent designator) {
		Obj dsgObj = currDsgObj.get(currDsgObj.size() - 1);
		Struct elemType = dsgObj.getType();
		boolean lastDsg = (designator.getParent() instanceof Designatort);
		int dsgType = currDsgType.get(currDsgType.size() - 1);
		String elem = designator.getDsgName();
		
		if (dsgObj.getKind() == Obj.Type) {
			dsgObj = Tabb.findExactStatic(dsgObj.getName() + "::" + elem);
			elemType = dsgObj.getType();
		}
		else {
			dsgObj = Tabb.findLocsClass(elemType, elem);
			elemType = dsgObj.getType();
		}
		
		if (dsgType == STORE_DSG || dsgType == BOTH_DSG) {
			if (lastDsg) {
				SyntaxNode dsgParent = designator.getParent().getParent();
				currStoreDesignator.set(currStoreDesignator.size() - 1, dsgObj);
				if (dsgType == BOTH_DSG) Codee.handleBothDesignator(dsgObj);
				else if (dsgParent instanceof DesignatorStmtSecond && ((DesignatorStmtSecond)dsgParent).getDesignator().equals(designator.getParent())) {
					Codee.getArrLenStoreDesignator(dsgObj);
				}
				currDsgObj.set(currDsgObj.size() - 1, null);
			}
			else {
				currDsgObj.set(currDsgObj.size() - 1, dsgObj);
				Codee.handleLoadDesignator(dsgObj, currCalledMethod, false);
			}
		}
		else {
			Codee.handleLoadDesignator(dsgObj, currCalledMethod, false);
			if (lastDsg) {
				SyntaxNode dsgParent = designator.getParent().getParent();
				if (dsgParent instanceof DesignatorStmtSecond && ((DesignatorStmtSecond)dsgParent).getDesignator1().equals(designator.getParent())) {
					Codee.getArrLenLoadDesignator(dsgObj);
				}
				currDsgObj.set(currDsgObj.size() - 1, null);
			}
			else currDsgObj.set(currDsgObj.size() - 1, dsgObj);
		}
	}
	
	public void visit(DesignatorPartsExpr designator) {
		Obj dsgObj = currDsgObj.get(currDsgObj.size() - 1);
		Struct elemType = dsgObj.getType();
		boolean lastDsg = (designator.getParent() instanceof Designatort);
		int dsgType = currDsgType.get(currDsgType.size() - 1);
		
		elemType = elemType.getElemType();
		dsgObj = new Obj(Obj.Elem, "", elemType);
		
		if (dsgType == BOTH_DSG || dsgType == STORE_DSG) {
			if (lastDsg) {
				currStoreDesignator.set(currStoreDesignator.size() - 1, dsgObj);
				if (dsgType == BOTH_DSG) Codee.handleBothDesignator(dsgObj);
				currDsgObj.set(currDsgObj.size() - 1, null);
			}
			else {
				currDsgObj.set(currDsgObj.size() - 1, dsgObj);
				Codee.handleLoadDesignator(dsgObj, currCalledMethod, false);
			}
		}
		else {
			Codee.handleLoadDesignator(dsgObj, currCalledMethod, false);
			if (lastDsg) currDsgObj.set(currDsgObj.size() - 1, null);
			else currDsgObj.set(currDsgObj.size() - 1, dsgObj);
		}
	}
}
