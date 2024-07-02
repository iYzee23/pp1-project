package rs.ac.bg.etf.pp1;

import java.util.Collection;

import rs.etf.pp1.symboltable.Tab;
import rs.etf.pp1.symboltable.concepts.Obj;
import rs.etf.pp1.symboltable.concepts.Scope;
import rs.etf.pp1.symboltable.concepts.Struct;

public class Tabb extends Tab {
	
	public static final Struct boolType = new Struct(Struct.Bool);
	public static Scope programScope = null;
	
	public static void init() {
		Tab.init();
		Tab.insert(Obj.Type, "bool", boolType);
	}
	
	public static Obj findLocal(String name) {
		Obj res = Tab.currentScope().findSymbol(name);
		if (res == null) res = Tab.noObj;
		return res;
	}
	
	public static Obj insertStatic(int kind, String name, Struct type) {
		Obj newObj = new Obj(kind, name, type, 0, 0); 
		if (!programScope.addToLocals(newObj)) {
			Obj res = programScope.findSymbol(name);
			return (res != null) ? res : noObj;
		}
		return newObj;
	}
	
	public static Obj findStatic(String name, Struct struct) {
		for (Scope s = currentScope; s != null; s = s.getOuter()) {
			if (s.getLocals() != null) {
				Collection<Obj> objs = s.values();
				for (Obj elem: objs) {
					if (elem.getKind() == Objj.Stat && elem.getName().endsWith(name) && elem.getType().equals(struct))
						return elem;
				}
			}
		}
		return noObj;
	}
	
	public static Obj getFormalParam(Obj meth, int index) {
		Collection<Obj> localList = meth.getLocalSymbols(); 
		for (Obj elem: localList) {
			if (elem.getAdr() == index)
				return elem;
		}
		return Tabb.noObj;
	}

}
