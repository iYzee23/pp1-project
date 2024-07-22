// generated with ast extension for cup
// version 0.8
// 21/6/2024 22:47:49


package rs.ac.bg.etf.pp1.ast;

public class StatInitListt extends StatInitList {

    private StatInits StatInits;

    public StatInitListt (StatInits StatInits) {
        this.StatInits=StatInits;
        if(StatInits!=null) StatInits.setParent(this);
    }

    public StatInits getStatInits() {
        return StatInits;
    }

    public void setStatInits(StatInits StatInits) {
        this.StatInits=StatInits;
    }

    public void accept(Visitor visitor) {
        visitor.visit(this);
    }

    public void childrenAccept(Visitor visitor) {
        if(StatInits!=null) StatInits.accept(visitor);
    }

    public void traverseTopDown(Visitor visitor) {
        accept(visitor);
        if(StatInits!=null) StatInits.traverseTopDown(visitor);
    }

    public void traverseBottomUp(Visitor visitor) {
        if(StatInits!=null) StatInits.traverseBottomUp(visitor);
        accept(visitor);
    }

    public String toString(String tab) {
        StringBuffer buffer=new StringBuffer();
        buffer.append(tab);
        buffer.append("StatInitListt(\n");

        if(StatInits!=null)
            buffer.append(StatInits.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        buffer.append(tab);
        buffer.append(") [StatInitListt]");
        return buffer.toString();
    }
}
