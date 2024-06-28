// generated with ast extension for cup
// version 0.8
// 28/5/2024 23:52:22


package rs.ac.bg.etf.pp1.ast;

public class StatInitListYes extends StatInitList {

    private StaticInitializer StaticInitializer;
    private StatInitList StatInitList;

    public StatInitListYes (StaticInitializer StaticInitializer, StatInitList StatInitList) {
        this.StaticInitializer=StaticInitializer;
        if(StaticInitializer!=null) StaticInitializer.setParent(this);
        this.StatInitList=StatInitList;
        if(StatInitList!=null) StatInitList.setParent(this);
    }

    public StaticInitializer getStaticInitializer() {
        return StaticInitializer;
    }

    public void setStaticInitializer(StaticInitializer StaticInitializer) {
        this.StaticInitializer=StaticInitializer;
    }

    public StatInitList getStatInitList() {
        return StatInitList;
    }

    public void setStatInitList(StatInitList StatInitList) {
        this.StatInitList=StatInitList;
    }

    public void accept(Visitor visitor) {
        visitor.visit(this);
    }

    public void childrenAccept(Visitor visitor) {
        if(StaticInitializer!=null) StaticInitializer.accept(visitor);
        if(StatInitList!=null) StatInitList.accept(visitor);
    }

    public void traverseTopDown(Visitor visitor) {
        accept(visitor);
        if(StaticInitializer!=null) StaticInitializer.traverseTopDown(visitor);
        if(StatInitList!=null) StatInitList.traverseTopDown(visitor);
    }

    public void traverseBottomUp(Visitor visitor) {
        if(StaticInitializer!=null) StaticInitializer.traverseBottomUp(visitor);
        if(StatInitList!=null) StatInitList.traverseBottomUp(visitor);
        accept(visitor);
    }

    public String toString(String tab) {
        StringBuffer buffer=new StringBuffer();
        buffer.append(tab);
        buffer.append("StatInitListYes(\n");

        if(StaticInitializer!=null)
            buffer.append(StaticInitializer.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        if(StatInitList!=null)
            buffer.append(StatInitList.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        buffer.append(tab);
        buffer.append(") [StatInitListYes]");
        return buffer.toString();
    }
}
