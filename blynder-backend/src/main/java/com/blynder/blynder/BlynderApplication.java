package com.blynder.blynder;

import com.blynder.blynder.service.IndexingService;
import com.blynder.blynder.service.StorageService;
import com.corundumstudio.socketio.Configuration;
import com.corundumstudio.socketio.SocketIOServer;
import com.corundumstudio.socketio.Transport;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class BlynderApplication implements CommandLineRunner {

	@Value("${socketio-host}")
	private String host;

	@Value("${socketio-port}")
	private Integer port;

	@Value("${socketio-origin}")
	private String origin;

	private final IndexingService is;
	private final StorageService storageService;

	public BlynderApplication(IndexingService is, StorageService storageService) {
		this.is = is;
		this.storageService = storageService;
	}

	@Bean
	public SocketIOServer socketIOServer() {
		Configuration config = new Configuration();
		config.setHostname(host);
		config.setPort(port);
		config.setOrigin(origin);
		config.setTransports(Transport.WEBSOCKET);
		return new SocketIOServer(config);
	}

	public static void main(String[] args) {
		SpringApplication.run(BlynderApplication.class, args);
	}


	@Override
	public void run(String... args) throws Exception {
		is.initiateIndexing();
		storageService.init();
	}
}
