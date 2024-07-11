// generated with ast extension for cup
// version 0.8
// 11/6/2024 22:2:1


package rs.ac.bg.etf.pp1.ast;

public class FactorDesignatorFirst extends Factor {

    private Designator Designator;
    private TLParen TLParen;
    private ActPars ActPars;
    private TRParen TRParen;

    public FactorDesignatorFirst (Designator Designator, TLParen TLParen, ActPars ActPars, TRParen TRParen) {
        this.Designator=Designator;
        if(Designator!=null) Designator.setParent(this);
        this.TLParen=TLParen;
        if(TLParen!=null) TLParen.setParent(this);
        this.ActPars=ActPars;
        if(ActPars!=null) ActPars.setParent(this);
        this.TRParen=TRParen;
        if(TRParen!=null) TRParen.setParent(this);
    }

    public Designator getDesignator() {
        return Designator;
    }

    public void setDesignator(Designator Designator) {
        this.Designator=Designator;
    }

    public TLParen getTLParen() {
        return TLParen;
    }

    public void setTLParen(TLParen TLParen) {
        this.TLParen=TLParen;
    }

    public ActPars getActPars() {
        return ActPars;
    }

    public void setActPars(ActPars ActPars) {
        this.ActPars=ActPars;
    }

    public TRParen getTRParen() {
        return TRParen;
    }

    public void setTRParen(TRParen TRParen) {
        this.TRParen=TRParen;
    }

    public void accept(Visitor visitor) {
        visitor.visit(this);
    }

    public void childrenAccept(Visitor visitor) {
        if(Designator!=null) Designator.accept(visitor);
        if(TLParen!=null) TLParen.accept(visitor);
        if(ActPars!=null) ActPars.accept(visitor);
        if(TRParen!=null) TRParen.accept(visitor);
    }

    public void traverseTopDown(Visitor visitor) {
        accept(visitor);
        if(Designator!=null) Designator.traverseTopDown(visitor);
        if(TLParen!=null) TLParen.traverseTopDown(visitor);
        if(ActPars!=null) ActPars.traverseTopDown(visitor);
        if(TRParen!=null) TRParen.traverseTopDown(visitor);
    }

    public void traverseBottomUp(Visitor visitor) {
        if(Designator!=null) Designator.traverseBottomUp(visitor);
        if(TLParen!=null) TLParen.traverseBottomUp(visitor);
        if(ActPars!=null) ActPars.traverseBottomUp(visitor);
        if(TRParen!=null) TRParen.traverseBottomUp(visitor);
        accept(visitor);
    }

    public String toString(String tab) {
        StringBuffer buffer=new StringBuffer();
        buffer.append(tab);
        buffer.append("FactorDesignatorFirst(\n");

        if(Designator!=null)
            buffer.append(Designator.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        if(TLParen!=null)
            buffer.append(TLParen.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        if(ActPars!=null)
            buffer.append(ActPars.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        if(TRParen!=null)
            buffer.append(TRParen.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        buffer.append(tab);
        buffer.append(") [FactorDesignatorFirst]");
        return buffer.toString();
    }
}
