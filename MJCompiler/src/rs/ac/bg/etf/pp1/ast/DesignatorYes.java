// generated with ast extension for cup
// version 0.8
// 11/6/2024 0:18:33


package rs.ac.bg.etf.pp1.ast;

public class DesignatorYes extends Designator {

    private String nspName;
    private String dsgName;
    private DesignatorParts DesignatorParts;

    public DesignatorYes (String nspName, String dsgName, DesignatorParts DesignatorParts) {
        this.nspName=nspName;
        this.dsgName=dsgName;
        this.DesignatorParts=DesignatorParts;
        if(DesignatorParts!=null) DesignatorParts.setParent(this);
    }

    public String getNspName() {
        return nspName;
    }

    public void setNspName(String nspName) {
        this.nspName=nspName;
    }

    public String getDsgName() {
        return dsgName;
    }

    public void setDsgName(String dsgName) {
        this.dsgName=dsgName;
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
        buffer.append("DesignatorYes(\n");

        buffer.append(" "+tab+nspName);
        buffer.append("\n");

        buffer.append(" "+tab+dsgName);
        buffer.append("\n");

        if(DesignatorParts!=null)
            buffer.append(DesignatorParts.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        buffer.append(tab);
        buffer.append(") [DesignatorYes]");
        return buffer.toString();
    }
}
