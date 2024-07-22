// generated with ast extension for cup
// version 0.8
// 21/6/2024 22:47:49


package rs.ac.bg.etf.pp1.ast;

public class DesignatorPartsExpr extends DesignatorParts {

    private DesignatorParts DesignatorParts;
    private TLBracket TLBracket;
    private Expr Expr;
    private TRBracket TRBracket;

    public DesignatorPartsExpr (DesignatorParts DesignatorParts, TLBracket TLBracket, Expr Expr, TRBracket TRBracket) {
        this.DesignatorParts=DesignatorParts;
        if(DesignatorParts!=null) DesignatorParts.setParent(this);
        this.TLBracket=TLBracket;
        if(TLBracket!=null) TLBracket.setParent(this);
        this.Expr=Expr;
        if(Expr!=null) Expr.setParent(this);
        this.TRBracket=TRBracket;
        if(TRBracket!=null) TRBracket.setParent(this);
    }

    public DesignatorParts getDesignatorParts() {
        return DesignatorParts;
    }

    public void setDesignatorParts(DesignatorParts DesignatorParts) {
        this.DesignatorParts=DesignatorParts;
    }

    public TLBracket getTLBracket() {
        return TLBracket;
    }

    public void setTLBracket(TLBracket TLBracket) {
        this.TLBracket=TLBracket;
    }

    public Expr getExpr() {
        return Expr;
    }

    public void setExpr(Expr Expr) {
        this.Expr=Expr;
    }

    public TRBracket getTRBracket() {
        return TRBracket;
    }

    public void setTRBracket(TRBracket TRBracket) {
        this.TRBracket=TRBracket;
    }

    public void accept(Visitor visitor) {
        visitor.visit(this);
    }

    public void childrenAccept(Visitor visitor) {
        if(DesignatorParts!=null) DesignatorParts.accept(visitor);
        if(TLBracket!=null) TLBracket.accept(visitor);
        if(Expr!=null) Expr.accept(visitor);
        if(TRBracket!=null) TRBracket.accept(visitor);
    }

    public void traverseTopDown(Visitor visitor) {
        accept(visitor);
        if(DesignatorParts!=null) DesignatorParts.traverseTopDown(visitor);
        if(TLBracket!=null) TLBracket.traverseTopDown(visitor);
        if(Expr!=null) Expr.traverseTopDown(visitor);
        if(TRBracket!=null) TRBracket.traverseTopDown(visitor);
    }

    public void traverseBottomUp(Visitor visitor) {
        if(DesignatorParts!=null) DesignatorParts.traverseBottomUp(visitor);
        if(TLBracket!=null) TLBracket.traverseBottomUp(visitor);
        if(Expr!=null) Expr.traverseBottomUp(visitor);
        if(TRBracket!=null) TRBracket.traverseBottomUp(visitor);
        accept(visitor);
    }

    public String toString(String tab) {
        StringBuffer buffer=new StringBuffer();
        buffer.append(tab);
        buffer.append("DesignatorPartsExpr(\n");

        if(DesignatorParts!=null)
            buffer.append(DesignatorParts.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        if(TLBracket!=null)
            buffer.append(TLBracket.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        if(Expr!=null)
            buffer.append(Expr.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        if(TRBracket!=null)
            buffer.append(TRBracket.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        buffer.append(tab);
        buffer.append(") [DesignatorPartsExpr]");
        return buffer.toString();
    }
}
