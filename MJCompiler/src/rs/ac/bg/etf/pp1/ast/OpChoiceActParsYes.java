// generated with ast extension for cup
// version 0.8
// 20/6/2024 2:25:22


package rs.ac.bg.etf.pp1.ast;

public class OpChoiceActParsYes extends OpChoice {

    private TLParen TLParen;
    private ActPars ActPars;
    private TRParen TRParen;

    public OpChoiceActParsYes (TLParen TLParen, ActPars ActPars, TRParen TRParen) {
        this.TLParen=TLParen;
        if(TLParen!=null) TLParen.setParent(this);
        this.ActPars=ActPars;
        if(ActPars!=null) ActPars.setParent(this);
        this.TRParen=TRParen;
        if(TRParen!=null) TRParen.setParent(this);
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
        if(TLParen!=null) TLParen.accept(visitor);
        if(ActPars!=null) ActPars.accept(visitor);
        if(TRParen!=null) TRParen.accept(visitor);
    }

    public void traverseTopDown(Visitor visitor) {
        accept(visitor);
        if(TLParen!=null) TLParen.traverseTopDown(visitor);
        if(ActPars!=null) ActPars.traverseTopDown(visitor);
        if(TRParen!=null) TRParen.traverseTopDown(visitor);
    }

    public void traverseBottomUp(Visitor visitor) {
        if(TLParen!=null) TLParen.traverseBottomUp(visitor);
        if(ActPars!=null) ActPars.traverseBottomUp(visitor);
        if(TRParen!=null) TRParen.traverseBottomUp(visitor);
        accept(visitor);
    }

    public String toString(String tab) {
        StringBuffer buffer=new StringBuffer();
        buffer.append(tab);
        buffer.append("OpChoiceActParsYes(\n");

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
        buffer.append(") [OpChoiceActParsYes]");
        return buffer.toString();
    }
}
