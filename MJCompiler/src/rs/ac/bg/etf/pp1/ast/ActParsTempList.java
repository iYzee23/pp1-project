// generated with ast extension for cup
// version 0.8
// 11/6/2024 22:2:1


package rs.ac.bg.etf.pp1.ast;

public class ActParsTempList extends ActParsTemp {

    private ActParsTemp ActParsTemp;
    private Expr Expr;

    public ActParsTempList (ActParsTemp ActParsTemp, Expr Expr) {
        this.ActParsTemp=ActParsTemp;
        if(ActParsTemp!=null) ActParsTemp.setParent(this);
        this.Expr=Expr;
        if(Expr!=null) Expr.setParent(this);
    }

    public ActParsTemp getActParsTemp() {
        return ActParsTemp;
    }

    public void setActParsTemp(ActParsTemp ActParsTemp) {
        this.ActParsTemp=ActParsTemp;
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
        if(ActParsTemp!=null) ActParsTemp.accept(visitor);
        if(Expr!=null) Expr.accept(visitor);
    }

    public void traverseTopDown(Visitor visitor) {
        accept(visitor);
        if(ActParsTemp!=null) ActParsTemp.traverseTopDown(visitor);
        if(Expr!=null) Expr.traverseTopDown(visitor);
    }

    public void traverseBottomUp(Visitor visitor) {
        if(ActParsTemp!=null) ActParsTemp.traverseBottomUp(visitor);
        if(Expr!=null) Expr.traverseBottomUp(visitor);
        accept(visitor);
    }

    public String toString(String tab) {
        StringBuffer buffer=new StringBuffer();
        buffer.append(tab);
        buffer.append("ActParsTempList(\n");

        if(ActParsTemp!=null)
            buffer.append(ActParsTemp.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        if(Expr!=null)
            buffer.append(Expr.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        buffer.append(tab);
        buffer.append(") [ActParsTempList]");
        return buffer.toString();
    }
}
