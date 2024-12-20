package rs.ac.bg.etf.pp1;

import java.util.ArrayList;

import rs.etf.pp1.mj.runtime.Code;
import rs.etf.pp1.symboltable.concepts.Obj;
import rs.etf.pp1.symboltable.concepts.Struct;

public class Codee extends Code {
	
	public static void load (Obj o) {
	    switch (o.getKind()) {
	    	case Obj.Con:
	    		if (o.getType() == Tabb.nullType) 
	    			put(const_n + 0);
	    		else 
	    			loadConst(o.getAdr()); 
	    		break;
	    	  
	    	case Obj.Var:
	    		if (o.getLevel()==0) {  
	    			put(getstatic); put2(o.getAdr() + CodeGenerator.numTemp); 
	    			break; 
	    		}
	    		if (0 <= o.getAdr() && o.getAdr() <= 3) 
	    			put(load_n + o.getAdr());
	    		else { 
	    			put(load); put(o.getAdr()); 
	    		} 
	    		break;
	    	  
	    	case Objj.Stat:
	    		put(getstatic); put2(o.getAdr() + CodeGenerator.numTemp);
	    		break;
	    	  
	    	case Obj.Fld:
	    		// 0th position is reserved for TVF
	    		put(getfield); put2(o.getAdr() + 1); 
	    		break;
	    	  
	    	case Obj.Elem:
	    		if (o.getType().getKind() == Struct.Char) put(baload);
	    		else if (o.getType().getKind() == Struct.Bool) put(baload);
	    		else put(aload); 
	    		break;
	    	  
	    	default:  
	    		error("Greska: nelegalan operand u Code.load");
	    }
	}
	
	public static void store(Obj o) {
		switch (o.getKind()) {
			case Obj.Var:
				if (o.getLevel()==0) {
					put(putstatic); put2(o.getAdr() + CodeGenerator.numTemp); 
					break;
		  		}
		  		if (0 <= o.getAdr() && o.getAdr() <= 3) 
		  			put(store_n + o.getAdr());
		  		else { 
		  			put(store); put(o.getAdr()); 
		  		} 
		  		break;
		  		
		  	case Objj.Stat:
		  		put(putstatic); put2(o.getAdr() + CodeGenerator.numTemp); 
	  			break;

		  	case Obj.Fld:
		  		// 0th position is reserved for TVF
		  		put(putfield); put2(o.getAdr() + 1); 
		  		break;
		        
		  	case Obj.Elem:
		  		if (o.getType().getKind() == Struct.Char) put(bastore);
		  		if (o.getType().getKind() == Struct.Bool) put(bastore);
		  		else put(astore); 
		  		break;
		      
		  	default:
		  		error("Greska: Na levoj strani dodele mora biti promenljiva!");
		}
	}

	public static void handleLoadDesignator(Obj dsgObj, ArrayList<Obj> currCalledMethod, boolean first) {
		switch(dsgObj.getKind()) {
			case Obj.Var:
				if (dsgObj.getLevel() == 0) {
					put(getstatic);
					put2(dsgObj.getAdr() + CodeGenerator.numTemp);
				}
				else if (0 <= dsgObj.getAdr() && dsgObj.getAdr() <= 3) {
					put(load_n + dsgObj.getAdr());
				}
				else {
					put(load);
					put(dsgObj.getAdr());
				}
				break;
			
			case Obj.Fld:
				// 0th position is reserved for TVF
				if (first) put(load_n);
				put(getfield);
				put2(dsgObj.getAdr() + 1);
				break;
				
			case Obj.Meth:
				currCalledMethod.add(dsgObj);
				if (dsgObj.getFpPos() > 0) {
					if (first) put(load_n);
					put(dup);
					// arg
					// dup_x1
					// pop
					// ...
					// getfield 0
					// invokevirtual name -1
				}
				else {
					// args
					// call offset
				}
				break;
				
			case Objj.Stat:
				put(getstatic);
				put2(dsgObj.getAdr() + CodeGenerator.numTemp);
				break;
				
			case Obj.Con:
				loadConst(dsgObj.getAdr());
				break;
				
			case Objj.Elem:
				// expr should put himself on the stack
				// that is the array's index for this elem
				// put(dup_x1);
				// put(pop);
				if (dsgObj.getType().equals(Tabb.charType)) put(baload);
				else if (dsgObj.getType().equals(Tabb.boolType)) put(baload);
				else put(aload);
				break;
				
			default:
				break;
		}
	}
	
