// generated with ast extension for cup
// version 0.8
// 24/7/2024 15:2:17


package rs.ac.bg.etf.pp1.ast;

public class StmtIfElseError extends Statement {

    private IfSymbol IfSymbol;
    private Statement Statement;
    private ElseOpt ElseOpt;

    public StmtIfElseError (IfSymbol IfSymbol, Statement Statement, ElseOpt ElseOpt) {
        this.IfSymbol=IfSymbol;
        if(IfSymbol!=null) IfSymbol.setParent(this);
        this.Statement=Statement;
        if(Statement!=null) Statement.setParent(this);
        this.ElseOpt=ElseOpt;
        if(ElseOpt!=null) ElseOpt.setParent(this);
    }

    public IfSymbol getIfSymbol() {
        return IfSymbol;
    }

    public void setIfSymbol(IfSymbol IfSymbol) {
        this.IfSymbol=IfSymbol;
    }

    public Statement getStatement() {
        return Statement;
    }

    public void setStatement(Statement Statement) {
        this.Statement=Statement;
    }

    public ElseOpt getElseOpt() {
        return ElseOpt;
    }

    public void setElseOpt(ElseOpt ElseOpt) {
        this.ElseOpt=ElseOpt;
    }

    public void accept(Visitor visitor) {
        visitor.visit(this);
    }

    public void childrenAccept(Visitor visitor) {
        if(IfSymbol!=null) IfSymbol.accept(visitor);
        if(Statement!=null) Statement.accept(visitor);
        if(ElseOpt!=null) ElseOpt.accept(visitor);
    }

    public void traverseTopDown(Visitor visitor) {
        accept(visitor);
        if(IfSymbol!=null) IfSymbol.traverseTopDown(visitor);
        if(Statement!=null) Statement.traverseTopDown(visitor);
        if(ElseOpt!=null) ElseOpt.traverseTopDown(visitor);
    }

    public void traverseBottomUp(Visitor visitor) {
        if(IfSymbol!=null) IfSymbol.traverseBottomUp(visitor);
        if(Statement!=null) Statement.traverseBottomUp(visitor);
        if(ElseOpt!=null) ElseOpt.traverseBottomUp(visitor);
        accept(visitor);
    }

    public String toString(String tab) {
        StringBuffer buffer=new StringBuffer();
        buffer.append(tab);
        buffer.append("StmtIfElseError(\n");

        if(IfSymbol!=null)
            buffer.append(IfSymbol.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        if(Statement!=null)
            buffer.append(Statement.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        if(ElseOpt!=null)
            buffer.append(ElseOpt.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        buffer.append(tab);
        buffer.append(") [StmtIfElseError]");
        return buffer.toString();
    }
}
