// generated with ast extension for cup
// version 0.8
// 21/6/2024 22:47:49


package rs.ac.bg.etf.pp1.ast;

public class ElseOptYes extends ElseOpt {

    private ElseSymbol ElseSymbol;
    private Statement Statement;

    public ElseOptYes (ElseSymbol ElseSymbol, Statement Statement) {
        this.ElseSymbol=ElseSymbol;
        if(ElseSymbol!=null) ElseSymbol.setParent(this);
        this.Statement=Statement;
        if(Statement!=null) Statement.setParent(this);
    }

    public ElseSymbol getElseSymbol() {
        return ElseSymbol;
    }

    public void setElseSymbol(ElseSymbol ElseSymbol) {
        this.ElseSymbol=ElseSymbol;
    }

    public Statement getStatement() {
        return Statement;
    }

    public void setStatement(Statement Statement) {
        this.Statement=Statement;
    }

    public void accept(Visitor visitor) {
        visitor.visit(this);
    }

    public void childrenAccept(Visitor visitor) {
        if(ElseSymbol!=null) ElseSymbol.accept(visitor);
        if(Statement!=null) Statement.accept(visitor);
    }

    public void traverseTopDown(Visitor visitor) {
        accept(visitor);
        if(ElseSymbol!=null) ElseSymbol.traverseTopDown(visitor);
        if(Statement!=null) Statement.traverseTopDown(visitor);
    }

    public void traverseBottomUp(Visitor visitor) {
        if(ElseSymbol!=null) ElseSymbol.traverseBottomUp(visitor);
        if(Statement!=null) Statement.traverseBottomUp(visitor);
        accept(visitor);
    }

    public String toString(String tab) {
        StringBuffer buffer=new StringBuffer();
        buffer.append(tab);
        buffer.append("ElseOptYes(\n");

        if(ElseSymbol!=null)
            buffer.append(ElseSymbol.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        if(Statement!=null)
            buffer.append(Statement.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        buffer.append(tab);
        buffer.append(") [ElseOptYes]");
        return buffer.toString();
    }
}
