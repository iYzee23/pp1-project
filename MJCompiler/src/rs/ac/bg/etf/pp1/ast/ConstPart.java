// generated with ast extension for cup
// version 0.8
// 26/5/2024 15:44:25


package rs.ac.bg.etf.pp1.ast;

public class ConstPart implements SyntaxNode {

    private SyntaxNode parent;
    private int line;
    private String I1;
    private ConstChoice ConstChoice;

    public ConstPart (String I1, ConstChoice ConstChoice) {
        this.I1=I1;
        this.ConstChoice=ConstChoice;
        if(ConstChoice!=null) ConstChoice.setParent(this);
    }

    public String getI1() {
        return I1;
    }

    public void setI1(String I1) {
        this.I1=I1;
    }

    public ConstChoice getConstChoice() {
        return ConstChoice;
    }

    public void setConstChoice(ConstChoice ConstChoice) {
        this.ConstChoice=ConstChoice;
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
        if(ConstChoice!=null) ConstChoice.accept(visitor);
    }

    public void traverseTopDown(Visitor visitor) {
        accept(visitor);
        if(ConstChoice!=null) ConstChoice.traverseTopDown(visitor);
    }

    public void traverseBottomUp(Visitor visitor) {
        if(ConstChoice!=null) ConstChoice.traverseBottomUp(visitor);
        accept(visitor);
    }

    public String toString(String tab) {
        StringBuffer buffer=new StringBuffer();
        buffer.append(tab);
        buffer.append("ConstPart(\n");

        buffer.append(" "+tab+I1);
        buffer.append("\n");

        if(ConstChoice!=null)
            buffer.append(ConstChoice.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        buffer.append(tab);
        buffer.append(") [ConstPart]");
        return buffer.toString();
    }
}
