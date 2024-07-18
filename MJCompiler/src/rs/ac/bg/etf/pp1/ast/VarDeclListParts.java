// generated with ast extension for cup
// version 0.8
// 18/6/2024 20:37:50


package rs.ac.bg.etf.pp1.ast;

public class VarDeclListParts extends VarParts {

    private VarParts VarParts;
    private VarPart VarPart;

    public VarDeclListParts (VarParts VarParts, VarPart VarPart) {
        this.VarParts=VarParts;
        if(VarParts!=null) VarParts.setParent(this);
        this.VarPart=VarPart;
        if(VarPart!=null) VarPart.setParent(this);
    }

    public VarParts getVarParts() {
        return VarParts;
    }

    public void setVarParts(VarParts VarParts) {
        this.VarParts=VarParts;
    }

    public VarPart getVarPart() {
        return VarPart;
    }

    public void setVarPart(VarPart VarPart) {
        this.VarPart=VarPart;
    }

    public void accept(Visitor visitor) {
        visitor.visit(this);
    }

    public void childrenAccept(Visitor visitor) {
        if(VarParts!=null) VarParts.accept(visitor);
        if(VarPart!=null) VarPart.accept(visitor);
    }

    public void traverseTopDown(Visitor visitor) {
        accept(visitor);
        if(VarParts!=null) VarParts.traverseTopDown(visitor);
        if(VarPart!=null) VarPart.traverseTopDown(visitor);
    }

    public void traverseBottomUp(Visitor visitor) {
        if(VarParts!=null) VarParts.traverseBottomUp(visitor);
        if(VarPart!=null) VarPart.traverseBottomUp(visitor);
        accept(visitor);
    }

    public String toString(String tab) {
        StringBuffer buffer=new StringBuffer();
        buffer.append(tab);
        buffer.append("VarDeclListParts(\n");

        if(VarParts!=null)
            buffer.append(VarParts.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        if(VarPart!=null)
            buffer.append(VarPart.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        buffer.append(tab);
        buffer.append(") [VarDeclListParts]");
        return buffer.toString();
    }
}
