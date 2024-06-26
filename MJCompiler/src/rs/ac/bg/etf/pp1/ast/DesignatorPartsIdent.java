// generated with ast extension for cup
// version 0.8
// 26/5/2024 22:37:14


package rs.ac.bg.etf.pp1.ast;

public class DesignatorPartsIdent extends DesignatorParts {

    private DesignatorParts DesignatorParts;
    private String I2;

    public DesignatorPartsIdent (DesignatorParts DesignatorParts, String I2) {
        this.DesignatorParts=DesignatorParts;
        if(DesignatorParts!=null) DesignatorParts.setParent(this);
        this.I2=I2;
    }

    public DesignatorParts getDesignatorParts() {
        return DesignatorParts;
    }

    public void setDesignatorParts(DesignatorParts DesignatorParts) {
        this.DesignatorParts=DesignatorParts;
    }

    public String getI2() {
        return I2;
    }

    public void setI2(String I2) {
        this.I2=I2;
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
        buffer.append("DesignatorPartsIdent(\n");

        if(DesignatorParts!=null)
            buffer.append(DesignatorParts.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        buffer.append(" "+tab+I2);
        buffer.append("\n");

        buffer.append(tab);
        buffer.append(") [DesignatorPartsIdent]");
        return buffer.toString();
    }
}
