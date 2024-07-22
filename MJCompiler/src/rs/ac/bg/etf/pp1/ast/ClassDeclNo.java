// generated with ast extension for cup
// version 0.8
// 21/6/2024 22:47:49


package rs.ac.bg.etf.pp1.ast;

public class ClassDeclNo extends ClassDecl {

    private ClassName ClassName;
    private StatVarDeclList StatVarDeclList;
    private StatInitList StatInitList;
    private VarDeclList VarDeclList;
    private MethodDeclListOpt MethodDeclListOpt;

    public ClassDeclNo (ClassName ClassName, StatVarDeclList StatVarDeclList, StatInitList StatInitList, VarDeclList VarDeclList, MethodDeclListOpt MethodDeclListOpt) {
        this.ClassName=ClassName;
        if(ClassName!=null) ClassName.setParent(this);
        this.StatVarDeclList=StatVarDeclList;
        if(StatVarDeclList!=null) StatVarDeclList.setParent(this);
        this.StatInitList=StatInitList;
        if(StatInitList!=null) StatInitList.setParent(this);
        this.VarDeclList=VarDeclList;
        if(VarDeclList!=null) VarDeclList.setParent(this);
        this.MethodDeclListOpt=MethodDeclListOpt;
        if(MethodDeclListOpt!=null) MethodDeclListOpt.setParent(this);
    }

    public ClassName getClassName() {
        return ClassName;
    }

    public void setClassName(ClassName ClassName) {
        this.ClassName=ClassName;
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
        if(ClassName!=null) ClassName.accept(visitor);
        if(StatVarDeclList!=null) StatVarDeclList.accept(visitor);
        if(StatInitList!=null) StatInitList.accept(visitor);
        if(VarDeclList!=null) VarDeclList.accept(visitor);
        if(MethodDeclListOpt!=null) MethodDeclListOpt.accept(visitor);
    }

    public void traverseTopDown(Visitor visitor) {
        accept(visitor);
        if(ClassName!=null) ClassName.traverseTopDown(visitor);
        if(StatVarDeclList!=null) StatVarDeclList.traverseTopDown(visitor);
        if(StatInitList!=null) StatInitList.traverseTopDown(visitor);
        if(VarDeclList!=null) VarDeclList.traverseTopDown(visitor);
        if(MethodDeclListOpt!=null) MethodDeclListOpt.traverseTopDown(visitor);
    }

    public void traverseBottomUp(Visitor visitor) {
        if(ClassName!=null) ClassName.traverseBottomUp(visitor);
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

        if(ClassName!=null)
            buffer.append(ClassName.toString("  "+tab));
        else
            buffer.append(tab+"  null");
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
