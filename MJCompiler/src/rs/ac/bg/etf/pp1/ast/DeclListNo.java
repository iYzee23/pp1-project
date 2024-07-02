// generated with ast extension for cup
// version 0.8
// 2/6/2024 4:11:0


package rs.ac.bg.etf.pp1.ast;

public class DeclListNo extends DeclList {

    public DeclListNo () {
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
        buffer.append("DeclListNo(\n");

        buffer.append(tab);
        buffer.append(") [DeclListNo]");
        return buffer.toString();
    }
}
