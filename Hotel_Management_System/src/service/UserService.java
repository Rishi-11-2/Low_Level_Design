package service;

import model.Booking;
import repository.BookingRepository;
import repository.UserRepository;
import java.util.List;

public class UserService {
    private final UserRepository userRepository;
    private final BookingRepository bookingRepository;

    public UserService(UserRepository userRepository, BookingRepository bookingRepository) {
        this.userRepository = userRepository;
        this.bookingRepository = bookingRepository;
    }

    public List<Booking> listUserBookings(String userId) {
        System.out.println("[UserService] Fetching bookings for user ID: " + userId);
        return bookingRepository.findByUser(userId);
    }
}
