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
	
	public static Obj findOuterLocal(String name) {
		Obj res = Tab.currentScope().getOuter().findSymbol(name);
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
	
	public static Obj findStatic(String name, Struct searchType) {
		Obj resObj = Tabb.noObj;
		int minDistance = 9999;
		
		if (programScope.getLocals() != null) {
			Collection<Obj> objs = programScope.values();
			for (Obj elem: objs) {
				if (elem.getKind() == Objj.Stat && elem.getName().endsWith(name)) {
					int lastIndexOf = elem.getName().lastIndexOf("::");
					if (lastIndexOf != -1) {
						String goalPart = elem.getName().substring(0, lastIndexOf);
						Struct goalType = Tabb.programScope.findSymbol(goalPart).getType();
						Struct currentType = searchType;
				        int currDistance = 0;
				        
				        while (currentType != null) {
				            if (currentType.equals(goalType) && currDistance < minDistance) {
				                minDistance = currDistance;
				                resObj = elem;
				                break;
				            } 
				            ++currDistance;
				            currentType = currentType.getElemType();
				        }	
					}
				}
				if (minDistance == 0) break;
			}
		}
		return resObj;
	}
	
	public static Obj findExactStatic(String name) {
		Obj resObj = Tabb.noObj;
		if (programScope.getLocals() != null) {
			resObj = programScope.getLocals().searchKey(name);
			if (resObj == null) resObj = Tabb.noObj;
		}
		return resObj;
	}
	
	public static Obj getFormalParam(Obj meth, int index) {
		Collection<Obj> localList = meth.getLocalSymbols(); 
		for (Obj elem: localList) {
			if (elem.getAdr() == index)
				return elem;
		}
		return Tabb.noObj;
	}
	
	public static void copyFromSuperclass(Struct currClass, Struct superClass) {
		Collection<Obj> members = superClass.getMembers();
		for (Obj elem: members) {
			Obj newElem = Tabb.insert(elem.getKind(), elem.getName(), elem.getType());
			newElem.setAdr(elem.getAdr());
			newElem.setLevel(elem.getLevel());
			
			if (elem.getKind() == Obj.Meth) {
				Collection<Obj> methMembers = elem.getLocalSymbols();
				
				Tabb.openScope();
				for (Obj mElem: methMembers) {
					Obj mNewElem = Tabb.insert(mElem.getKind(), mElem.getName(), mElem.getName().equals("this") ? currClass : mElem.getType());
					mNewElem.setAdr(mElem.getAdr());
					mNewElem.setLevel(mElem.getLevel());
				}
				Tabb.chainLocalSymbols(newElem);
				Tabb.closeScope();
			}
		}
	}
	
	public static boolean compareTwoMethods(Obj firstMeth, Obj secondMeth) {
		int len = firstMeth.getLevel();
		if (secondMeth.getLevel() != len)
			return false;
		
		for (int i = 0; i < len; ++i) {
			Obj fVar = getFormalParam(firstMeth, i);
			Obj sVar = getFormalParam(secondMeth, i);
			if (!fVar.getType().equals(sVar.getType()))
				return false;
		}
		return true;
	}
	
	public static void overrideMethod(Obj overrideMeth) {
		Tabb.currentScope().getLocals().deleteKey(overrideMeth.getName());
		Tabb.currentScope().getLocals().insertKey(overrideMeth);
		overrideMeth.setFpPos(1);
	}
	
	public static boolean firstAssignableToSecond(Struct first, Struct second) {
		while (first != null) {
			if (first.assignableTo(second)) return true;
			first = first.getElemType();
		}
		return false;
	}
	
	public static int getAndUpdateNumGlobStat() {
		int nVars = 0;
		Collection<Obj> symbs = Tabb.programScope.values();
		
		for (Obj elem: symbs)
			if (elem.getKind() == Obj.Var)
				++nVars;
		
		for (Obj elem: symbs)
			if (elem.getKind() == Objj.Stat)
				elem.setAdr(nVars++);
		
		return nVars;
	}

	public static Obj findLocsMethod(Obj node, String name) {
		Collection<Obj> locs = node.getLocalSymbols();
		for (Obj elem: locs)
			if (elem.getName().equals(name))
				return elem;
		return noObj;
	}
	
	public static Obj findLocsClass(Struct node, String name) {
		Collection<Obj> locs = node.getMembers();
		for (Obj elem: locs)
			if (elem.getName().equals(name))
				return elem;
		return noObj;
	}
	
}
