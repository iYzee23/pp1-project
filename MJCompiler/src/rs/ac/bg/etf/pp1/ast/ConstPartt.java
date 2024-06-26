// generated with ast extension for cup
// version 0.8
// 26/5/2024 22:37:14


package rs.ac.bg.etf.pp1.ast;

public class ConstPartt extends ConstPart {

    private String I1;
    private ConstChoice ConstChoice;

    public ConstPartt (String I1, ConstChoice ConstChoice) {
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
        buffer.append("ConstPartt(\n");

        buffer.append(" "+tab+I1);
        buffer.append("\n");

        if(ConstChoice!=null)
            buffer.append(ConstChoice.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        buffer.append(tab);
        buffer.append(") [ConstPartt]");
        return buffer.toString();
    }
}
