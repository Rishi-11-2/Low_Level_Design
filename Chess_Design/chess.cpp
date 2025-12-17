#include<iostream>
#include<set>
#include<vector>
#include<string>

enum class COLOR{
    WHITE,
    BLACK
};

enum class Type{
    PAWN,
    KNIGHT,
    KING,
    QUEEN,
    BISHOP,
    ROOK
};

class Player{
    public:
    COLOR color ;
    string name;

    Player(string name,COLOR color):name(name),color(color){}
};
class Piece{

    public:
    COLOR color ;

    Player* owner;
    Type type ;
    Piece(COLOR color,Type type,Player* owner): color(color), type(type), owner(owner){

    }
};


class Cell{

    public:
    char x_value;
    int y_value ;

    Piece* p;

    Cell(char x_value,int y_value):x_value(x_value),y_value(y_value){

    }

    void addPiece(Piece* pp)
    {
        p = pp;
    }
    void removePiece()
    {
        p = nullptr;
    }
    ~Cell()
    {
        delete p;
    }
};

class Board{

    public:
    vector<vector<Cell*>>grid(8,vector<Cell*>(8));
    map <Cell*, Piece*>occupied;
    Board(Player* p1,Player* p2)
    {
        for(char c='A';c<='H';c++)
        {
            for(int i=1;i<=8;i++)
            {
                Cell* cc = new Cell(c,i);
                grid[c-'A'][i-1]= cc;
            }
        }
        init(p1,p2);
    }

    void init(Player* p1, Player* p2)
    {
        // setting up the initial configuration of the board

        for(int i=0;i<8;i++)
        {
            Piece* p = new Piece(COLOR::WHITE,Type::PAWN,p1);
            grid[1][i] ->p =  p;
            occupied[grid[1][i]] = p;
        }
        for(int i=0;i<8;i++)
        {
            Piece* p = new Piece(COLOR::BLACK,Type::PAWN,p2);
            grid[6][i] ->p =  p;
            occupied[grid[6][i]] = p;
        }

        Type backRank[8] = {
            Type::ROOK,   Type::KNIGHT, Type::BISHOP, Type::QUEEN,
            Type::KING,   Type::BISHOP, Type::KNIGHT, Type::ROOK
        };

        for (int i = 0; i < 8; ++i) {
            Piece* p1 =  new Piece(COLOR::WHITE, backRank[i], p1);
            Piece* p2 =  new Piece(COLOR::WHITE, backRank[i], p1);
            grid[0][i]->p = p1
            grid[7][i]->p = p2;

            occupied[grid[0][i]] = p1;
            occupied[grid[7][i]] = p2;
        }
    }

    ~Board() {
        for (int r = 0; r < 8; ++r) {
            for (int c = 0; c < 8; ++c) {
                delete grid[r][c]; // Cell destructor deletes any Piece
            }
        }
    }

};

class Game{

    public:
    Board* b;
    Player* p1;
    Player* p2;
    Game(Player* p1,Player* p2):p1(p1),p2(p2)
    {
        b = new Board(p1,p2);
    }

    bool validate(Cell* sc, Cell* tc, Piece* p)
    {
        if(occupied[tc]!=nullptr)
        return false;
        int diffx = abs((int)(sc->x_value - tc->x_value));
        int diffy = abs(sc->y_value - tc->y_value);
        switch(p->type){
            case Type::ROOK:

            if(sc->x_value!=tc->x_value && sc->y_value!=tc->y_value)
            {
                return false;
            }
            break;

            case Type::PAWN:

            if(sc->x_value!=tc->x_value || abs(sc->y_value-tc->y_value)>2)
            {
                return false;
            }
            break;

            case Type::BISHOP:

            if(diffx!=diffy)
            {
                return false;
            }
            break;

            case Type::QUEEN:

            if((diffx!=diffy) && (sc->x_value!=tc->x_value) && (sc->y_value!=tc->y_value))
            {
                return false;
            }

            break;

            case Type::KNIGHT:
            if(diffx==0 && diffy ==0)
            return false;
            if(diffx!=2*diffy && diffy!=2*diffx)
            {
                return false;
            }

            break;




        }
    }

    bool  move(Player* player , Cell* sc, Cell* tc)
    {
        Piece* p = sc->p;
        if(p==nullptr || (p->owner->name != player->name))
        return  false;

        if(tc->p==Type::KING)
        return false ;  // cannot move to that square 


        if(!validate(sc,tc,p))
        return false;


        occupied[sc] = nullptr;


        if(tc->p != nullptr)
        delete tc->p;  // capturing target piece
        sc->p = nullptr;
        tc->p = p;

        occupied[tc] = p;



    }


};