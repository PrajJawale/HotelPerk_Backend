package com.example.Hotelperk.response;

import com.example.Hotelperk.model.Room;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.FetchType;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor

public class BookingResponse {
    private Long Id;

    private LocalDate checkInDate;


    private  LocalDate checkOutDate;


    private String guestFullName;


    private String guestEmail;


    private int numOfAdults;


    private int numOfChildren;


    private int totalNumberOfGuest;



    private String bookingConfirmationCode;


    private RoomResponse room;

    public BookingResponse(Long id, LocalDate checkInDate, LocalDate checkOutDate, String bookingConfirmationCode) {
        this.Id = id;
        this.checkInDate = checkInDate;
        this.checkOutDate = checkOutDate;
        this.bookingConfirmationCode = bookingConfirmationCode;
    }


    public BookingResponse(Long bookingId, LocalDate checkInDate, LocalDate checkOutDate, String bookingConfirmationCode, String guestEmail, int numOfAdults, int numOfChildren, int totalNumberOfGuest, RoomResponse room) {
       this.Id = bookingId;
       this.checkInDate = checkInDate;
       this.checkOutDate = checkOutDate;
       this.bookingConfirmationCode= bookingConfirmationCode;
       this.guestEmail = guestEmail;
       this.numOfAdults=numOfAdults;
       this.numOfChildren=numOfChildren;
       this.totalNumberOfGuest=totalNumberOfGuest;
       this.room =room;

    }
}
