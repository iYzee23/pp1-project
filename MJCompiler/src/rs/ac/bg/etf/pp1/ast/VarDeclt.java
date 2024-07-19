// generated with ast extension for cup
// version 0.8
// 19/6/2024 17:32:25


package rs.ac.bg.etf.pp1.ast;

public class VarDeclt extends VarDecl {

    private VarParts VarParts;

    public VarDeclt (VarParts VarParts) {
        this.VarParts=VarParts;
        if(VarParts!=null) VarParts.setParent(this);
    }

    public VarParts getVarParts() {
        return VarParts;
    }

    public void setVarParts(VarParts VarParts) {
        this.VarParts=VarParts;
    }

    public void accept(Visitor visitor) {
        visitor.visit(this);
    }

    public void childrenAccept(Visitor visitor) {
        if(VarParts!=null) VarParts.accept(visitor);
    }

    public void traverseTopDown(Visitor visitor) {
        accept(visitor);
        if(VarParts!=null) VarParts.traverseTopDown(visitor);
    }

    public void traverseBottomUp(Visitor visitor) {
        if(VarParts!=null) VarParts.traverseBottomUp(visitor);
        accept(visitor);
    }

    public String toString(String tab) {
        StringBuffer buffer=new StringBuffer();
        buffer.append(tab);
        buffer.append("VarDeclt(\n");

        if(VarParts!=null)
            buffer.append(VarParts.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        buffer.append(tab);
        buffer.append(") [VarDeclt]");
        return buffer.toString();
    }
}
