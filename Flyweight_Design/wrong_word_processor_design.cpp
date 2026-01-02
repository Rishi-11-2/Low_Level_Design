#include <iostream>
using namespace std;


class Character{

    public:
    char character;
    string fontType;
    int size;
    int row;
    int column;

    Character(char character,string fontType,int size, int row,int column):character(character),fontType(fontType),size(size),row(row),column(column){

    }
};


int main()
{
    // this is the statement that we want to write 

    string s= "this is the statement that we want to write";

    for(int i=0;i<(int)s.size();i++)
    {
        Character* cc = new Character(s[i],"ARIAL",10,0,i);
        delete cc ;
    }
}