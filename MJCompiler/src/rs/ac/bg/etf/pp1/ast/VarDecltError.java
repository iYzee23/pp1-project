// generated with ast extension for cup
// version 0.8
// 11/6/2024 22:2:1


package rs.ac.bg.etf.pp1.ast;

public class VarDecltError extends VarDecl {

    public VarDecltError () {
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
        buffer.append("VarDecltError(\n");

        buffer.append(tab);
        buffer.append(") [VarDecltError]");
        return buffer.toString();
    }
}
