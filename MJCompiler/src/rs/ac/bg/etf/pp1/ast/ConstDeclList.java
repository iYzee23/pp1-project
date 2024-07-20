// generated with ast extension for cup
// version 0.8
// 20/6/2024 2:25:22


package rs.ac.bg.etf.pp1.ast;

public class ConstDeclList extends ConstParts {

    private ConstParts ConstParts;
    private ConstPart ConstPart;

    public ConstDeclList (ConstParts ConstParts, ConstPart ConstPart) {
        this.ConstParts=ConstParts;
        if(ConstParts!=null) ConstParts.setParent(this);
        this.ConstPart=ConstPart;
        if(ConstPart!=null) ConstPart.setParent(this);
    }

    public ConstParts getConstParts() {
        return ConstParts;
    }

    public void setConstParts(ConstParts ConstParts) {
        this.ConstParts=ConstParts;
    }

    public ConstPart getConstPart() {
        return ConstPart;
    }

    public void setConstPart(ConstPart ConstPart) {
        this.ConstPart=ConstPart;
    }

    public void accept(Visitor visitor) {
        visitor.visit(this);
    }

    public void childrenAccept(Visitor visitor) {
        if(ConstParts!=null) ConstParts.accept(visitor);
        if(ConstPart!=null) ConstPart.accept(visitor);
    }

    public void traverseTopDown(Visitor visitor) {
        accept(visitor);
        if(ConstParts!=null) ConstParts.traverseTopDown(visitor);
        if(ConstPart!=null) ConstPart.traverseTopDown(visitor);
    }

    public void traverseBottomUp(Visitor visitor) {
        if(ConstParts!=null) ConstParts.traverseBottomUp(visitor);
        if(ConstPart!=null) ConstPart.traverseBottomUp(visitor);
        accept(visitor);
    }

    public String toString(String tab) {
        StringBuffer buffer=new StringBuffer();
        buffer.append(tab);
        buffer.append("ConstDeclList(\n");

        if(ConstParts!=null)
            buffer.append(ConstParts.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        if(ConstPart!=null)
            buffer.append(ConstPart.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        buffer.append(tab);
        buffer.append(") [ConstDeclList]");
        return buffer.toString();
    }
}
