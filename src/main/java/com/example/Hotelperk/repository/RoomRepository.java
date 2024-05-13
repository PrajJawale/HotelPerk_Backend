package com.example.Hotelperk.repository;

import com.example.Hotelperk.model.Room;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.awt.*;
import java.util.List;

@Repository
public interface RoomRepository extends JpaRepository<Room,Long> {
    @Query("SELECT DISTINCT r.roomType from Room r")
    List<String> getAllRoomTypes();
}
