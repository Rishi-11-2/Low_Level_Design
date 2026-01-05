#include <iostream>
#include <string>
#include <vector>
using namespace std;

class Book {
    int price;
    string name;

public:
    Book(int price, string name) : price(price), name(name) {}

    int getPrice() const { return price; }
    const string& getName() const { return name; }
};

template<typename T>
class Iterator {
public:
    virtual bool hasNext()  = 0;
    virtual T& next() = 0;
    virtual ~Iterator() = default;
};

class LibraryIterator : public Iterator<Book> {
    size_t index;
    vector<Book>& books;   // borrowed reference

public:
    LibraryIterator(vector<Book>& books)
        : index(0), books(books) {}

    bool hasNext()  override {
        return index < books.size();
    }

    Book& next() override {
        return books[index++];
    }
};


class ReversedLibraryIterator: public Iterator<Book>{
    int  index;
    vector<Book>& books;

    public:
    ReversedLibraryIterator(vector<Book>&books) :books(books)
    {
        index = (int)books.size() - 1;
    }
    bool hasNext() override
    {
        return index>=0;
    }
    Book& next() override
    {
        return books[index--];
    }
};

class Library {   // concrete aggregator
    vector<Book>& books;  // borrowed, NOT owned

public:
    Library(vector<Book>& books) : books(books) {}

    LibraryIterator createIterator() {
        return LibraryIterator(books);
    }
    ReversedLibraryIterator createReverseIterator()
    {
        return ReversedLibraryIterator(books);
    }
};

int main() {

    cout<<"Forward Iteration"<<endl;
    vector<Book> books = {
        Book(100, "C++"),
        Book(200, "DSA")
    };

    Library lib(books);
    auto it = lib.createIterator();

    while (it.hasNext()) {
        Book& b = it.next();
        cout << b.getName() << " " << b.getPrice() << endl;
    }

    cout<<"Reverse Iteration:"<<endl;
    auto rit = lib.createReverseIterator();
    while(rit.hasNext())
    {
        Book& b = rit.next();
        cout << b.getName() << " " << b.getPrice() << endl;

    }
}
