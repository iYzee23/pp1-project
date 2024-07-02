// generated with ast extension for cup
// version 0.8
// 2/6/2024 4:11:0


package rs.ac.bg.etf.pp1.ast;

public class NamespaceNamet extends NamespaceName {

    private String namespaceName;

    public NamespaceNamet (String namespaceName) {
        this.namespaceName=namespaceName;
    }

    public String getNamespaceName() {
        return namespaceName;
    }

    public void setNamespaceName(String namespaceName) {
        this.namespaceName=namespaceName;
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
        buffer.append("NamespaceNamet(\n");

        buffer.append(" "+tab+namespaceName);
        buffer.append("\n");

        buffer.append(tab);
        buffer.append(") [NamespaceNamet]");
        return buffer.toString();
    }
}
