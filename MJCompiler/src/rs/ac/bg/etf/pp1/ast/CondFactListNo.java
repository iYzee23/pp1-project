// generated with ast extension for cup
// version 0.8
// 2/6/2024 4:11:1


package rs.ac.bg.etf.pp1.ast;

public class CondFactListNo extends CondFactList {

    public CondFactListNo () {
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
        buffer.append("CondFactListNo(\n");

        buffer.append(tab);
        buffer.append(") [CondFactListNo]");
        return buffer.toString();
    }
}
