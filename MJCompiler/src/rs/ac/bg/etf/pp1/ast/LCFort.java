// generated with ast extension for cup
// version 0.8
// 23/6/2024 15:28:50


package rs.ac.bg.etf.pp1.ast;

public class LCFort extends LCFor {

    public LCFort () {
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
        buffer.append("LCFort(\n");

        buffer.append(tab);
        buffer.append(") [LCFort]");
        return buffer.toString();
    }
}