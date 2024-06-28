// generated with ast extension for cup
// version 0.8
// 28/5/2024 23:52:22


package rs.ac.bg.etf.pp1.ast;

public class DesignatorStmtFirst extends DesignatorStatement {

    private Designator Designator;
    private OpChoice OpChoice;

    public DesignatorStmtFirst (Designator Designator, OpChoice OpChoice) {
        this.Designator=Designator;
        if(Designator!=null) Designator.setParent(this);
        this.OpChoice=OpChoice;
        if(OpChoice!=null) OpChoice.setParent(this);
    }

    public Designator getDesignator() {
        return Designator;
    }

    public void setDesignator(Designator Designator) {
        this.Designator=Designator;
    }

    public OpChoice getOpChoice() {
        return OpChoice;
    }

    public void setOpChoice(OpChoice OpChoice) {
        this.OpChoice=OpChoice;
    }

    public void accept(Visitor visitor) {
        visitor.visit(this);
    }

    public void childrenAccept(Visitor visitor) {
        if(Designator!=null) Designator.accept(visitor);
        if(OpChoice!=null) OpChoice.accept(visitor);
    }

    public void traverseTopDown(Visitor visitor) {
        accept(visitor);
        if(Designator!=null) Designator.traverseTopDown(visitor);
        if(OpChoice!=null) OpChoice.traverseTopDown(visitor);
    }

    public void traverseBottomUp(Visitor visitor) {
        if(Designator!=null) Designator.traverseBottomUp(visitor);
        if(OpChoice!=null) OpChoice.traverseBottomUp(visitor);
        accept(visitor);
    }

    public String toString(String tab) {
        StringBuffer buffer=new StringBuffer();
        buffer.append(tab);
        buffer.append("DesignatorStmtFirst(\n");

        if(Designator!=null)
            buffer.append(Designator.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        if(OpChoice!=null)
            buffer.append(OpChoice.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        buffer.append(tab);
        buffer.append(") [DesignatorStmtFirst]");
        return buffer.toString();
    }
}
