// generated with ast extension for cup
// version 0.8
// 14/6/2024 4:27:56


package rs.ac.bg.etf.pp1.ast;

public class RelopGrtEqual extends Relop {

    public RelopGrtEqual () {
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
        buffer.append("RelopGrtEqual(\n");

        buffer.append(tab);
        buffer.append(") [RelopGrtEqual]");
        return buffer.toString();
    }
}
