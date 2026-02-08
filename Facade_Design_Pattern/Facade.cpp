#include <iostream>
using namespace std;

// Service class responsible for handling payments
class PaymentService {
public:
    void makePayment(string accountId, double amount) {
        cout << "Payment of ₹" << amount << " successful for account " << accountId << endl;
    }
};

// Service class responsible for reserving seats
class SeatReservationService {
public:
    void reserveSeat(string movieId, string seatNumber) {
        cout << "Seat " << seatNumber << " reserved for movie " << movieId << endl;
    }
};

// Service class responsible for sending notifications
class NotificationService {
public:
    void sendBookingConfirmation(string userEmail) {
        cout << "Booking confirmation sent to " << userEmail << endl;
    }
};

// Service class for managing loyalty/reward points
class LoyaltyPointsService {
public:
    void addPoints(string accountId, int points) {
        cout << points << " loyalty points added to account " << accountId << endl;
    }
};

// Service class for generating movie tickets
class TicketService {
public:
    void generateTicket(string movieId, string seatNumber) {
        cout << "Ticket generated for movie " << movieId << ", Seat: " << seatNumber << endl;
    }
};


// ========== The MovieBookingFacade class  ==============
class MovieBookingFacade {
private:
    PaymentService paymentService;
    SeatReservationService seatReservationService;
    NotificationService notificationService;
    LoyaltyPointsService loyaltyPointsService;
    TicketService ticketService;

public:
    // Constructor initializes all the subsystem services
    MovieBookingFacade() {}

    // Method providing a simplified interface for booking a movie ticket
    void bookMovieTicket(string accountId, string movieId, string seatNumber, string userEmail, double amount) {
        paymentService.makePayment(accountId, amount);
        seatReservationService.reserveSeat(movieId, seatNumber);
        ticketService.generateTicket(movieId, seatNumber);
        loyaltyPointsService.addPoints(accountId, 50);
        notificationService.sendBookingConfirmation(userEmail);

        // Indicate successful completion of the entire booking process.
        cout << "Movie ticket booking completed successfully!" << endl;
    }
};


// Client Code
int main() {
    // Booking a movie ticket manually (using facade)
    MovieBookingFacade movieBookingFacade;
    movieBookingFacade.bookMovieTicket("user123", "movie456", "A10", "user@example.com", 500);
    return 0;
}
