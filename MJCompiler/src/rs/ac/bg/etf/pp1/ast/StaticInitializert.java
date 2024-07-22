// generated with ast extension for cup
// version 0.8
// 21/6/2024 22:47:49


package rs.ac.bg.etf.pp1.ast;

public class StaticInitializert extends StaticInitializer {

    private StaticSymbol StaticSymbol;
    private StatementList StatementList;

    public StaticInitializert (StaticSymbol StaticSymbol, StatementList StatementList) {
        this.StaticSymbol=StaticSymbol;
        if(StaticSymbol!=null) StaticSymbol.setParent(this);
        this.StatementList=StatementList;
        if(StatementList!=null) StatementList.setParent(this);
    }

    public StaticSymbol getStaticSymbol() {
        return StaticSymbol;
    }

    public void setStaticSymbol(StaticSymbol StaticSymbol) {
        this.StaticSymbol=StaticSymbol;
    }

    public StatementList getStatementList() {
        return StatementList;
    }

    public void setStatementList(StatementList StatementList) {
        this.StatementList=StatementList;
    }

    public void accept(Visitor visitor) {
        visitor.visit(this);
    }

    public void childrenAccept(Visitor visitor) {
        if(StaticSymbol!=null) StaticSymbol.accept(visitor);
        if(StatementList!=null) StatementList.accept(visitor);
    }

    public void traverseTopDown(Visitor visitor) {
        accept(visitor);
        if(StaticSymbol!=null) StaticSymbol.traverseTopDown(visitor);
        if(StatementList!=null) StatementList.traverseTopDown(visitor);
    }

    public void traverseBottomUp(Visitor visitor) {
        if(StaticSymbol!=null) StaticSymbol.traverseBottomUp(visitor);
        if(StatementList!=null) StatementList.traverseBottomUp(visitor);
        accept(visitor);
    }

    public String toString(String tab) {
        StringBuffer buffer=new StringBuffer();
        buffer.append(tab);
        buffer.append("StaticInitializert(\n");

        if(StaticSymbol!=null)
            buffer.append(StaticSymbol.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        if(StatementList!=null)
            buffer.append(StatementList.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        buffer.append(tab);
        buffer.append(") [StaticInitializert]");
        return buffer.toString();
    }
}
