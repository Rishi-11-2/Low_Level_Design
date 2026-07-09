

import java.util.*;


class DB_Pool{
    private final int id;
    DB_Pool(int id)
    {
        this.id = id;
    }

    private static class Holder{
        private static final int MAX_POOL = 10;
        private static final List<DB_Pool> freeInstances = new ArrayList<>();
        private static final List<DB_Pool>occupiedInstances = new ArrayList<>();

        static{
            for(int i =1;i<=MAX_POOL;i++)
            {
                freeInstances.add(new DB_Pool(i));
            }
        }
    }

    public static synchronized DB_Pool acquireConnection()
    {
        if(Holder.freeInstances.isEmpty())
        {
            System.out.println("no free connections avaliable");
            return null;
        }
        DB_Pool instance = Holder.freeInstances.get(0);
        Holder.freeInstances.remove(0);
        Holder.occupiedInstances.add(instance);

        System.out.println("Acquired connection. free left : "+Holder.freeInstances.size());

        return instance;
    }

    public static synchronized void releaseConnection(DB_Pool instance)
    {
        Holder.occupiedInstances.remove(instance);
        Holder.freeInstances.add(instance);

        System.out.println("Released connection. Free left:"+Holder.freeInstances.size());
    }
    
}

public class DB_Pool_Singleton_Lazy_Loading {
    public static void main(String args[])
    {
        DB_Pool conn1 = DB_Pool.acquireConnection();
        DB_Pool conn2 = DB_Pool.acquireConnection();

        DB_Pool.releaseConnection(conn1);

        DB_Pool conn3 = DB_Pool.acquireConnection();
    }
}
