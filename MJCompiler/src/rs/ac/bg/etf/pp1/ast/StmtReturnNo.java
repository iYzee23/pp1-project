// generated with ast extension for cup
// version 0.8
// 24/7/2024 15:2:17


package rs.ac.bg.etf.pp1.ast;

public class StmtReturnNo extends Statement {

    public StmtReturnNo () {
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
        buffer.append("StmtReturnNo(\n");

        buffer.append(tab);
        buffer.append(") [StmtReturnNo]");
        return buffer.toString();
    }
}
