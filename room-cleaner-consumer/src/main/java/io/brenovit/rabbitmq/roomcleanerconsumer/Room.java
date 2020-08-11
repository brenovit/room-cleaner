package io.brenovit.rabbitmq.roomcleanerconsumer;

import lombok.Data;

@Data
public class Room {
	
	private long id;
	private String name;
	private String number;
	private String info;

}
