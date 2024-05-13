package com.example.Hotelperk.services;

import com.example.Hotelperk.exception.UpdateRoomException;
import com.example.Hotelperk.model.Room;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.math.BigDecimal;
import java.sql.Blob;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

public interface IRoomService {
    public Room addNewRoom(MultipartFile photo , String roomType, BigDecimal roomPrice) throws SQLException, IOException;

    List<String> getAllRoomTypes();

    List<Room> getAllRooms();

    byte[] getRoomPhotoByRoomId(Long roomId) throws SQLException;


    void deleteRoom(Long roomId);

    Room updateRoom(Long roomId, String roomType, BigDecimal roomPrice, byte[] photoBytes) throws UpdateRoomException;

    Optional<Room> getRoomByID(Long roomId);
}
