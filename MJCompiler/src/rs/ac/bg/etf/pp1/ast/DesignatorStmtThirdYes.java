// generated with ast extension for cup
// version 0.8
// 21/6/2024 22:47:49


package rs.ac.bg.etf.pp1.ast;

public class DesignatorStmtThirdYes extends DesignatorStatement {

    private Designator Designator;
    private LCFor LCFor;
    private Expr Expr;
    private LCIn LCIn;
    private Designator Designator1;
    private LCIf LCIf;
    private Condition Condition;

    public DesignatorStmtThirdYes (Designator Designator, LCFor LCFor, Expr Expr, LCIn LCIn, Designator Designator1, LCIf LCIf, Condition Condition) {
        this.Designator=Designator;
        if(Designator!=null) Designator.setParent(this);
        this.LCFor=LCFor;
        if(LCFor!=null) LCFor.setParent(this);
        this.Expr=Expr;
        if(Expr!=null) Expr.setParent(this);
        this.LCIn=LCIn;
        if(LCIn!=null) LCIn.setParent(this);
        this.Designator1=Designator1;
        if(Designator1!=null) Designator1.setParent(this);
        this.LCIf=LCIf;
        if(LCIf!=null) LCIf.setParent(this);
        this.Condition=Condition;
        if(Condition!=null) Condition.setParent(this);
    }

    public Designator getDesignator() {
        return Designator;
    }

    public void setDesignator(Designator Designator) {
        this.Designator=Designator;
    }

    public LCFor getLCFor() {
        return LCFor;
    }

    public void setLCFor(LCFor LCFor) {
        this.LCFor=LCFor;
    }

    public Expr getExpr() {
        return Expr;
    }

    public void setExpr(Expr Expr) {
        this.Expr=Expr;
    }

    public LCIn getLCIn() {
        return LCIn;
    }

    public void setLCIn(LCIn LCIn) {
        this.LCIn=LCIn;
    }

    public Designator getDesignator1() {
        return Designator1;
    }

    public void setDesignator1(Designator Designator1) {
        this.Designator1=Designator1;
    }

    public LCIf getLCIf() {
        return LCIf;
    }

    public void setLCIf(LCIf LCIf) {
        this.LCIf=LCIf;
    }

    public Condition getCondition() {
        return Condition;
    }

    public void setCondition(Condition Condition) {
        this.Condition=Condition;
    }

    public void accept(Visitor visitor) {
        visitor.visit(this);
    }

    public void childrenAccept(Visitor visitor) {
        if(Designator!=null) Designator.accept(visitor);
        if(LCFor!=null) LCFor.accept(visitor);
        if(Expr!=null) Expr.accept(visitor);
        if(LCIn!=null) LCIn.accept(visitor);
        if(Designator1!=null) Designator1.accept(visitor);
        if(LCIf!=null) LCIf.accept(visitor);
        if(Condition!=null) Condition.accept(visitor);
    }

    public void traverseTopDown(Visitor visitor) {
        accept(visitor);
        if(Designator!=null) Designator.traverseTopDown(visitor);
        if(LCFor!=null) LCFor.traverseTopDown(visitor);
        if(Expr!=null) Expr.traverseTopDown(visitor);
        if(LCIn!=null) LCIn.traverseTopDown(visitor);
        if(Designator1!=null) Designator1.traverseTopDown(visitor);
        if(LCIf!=null) LCIf.traverseTopDown(visitor);
        if(Condition!=null) Condition.traverseTopDown(visitor);
    }

    public void traverseBottomUp(Visitor visitor) {
        if(Designator!=null) Designator.traverseBottomUp(visitor);
        if(LCFor!=null) LCFor.traverseBottomUp(visitor);
        if(Expr!=null) Expr.traverseBottomUp(visitor);
        if(LCIn!=null) LCIn.traverseBottomUp(visitor);
        if(Designator1!=null) Designator1.traverseBottomUp(visitor);
        if(LCIf!=null) LCIf.traverseBottomUp(visitor);
        if(Condition!=null) Condition.traverseBottomUp(visitor);
        accept(visitor);
    }

    public String toString(String tab) {
        StringBuffer buffer=new StringBuffer();
        buffer.append(tab);
        buffer.append("DesignatorStmtThirdYes(\n");

        if(Designator!=null)
            buffer.append(Designator.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        if(LCFor!=null)
            buffer.append(LCFor.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        if(Expr!=null)
            buffer.append(Expr.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        if(LCIn!=null)
            buffer.append(LCIn.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        if(Designator1!=null)
            buffer.append(Designator1.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        if(LCIf!=null)
            buffer.append(LCIf.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        if(Condition!=null)
            buffer.append(Condition.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        buffer.append(tab);
        buffer.append(") [DesignatorStmtThirdYes]");
        return buffer.toString();
    }
}
