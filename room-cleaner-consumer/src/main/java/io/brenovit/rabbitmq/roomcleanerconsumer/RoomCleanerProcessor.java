package io.brenovit.rabbitmq.roomcleanerconsumer;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.extern.log4j.Log4j2;

@Log4j2
@Component
public class RoomCleanerProcessor {
	
	private final ObjectMapper mapper;
	
	@Autowired
	public RoomCleanerProcessor(ObjectMapper mapper) {
		super();
		this.mapper = mapper;		
	}
	
	public void receiveMessage(String roomJson) {
		log.info("Message received");
		try {
			Room room  = this.mapper.readValue(roomJson, Room.class);
			log.info("Room ready for cleaning "+room.getNumber());
		}catch (Exception e) {
			log.error(e.getMessage(), e);
		}
	}
	
}
