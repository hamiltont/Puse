package userinterface;

/**
 * Left menu items
 */
public class DrawerItem {

    String name;
    String description;

    public DrawerItem() {
        // TODO Auto-generated constructor stub
    }

    DrawerItem(String name, String description) {
        this.name = name;
        this.description = description;
    }

    @Override
    public String toString() {

        return name;
    }

}// end of class 