	public static void getArrLenLoadDesignator(Obj dsgObj) {
		// adr
		put(dup);
		put(arraylength);
		put(putstatic);
		put2(1);
	}
	
	public static void handleStoreDesignator(Obj dsgObj) {
		switch(dsgObj.getKind()) {
			case Obj.Var:
				// val
				if (dsgObj.getLevel() == 0) {
					put(putstatic); 
					put2(dsgObj.getAdr() + CodeGenerator.numTemp); 
		  		}
				else if (0 <= dsgObj.getAdr() && dsgObj.getAdr() <= 3) 
		  			put(store_n + dsgObj.getAdr());
		  		else { 
		  			put(store); 
		  			put(dsgObj.getAdr()); 
		  		} 
				break;
				
			case Obj.Fld:
				// 0th position is reserved for TVF
				// if (first) put(load_n);
				// val
				put(putfield);
				put2(dsgObj.getAdr() + 1);
				break;
				
			case Objj.Stat:
				// val
				put(putstatic);
				put2(dsgObj.getAdr() + CodeGenerator.numTemp);
				break;
				
			case Objj.Elem:
				// expr should put himself on the stack
				// that is the array's index for this elem
				// val
				// put(dup_x1);
				// put(pop);
				// put(dup_x2);
				// put(pop);
				if (dsgObj.getType().equals(Tabb.charType)) put(bastore);
				else if (dsgObj.getType().equals(Tabb.boolType)) put(bastore);
				else put(astore);
				break;
				
			default:
				break;
		}
	}
	
	public static void getArrAdr(Obj dsgObj) {
		switch(dsgObj.getKind()) {
			case Obj.Var:
				// nothing
				if (dsgObj.getLevel() == 0) {
					put(getstatic); 
					put2(dsgObj.getAdr() + CodeGenerator.numTemp); 
		  		}
				else if (0 <= dsgObj.getAdr() && dsgObj.getAdr() <= 3) 
		  			put(load_n + dsgObj.getAdr());
		  		else { 
		  			put(load); 
		  			put(dsgObj.getAdr()); 
		  		} 
				break;
				
			case Objj.Stat:
				// nothing
				put(getstatic);
				put2(dsgObj.getAdr() + CodeGenerator.numTemp);
				break;
				
			default:
				break;
		}
	}
	
	public static void getArrLenStoreDesignator(Obj dsgObj) {
		getArrAdr(dsgObj);
		put(dup);
		put(arraylength);
		put(putstatic);
		put2(0);
	}

	public static void handleBothDesignator(Obj dsgObj) {
		switch(dsgObj.getKind()) {
			case Obj.Var:
				// nothing
				if (dsgObj.getLevel() == 0) {
					put(getstatic); 
					put2(dsgObj.getAdr() + CodeGenerator.numTemp); 
		  		}
				else if (0 <= dsgObj.getAdr() && dsgObj.getAdr() <= 3) 
		  			put(load_n + dsgObj.getAdr());
		  		else { 
		  			put(load); 
		  			put(dsgObj.getAdr()); 
		  		} 
				break;
				
			case Obj.Fld:
				// 0th position is reserved for TVF
				// adr
				put(dup);
				put(getfield);
				put2(dsgObj.getAdr() + 1);
				break;
				
			case Objj.Stat:
				// nothing
				put(getstatic);
				put2(dsgObj.getAdr() + CodeGenerator.numTemp);
				break;
				
			case Objj.Elem:
				// adr
				// index
				put(dup2);
				if (dsgObj.getType().equals(Tabb.charType)) put(baload);
				else if (dsgObj.getType().equals(Tabb.boolType)) put(baload);
				else put(aload);
				break;
				
			default:
				break;
		}
	}
	
