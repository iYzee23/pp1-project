// generated with ast extension for cup
// version 0.8
// 26/5/2024 15:44:25


package rs.ac.bg.etf.pp1.ast;

public class ClassDeclNoNo extends ClassDecl {

    private String I1;
    private StatVarDeclList StatVarDeclList;
    private StatInitList StatInitList;
    private VarDeclList VarDeclList;

    public ClassDeclNoNo (String I1, StatVarDeclList StatVarDeclList, StatInitList StatInitList, VarDeclList VarDeclList) {
        this.I1=I1;
        this.StatVarDeclList=StatVarDeclList;
        if(StatVarDeclList!=null) StatVarDeclList.setParent(this);
        this.StatInitList=StatInitList;
        if(StatInitList!=null) StatInitList.setParent(this);
        this.VarDeclList=VarDeclList;
        if(VarDeclList!=null) VarDeclList.setParent(this);
    }

    public String getI1() {
        return I1;
    }

    public void setI1(String I1) {
        this.I1=I1;
    }

    public StatVarDeclList getStatVarDeclList() {
        return StatVarDeclList;
    }

    public void setStatVarDeclList(StatVarDeclList StatVarDeclList) {
        this.StatVarDeclList=StatVarDeclList;
    }

    public StatInitList getStatInitList() {
        return StatInitList;
    }

    public void setStatInitList(StatInitList StatInitList) {
        this.StatInitList=StatInitList;
    }

    public VarDeclList getVarDeclList() {
        return VarDeclList;
    }

    public void setVarDeclList(VarDeclList VarDeclList) {
        this.VarDeclList=VarDeclList;
    }

    public void accept(Visitor visitor) {
        visitor.visit(this);
    }

    public void childrenAccept(Visitor visitor) {
        if(StatVarDeclList!=null) StatVarDeclList.accept(visitor);
        if(StatInitList!=null) StatInitList.accept(visitor);
        if(VarDeclList!=null) VarDeclList.accept(visitor);
    }

    public void traverseTopDown(Visitor visitor) {
        accept(visitor);
        if(StatVarDeclList!=null) StatVarDeclList.traverseTopDown(visitor);
        if(StatInitList!=null) StatInitList.traverseTopDown(visitor);
        if(VarDeclList!=null) VarDeclList.traverseTopDown(visitor);
    }

    public void traverseBottomUp(Visitor visitor) {
        if(StatVarDeclList!=null) StatVarDeclList.traverseBottomUp(visitor);
        if(StatInitList!=null) StatInitList.traverseBottomUp(visitor);
        if(VarDeclList!=null) VarDeclList.traverseBottomUp(visitor);
        accept(visitor);
    }

    public String toString(String tab) {
        StringBuffer buffer=new StringBuffer();
        buffer.append(tab);
        buffer.append("ClassDeclNoNo(\n");

        buffer.append(" "+tab+I1);
        buffer.append("\n");

        if(StatVarDeclList!=null)
            buffer.append(StatVarDeclList.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        if(StatInitList!=null)
            buffer.append(StatInitList.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        if(VarDeclList!=null)
            buffer.append(VarDeclList.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        buffer.append(tab);
        buffer.append(") [ClassDeclNoNo]");
        return buffer.toString();
    }
}
