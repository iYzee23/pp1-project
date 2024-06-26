// generated with ast extension for cup
// version 0.8
// 26/5/2024 15:44:25


package rs.ac.bg.etf.pp1.ast;

public class ConstDecl implements SyntaxNode {

    private SyntaxNode parent;
    private int line;
    private ConstParts ConstParts;

    public ConstDecl (ConstParts ConstParts) {
        this.ConstParts=ConstParts;
        if(ConstParts!=null) ConstParts.setParent(this);
    }

    public ConstParts getConstParts() {
        return ConstParts;
    }

    public void setConstParts(ConstParts ConstParts) {
        this.ConstParts=ConstParts;
    }

    public SyntaxNode getParent() {
        return parent;
    }

    public void setParent(SyntaxNode parent) {
        this.parent=parent;
    }

    public int getLine() {
        return line;
    }

    public void setLine(int line) {
        this.line=line;
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
        buffer.append("ConstDecl(\n");

        if(ConstParts!=null)
            buffer.append(ConstParts.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        buffer.append(tab);
        buffer.append(") [ConstDecl]");
        return buffer.toString();
    }
}
