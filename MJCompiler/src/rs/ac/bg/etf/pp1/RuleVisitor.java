package rs.ac.bg.etf.pp1;

import org.apache.log4j.Logger;
import rs.ac.bg.etf.pp1.ast.*;

public class RuleVisitor extends VisitorAdaptor {

	int printCallCount = 0;
	
	Logger log = Logger.getLogger(getClass());

	public void visit(StmtPrintNo vardecl){
		printCallCount++;
	}
	
    public void visit(StmtPrintYes print) {
		printCallCount++;
	}

}