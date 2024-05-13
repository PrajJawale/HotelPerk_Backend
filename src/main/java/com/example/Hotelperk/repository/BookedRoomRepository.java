package com.example.Hotelperk.repository;

import com.example.Hotelperk.model.BookedRoom;
import com.example.Hotelperk.model.Room;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BookedRoomRepository extends JpaRepository<BookedRoom,Long> {
    List<BookedRoom> findByRoomId(Long roomId);
    BookedRoom findByBookingConfirmationCode(String confirmationCode);
}
