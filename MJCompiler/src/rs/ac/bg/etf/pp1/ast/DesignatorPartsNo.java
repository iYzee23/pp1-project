// generated with ast extension for cup
// version 0.8
// 28/5/2024 23:52:22


package rs.ac.bg.etf.pp1.ast;

public class DesignatorPartsNo extends DesignatorParts {

    public DesignatorPartsNo () {
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
        buffer.append("DesignatorPartsNo(\n");

        buffer.append(tab);
        buffer.append(") [DesignatorPartsNo]");
        return buffer.toString();
    }
}