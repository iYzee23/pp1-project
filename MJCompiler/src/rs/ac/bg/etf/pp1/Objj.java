package rs.ac.bg.etf.pp1;

import rs.etf.pp1.symboltable.concepts.Obj;
import rs.etf.pp1.symboltable.concepts.Struct;

public class Objj extends Obj {
	
	public static final int Namesp = 7, Stat = 8, Labl = 9;

	public Objj(int kind, String name, Struct type) {
		super(kind, name, type);
	}
	
	public Objj(int kind, String name, Struct type, int adr, int lvl) {
		super(kind, name, type, adr, lvl);
	}

}
