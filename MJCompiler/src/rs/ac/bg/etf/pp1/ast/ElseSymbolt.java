// generated with ast extension for cup
// version 0.8
// 20/6/2024 2:25:22


package rs.ac.bg.etf.pp1.ast;

public class ElseSymbolt extends ElseSymbol {

    public ElseSymbolt () {
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
        buffer.append("ElseSymbolt(\n");

        buffer.append(tab);
        buffer.append(") [ElseSymbolt]");
        return buffer.toString();
    }
}