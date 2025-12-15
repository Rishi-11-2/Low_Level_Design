#include<iostream>
#include<string>
#include<vector>

using namespace std;


class FileSystem{

    protected:
    FileSystem* parent;
    public:
    string name;
    FileSystem(string name):name(name)
    {}
    void setParent(FileSystem* f)
    {
        parent =f ;
    }
    FileSystem* getParent()
    {
        return parent;
    }
    virtual void ls() = 0;
    virtual void add(FileSystem* f){
        throw logic_error("Add not supported");
    }
    virtual ~FileSystem(){}
};

class File: public FileSystem{
    public:
    File(string name):FileSystem(name){}
    void ls ()
    {
        cout<<"File: "<<name<<" "<<" Parent Directory:"<<getParent()->name<<endl;
    }
};

class Directory: public FileSystem{

    public:
    vector<FileSystem*>list;
    Directory(string name):FileSystem(name){}

     void add(FileSystem* f)
    {
        f->setParent(this);
        list.push_back(f);
    }

    void ls()
    {
        if(getParent() != nullptr)
        cout<<"Directory Name: "<<name<<" Parent Directory:"<<getParent()->name<<endl;
        for(auto it:list)
        {
            it->ls();
        }
    }

    ~Directory()
    {
        for (auto child : list)
        {
            delete child ;
        }
    }
};

int main()
{
    FileSystem* dir = new Directory("Movies");
    FileSystem* f1 = new File("Border");

    FileSystem* dir1 = new Directory("Netflix");
    dir->add(f1);
    dir->add(dir1);

    FileSystem* f2 = new File("Saiyaara");
    dir1->add(f2);
    dir->ls();
}