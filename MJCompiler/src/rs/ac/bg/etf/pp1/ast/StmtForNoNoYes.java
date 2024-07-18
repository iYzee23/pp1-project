// generated with ast extension for cup
// version 0.8
// 18/6/2024 20:37:50


package rs.ac.bg.etf.pp1.ast;

public class StmtForNoNoYes extends Statement {

    private ForSymbol ForSymbol;
    private FirstSemi FirstSemi;
    private SecondSemi SecondSemi;
    private DesignatorStatement DesignatorStatement;
    private DesignatorStmtList DesignatorStmtList;
    private RParenFor RParenFor;
    private Statement Statement;

    public StmtForNoNoYes (ForSymbol ForSymbol, FirstSemi FirstSemi, SecondSemi SecondSemi, DesignatorStatement DesignatorStatement, DesignatorStmtList DesignatorStmtList, RParenFor RParenFor, Statement Statement) {
        this.ForSymbol=ForSymbol;
        if(ForSymbol!=null) ForSymbol.setParent(this);
        this.FirstSemi=FirstSemi;
        if(FirstSemi!=null) FirstSemi.setParent(this);
        this.SecondSemi=SecondSemi;
        if(SecondSemi!=null) SecondSemi.setParent(this);
        this.DesignatorStatement=DesignatorStatement;
        if(DesignatorStatement!=null) DesignatorStatement.setParent(this);
        this.DesignatorStmtList=DesignatorStmtList;
        if(DesignatorStmtList!=null) DesignatorStmtList.setParent(this);
        this.RParenFor=RParenFor;
        if(RParenFor!=null) RParenFor.setParent(this);
        this.Statement=Statement;
        if(Statement!=null) Statement.setParent(this);
    }

    public ForSymbol getForSymbol() {
        return ForSymbol;
    }

    public void setForSymbol(ForSymbol ForSymbol) {
        this.ForSymbol=ForSymbol;
    }

    public FirstSemi getFirstSemi() {
        return FirstSemi;
    }

    public void setFirstSemi(FirstSemi FirstSemi) {
        this.FirstSemi=FirstSemi;
    }

    public SecondSemi getSecondSemi() {
        return SecondSemi;
    }

    public void setSecondSemi(SecondSemi SecondSemi) {
        this.SecondSemi=SecondSemi;
    }

    public DesignatorStatement getDesignatorStatement() {
        return DesignatorStatement;
    }

    public void setDesignatorStatement(DesignatorStatement DesignatorStatement) {
        this.DesignatorStatement=DesignatorStatement;
    }

    public DesignatorStmtList getDesignatorStmtList() {
        return DesignatorStmtList;
    }

    public void setDesignatorStmtList(DesignatorStmtList DesignatorStmtList) {
        this.DesignatorStmtList=DesignatorStmtList;
    }

    public RParenFor getRParenFor() {
        return RParenFor;
    }

    public void setRParenFor(RParenFor RParenFor) {
        this.RParenFor=RParenFor;
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
        if(ForSymbol!=null) ForSymbol.accept(visitor);
        if(FirstSemi!=null) FirstSemi.accept(visitor);
        if(SecondSemi!=null) SecondSemi.accept(visitor);
        if(DesignatorStatement!=null) DesignatorStatement.accept(visitor);
        if(DesignatorStmtList!=null) DesignatorStmtList.accept(visitor);
        if(RParenFor!=null) RParenFor.accept(visitor);
        if(Statement!=null) Statement.accept(visitor);
    }

    public void traverseTopDown(Visitor visitor) {
        accept(visitor);
        if(ForSymbol!=null) ForSymbol.traverseTopDown(visitor);
        if(FirstSemi!=null) FirstSemi.traverseTopDown(visitor);
        if(SecondSemi!=null) SecondSemi.traverseTopDown(visitor);
        if(DesignatorStatement!=null) DesignatorStatement.traverseTopDown(visitor);
        if(DesignatorStmtList!=null) DesignatorStmtList.traverseTopDown(visitor);
        if(RParenFor!=null) RParenFor.traverseTopDown(visitor);
        if(Statement!=null) Statement.traverseTopDown(visitor);
    }

    public void traverseBottomUp(Visitor visitor) {
        if(ForSymbol!=null) ForSymbol.traverseBottomUp(visitor);
        if(FirstSemi!=null) FirstSemi.traverseBottomUp(visitor);
        if(SecondSemi!=null) SecondSemi.traverseBottomUp(visitor);
        if(DesignatorStatement!=null) DesignatorStatement.traverseBottomUp(visitor);
        if(DesignatorStmtList!=null) DesignatorStmtList.traverseBottomUp(visitor);
        if(RParenFor!=null) RParenFor.traverseBottomUp(visitor);
        if(Statement!=null) Statement.traverseBottomUp(visitor);
        accept(visitor);
    }

    public String toString(String tab) {
        StringBuffer buffer=new StringBuffer();
        buffer.append(tab);
        buffer.append("StmtForNoNoYes(\n");

        if(ForSymbol!=null)
            buffer.append(ForSymbol.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        if(FirstSemi!=null)
            buffer.append(FirstSemi.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        if(SecondSemi!=null)
            buffer.append(SecondSemi.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        if(DesignatorStatement!=null)
            buffer.append(DesignatorStatement.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        if(DesignatorStmtList!=null)
            buffer.append(DesignatorStmtList.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        if(RParenFor!=null)
            buffer.append(RParenFor.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        if(Statement!=null)
            buffer.append(Statement.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        buffer.append(tab);
        buffer.append(") [StmtForNoNoYes]");
        return buffer.toString();
    }
}
