package com.example.server.rtc;

import org.kurento.client.KurentoClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

public class KurentoRoomManager {

    private final Logger log = LoggerFactory.getLogger(KurentoRoomManager.class);

    @Autowired
    private KurentoClient kurento;

    private final ConcurrentMap<String, KurentoRoom> rooms = new ConcurrentHashMap<>();

    public KurentoRoom getRoom(String roomName) {
        log.debug("Searching for room {}", roomName);
        KurentoRoom kurentoRoom = rooms.get(roomName);

        if (kurentoRoom == null) {
            log.debug("Room {} not existent. Will create now!", roomName);
            kurentoRoom = new KurentoRoom(roomName, kurento.createMediaPipeline());
            rooms.put(roomName, kurentoRoom);
        }
        log.debug("Room {} found", roomName);
        return kurentoRoom;
    }

    public void removeRoom(KurentoRoom kurentoRoom) {
        this.rooms.remove(kurentoRoom.getName());
        kurentoRoom.close();
        log.info("Room {} removed and closed", kurentoRoom.getName());
    }
}
