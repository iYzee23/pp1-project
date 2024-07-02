// generated with ast extension for cup
// version 0.8
// 2/6/2024 4:11:0


package rs.ac.bg.etf.pp1.ast;

public class ClassDeclNo extends ClassDecl {

    private String I1;
    private StatVarDeclList StatVarDeclList;
    private StatInitList StatInitList;
    private VarDeclList VarDeclList;
    private MethodDeclListOpt MethodDeclListOpt;

    public ClassDeclNo (String I1, StatVarDeclList StatVarDeclList, StatInitList StatInitList, VarDeclList VarDeclList, MethodDeclListOpt MethodDeclListOpt) {
        this.I1=I1;
        this.StatVarDeclList=StatVarDeclList;
        if(StatVarDeclList!=null) StatVarDeclList.setParent(this);
        this.StatInitList=StatInitList;
        if(StatInitList!=null) StatInitList.setParent(this);
        this.VarDeclList=VarDeclList;
        if(VarDeclList!=null) VarDeclList.setParent(this);
        this.MethodDeclListOpt=MethodDeclListOpt;
        if(MethodDeclListOpt!=null) MethodDeclListOpt.setParent(this);
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

    public MethodDeclListOpt getMethodDeclListOpt() {
        return MethodDeclListOpt;
    }

    public void setMethodDeclListOpt(MethodDeclListOpt MethodDeclListOpt) {
        this.MethodDeclListOpt=MethodDeclListOpt;
    }

    public void accept(Visitor visitor) {
        visitor.visit(this);
    }

    public void childrenAccept(Visitor visitor) {
        if(StatVarDeclList!=null) StatVarDeclList.accept(visitor);
        if(StatInitList!=null) StatInitList.accept(visitor);
        if(VarDeclList!=null) VarDeclList.accept(visitor);
        if(MethodDeclListOpt!=null) MethodDeclListOpt.accept(visitor);
    }

    public void traverseTopDown(Visitor visitor) {
        accept(visitor);
        if(StatVarDeclList!=null) StatVarDeclList.traverseTopDown(visitor);
        if(StatInitList!=null) StatInitList.traverseTopDown(visitor);
        if(VarDeclList!=null) VarDeclList.traverseTopDown(visitor);
        if(MethodDeclListOpt!=null) MethodDeclListOpt.traverseTopDown(visitor);
    }

    public void traverseBottomUp(Visitor visitor) {
        if(StatVarDeclList!=null) StatVarDeclList.traverseBottomUp(visitor);
        if(StatInitList!=null) StatInitList.traverseBottomUp(visitor);
        if(VarDeclList!=null) VarDeclList.traverseBottomUp(visitor);
        if(MethodDeclListOpt!=null) MethodDeclListOpt.traverseBottomUp(visitor);
        accept(visitor);
    }

    public String toString(String tab) {
        StringBuffer buffer=new StringBuffer();
        buffer.append(tab);
        buffer.append("ClassDeclNo(\n");

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

        if(MethodDeclListOpt!=null)
            buffer.append(MethodDeclListOpt.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        buffer.append(tab);
        buffer.append(") [ClassDeclNo]");
        return buffer.toString();
    }
}
