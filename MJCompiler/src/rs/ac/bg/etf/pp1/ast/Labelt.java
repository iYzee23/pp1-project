// generated with ast extension for cup
// version 0.8
// 21/6/2024 18:3:57


package rs.ac.bg.etf.pp1.ast;

public class Labelt extends Label {

    private String labName;

    public Labelt (String labName) {
        this.labName=labName;
    }

    public String getLabName() {
        return labName;
    }

    public void setLabName(String labName) {
        this.labName=labName;
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
        buffer.append("Labelt(\n");

        buffer.append(" "+tab+labName);
        buffer.append("\n");

        buffer.append(tab);
        buffer.append(") [Labelt]");
        return buffer.toString();
    }
}
