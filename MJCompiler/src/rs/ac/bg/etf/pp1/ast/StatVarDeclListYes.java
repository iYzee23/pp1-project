// generated with ast extension for cup
// version 0.8
// 18/6/2024 20:37:50


package rs.ac.bg.etf.pp1.ast;

public class StatVarDeclListYes extends StatVarDeclList {

    private StatVarDeclList StatVarDeclList;
    private StatVarDeclInit StatVarDeclInit;
    private VarDecl VarDecl;

    public StatVarDeclListYes (StatVarDeclList StatVarDeclList, StatVarDeclInit StatVarDeclInit, VarDecl VarDecl) {
        this.StatVarDeclList=StatVarDeclList;
        if(StatVarDeclList!=null) StatVarDeclList.setParent(this);
        this.StatVarDeclInit=StatVarDeclInit;
        if(StatVarDeclInit!=null) StatVarDeclInit.setParent(this);
        this.VarDecl=VarDecl;
        if(VarDecl!=null) VarDecl.setParent(this);
    }

    public StatVarDeclList getStatVarDeclList() {
        return StatVarDeclList;
    }

    public void setStatVarDeclList(StatVarDeclList StatVarDeclList) {
        this.StatVarDeclList=StatVarDeclList;
    }

    public StatVarDeclInit getStatVarDeclInit() {
        return StatVarDeclInit;
    }

    public void setStatVarDeclInit(StatVarDeclInit StatVarDeclInit) {
        this.StatVarDeclInit=StatVarDeclInit;
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
        if(StatVarDeclInit!=null) StatVarDeclInit.accept(visitor);
        if(VarDecl!=null) VarDecl.accept(visitor);
    }

    public void traverseTopDown(Visitor visitor) {
        accept(visitor);
        if(StatVarDeclList!=null) StatVarDeclList.traverseTopDown(visitor);
        if(StatVarDeclInit!=null) StatVarDeclInit.traverseTopDown(visitor);
        if(VarDecl!=null) VarDecl.traverseTopDown(visitor);
    }

    public void traverseBottomUp(Visitor visitor) {
        if(StatVarDeclList!=null) StatVarDeclList.traverseBottomUp(visitor);
        if(StatVarDeclInit!=null) StatVarDeclInit.traverseBottomUp(visitor);
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

        if(StatVarDeclInit!=null)
            buffer.append(StatVarDeclInit.toString("  "+tab));
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
