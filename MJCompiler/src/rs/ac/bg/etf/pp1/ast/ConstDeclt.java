// generated with ast extension for cup
// version 0.8
// 11/6/2024 0:18:33


package rs.ac.bg.etf.pp1.ast;

public class ConstDeclt extends ConstDecl {

    private ConstParts ConstParts;

    public ConstDeclt (ConstParts ConstParts) {
        this.ConstParts=ConstParts;
        if(ConstParts!=null) ConstParts.setParent(this);
    }

    public ConstParts getConstParts() {
        return ConstParts;
    }

    public void setConstParts(ConstParts ConstParts) {
        this.ConstParts=ConstParts;
    }

    public void accept(Visitor visitor) {
        visitor.visit(this);
    }

    public void childrenAccept(Visitor visitor) {
        if(ConstParts!=null) ConstParts.accept(visitor);
    }

    public void traverseTopDown(Visitor visitor) {
        accept(visitor);
        if(ConstParts!=null) ConstParts.traverseTopDown(visitor);
    }

    public void traverseBottomUp(Visitor visitor) {
        if(ConstParts!=null) ConstParts.traverseBottomUp(visitor);
        accept(visitor);
    }

    public String toString(String tab) {
        StringBuffer buffer=new StringBuffer();
        buffer.append(tab);
        buffer.append("ConstDeclt(\n");

        if(ConstParts!=null)
            buffer.append(ConstParts.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        buffer.append(tab);
        buffer.append(") [ConstDeclt]");
        return buffer.toString();
    }
}
