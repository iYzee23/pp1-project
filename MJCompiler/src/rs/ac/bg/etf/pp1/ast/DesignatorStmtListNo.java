// generated with ast extension for cup
// version 0.8
// 21/6/2024 22:47:49


package rs.ac.bg.etf.pp1.ast;

public class DesignatorStmtListNo extends DesignatorStmtList {

    public DesignatorStmtListNo () {
    }

    public void accept(Visitor visitor) {
        visitor.visit(this);
    }

    public void childrenAccept(Visitor visitor) {
    }

    public void traverseTopDown(Visitor visitor) {
        accept(visitor);
    }

    public void traverseBottomUp(Visitor visitor) {
        accept(visitor);
    }

    public String toString(String tab) {
        StringBuffer buffer=new StringBuffer();
        buffer.append(tab);
        buffer.append("DesignatorStmtListNo(\n");

        buffer.append(tab);
        buffer.append(") [DesignatorStmtListNo]");
        return buffer.toString();
    }
}
