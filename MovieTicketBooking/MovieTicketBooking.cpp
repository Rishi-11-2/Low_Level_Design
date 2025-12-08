#include<iostream>
using namespace std;


enum class SeatCategory{
    SILVER,
    GOLD,
    PLATINUM
};

class Movie{
    private:
    int id;
    int duration;
    string name ;

    Movie(int id,int duration,string name):id(id),duration(duration),name(name){}
    int getMovieId()
    {
        return id;
    }

    string getMovieName()
    {
        return name;
    }
    int getMovieDuration()
    {
        return duration;
    }

};


class MovieController{
    private: // has-a movie and city
    map<City*,set<Movie*>>mp;
    set<Movie*>allMovies;

    public:
    MovieController(){

    }

    void addMovie(City* c, Movie* m)
    {
        mp[c].insert(m);
    }
    void removeMovie(City* c,Movie* m)
    {
        mp[c].erase(m);
    }

};

class Screen{
    private:
    int id;
    set<Seat*>seats;

    public:
    Screen(int id):id(id){

    }

};

class Seat{
    public:
    int id;
    int rowInformation;
    SeatCategory sc ;  

    int price;

    Seat(int id,int rowInformation,SeatCategory sc):id(id),rowInformation(rowInformation),sc(sc),price(price){}

};

class Show{
    public:
    int id ;
    Movie* movie;
    Screen* screenInfo;
    int startTime;
    set<Seat*>bookedSeatIds;

    public:
    Show(int id,Movie* movie,Screen* screenInfo,int startTime):id(id),movie(movie),screenInfo(screenInfo),startTime(startTime){}


    void addSeat(Seat* s)
    {
        bookedSeatIds.insert(s);
    }
    void removeSeat(Seat* s)
    {
        bookedSeatIds.erase(s);
    }
};

class Theatre{
    private:
    int id;
    string address;
    City* c;

    set<Screen*>screen;
    set<Show*>shows;

    public:
    Theatre(int id,string address):id(id),address(address){}

    set<Show*> getAllShows()
    {
        return shows ;
    }

};

class TheatreController{ // has-a relationship with Theatre
    private:
    map<City*,set<Theatre*>>mp;
    set<Theatre*>allTheatres;

    public:

    void addTheatre(City* c, Theatre* th)
    {
        mp[c].insert(th);
        allTheatres.insert(th);
    }

    void removeTheatre(City* c, Theatre* th)
    {
        mp[c].erase(th);
        allTheatres.erase(th);
    }

    vector<Show*> getAllShows(Movie* m,City* c)
    {
        auto theatres = mp[c];
        vector<Show*>shows;
        for(auto theatre:theatres)
        {
            auto shows =theatre->getAllShows();

            for(auto show:shows)
            {
                if(shows->movie->name == m->name)
                res.push_back(shows);
            }
        }
        return res;
    }



};

class Booking{

    private:
    Show* show;
    set<Seat*>bookedSeats;

    Payment* paymentDetails;

    public:
    Booking(Show* show,set<Seat*>bookedSeats,Payment* paymentDetails):show(show),bookedSeats(bookedSeats),paymentDetails(paymentDetails){

    }
};

class BookMyShow{
    private:
    MovieController* mc;
    TheatreController* tc;

    public:
    
};