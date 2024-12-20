// generated with ast extension for cup
// version 0.8
// 24/7/2024 15:2:17


package rs.ac.bg.etf.pp1.ast;

public class VarDeclListPartsError extends VarParts {

    private VarPart VarPart;

    public VarDeclListPartsError (VarPart VarPart) {
        this.VarPart=VarPart;
        if(VarPart!=null) VarPart.setParent(this);
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
        if(VarPart!=null) VarPart.accept(visitor);
    }

    public void traverseTopDown(Visitor visitor) {
        accept(visitor);
        if(VarPart!=null) VarPart.traverseTopDown(visitor);
    }

    public void traverseBottomUp(Visitor visitor) {
        if(VarPart!=null) VarPart.traverseBottomUp(visitor);
        accept(visitor);
    }

    public String toString(String tab) {
        StringBuffer buffer=new StringBuffer();
        buffer.append(tab);
        buffer.append("VarDeclListPartsError(\n");

        if(VarPart!=null)
            buffer.append(VarPart.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        buffer.append(tab);
        buffer.append(") [VarDeclListPartsError]");
        return buffer.toString();
    }
}
