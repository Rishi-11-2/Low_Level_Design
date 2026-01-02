#include <iostream>
#include<map>
using namespace std;



class ILetter{

    public:
    virtual void display(int row,int col) = 0;
    virtual ~ILetter()= default;
};
class DocumentCharacter:public ILetter{

    public:
    // intrinsic data that is shared by multiple objects
    char character;
    string fontType;
    int size;
    

    DocumentCharacter(char character,string fontType,int size):character(character),fontType(fontType),size(size){

    }

    void display(int row,int col) //extrinsic data in which objects differ
    {
        cout<<"Character:"<<character<<" is displayed at row:"<<row<<" Column:"<<col<<endl;
    }
};


class CharacterFactory{

    private:
    static map<char,ILetter*>mp;
    public:

    static ILetter* getChar(char ch)
    {
        if(mp.find(ch)!=mp.end())
        return mp[ch];

        mp[ch] = new DocumentCharacter(ch,"ARIAL",10);
        return mp[ch];
    }

    ~CharacterFactory()
    {
        for(auto &it:mp)
        delete it.second;
    mp.clear();
    }
};

map<char,ILetter*>CharacterFactory::mp;
int main()
{
    // this is the statement that we want to write 

    string s= "this is the statement that we want to write";

    for(int i=0;i<(int)s.size();i++)
    {
        ILetter* c = CharacterFactory::getChar(s[i]);
        c->display(0,i);

    }
}