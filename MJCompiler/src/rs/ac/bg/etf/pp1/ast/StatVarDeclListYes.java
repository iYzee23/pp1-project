// generated with ast extension for cup
// version 0.8
// 26/5/2024 15:44:25


package rs.ac.bg.etf.pp1.ast;

public class StatVarDeclListYes extends StatVarDeclList {

    private StatVarDeclList StatVarDeclList;
    private VarDecl VarDecl;

    public StatVarDeclListYes (StatVarDeclList StatVarDeclList, VarDecl VarDecl) {
        this.StatVarDeclList=StatVarDeclList;
        if(StatVarDeclList!=null) StatVarDeclList.setParent(this);
        this.VarDecl=VarDecl;
        if(VarDecl!=null) VarDecl.setParent(this);
    }

    public StatVarDeclList getStatVarDeclList() {
        return StatVarDeclList;
    }

    public void setStatVarDeclList(StatVarDeclList StatVarDeclList) {
        this.StatVarDeclList=StatVarDeclList;
    }

    public VarDecl getVarDecl() {
        return VarDecl;
    }

    public void setVarDecl(VarDecl VarDecl) {
        this.VarDecl=VarDecl;
    }

    public void accept(Visitor visitor) {
        visitor.visit(this);
    }

    public void childrenAccept(Visitor visitor) {
        if(StatVarDeclList!=null) StatVarDeclList.accept(visitor);
        if(VarDecl!=null) VarDecl.accept(visitor);
    }

    public void traverseTopDown(Visitor visitor) {
        accept(visitor);
        if(StatVarDeclList!=null) StatVarDeclList.traverseTopDown(visitor);
        if(VarDecl!=null) VarDecl.traverseTopDown(visitor);
    }

    public void traverseBottomUp(Visitor visitor) {
        if(StatVarDeclList!=null) StatVarDeclList.traverseBottomUp(visitor);
        if(VarDecl!=null) VarDecl.traverseBottomUp(visitor);
        accept(visitor);
    }

    public String toString(String tab) {
        StringBuffer buffer=new StringBuffer();
        buffer.append(tab);
        buffer.append("StatVarDeclListYes(\n");

        if(StatVarDeclList!=null)
            buffer.append(StatVarDeclList.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        if(VarDecl!=null)
            buffer.append(VarDecl.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        buffer.append(tab);
        buffer.append(") [StatVarDeclListYes]");
        return buffer.toString();
    }
}
