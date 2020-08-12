package com.frankmoley.boot.clr.roomclrapp;

import java.util.Arrays;
import java.util.List;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class RoomCleaningPrimer implements CommandLineRunner{
	
	@Value("${amqp.queue.name")
	private String queueName;

    private RestTemplate restTemplate;
    private final RabbitTemplate rabbitTemplate;
    private final ConfigurableApplicationContext context;
    private final ObjectMapper objectMapper;

    @Autowired
    public RoomCleaningPrimer(RabbitTemplate rabbitTemplate, ConfigurableApplicationContext context, ObjectMapper mapper){
        super();
        this.restTemplate = new RestTemplate();
        this.rabbitTemplate = rabbitTemplate;
        this.context = context;
        this.objectMapper = mapper;
    }
    
    @Override
    public void run(String... strings) throws Exception {
        String url = "http://localhost:8080/api/rooms";
        Room[] roomArray = this.restTemplate.getForObject(url, Room[].class);
        List<Room> rooms = Arrays.asList(roomArray);
        rooms.forEach(System.out::println);
        rooms.forEach(r -> {
        	log.info("Seding message");
        	try {
				String jsonString = objectMapper.writeValueAsString(r);
				rabbitTemplate.convertAndSend(queueName, jsonString);
			} catch (JsonProcessingException e) {
				log.error(e.getMessage(), e);
			}
        });
        
        System.exit(SpringApplication.exit(context));
    }
}
