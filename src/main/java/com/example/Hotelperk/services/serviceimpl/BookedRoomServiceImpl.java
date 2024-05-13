package com.example.Hotelperk.services.serviceimpl;

import com.example.Hotelperk.exception.InvalidBookingRequestException;
import com.example.Hotelperk.model.BookedRoom;
import com.example.Hotelperk.model.Room;
import com.example.Hotelperk.repository.BookedRoomRepository;
import com.example.Hotelperk.repository.RoomRepository;
import com.example.Hotelperk.services.IBookedRoomService;
import com.example.Hotelperk.services.IRoomService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BookedRoomServiceImpl implements IBookedRoomService {
    @Autowired
    BookedRoomRepository bookingRepository;

    @Autowired
    IRoomService roomService;
    @Autowired
    RoomRepository roomRepository;
    public List<BookedRoom> getAllBookingsByRoomId(Long roomId) {

        return bookingRepository.findByRoomId(roomId);
    }

    @Override
    public List<BookedRoom> getAllBookings() {
       return bookingRepository.findAll();
    }

    @Override
    public BookedRoom findByBookingCinfirmationCode(String confirmationCode) {
        return bookingRepository.findByBookingConfirmationCode(confirmationCode);
    }

    @Override
    public String saveBooking(Long roomId, BookedRoom bookingRequest) {
        if(bookingRequest.getCheckOutDate().isBefore(bookingRequest.getCheckInDate())){
            throw new InvalidBookingRequestException("check-in Date must come before the check-out date");
        }
        Room room = roomService.getRoomByID(roomId).get();
        List<BookedRoom> existindBookings = room.getBookings();
        boolean roomIsAvailable = roomIsAvailable(bookingRequest,existindBookings);
        if(roomIsAvailable){
            room.addBooing(bookingRequest);
            bookingRepository.save(bookingRequest);
        }
        else{
            throw new InvalidBookingRequestException("Sorry, this room is not avialable for the selectd dates");
        }
        return bookingRequest.getBookingConfirmationCode();
    }

    private boolean roomIsAvailable(BookedRoom bookingRequest, List<BookedRoom> existingBookings) {
        return existingBookings.stream()
                .noneMatch(existingBooking ->
                        bookingRequest.getCheckInDate().equals(existingBooking.getCheckInDate())
                                || bookingRequest.getCheckOutDate().isBefore(existingBooking.getCheckOutDate())
                                || (bookingRequest.getCheckInDate().isAfter(existingBooking.getCheckInDate())
                                && bookingRequest.getCheckInDate().isBefore(existingBooking.getCheckOutDate()))
                                || (bookingRequest.getCheckInDate().isBefore(existingBooking.getCheckInDate())

                                && bookingRequest.getCheckOutDate().equals(existingBooking.getCheckOutDate()))
                                || (bookingRequest.getCheckInDate().isBefore(existingBooking.getCheckInDate())

                                && bookingRequest.getCheckOutDate().isAfter(existingBooking.getCheckOutDate()))

                                || (bookingRequest.getCheckInDate().equals(existingBooking.getCheckOutDate())
                                && bookingRequest.getCheckOutDate().equals(existingBooking.getCheckInDate()))

                                || (bookingRequest.getCheckInDate().equals(existingBooking.getCheckOutDate())
                                && bookingRequest.getCheckOutDate().equals(bookingRequest.getCheckInDate()))
                );
    }

    @Override
    public void cancelBooking(Long bookingId) {
       bookingRepository.deleteById(bookingId);
    }
}
