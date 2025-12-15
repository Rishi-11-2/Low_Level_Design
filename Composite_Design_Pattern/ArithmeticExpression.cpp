#include<iostream>
#include<vector>
#include<string>

using namespace std;

enum class Operation{

    ADD,
    SUBTRACT,
    MULTIPLY,
    DIVIDE
};
class ArithmeticExpression{

    public:
    virtual int evaluate() = 0;
};

class Number : public ArithmeticExpression{

    public:

    int num;

    Number(int num):num(num){

    }


    int evaluate()
    {
        return num;
    }


};

class Operator: public ArithmeticExpression{

    public:
    Operation ops ;
    ArithmeticExpression* left;
    ArithmeticExpression* right;
    Operator(Operation ops,ArithmeticExpression* left,ArithmeticExpression* right):ops(ops),left(left),right(right){
    }

    int  evaluate()
    {
        switch(ops){
            case Operation::ADD:

            return left->evaluate() + right->evaluate();
            break;

            case Operation::SUBTRACT:
            return left->evaluate() - right->evaluate();
            break;

            case Operation :: MULTIPLY:
            return left->evaluate() * right->evaluate();
            break;

            case Operation::DIVIDE:
            return (left->evaluate()/right->evaluate());
            break;
            default:
            return 0;
            break;
        }
        return 0;
    }
};


int main()
{
    ArithmeticExpression* n1 = new Number(1);
    ArithmeticExpression* n2 = new Number(5);
    ArithmeticExpression* n3 = new Number(3);
    ArithmeticExpression* op2 = new Operator(Operation::MULTIPLY,n2,n3);
    ArithmeticExpression* op1 = new Operator(Operation::ADD,n1,op2);
    cout<<op1->evaluate()<<endl;
}