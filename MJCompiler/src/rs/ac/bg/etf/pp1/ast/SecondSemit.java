// generated with ast extension for cup
// version 0.8
// 20/6/2024 21:1:26


package rs.ac.bg.etf.pp1.ast;

public class SecondSemit extends SecondSemi {

    public SecondSemit () {
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
        buffer.append("SecondSemit(\n");

        buffer.append(tab);
        buffer.append(") [SecondSemit]");
        return buffer.toString();
    }
}
