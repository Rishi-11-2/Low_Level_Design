#include<iostream>
#include<vector>
#include<deque>
#include<utility>
#include<string>
using namespace std;



enum class PieceType{
    X,
    O
};
class PlayingPiece
{

    public:
    PieceType type;
    PlayingPiece(PieceType type): type(type){}

    PieceType getType()
    {
        return type;
    }
    virtual char getSymbol() =0;
};


class PieceX: public PlayingPiece{

    public:
    PieceX():PlayingPiece(PieceType::X){}

    char getSymbol()
    {
        return 'X';
    }
};

class PieceO : public PlayingPiece{

    public:
    PieceO():PlayingPiece(PieceType::O){}
    char getSymbol()
    {
        return 'O';
    }
};


class Board{

    // board has-a relationship with piece
    public:
    int size;

    vector<vector<PlayingPiece*>> board;
    Board(int size):size(size){
        
        board = vector<vector<PlayingPiece*>>(size, vector<PlayingPiece*>(size, nullptr));
        for(int i=0;i<size;i++)
        {
            for(int j=0;j<size;j++)
            board[i][j]=nullptr;
        }
    }


    bool addPiece(int row,int column,PieceType type)
    {
        if(board[row][column]!=nullptr)
        return false;

        if (type == PieceType::X) board[row][column] = new PieceX();
       else board[row][column] = new PieceO(); // or PieceO if renamed

       return true;

    }

    vector<pair<int,int>> getFreeSpaces()
    {
        vector<pair<int,int>>v;

        for(int i=0;i<size;i++)
        {
            for(int j=0;j<size;j++)
            {
                if(board[i][j]==nullptr)
                v.push_back({i,j});
            }
        }
        return v;
    }

    void displayBoard()
    {
        // top border
        cout << string(size * 4 + 1, '-') << "\n";

        for(int i = 0; i < size; i++)
        {
            cout << "|";
            for(int j = 0; j < size; j++)
            {
                if (board[i][j] == nullptr)
                    cout << " . |";
                else
                    cout << " " << board[i][j]->getSymbol() << " |";
            }
            cout << "\n";

            // row separator
            cout << string(size * 4 + 1, '-') << "\n";
        }
    }
    bool isThereWinner(int row,int column,PieceType piece)
    {
        int rowEqual = 1;
        int colEqual = 1;
        int diag1 =1;
        int diag2 =1;
        for(int i=0;i<size;i++)
        {
            if(board[row][i]==nullptr || board[row][i]->getType()!=piece)
            rowEqual = 0;
        }

        for(int i=0;i<size;i++)
        {
            if(board[i][column]==nullptr || board[i][column]->getType()!=piece)
            colEqual=0;
        }

        for(int i=0;i<size;i++)
        {
            if(board[i][i]==nullptr ||  board[i][i]->getType()!=piece)
            diag1 =0;
        }

        for(int i=0;i<size;i++)
        {
            if(board[i][size-i-1]==nullptr ||  board[i][size-i-1]->getType()!=piece)
            diag2 =0;
        }

        int res = rowEqual| colEqual | diag1 | diag2;

        return res;
        
    }
    ~Board()
    {
        
    }
};

class Player{

    // player has-a relationship with piece
    public:
    string name;
    PieceType piece;


    Player(string name, PieceType type):name(name),piece(type){

    }

    string getName()
    {
        return name;
    }


};

class Game{

    // has-a relationship with board and players;
    public:
    deque<Player*>players;
    Board* board;

    Game(){

        initializeGame();
    }

    void initializeGame()
    {
        

        Player* player1 = new Player("Rishi",PieceType::X);

        Player* player2 = new Player("Bozz",PieceType::O);

        players.push_back(player1);
        players.push_back(player2);

        board = new Board(3);
    }

    string startGame()
    {
        bool isWinner = false;

        while(!isWinner)
        {
            Player* p = players.front();

            board->displayBoard();

            auto freeSpaces = board->getFreeSpaces();
            if(freeSpaces.empty())
            {
                isWinner= true;
                continue;
            }

            cout<<"Player: "<<p->name<<"Enter row and column:"<<endl;

            int row,column;
            cin>>row>>column;

            bool pieceAddedSuccessfully = board->addPiece(row,column,p->piece);

            if(!pieceAddedSuccessfully)
            {
                cout<<"Incorrect position try again"<<endl;
                continue;
            }
            players.pop_front();
            players.push_back(p);

            bool winner = board->isThereWinner(row,column,p->piece);

            if(winner)
            return p->name;


        }
        return "tie";
    }

};


int main()
{
    Game* g = new Game();

    cout<<g->startGame()<<endl;
}