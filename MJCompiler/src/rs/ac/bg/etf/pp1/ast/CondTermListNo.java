// generated with ast extension for cup
// version 0.8
// 20/6/2024 2:25:22


package rs.ac.bg.etf.pp1.ast;

public class CondTermListNo extends CondTermList {

    public CondTermListNo () {
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
        buffer.append("CondTermListNo(\n");

        buffer.append(tab);
        buffer.append(") [CondTermListNo]");
        return buffer.toString();
    }
}
