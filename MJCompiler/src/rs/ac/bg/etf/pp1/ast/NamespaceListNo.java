// generated with ast extension for cup
// version 0.8
// 2/6/2024 4:11:0


package rs.ac.bg.etf.pp1.ast;

public class NamespaceListNo extends NamespaceList {

    public NamespaceListNo () {
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
        buffer.append("NamespaceListNo(\n");

        buffer.append(tab);
        buffer.append(") [NamespaceListNo]");
        return buffer.toString();
    }
}
