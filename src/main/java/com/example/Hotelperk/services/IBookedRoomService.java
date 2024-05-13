package com.example.Hotelperk.services;

import com.example.Hotelperk.model.BookedRoom;
import com.example.Hotelperk.model.Room;

import java.util.List;

public interface IBookedRoomService {
    List<BookedRoom> getAllBookings();


    BookedRoom findByBookingCinfirmationCode(String confirmationCode);

    String saveBooking(Long roomId, BookedRoom bookingRequest);

    void cancelBooking(Long bookingId);
}
