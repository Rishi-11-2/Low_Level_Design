


import java.util.*;

class TreeType{
    private String name;
    private String color;
    private String texture;
    

    public TreeType(String name, String color,String texture)
    {
        this.name = name;
        this.color = color;
        this.texture = texture;
    }

    public void draw(int x,int y)
    {
        System.out.println("Coordinate:"+x+": "+y);
    }
}

class Tree{

    private int x;
    private int y;
    private TreeType treeType;

    public Tree(int x,int y,TreeType treeType)
    {
        this.x = x;
        this.y = y;
        this.treeType = treeType;
    }
    public void draw()
    {
        treeType.draw(x, y);
    }
}

class TreeFactory{
    static Map<String,TreeType> mp = new HashMap<>();

    public static TreeType getTreeType(String name,String color,String texture)
    {
        String s = name + color + texture;
        if(!mp.containsKey(s))
        {
            mp.put(s,new TreeType(name, color, texture));
        }

        return mp.get(s);
    }
}

class Forest{
    private List<Tree>treeList = new ArrayList<>();
    public void plantTree(String name,String color,String texture)
    {
        for(int i= 1;i<=100;i++)
        {
            treeList.add(new Tree(i, i, TreeFactory.getTreeType(name, color, texture)));
        }
    }

    public void draw()
    {
        for(Tree tree: treeList)
        {
            tree.draw();
        }
    }
}
public class Flyweight{

    public static void main(String args[])
    {
        Forest forest = new Forest();
        forest.plantTree("Rishi", "Green", "Soft");
        forest.draw();
    }
}