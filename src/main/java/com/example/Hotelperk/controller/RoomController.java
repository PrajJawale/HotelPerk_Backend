package com.example.Hotelperk.controller;

import com.example.Hotelperk.exception.PhotoReteivevalError;
import com.example.Hotelperk.exception.ResourseNotFoundException;
import com.example.Hotelperk.exception.UpdateRoomException;
import com.example.Hotelperk.model.BookedRoom;
import com.example.Hotelperk.model.Room;
import com.example.Hotelperk.response.BookingResponse;
import com.example.Hotelperk.response.RoomResponse;
import com.example.Hotelperk.services.serviceimpl.BookedRoomServiceImpl;
import com.example.Hotelperk.services.serviceimpl.RoomServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.sql.rowset.serial.SerialBlob;
import java.io.IOException;
import java.math.BigDecimal;
import java.sql.Blob;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;
import java.util.Optional;

@RestController
@CrossOrigin(origins = "http://localhost:5173") // Allow CORS requests from this origin
@RequestMapping("/rooms")
public class RoomController {
    @Autowired
    RoomServiceImpl roomService;

    @Autowired
    BookedRoomServiceImpl bookedRoomService;

    @PostMapping("/add/new-room")
    public ResponseEntity<RoomResponse> addNewRoom(
            @RequestParam("photo") MultipartFile photo,
            @RequestParam("roomType") String roomType,
            @RequestParam("roomPrice") BigDecimal  roomPrice) throws SQLException, IOException {
       Room savedRoom = roomService.addNewRoom(photo,roomType,roomPrice);
       RoomResponse response = new RoomResponse(savedRoom.getId(), savedRoom.getRoomType(), savedRoom.getRoomPrice());
       return  ResponseEntity.ok(response);

    }

    @GetMapping("/room-types")
    public List<String> getAllRoomTypes(){
        return roomService.getAllRoomTypes();
    }

    @GetMapping("/all-rooms")
    public ResponseEntity<List<RoomResponse>> getAllRooms() throws SQLException, PhotoReteivevalError {
        List<Room> rooms = roomService.getAllRooms();
        List<RoomResponse> roomResponses= new ArrayList<>();
        for(Room room : rooms){
            byte[] photoBytes = roomService.getRoomPhotoByRoomId(room.getId());
            if(photoBytes!= null && photoBytes.length>0){
                String base64photo = Base64.getEncoder().encodeToString(photoBytes);
                RoomResponse roomResponse = getRoomResponse(room);
                roomResponse.setPhoto(base64photo);
                roomResponses.add(roomResponse);
            }

         }
        return ResponseEntity.ok(roomResponses);
    }

    @DeleteMapping("/delete/room/{roomId}")
    public ResponseEntity<Void> deleteRoom(@PathVariable Long roomId){
        roomService.deleteRoom(roomId);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @PutMapping("/update/{roomId}")
    public ResponseEntity<RoomResponse> updateRoom(@PathVariable Long roomId,
                                                   @RequestParam (required = false)String roomType,
                                                   @RequestParam(required = false) BigDecimal roomPrice,
                                                   @RequestParam (required = false) MultipartFile photo) throws SQLException, IOException, PhotoReteivevalError, InterruptedException, UpdateRoomException {
       byte[] photoBytes = photo != null && !photo.isEmpty() ? photo.getBytes() : roomService.getRoomPhotoByRoomId(roomId);
       Blob photoBlob = photoBytes!=null && photoBytes.length>0 ? new SerialBlob(photoBytes):null;
        Room theRoom = roomService.updateRoom(roomId,roomType,roomPrice,photoBytes);
       theRoom.setPhoto(photoBlob);
       RoomResponse roomResponse = getRoomResponse(theRoom);
       return ResponseEntity.ok(roomResponse);

     }

     @GetMapping("/room/{roomId}")
     public ResponseEntity<Optional<RoomResponse>> getRoomById(@PathVariable Long roomId){
        Optional<Room> theRoom = roomService.getRoomByID(roomId);
        return theRoom.map(room->{
            RoomResponse roomResponse = null;
            try {
                roomResponse = getRoomResponse(room);
            } catch (PhotoReteivevalError e) {
                throw new RuntimeException(e);
            }
            return ResponseEntity.ok(Optional.of(roomResponse));
        }).orElseThrow(()-> new ResourseNotFoundException("Room Not Found"));
     }

    private RoomResponse getRoomResponse(Room room) throws PhotoReteivevalError {
        List<BookedRoom> bookings = getAllRoomByBookingId(room.getId());
//        List<BookingResponse> bookingInfo = bookings.
//                stream()
//                .map(booking -> new BookingResponse(booking.getBookingId(),booking.getCheckInDate(),booking.getCheckOutDate(),booking.getBookingConfirmationCode())).toList();

        byte[] phtoBytes = null;
        Blob photoBlob = room.getPhoto();
        if(photoBlob!=null){
            try{
                phtoBytes = photoBlob.getBytes(1,(int) photoBlob.length());
            }catch (SQLException e){
                throw new PhotoReteivevalError("Photo Retrieving Error");

            }
        }
        return new RoomResponse(room.getId(),
                                room.getRoomType(),
                                room.getRoomPrice(),
                                room.isBooked(),
                                phtoBytes);




    }

    private List<BookedRoom> getAllRoomByBookingId(Long roomId) {
        return bookedRoomService.getAllBookingsByRoomId(roomId);
    }




}
