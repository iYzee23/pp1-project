// generated with ast extension for cup
// version 0.8
// 21/6/2024 22:47:49


package rs.ac.bg.etf.pp1.ast;

public class FactorNew extends Factor {

    private Type Type;
    private NewChoice NewChoice;

    public FactorNew (Type Type, NewChoice NewChoice) {
        this.Type=Type;
        if(Type!=null) Type.setParent(this);
        this.NewChoice=NewChoice;
        if(NewChoice!=null) NewChoice.setParent(this);
    }

    public Type getType() {
        return Type;
    }

    public void setType(Type Type) {
        this.Type=Type;
    }

    public NewChoice getNewChoice() {
        return NewChoice;
    }

    public void setNewChoice(NewChoice NewChoice) {
        this.NewChoice=NewChoice;
    }

    public void accept(Visitor visitor) {
        visitor.visit(this);
    }

    public void childrenAccept(Visitor visitor) {
        if(Type!=null) Type.accept(visitor);
        if(NewChoice!=null) NewChoice.accept(visitor);
    }

    public void traverseTopDown(Visitor visitor) {
        accept(visitor);
        if(Type!=null) Type.traverseTopDown(visitor);
        if(NewChoice!=null) NewChoice.traverseTopDown(visitor);
    }

    public void traverseBottomUp(Visitor visitor) {
        if(Type!=null) Type.traverseBottomUp(visitor);
        if(NewChoice!=null) NewChoice.traverseBottomUp(visitor);
        accept(visitor);
    }

    public String toString(String tab) {
        StringBuffer buffer=new StringBuffer();
        buffer.append(tab);
        buffer.append("FactorNew(\n");

        if(Type!=null)
            buffer.append(Type.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        if(NewChoice!=null)
            buffer.append(NewChoice.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        buffer.append(tab);
        buffer.append(") [FactorNew]");
        return buffer.toString();
    }
}
