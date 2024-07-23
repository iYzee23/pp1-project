// generated with ast extension for cup
// version 0.8
// 23/6/2024 15:28:50


package rs.ac.bg.etf.pp1.ast;

public class Designatort extends Designator {

    private FirstDesignator FirstDesignator;
    private DesignatorParts DesignatorParts;

    public Designatort (FirstDesignator FirstDesignator, DesignatorParts DesignatorParts) {
        this.FirstDesignator=FirstDesignator;
        if(FirstDesignator!=null) FirstDesignator.setParent(this);
        this.DesignatorParts=DesignatorParts;
        if(DesignatorParts!=null) DesignatorParts.setParent(this);
    }

    public FirstDesignator getFirstDesignator() {
        return FirstDesignator;
    }

    public void setFirstDesignator(FirstDesignator FirstDesignator) {
        this.FirstDesignator=FirstDesignator;
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
        if(FirstDesignator!=null) FirstDesignator.accept(visitor);
        if(DesignatorParts!=null) DesignatorParts.accept(visitor);
    }

    public void traverseTopDown(Visitor visitor) {
        accept(visitor);
        if(FirstDesignator!=null) FirstDesignator.traverseTopDown(visitor);
        if(DesignatorParts!=null) DesignatorParts.traverseTopDown(visitor);
    }

    public void traverseBottomUp(Visitor visitor) {
        if(FirstDesignator!=null) FirstDesignator.traverseBottomUp(visitor);
        if(DesignatorParts!=null) DesignatorParts.traverseBottomUp(visitor);
        accept(visitor);
    }

    public String toString(String tab) {
        StringBuffer buffer=new StringBuffer();
        buffer.append(tab);
        buffer.append("Designatort(\n");

        if(FirstDesignator!=null)
            buffer.append(FirstDesignator.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        if(DesignatorParts!=null)
            buffer.append(DesignatorParts.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        buffer.append(tab);
        buffer.append(") [Designatort]");
        return buffer.toString();
    }
}
