// generated with ast extension for cup
// version 0.8
// 23/6/2024 15:28:50


package rs.ac.bg.etf.pp1.ast;

public class ActParst extends ActPars {

    private ActParsTemp ActParsTemp;

    public ActParst (ActParsTemp ActParsTemp) {
        this.ActParsTemp=ActParsTemp;
        if(ActParsTemp!=null) ActParsTemp.setParent(this);
    }

    public ActParsTemp getActParsTemp() {
        return ActParsTemp;
    }

    public void setActParsTemp(ActParsTemp ActParsTemp) {
        this.ActParsTemp=ActParsTemp;
    }

    public void accept(Visitor visitor) {
        visitor.visit(this);
    }

    public void childrenAccept(Visitor visitor) {
        if(ActParsTemp!=null) ActParsTemp.accept(visitor);
    }

    public void traverseTopDown(Visitor visitor) {
        accept(visitor);
        if(ActParsTemp!=null) ActParsTemp.traverseTopDown(visitor);
    }

    public void traverseBottomUp(Visitor visitor) {
        if(ActParsTemp!=null) ActParsTemp.traverseBottomUp(visitor);
        accept(visitor);
    }

    public String toString(String tab) {
        StringBuffer buffer=new StringBuffer();
        buffer.append(tab);
        buffer.append("ActParst(\n");

        if(ActParsTemp!=null)
            buffer.append(ActParsTemp.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        buffer.append(tab);
        buffer.append(") [ActParst]");
        return buffer.toString();
    }
}
