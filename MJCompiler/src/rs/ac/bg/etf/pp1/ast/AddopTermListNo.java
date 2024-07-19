// generated with ast extension for cup
// version 0.8
// 19/6/2024 17:32:25


package rs.ac.bg.etf.pp1.ast;

public class AddopTermListNo extends AddopTermList {

    public AddopTermListNo () {
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
        buffer.append("AddopTermListNo(\n");

        buffer.append(tab);
        buffer.append(") [AddopTermListNo]");
        return buffer.toString();
    }
}
