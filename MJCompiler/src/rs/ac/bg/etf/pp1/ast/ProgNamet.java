// generated with ast extension for cup
// version 0.8
// 11/6/2024 15:15:45


package rs.ac.bg.etf.pp1.ast;

public class ProgNamet extends ProgName {

    private String progName;

    public ProgNamet (String progName) {
        this.progName=progName;
    }

    public String getProgName() {
        return progName;
    }

    public void setProgName(String progName) {
        this.progName=progName;
    }

    public void accept(Visitor visitor) {
        visitor.visit(this);
    }

    public void childrenAccept(Visitor visitor) {
    }

    public void traverseTopDown(Visitor visitor) {
        accept(visitor);
    }

    public void traverseBottomUp(Visitor visitor) {
        accept(visitor);
    }

    public String toString(String tab) {
        StringBuffer buffer=new StringBuffer();
        buffer.append(tab);
        buffer.append("ProgNamet(\n");

        buffer.append(" "+tab+progName);
        buffer.append("\n");

        buffer.append(tab);
        buffer.append(") [ProgNamet]");
        return buffer.toString();
    }
}
