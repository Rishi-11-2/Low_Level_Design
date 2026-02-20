import java.util.*;

class EagerSingelton{

    // it is thread safe because object initialization happen on class loading level because of such , it is thread safe
    private  static final  EagerSingelton instance = new EagerSingelton();

    private EagerSingelton()
    {
    }

    public static EagerSingelton getEagerSingleton()
    {
        return instance;
    }
}


public class EagerLoading{
    public static void main(String [] args)
{
    EagerSingelton eg1 = EagerSingelton.getEagerSingleton();
    EagerSingelton eg2 = EagerSingelton.getEagerSingleton();

    System.out.println(eg1);
    System.out.println(eg2);
}
}
