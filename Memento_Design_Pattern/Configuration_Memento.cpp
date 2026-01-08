#include <iostream>
#include <vector>
#include <stdexcept>

using namespace std;

class ConfigurationMemento{
    private:
    int height;
    int width;

    public:
    ConfigurationMemento(int height,int width):height(height),width(width){}

    int getHeight()
    {
        return height;
    }
    int getWidth()
    {
        return width;
    }
};

class ConfiguationOriginator{
    private:
    int width;
    int height;
    public:
    ConfiguationOriginator(int width,int height):width(width),height(height){}

    void setHeight(int h)
    {
        height = h;
    }
    void setWidth(int w)
    {
        width = w;
    }

    ConfigurationMemento createMemento()
    {
        return ConfigurationMemento(height,width);
    }

    // take by value so rvalues work (no const added)
    void restoreMemento(ConfigurationMemento mem)
    {
        // correct mapping
        width = mem.getWidth();
        height = mem.getHeight();
    }
    void display()
    {
        cout<<"Height: "<<height<<" Width: "<<width<<endl;
    }
};

class ConfigurationCaretaker{
    private:
    vector<ConfigurationMemento> history;
    public:

    // accept by value so temporaries from createMemento() bind
    void addMemento(ConfigurationMemento mem)
    {
        history.push_back(mem);
    }

    // return by value (no bool, no dangling reference)
    ConfigurationMemento undo()
    {
        if ((int)history.size() == 0)
        {
            throw runtime_error("No mementos to undo");
        }
        ConfigurationMemento mem = history.back();
        history.pop_back();
        return mem;
    }
};

int main()
{
    ConfiguationOriginator origin(800, 600);
    ConfigurationCaretaker caretaker;

    caretaker.addMemento(origin.createMemento());

    origin.setWidth(1024);
    origin.setHeight(768);
    caretaker.addMemento(origin.createMemento());

    origin.setWidth(1280);
    origin.setHeight(720);
    origin.display();
    try {
        origin.restoreMemento(caretaker.undo());
        std::cout << "Undo successful\n";
        origin.display();
    } catch (const std::exception& e) {
        std::cout << e.what() << '\n';
    }
}
