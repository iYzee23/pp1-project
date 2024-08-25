// generated with ast extension for cup
// version 0.8
// 24/7/2024 15:2:17


package rs.ac.bg.etf.pp1.ast;

public class NewChoiceExprExpr extends NewChoice {

    private NewLBracket NewLBracket;
    private Expr Expr;
    private NewLBracket NewLBracket1;
    private Expr Expr2;

    public NewChoiceExprExpr (NewLBracket NewLBracket, Expr Expr, NewLBracket NewLBracket1, Expr Expr2) {
        this.NewLBracket=NewLBracket;
        if(NewLBracket!=null) NewLBracket.setParent(this);
        this.Expr=Expr;
        if(Expr!=null) Expr.setParent(this);
        this.NewLBracket1=NewLBracket1;
        if(NewLBracket1!=null) NewLBracket1.setParent(this);
        this.Expr2=Expr2;
        if(Expr2!=null) Expr2.setParent(this);
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

    public NewLBracket getNewLBracket1() {
        return NewLBracket1;
    }

    public void setNewLBracket1(NewLBracket NewLBracket1) {
        this.NewLBracket1=NewLBracket1;
    }

    public Expr getExpr2() {
        return Expr2;
    }

    public void setExpr2(Expr Expr2) {
        this.Expr2=Expr2;
    }

    public void accept(Visitor visitor) {
        visitor.visit(this);
    }

    public void childrenAccept(Visitor visitor) {
        if(NewLBracket!=null) NewLBracket.accept(visitor);
        if(Expr!=null) Expr.accept(visitor);
        if(NewLBracket1!=null) NewLBracket1.accept(visitor);
        if(Expr2!=null) Expr2.accept(visitor);
    }

    public void traverseTopDown(Visitor visitor) {
        accept(visitor);
        if(NewLBracket!=null) NewLBracket.traverseTopDown(visitor);
        if(Expr!=null) Expr.traverseTopDown(visitor);
        if(NewLBracket1!=null) NewLBracket1.traverseTopDown(visitor);
        if(Expr2!=null) Expr2.traverseTopDown(visitor);
    }

    public void traverseBottomUp(Visitor visitor) {
        if(NewLBracket!=null) NewLBracket.traverseBottomUp(visitor);
        if(Expr!=null) Expr.traverseBottomUp(visitor);
        if(NewLBracket1!=null) NewLBracket1.traverseBottomUp(visitor);
        if(Expr2!=null) Expr2.traverseBottomUp(visitor);
        accept(visitor);
    }

    public String toString(String tab) {
        StringBuffer buffer=new StringBuffer();
        buffer.append(tab);
        buffer.append("NewChoiceExprExpr(\n");

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

        if(NewLBracket1!=null)
            buffer.append(NewLBracket1.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        if(Expr2!=null)
            buffer.append(Expr2.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        buffer.append(tab);
        buffer.append(") [NewChoiceExprExpr]");
        return buffer.toString();
    }
}
