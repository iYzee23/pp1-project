// generated with ast extension for cup
// version 0.8
// 20/6/2024 21:1:26


package rs.ac.bg.etf.pp1.ast;

public class FirstSemit extends FirstSemi {

    public FirstSemit () {
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
        buffer.append("FirstSemit(\n");

        buffer.append(tab);
        buffer.append(") [FirstSemit]");
        return buffer.toString();
    }
}
