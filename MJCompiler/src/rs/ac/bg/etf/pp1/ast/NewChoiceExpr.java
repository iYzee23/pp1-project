// generated with ast extension for cup
// version 0.8
// 24/7/2024 15:2:17


package rs.ac.bg.etf.pp1.ast;

public class NewChoiceExpr extends NewChoice {

    private NewLBracket NewLBracket;
    private Expr Expr;

    public NewChoiceExpr (NewLBracket NewLBracket, Expr Expr) {
        this.NewLBracket=NewLBracket;
        if(NewLBracket!=null) NewLBracket.setParent(this);
        this.Expr=Expr;
        if(Expr!=null) Expr.setParent(this);
    }

    public NewLBracket getNewLBracket() {
        return NewLBracket;
    }

    public void setNewLBracket(NewLBracket NewLBracket) {
        this.NewLBracket=NewLBracket;
    }

    public Expr getExpr() {
        return Expr;
    }

    public void setExpr(Expr Expr) {
        this.Expr=Expr;
    }

    public void accept(Visitor visitor) {
        visitor.visit(this);
    }

    public void childrenAccept(Visitor visitor) {
        if(NewLBracket!=null) NewLBracket.accept(visitor);
        if(Expr!=null) Expr.accept(visitor);
    }

    public void traverseTopDown(Visitor visitor) {
        accept(visitor);
        if(NewLBracket!=null) NewLBracket.traverseTopDown(visitor);
        if(Expr!=null) Expr.traverseTopDown(visitor);
    }

    public void traverseBottomUp(Visitor visitor) {
        if(NewLBracket!=null) NewLBracket.traverseBottomUp(visitor);
        if(Expr!=null) Expr.traverseBottomUp(visitor);
        accept(visitor);
    }

    public String toString(String tab) {
        StringBuffer buffer=new StringBuffer();
        buffer.append(tab);
        buffer.append("NewChoiceExpr(\n");

        if(NewLBracket!=null)
            buffer.append(NewLBracket.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        if(Expr!=null)
            buffer.append(Expr.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        buffer.append(tab);
        buffer.append(") [NewChoiceExpr]");
        return buffer.toString();
    }
}
