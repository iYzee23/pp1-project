// generated with ast extension for cup
// version 0.8
// 28/5/2024 23:52:22


package rs.ac.bg.etf.pp1.ast;

public class DesignatorNo extends Designator {

    private String I1;
    private DesignatorParts DesignatorParts;

    public DesignatorNo (String I1, DesignatorParts DesignatorParts) {
        this.I1=I1;
        this.DesignatorParts=DesignatorParts;
        if(DesignatorParts!=null) DesignatorParts.setParent(this);
    }

    public String getI1() {
        return I1;
    }

    public void setI1(String I1) {
        this.I1=I1;
    }

    public DesignatorParts getDesignatorParts() {
        return DesignatorParts;
    }

    public void setDesignatorParts(DesignatorParts DesignatorParts) {
        this.DesignatorParts=DesignatorParts;
    }

    public void accept(Visitor visitor) {
        visitor.visit(this);
    }

    public void childrenAccept(Visitor visitor) {
        if(DesignatorParts!=null) DesignatorParts.accept(visitor);
    }

    public void traverseTopDown(Visitor visitor) {
        accept(visitor);
        if(DesignatorParts!=null) DesignatorParts.traverseTopDown(visitor);
    }

    public void traverseBottomUp(Visitor visitor) {
        if(DesignatorParts!=null) DesignatorParts.traverseBottomUp(visitor);
        accept(visitor);
    }

    public String toString(String tab) {
        StringBuffer buffer=new StringBuffer();
        buffer.append(tab);
        buffer.append("DesignatorNo(\n");

        buffer.append(" "+tab+I1);
        buffer.append("\n");

        if(DesignatorParts!=null)
            buffer.append(DesignatorParts.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        buffer.append(tab);
        buffer.append(") [DesignatorNo]");
        return buffer.toString();
    }
}
