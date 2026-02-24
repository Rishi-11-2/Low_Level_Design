
import java.util.*;



abstract class SupportHandler{
    protected SupportHandler nextHandler;
    public void setNextHandler(SupportHandler nextHandler)
    {
        this.nextHandler = nextHandler;
    }
    abstract public void handleRequeust(String requestType);
}


class GeneralSupport extends SupportHandler{
    public void handleRequeust(String requestType)
    {
        if(requestType.equalsIgnoreCase("general"))
        {
            System.out.println("Handling general case");
        }
        else if(nextHandler!=null)
        {
            nextHandler.handleRequeust(requestType);
        }
    }
}

class AdminSupport extends SupportHandler{
    public void handleRequeust(String requestType)
    {
        if(requestType.equalsIgnoreCase("admin"))
        {
            System.out.println("Handling admin case");
        }
        else if(nextHandler!=null)
        {
            nextHandler.handleRequeust(requestType);
        }
    }
}
class BillingSupport extends SupportHandler{
    public void handleRequeust(String requestType)
    {
        if(requestType.equalsIgnoreCase("refund"))
        {
            System.out.println("Handling billing case");
        }
        else if(nextHandler!=null)
        {
            nextHandler.handleRequeust(requestType);
        }
    }
}
class TechnicalSupport extends SupportHandler{
    public void handleRequeust(String requestType)
    {
        if(requestType.equalsIgnoreCase("technical"))
        {
            System.out.println("Handling technical case");
        }
        else if(nextHandler!=null)
        {
            nextHandler.handleRequeust(requestType);
        }
    }
}
class DeliverySupport extends SupportHandler{
    public void handleRequeust(String requestType)
    {
        if(requestType.equalsIgnoreCase("delivery"))
        {
            System.out.println("Handling delivery case");
        }
        else if(nextHandler!=null)
        {
            nextHandler.handleRequeust(requestType);
        }
        else
        {
            System.out.println("Invalid type!!!!!!!");
        }
    }
}

public  class Chain_Of_Responsibility{


    public static void main(String args[])
    {
        GeneralSupport gs = new GeneralSupport();
        AdminSupport as = new AdminSupport();
        TechnicalSupport ts = new TechnicalSupport();
        DeliverySupport ds =  new DeliverySupport();
        BillingSupport bs = new BillingSupport();

        gs.setNextHandler(as);
        as.setNextHandler(bs);
        bs.setNextHandler(ts);
        ts.setNextHandler(ds);

        gs.handleRequeust("general");
        gs.handleRequeust("admin");
        gs.handleRequeust("delivery");
        gs.handleRequeust("unknow");
    }

}