	public static void handleNewDesignator(Obj dsgObj, Obj latestNew) {
		switch(dsgObj.getKind()) {
			case Obj.Var:
				if (dsgObj.getLevel() == 0) {
					put(getstatic); 
					put2(dsgObj.getAdr() + CodeGenerator.numTemp); 
		  		}
				else if (0 <= dsgObj.getAdr() && dsgObj.getAdr() <= 3) 
		  			put(load_n + dsgObj.getAdr());
		  		else { 
		  			put(load); 
		  			put(dsgObj.getAdr()); 
		  		} 
				break;
			
			case Obj.Fld:
				put(getfield);
				put2(dsgObj.getAdr() + 1);
				break;
				
			case Objj.Stat:
				put(getstatic);
				put2(dsgObj.getAdr() + CodeGenerator.numTemp);
				break;
				
			case Obj.Elem:
				// put(dup_x1);
				// put(pop);
				put(aload);
				break;
				
			default:
				break;
		}
		put(const_); 
		put4(latestNew.getAdr());
		// loadConst(latestNew.getAdr());
		
		if (!CodeGenerator.tvfInitDone) {
			CodeGenerator.fixupsTvf.put(pc - 4, latestNew);
		}
		
		put(putfield);
		put2(0);
	}

	public static void writeDsgStmtToCode(int dsgStmtCounter, int rightInstr, int leftInstr) {
		/*
		 	0 --> leftLen
		 	1 --> rightLen
		 	2 --> dsgStmtCounter
		 	3 --> cnt
		*/
		
		Codee.loadConst(dsgStmtCounter);
		Codee.put(Codee.putstatic); Codee.put2(2);
		Codee.put(Codee.const_n);
		Codee.put(Codee.putstatic); Codee.put2(3);
		Codee.put(Codee.getstatic); Codee.put2(1);
		Codee.put(Codee.getstatic); Codee.put2(2);
		Codee.put(Codee.sub);
		Codee.put(Codee.const_n);
		Codee.put(Codee.jcc + Codee.le); Codee.put2(18);
		Codee.put(Codee.getstatic); Codee.put2(0);
		Codee.put(Codee.getstatic); Codee.put2(1);
		Codee.put(Codee.sub);
		Codee.put(Codee.getstatic); Codee.put2(2);
		Codee.put(Codee.add);
		Codee.put(Codee.const_n);
		Codee.put(Codee.jcc + Codee.ge); Codee.put2(5);
		Codee.put(Codee.trap); Codee.put(1);
		Codee.put(Codee.getstatic); Codee.put2(1);
		Codee.put(Codee.getstatic); Codee.put2(2);
		Codee.put(Codee.getstatic); Codee.put2(3);
		Codee.put(Codee.add);
		Codee.put(Codee.const_1);
		Codee.put(Codee.add);
		Codee.put(Codee.jcc + Codee.lt); Codee.put2(48);
		Codee.put(Codee.getstatic); Codee.put2(1);
		Codee.put(Codee.getstatic); Codee.put2(2);
		Codee.put(Codee.getstatic); Codee.put2(3);
		Codee.put(Codee.add);
		Codee.put(Codee.const_1);
		Codee.put(Codee.add);
		Codee.put(Codee.jcc + Code.le); Codee.put2(7);
		Codee.put(Codee.dup2);
		Codee.put(Codee.jmp); Codee.put2(4);
		Codee.put(Codee.dup_x1);
		Codee.put(Codee.getstatic); Codee.put2(2);
		Codee.put(Codee.getstatic); Codee.put2(3);
		Codee.put(Codee.add);
		Codee.put(Codee.getstatic); Codee.put2(3);
		Codee.put(Codee.dup_x2);
		Codee.put(Codee.pop);
		Codee.put(rightInstr);
		Codee.put(leftInstr);
		Codee.put(Codee.getstatic); Codee.put2(3);
		Codee.put(Codee.const_1);
		Codee.put(Codee.add);
		Codee.put(Codee.putstatic); Codee.put2(3);
		Codee.put(Codee.jmp); Codee.put2(-57);
	}

