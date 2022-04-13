package com.careerit.sl.util;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import com.careerit.sl.domain.Player;
import com.careerit.sl.domain.Team;
import com.careerit.sl.dto.PlayerDto;
import com.careerit.sl.dto.TeamDto;
import com.careerit.sl.repo.TeamRepo;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

@Component
public class JsonLoader {
	
	private TeamRepo teamRepo;

	@Autowired
	public JsonLoader(final TeamRepo teamRepo) {
		this.teamRepo = teamRepo;
	}
	
	private List<TeamDto> getTeamsFromJSON() {
		ObjectMapper obj = new ObjectMapper();
		List<TeamDto> teamList = null;
		try {
			teamList = obj.readValue(this.getClass().getResourceAsStream("/ipl2020.json"),
					new TypeReference<List<TeamDto>>() {
					});
		} catch (IOException e) {
			e.printStackTrace();
		}
		return teamList;
	}

	private List<Player> convertToPlayers(List<PlayerDto> list, Team team) {
		List<Player> players = new ArrayList<>();
		for (PlayerDto p : list) {
			players.add(Player.builder().name(p.getName()).role(p.getRole()).price(p.getPrice()).team(team).build());
		}
		return players;
	}

	private Team convetToTeam(TeamDto teamDTO) {
		Team team = new Team();
		team.setCity(teamDTO.getCity());
		team.setCoach(teamDTO.getCoach());
		team.setHome(teamDTO.getHome());
		team.setName(teamDTO.getName());
		team.setLabel(teamDTO.getLabel());

		return team;
	}

	public void loadDataToDB() {
		// get teamDTO with mapped teamDTO
		List<TeamDto> teamDTOList = getTeamsFromJSON();
		
		for (TeamDto obj : teamDTOList) {
			// get the playerDTO
			List<PlayerDto> playerDTOList = obj.getPlayers();
			
			// convert teamDTO to teams
			Team team = convetToTeam(obj);
			
			// convert player DTO to player
			List<Player> players = convertToPlayers(playerDTOList, team);
			
			// add the players back to teams
			team.addPlayers(players);
			
			// save teams again then players will be saved into DB.
			teamRepo.save(team);
			
		}
		
		System.out.println("Loading data to DB successfully");
	}

}
