package com.example.Hotelperk.controller;

import com.example.Hotelperk.exception.InvalidBookingRequestException;
import com.example.Hotelperk.exception.ResourseNotFoundException;
import com.example.Hotelperk.model.BookedRoom;
import com.example.Hotelperk.model.Room;
import com.example.Hotelperk.response.BookingResponse;
import com.example.Hotelperk.response.RoomResponse;
import com.example.Hotelperk.services.IBookedRoomService;
import com.example.Hotelperk.services.IRoomService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/bookings")
public class BookedRoomController {
   @Autowired
    IBookedRoomService bookingService;

   @Autowired
    IRoomService roomService;
    @GetMapping("/all-bookings")
    public ResponseEntity<List<BookingResponse>> getAllBookings(){
        List<BookedRoom> bookings = bookingService.getAllBookings();
        List<BookingResponse> bookingResponses = new ArrayList<>();
        for(BookedRoom booking : bookings){
            BookingResponse bookingResponse = getBookingResponse(booking);
            bookingResponses.add(bookingResponse);
        }
        return ResponseEntity.ok(bookingResponses);
    }

    private BookingResponse getBookingResponse(BookedRoom booking) {
        Room theRoom =  roomService.getRoomByID(booking.getRoom().getId()).get();
        RoomResponse room = new RoomResponse(theRoom.getId(),theRoom.getRoomType(),theRoom.getRoomPrice());
        return new BookingResponse(booking.getBookingId(),
                booking.getCheckInDate(),booking.getCheckOutDate(),
                booking.getBookingConfirmationCode(),booking.getGuestEmail(),
                booking.getNumOfAdults(),booking.getNumOfChildren(),
                booking.getTotalNumberOfGuest(),room);
    }


    @GetMapping("/confirmation/{confirmationCode}")
    public ResponseEntity<?> getBookingByConfirmationCode(@PathVariable String confirmationCode){
       try{
           BookedRoom booking = bookingService.findByBookingCinfirmationCode(confirmationCode);
           BookingResponse bookingResponse = getBookingResponse(booking);
           return ResponseEntity.ok(bookingResponse);

       }catch (ResourseNotFoundException e){
           return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());

       }
    }

    @PostMapping("/room/{roomId}/booking")
    public ResponseEntity<?> saveBooking(
            @PathVariable Long roomId ,
            @RequestBody BookedRoom bookingRequest){
       try{
         String confirmationCode = bookingService.saveBooking(roomId,bookingRequest);
         return ResponseEntity.ok("Room Booked SuccessFully ! Your Confirmation Code is "+ confirmationCode);

       }catch(InvalidBookingRequestException e){
         return ResponseEntity.badRequest().body(e.getMessage());
       }

    }

    @DeleteMapping("/booking/{bookingId}/delete")
    public void cancelBooking(@PathVariable Long bookingId){
        bookingService.cancelBooking(bookingId);
    }





}
