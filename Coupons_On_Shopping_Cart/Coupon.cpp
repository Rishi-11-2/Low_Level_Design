#include<iostream>
#include<string>
#include<vector>
#include<set>
using namespace std;


enum class ProductType{

    ELECTRONICS,
    FURNITURE,
    DECORATIVES,
    CLOTHING
};


class Product{

    private:
    string name;
    ProductType type;
    protected:
    double originalPrice;

    public:
    Product()
    {

    }
    Product(string name,double originalPrice,ProductType type):name(name),originalPrice(originalPrice),type(type)
    {

    }

    virtual double getPrice() = 0;
    ProductType getType()
    {
        return type;
    }

    virtual ~Product() = default ;
};


set<ProductType>productList;

class CouponDecorator:public Product{
    public:
    Product* product;

    CouponDecorator(Product* product):product(product)
    {

    }

    ProductType getType()
    {
        return product->getType();
    }

    virtual ~CouponDecorator()
    {
        delete product;
    }
};


class PercentageCouponDecorator: public CouponDecorator{
    public:
    int discountPercentage;
    PercentageCouponDecorator(Product* product,int discountPercentage):CouponDecorator(product),discountPercentage(discountPercentage){

    }

    double getPrice()
    {
        return product->getPrice()*(1.0-(discountPercentage/100.0));
    }
};


class TypeCouponDecorator:public CouponDecorator
{
    public:
    int discountPercentage;

    TypeCouponDecorator(Product* product,int discountPercentage):CouponDecorator(product),discountPercentage(discountPercentage)
    {

    }


    double getPrice()
    {
        if(productList.find(product->getType())!=productList.end())
        return product->getPrice()*(1.0-(discountPercentage/100.0));
        
        return product->getPrice();
    }
};
class Mobile : public Product{
    public:
    Mobile(string name,double originalPrice,ProductType type):Product(name,originalPrice,type)
    {

    }

    double getPrice()
    {
        return originalPrice;
    }
};

class Door : public Product{
    public:
    Door(string name,double originalPrice,ProductType type):Product(name,originalPrice,type)
    {

    }
    double getPrice()
    {
        return originalPrice;
    }
};


class ShoppingCart{

    private:
    set<Product*>productList;

    public:
    ShoppingCart()
    {

    }


    void addToCart(Product* product)
    {
        Product *productWithEligibleDiscount = new TypeCouponDecorator(new PercentageCouponDecorator(product,10),10);

        
        productList.insert(productWithEligibleDiscount);
    }


    double getTotalPrice()
    {
        double totalPrice = 0;
        for(auto product : productList)
        {
            totalPrice+=product->getPrice();
        }
        return totalPrice;
    }



    ~ShoppingCart()
    {
        for(auto it:productList)
        delete it ;
    }

};

int main()
{
    ShoppingCart sc;
    Mobile* m1 = new Mobile("Samsung",11019,ProductType::ELECTRONICS);
    Door* d1 = new Door("WOOD",815,ProductType::FURNITURE);

    sc.addToCart(m1);
    sc.addToCart(d1);
    // productList.insert(ProductType::ELECTRONICS);
    cout<<sc.getTotalPrice()<<endl;
}