	public static void printArray(Struct type) {
		boolean isChar = type.equals(Tabb.charType);
		boolean isBool = type.equals(Tabb.boolType);
		
		int loadInstr = (isChar || isBool) ? Codee.baload : Codee.aload;
		int printInstr = isChar ? Codee.bprint : Codee.print;
		
		/*
			0 --> i
			1 --> arr.len
			2 --> adr_arr
			3 --> numConst
		*/
		
		Codee.put(Codee.putstatic); Codee.put2(3);
		Codee.put(Codee.dup);
		Codee.put(Codee.arraylength);
		Codee.put(Codee.putstatic); Codee.put2(1);
		Codee.put(Codee.putstatic); Codee.put2(2);
		Codee.put(Codee.const_n);
		Codee.put(Codee.putstatic); Codee.put2(0);
		
		Codee.put(Codee.getstatic); Codee.put2(0);
		Codee.put(Codee.getstatic); Codee.put2(1);
		Codee.put(Codee.jcc + Codee.ge); Codee.put2(25);
		Codee.put(Codee.getstatic); Codee.put2(2);
		Codee.put(Codee.getstatic); Codee.put2(0);
		Codee.put(loadInstr);
		Codee.put(Codee.getstatic); Codee.put2(3);
		Codee.put(printInstr);
		Codee.put(Codee.getstatic); Codee.put2(0);
		Codee.put(Codee.const_1);
		Codee.put(Codee.add);
		Codee.put(Codee.putstatic); Codee.put2(0);
		Codee.put(Codee.jmp); Codee.put2(-28);
	}

	public static void processRange() {
		/*
			0 --> i
			1 --> 6
			2 --> adr_arr
		*/
		
		Codee.put(Codee.dup);
		Codee.put(Codee.putstatic); Codee.put2(1);
		Codee.put(Codee.newarray); Codee.put(1);
		Codee.put(Codee.putstatic); Codee.put2(2);
		Codee.put(Codee.const_n);
		Codee.put(Codee.putstatic); Codee.put2(0);
		Codee.put(Codee.getstatic); Codee.put2(0);
		Codee.put(Codee.getstatic); Codee.put2(1);
		Codee.put(Codee.jcc + Codee.ge); Codee.put2(24);
		Codee.put(Codee.getstatic); Codee.put2(2);
		Codee.put(Codee.getstatic); Codee.put2(0);
		Codee.put(Codee.getstatic); Codee.put2(0);
		Codee.put(Codee.astore);
		Codee.put(Codee.getstatic); Codee.put2(0);
		Codee.put(Codee.const_1);
		Codee.put(Codee.add);
		Codee.put(Codee.putstatic); Codee.put2(0);
		Codee.put(Codee.jmp); Codee.put2(-27);
		Codee.put(Codee.getstatic); Codee.put2(2);
	}
	
	public static void processLCFor() {
		/*
			0 --> adr_arr
			1 --> adr_tArr
			2 --> tArrLen
			3 --> i
			4 --> j
		*/
	
		Codee.put(Codee.putstatic); Codee.put2(0);
	}
	
