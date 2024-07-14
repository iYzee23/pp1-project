package rs.ac.bg.etf.pp1;

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
	    			put(getstatic); put2(o.getAdr()); 
	    			break; 
	    		}
	    		if (0 <= o.getAdr() && o.getAdr() <= 3) 
	    			put(load_n + o.getAdr());
	    		else { 
	    			put(load); put(o.getAdr()); 
	    		} 
	    		break;
	    	  
	    	case Objj.Stat:
	    		put(getstatic); put2(o.getAdr());
	    		break;
	    	  
	    	case Obj.Fld:
	    		put(getfield); put2(o.getAdr()); 
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
					put(putstatic); put2(o.getAdr()); 
					break;
		  		}
		  		if (0 <= o.getAdr() && o.getAdr() <= 3) 
		  			put(store_n + o.getAdr());
		  		else { 
		  			put(store); put(o.getAdr()); 
		  		} 
		  		break;
		  		
		  	case Objj.Stat:
		  		put(putstatic); put2(o.getAdr()); 
	  			break;

		  	case Obj.Fld:
		  		put(putfield); put2(o.getAdr()); 
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

	public static void handleLoadDesignator(Obj dsgObj, boolean classMeth, boolean first) {
		switch(dsgObj.getKind()) {
			case Obj.Var:
				if (dsgObj.getLevel() == 0) {
					put(getstatic);
					put2(dsgObj.getAdr());
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
				if (first) put(load_n);
				put(getfield);
				put2(dsgObj.getAdr());
				break;
				
			case Obj.Meth:
				if (classMeth) {
					if (first) {
						put(load_n);
						// args
						// load_0
						// getfield 0
						// invokevirtual name -1
					}
					else {
						put(dup);
						// arg
						// dup_x1
						// pop
						// ...
						// getfield 0
						// invokevirtual name -1
					}
				}
				else {
					// args
					// call offset
				}
				break;
				
			case Objj.Stat:
				put(getstatic);
				put2(dsgObj.getAdr());
				break;
				
			case Obj.Con:
				loadConst(dsgObj.getAdr());
				break;
				
			case Objj.Elem:
				// expr should put himself on the stack
				// that is the array's index for this elem
				if (dsgObj.getType().equals(Tabb.charType)) put(baload);
				else if (dsgObj.getType().equals(Tabb.boolType)) put(baload);
				else put(aload);
				break;
				
			default:
				break;
		}
	}
	
	
	public static void handleStoreDesignator(Obj dsgObj) {
		switch(dsgObj.getKind()) {
			case Obj.Var:
				// val
				if (dsgObj.getLevel() == 0) {
					put(putstatic); 
					put2(dsgObj.getAdr()); 
		  		}
				else if (0 <= dsgObj.getAdr() && dsgObj.getAdr() <= 3) 
		  			put(store_n + dsgObj.getAdr());
		  		else { 
		  			put(store); 
		  			put(dsgObj.getAdr()); 
		  		} 
				break;
				
			case Obj.Fld:
				// if (first) put(load_n);
				// val
				put(putfield);
				put2(dsgObj.getAdr());
				break;
				
			case Objj.Stat:
				// val
				put(putstatic);
				put2(dsgObj.getAdr());
				break;
				
			case Objj.Elem:
				// expr should put himself on the stack
				// that is the array's index for this elem
				// val
				if (dsgObj.getType().equals(Tabb.charType)) put(bastore);
				else if (dsgObj.getType().equals(Tabb.boolType)) put(bastore);
				else put(astore);
				break;
				
			default:
				break;
		}
	}
}