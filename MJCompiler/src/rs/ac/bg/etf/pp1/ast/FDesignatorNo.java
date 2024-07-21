// generated with ast extension for cup
// version 0.8
// 21/6/2024 18:3:57


package rs.ac.bg.etf.pp1.ast;

public class FDesignatorNo extends FirstDesignator {

    private String dsgName;

    public FDesignatorNo (String dsgName) {
        this.dsgName=dsgName;
    }

    public String getDsgName() {
        return dsgName;
    }

    public void setDsgName(String dsgName) {
        this.dsgName=dsgName;
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
        buffer.append("FDesignatorNo(\n");

        buffer.append(" "+tab+dsgName);
        buffer.append("\n");

        buffer.append(tab);
        buffer.append(") [FDesignatorNo]");
        return buffer.toString();
    }
}
