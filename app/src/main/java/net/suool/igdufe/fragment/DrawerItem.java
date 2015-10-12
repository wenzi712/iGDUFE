package net.suool.igdufe.fragment;

public class DrawerItem {

    private String text;
    public static enum Type {HEADER, MENU, DIVIDER}

    private final Type type;

    public DrawerItem(Type type) {
        this.type = type;
    }

    public Type getType() {
        return type;
    }

    public String getText() {
        return text;
    }

    public DrawerItem setText(String text) {
        this.text = text;
        return this;
    }
}