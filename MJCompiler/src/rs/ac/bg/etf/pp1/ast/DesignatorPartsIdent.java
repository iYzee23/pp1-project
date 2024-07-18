// generated with ast extension for cup
// version 0.8
// 18/6/2024 20:37:50


package rs.ac.bg.etf.pp1.ast;

public class DesignatorPartsIdent extends DesignatorParts {

    private DesignatorParts DesignatorParts;
    private String dsgName;

    public DesignatorPartsIdent (DesignatorParts DesignatorParts, String dsgName) {
        this.DesignatorParts=DesignatorParts;
        if(DesignatorParts!=null) DesignatorParts.setParent(this);
        this.dsgName=dsgName;
    }

    public DesignatorParts getDesignatorParts() {
        return DesignatorParts;
    }

    public void setDesignatorParts(DesignatorParts DesignatorParts) {
        this.DesignatorParts=DesignatorParts;
    }

    public String getDsgName() {
        return dsgName;
    }

    public void setDsgName(String dsgName) {
        this.dsgName=dsgName;
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

        buffer.append(" "+tab+dsgName);
        buffer.append("\n");

        buffer.append(tab);
        buffer.append(") [DesignatorPartsIdent]");
        return buffer.toString();
    }
}
