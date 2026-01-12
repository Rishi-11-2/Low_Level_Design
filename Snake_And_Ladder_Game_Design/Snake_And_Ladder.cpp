#include<iostream>
#include<string>
#include<vector>
#include <deque>
#include <cstdlib>
#include<ctime>
using namespace std;


enum class Type{
    SNAKE,
    LADDER
};
class Player{

    public:
    string id;
    int currentPos;
    Player(string id):id(id)
    {
        currentPos = 0;
    }
};

class Dice{

    public:
    int diceCount;
    int min;
    int max;

    Dice(int diceCount):diceCount(diceCount)
    {
        min = 1;
        max = 6;
    }

    int rollDice()
    {
        int totalSum =0;
        int count = 0;

        while(count<diceCount)
        {
            srand(time(0));
            int x = 1+(int)rand()%6;

            totalSum+=x;
            count++;
        }
        return totalSum;
    }
};

class Jump{
    public:
    int start ;
    int end;
    Type type ;

    Jump(int start,int end,Type type):start(start),end(end),type(type){

    }

};


class Cell{

    public:
    Jump* j;
    Cell()
    {
        j = nullptr;
    }
    Cell(Jump* j):j(j){

    }
    void setJump(Jump* jump)
    {
        j = jump;
    }

};

class Board{
    public:
    vector<vector<Cell>>board;
    int numberOfSnakes;
    int numberOfLadder;
    int size;
    Board(int size,int numberOfLadder,int numberOfSnakes):size(size),board(size,vector<Cell>(size)),numberOfLadder(numberOfLadder),numberOfSnakes(numberOfSnakes)
    {}


    void addSnake()
    {
        int count = 0;
        srand(time(0));
        while(count<numberOfSnakes)
        {
            int snakeHead = rand()%100;
            int snakeTail = rand()%100;
            if(snakeTail>snakeHead)
            continue;
            Jump* jump = new Jump(snakeHead,snakeTail,Type::SNAKE);
            int i = snakeHead/size;
            int j = snakeHead%size;
            cout<<"Placing snake from :"<<snakeHead<<" "<<snakeTail<<endl;
            board[i][j].setJump(jump);
            count++;
        }
    }
    void addLadder()
    {
        int count = 0;
        srand(time(0));
        while(count<numberOfLadder)
        {
            int ladderHead = rand()%100;
            int ladderTail = rand()%100;
            if(ladderTail<ladderHead)
            continue;
            Jump* jump = new Jump(ladderHead,ladderTail,Type::LADDER);
            int i = ladderHead/size;
            int j = ladderHead%size;
            cout<<"Placing ladder from :"<<ladderHead<<" "<<ladderTail<<endl;
            board[i][j].setJump(jump);
            count++;
        }
    }

    ~Board(){
        for(auto& it: board){
            for(auto& i: it)
            if(i.j!=nullptr)
            delete i.j;
        }
    }

};

class Game{

    public:
    Board board;
    Dice dice;
    deque<Player>dq;
    Player* winner;
    Game(int boardSize,int numberOfLadder,int numberOfSnakes,int numberOfDice):board(boardSize,numberOfLadder,numberOfSnakes),dice(numberOfDice),winner(nullptr)
    {
        initializeGame();
    }
    void addPlayer(Player& p)
    {
        dq.push_back(p);
    }
    void initializeGame()
    {
        cout<<"Initializing game"<<endl;
        // startGame();
        board.addSnake();
        board.addLadder();

    }

    void startGame()
    {
        while(winner == nullptr)
        {
            auto player = dq.front();
            dq.pop_front();
            
            int r = dice.rollDice();

            int pos = player.currentPos+ r;
            pos = min(pos,99);
            int row = pos/board.size;
            int col = pos%board.size;
            auto jump =board.board[row][col].j;
            if(jump!=nullptr){
                if(jump->type == Type::SNAKE)
                {
                    cout << player.id << " bitten by snake at " << pos << "! Going down to " << jump->end << endl;
                    pos = jump->end;
                }
                else if(jump->type == Type::LADDER)
                {
                    cout << player.id << " found ladder at " << pos << "! Going up to " << jump->end << endl;
                    pos= jump->end;
                }
            }
            cout<<"Player: "<<player.id<<" is currently at: "<<pos<<endl;
            player.currentPos = pos;
            if(pos >= 99)
            winner =new Player(player.id);
            dq.push_back(player);
        }
        if (winner!=nullptr)
        {
            cout<<"The winner is :"<<winner->id<<endl;
        }
    }

    ~Game()
    {
        delete winner;
    }

    
};


int main()
{
    Player p1("H");
    Player p2 ("R");
    Game g(10,5,4,1);
    g.addPlayer(p1);
    g.addPlayer(p2);
    g.startGame();
}