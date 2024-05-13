package com.example.Hotelperk.services.serviceimpl;

import com.example.Hotelperk.exception.ResourseNotFoundException;
import com.example.Hotelperk.exception.UpdateRoomException;
import com.example.Hotelperk.model.Room;
import com.example.Hotelperk.repository.RoomRepository;
import com.example.Hotelperk.services.IRoomService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.sql.rowset.serial.SerialBlob;
import javax.sql.rowset.serial.SerialException;
import java.io.IOException;
import java.math.BigDecimal;
import java.sql.Blob;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

@Service
public class RoomServiceImpl implements IRoomService {
    @Autowired
    RoomRepository roomRepository;
    @Override
    public Room addNewRoom(MultipartFile photo, String roomType, BigDecimal roomPrice) throws SQLException, IOException {
        Room room = new Room();
        room.setRoomType(roomType);
        room.setRoomPrice(roomPrice);
        if(!photo.isEmpty()){
           byte[] photoBytes= photo.getBytes();
            Blob photoBlob = new SerialBlob(photoBytes);
            room.setPhoto(photoBlob);
        }
        return roomRepository.save(room);
    }

    @Override
    public List<String> getAllRoomTypes() {
        return roomRepository.getAllRoomTypes();
    }

    @Override
    public List<Room> getAllRooms() {
        return roomRepository.findAll();
    }

    @Override
    public byte[] getRoomPhotoByRoomId(Long roomId) throws SQLException {
        Optional<Room> theRoom = roomRepository.findById(roomId);
        if(theRoom.isEmpty()){
            throw  new ResourseNotFoundException("Sorry ,Room Not Found");
        }
        Blob photoBlob  = theRoom.get().getPhoto();
        if(photoBlob!= null){
            return photoBlob.getBytes(1,(int)photoBlob.length());
        }
        return null;
    }

    @Override
    public void deleteRoom(Long roomId) {
        Optional<Room> theRoom = roomRepository.findById(roomId);
        if(theRoom.isPresent()){
            roomRepository.deleteById(roomId);
        }
    }

    @Override
    public Room updateRoom(Long roomId, String roomType, BigDecimal roomPrice, byte[] photoBytes) throws UpdateRoomException {
        Room room = roomRepository.findById(roomId).orElseThrow(()->new ResourseNotFoundException("Room Not Exist"));
        if(roomType!=null){
            room.setRoomType(roomType);
        }
        if(roomPrice!=null){
            room.setRoomPrice(roomPrice);
        }
        if(photoBytes != null){
            try{
                room.setPhoto(new SerialBlob(photoBytes));
            }
            catch (SQLException e) {
                throw new UpdateRoomException("Error in updating the room ");
            }
        }
        return roomRepository.save(room);
    }

    @Override
    public Optional<Room> getRoomByID(Long roomId) {
        return Optional.of(roomRepository.findById(roomId).get());
    }


}
