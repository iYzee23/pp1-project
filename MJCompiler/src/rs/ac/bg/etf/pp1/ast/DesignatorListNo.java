// generated with ast extension for cup
// version 0.8
// 21/6/2024 18:3:57


package rs.ac.bg.etf.pp1.ast;

public class DesignatorListNo extends DesignatorList {

    public DesignatorListNo () {
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
        buffer.append("DesignatorListNo(\n");

        buffer.append(tab);
        buffer.append(") [DesignatorListNo]");
        return buffer.toString();
    }
}
