package com.careerit.sl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import com.careerit.sl.dto.TeamReqDto;
import com.careerit.sl.service.TeamService;
import com.careerit.sl.util.JsonLoader;

@SpringBootApplication
public class JpalApplication implements CommandLineRunner {
	
	private JsonLoader loader;
	private TeamService service;
	
	@Autowired
	public JpalApplication(final JsonLoader loader, final TeamService service) {
		this.loader = loader;
		this.service = service;
		
	}

	public static void main(String[] args) {
		SpringApplication.run(JpalApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
		List<TeamReqDto> teams = service.getTeams();
		if(teams.size() == 0) {
			loader.loadDataToDB();			
		}
	}
}
