// generated with ast extension for cup
// version 0.8
// 20/6/2024 21:1:26


package rs.ac.bg.etf.pp1.ast;

public class Conditiont extends Condition {

    private CondTerm CondTerm;
    private CondTermList CondTermList;

    public Conditiont (CondTerm CondTerm, CondTermList CondTermList) {
        this.CondTerm=CondTerm;
        if(CondTerm!=null) CondTerm.setParent(this);
        this.CondTermList=CondTermList;
        if(CondTermList!=null) CondTermList.setParent(this);
    }

    public CondTerm getCondTerm() {
        return CondTerm;
    }

    public void setCondTerm(CondTerm CondTerm) {
        this.CondTerm=CondTerm;
    }

    public CondTermList getCondTermList() {
        return CondTermList;
    }

    public void setCondTermList(CondTermList CondTermList) {
        this.CondTermList=CondTermList;
    }

    public void accept(Visitor visitor) {
        visitor.visit(this);
    }

    public void childrenAccept(Visitor visitor) {
        if(CondTerm!=null) CondTerm.accept(visitor);
        if(CondTermList!=null) CondTermList.accept(visitor);
    }

    public void traverseTopDown(Visitor visitor) {
        accept(visitor);
        if(CondTerm!=null) CondTerm.traverseTopDown(visitor);
        if(CondTermList!=null) CondTermList.traverseTopDown(visitor);
    }

    public void traverseBottomUp(Visitor visitor) {
        if(CondTerm!=null) CondTerm.traverseBottomUp(visitor);
        if(CondTermList!=null) CondTermList.traverseBottomUp(visitor);
        accept(visitor);
    }

    public String toString(String tab) {
        StringBuffer buffer=new StringBuffer();
        buffer.append(tab);
        buffer.append("Conditiont(\n");

        if(CondTerm!=null)
            buffer.append(CondTerm.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        if(CondTermList!=null)
            buffer.append(CondTermList.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        buffer.append(tab);
        buffer.append(") [Conditiont]");
        return buffer.toString();
    }
}