	public static void processLCSecondArray(Obj listComprObj) {
		/*
			0 --> adr_arr
			1 --> adr_tArr
			2 --> tArrLen
			3 --> i
			4 --> j
		*/		
		
		Codee.put(Codee.dup);
		Codee.put(Codee.putstatic); Codee.put2(1);
		Codee.put(Codee.arraylength);
		Codee.put(Codee.putstatic); Codee.put2(2);
		Codee.put(Codee.pop);
		Codee.put(Codee.const_n);
		Codee.put(Codee.putstatic); Codee.put2(4);
		Codee.put(Codee.const_n);
		Codee.put(Codee.putstatic); Codee.put2(3);
		
		CodeGenerator.pcLCCond = Codee.pc;
		Codee.put(Codee.getstatic); Codee.put2(3);
		Codee.put(Codee.getstatic); Codee.put2(2);
		Codee.put(Codee.jcc + Codee.ge); Codee.put2(0);
		CodeGenerator.fixupLCNoVar = Codee.pc - 2;
		
		if (listComprObj != null) {
			boolean isChar = listComprObj.getType().equals(Tabb.charType);
			boolean isBool = listComprObj.getType().equals(Tabb.boolType);
			int instr = (isBool || isChar) ? Codee.baload : Codee.aload; 
			
			Codee.put(Codee.getstatic); Codee.put2(1);
			Codee.put(Codee.getstatic); Codee.put2(3);
			Codee.put(instr);
			Codee.handleStoreDesignator(listComprObj);
		}
	}
	
	public static void processLCBody(ArrayList<Object> instrList, boolean isCharBool, boolean haveElse) {
		/*
			0 --> adr_arr
			1 --> adr_tArr
			2 --> tArrLen
			3 --> i
			4 --> j
		*/
		
		Codee.put(Codee.getstatic); Codee.put2(0);
		Codee.put(Codee.getstatic); Codee.put2(4);
		
		for (Object elem: instrList) {
			if (elem instanceof Obj) handleLoadDesignator((Obj)elem, null, false);
			else {
				CodeGenerator.LCElem lcElem = (CodeGenerator.LCElem)elem;
				if (lcElem.isOperator) Codee.put(lcElem.value);
				else loadConst(lcElem.value);
			}
		}
		instrList.clear();
		
		int instr = isCharBool ? Codee.bastore : Codee.astore;
		Codee.put(instr);
		
		if (haveElse) {
			Codee.put(Codee.jmp); Codee.put2(11);
		}
	}
	
	public static void processLCElse() {
		/*
			0 --> adr_arr
			1 --> adr_tArr
			2 --> tArrLen
			3 --> i
			4 --> j
		*/
		
		Codee.put(Codee.getstatic); Codee.put2(4);
		Codee.put(Codee.const_1);
		Codee.put(Codee.sub);
		Codee.put(Codee.putstatic); Codee.put2(4);
	}
	
	public static void processLCPost() {
		/*
			0 --> adr_arr
			1 --> adr_tArr
			2 --> tArrLen
			3 --> i
			4 --> j
		*/
		
		Codee.put(Codee.getstatic); Codee.put2(3);
		Codee.put(Codee.const_1);
		Codee.put(Codee.add);
		Codee.put(Codee.putstatic); Codee.put2(3);
		Codee.put(Codee.getstatic); Codee.put2(4);
		Codee.put(Codee.const_1);
		Codee.put(Codee.add);
		Codee.put(Codee.putstatic); Codee.put2(4);
		Codee.putJump(CodeGenerator.pcLCCond);
	}
	
