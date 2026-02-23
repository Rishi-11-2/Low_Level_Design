
import java.util.*;

class Lazy_Loading{

    private Lazy_Loading()
    {

    }
    private static class Holder{ // when outer class is loaded, this class is not loaded into memory
        // object of the Lazy_Loading instance is created only when its required

        private static final  Lazy_Loading instance = new Lazy_Loading();
    }

    public static Lazy_Loading getInstance()
    {
        return Holder.instance;
    }
}
public class Lazy_Loading_Thread_Safe {

    public static void main(String args[])
    {
            Lazy_Loading ob1 = Lazy_Loading.getInstance();
    Lazy_Loading ob2 = Lazy_Loading.getInstance();

    System.out.println(ob1);
    System.out.println(ob2);
    }

}