	public static void allocateMatrix(boolean ind) {
		/*
		 	0 --> nRows
		 	1 --> nCols
		 	2 --> adrMat
		 	3 --> i
		*/
		
		Codee.put(Codee.putstatic); Codee.put2(1);
		Codee.put(Codee.dup);
		Codee.put(Codee.putstatic); Codee.put2(0);
		Codee.put(Codee.newarray); Codee.put(1);
		Codee.put(Codee.putstatic); Codee.put2(2);
		
		Codee.loadConst(0);
		Codee.put(Codee.putstatic); Codee.put2(3);
		Codee.put(Codee.getstatic); Codee.put2(3);
		Codee.put(Codee.getstatic); Codee.put2(0);
		Codee.put(Codee.jcc + Codee.ge); Codee.put2(26);
		Codee.put(Codee.getstatic); Codee.put2(2);
		Codee.put(Codee.getstatic); Codee.put2(3);
		Codee.put(Codee.getstatic); Codee.put2(1);
		Codee.put(Codee.newarray);
		if (ind) Codee.put(1);
		else Codee.put(0);
		Codee.put(Codee.astore);
		Codee.put(Codee.getstatic); Codee.put2(3);
		Codee.loadConst(1);
		Codee.put(Codee.add);
		Codee.put(Codee.putstatic); Codee.put2(3);
		Codee.put(Codee.jmp); Codee.put2(-29);
		Codee.put(Codee.getstatic); Codee.put2(2);
	}

	public static void printMatrix(Struct type) {
		boolean isChar = type.equals(Tabb.charType);
		boolean isBool = type.equals(Tabb.boolType);
		
		int loadInstr = (isChar || isBool) ? Codee.baload : Codee.aload;
		int printInstr = isChar ? Codee.bprint : Codee.print;
		
		/*
			0 --> i
			1 --> j
			2 --> lenMatr
			3 --> lenMatrElem
			4 --> adrMatr
			5 --> numConst
		*/
		
		Codee.put(Codee.putstatic); Codee.put2(5);
		Codee.put(Codee.dup);
		Codee.put(Codee.dup);
		Codee.put(Codee.putstatic); Codee.put2(4);
		Codee.put(Codee.arraylength);
		Codee.put(Codee.putstatic); Codee.put2(2);
		Codee.loadConst(0);
		Codee.put(Codee.aload);
		Codee.put(Codee.arraylength);
		Codee.put(Codee.putstatic); Codee.put2(3);
		
		Codee.loadConst(0);
		Codee.put(Codee.putstatic); Codee.put2(0);
		Codee.put(Codee.getstatic); Codee.put2(0);
		Codee.put(Codee.getstatic); Codee.put2(2);
		Codee.put(Codee.jcc + Codee.ge); Codee.put2(60);
		Codee.loadConst(0);
		Codee.put(Codee.putstatic); Codee.put2(1);
		Codee.put(Codee.getstatic); Codee.put2(1);
		Codee.put(Codee.getstatic); Codee.put2(3);
		Codee.put(Codee.jcc + Codee.ge); Codee.put2(29);
		Codee.put(Codee.getstatic); Codee.put2(4);
		Codee.put(Codee.getstatic); Codee.put2(0);
		Codee.put(Codee.aload);
		Codee.put(Codee.getstatic); Codee.put2(1);
		Codee.put(loadInstr);
		Codee.put(Codee.getstatic); Codee.put2(5);
		Codee.put(printInstr);
		Codee.put(Codee.getstatic); Codee.put2(1);
		Codee.loadConst(1);
		Codee.put(Codee.add);
		Codee.put(Codee.putstatic); Codee.put2(1);
		Codee.put(Codee.jmp); Codee.put2(-32);
		Codee.loadConst(10);
		Codee.loadConst(4);
		Codee.put(Codee.bprint);
		Codee.put(Codee.getstatic); Codee.put2(0);
		Codee.loadConst(1);
		Codee.put(Codee.add);
		Codee.put(Codee.putstatic); Codee.put2(0);
		Codee.put(Codee.jmp); Codee.put2(-63);
	}
	
	public static void putTrueJump (int op, int adr) {
		put(jcc + op); put2(adr-pc+1);
	}
	
	public static void fixupTvf(int addr, Obj tvf) {
	    int old = pc;
	    pc = addr;
	    put4(tvf.getAdr());
	    pc = old;
	}
}
