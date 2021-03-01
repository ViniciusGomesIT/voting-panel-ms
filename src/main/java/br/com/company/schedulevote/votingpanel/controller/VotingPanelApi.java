package br.com.company.schedulevote.votingpanel.controller;

import javax.servlet.http.HttpServletRequest;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import br.com.company.schedulevote.votingpanel.dto.request.VotingPanelCreateRequest;
import br.com.company.schedulevote.votingpanel.dto.request.VotingPanelVoteRequest;
import br.com.company.schedulevote.votingpanel.dto.response.VotingPanelCreateResponse;
import br.com.company.schedulevote.votingpanel.dto.response.VotingPanelVoteResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;

@RequestMapping(value = "/api/v1", produces = "application/json;charset=UTF-8")
public interface VotingPanelApi {
	
	@PostMapping(value = "/management/panel", consumes = "application/json;charset=UTF-8")
	@Operation(summary = "Criação de uma pauta de votação", description = "Realiza a criação de uma pauta de votação")
	@ApiResponses(value = {
			  @ApiResponse(responseCode = "201", description = "Created"),
			  @ApiResponse(responseCode = "400", description = "Bad Request"), 
			  @ApiResponse(responseCode = "404", description = "Not found"),
			  @ApiResponse(responseCode = "500", description = "Internal Server Error") })
	ResponseEntity<VotingPanelCreateResponse> create(@RequestBody(required = true) VotingPanelCreateRequest votingPanelCreatreRequest, HttpServletRequest servletRequest);
	
	@PostMapping(value = "/management/vote", consumes = "application/json;charset=UTF-8")
	@Operation(summary = "Criação de uma pauta de votação", description = "Realiza a votação em uma determinada pauta de votação")
	@ApiResponses(value = {
			  @ApiResponse(responseCode = "20", description = "Ok"),
			  @ApiResponse(responseCode = "400", description = "Bad Request"), 
			  @ApiResponse(responseCode = "404", description = "Not found"),
			  @ApiResponse(responseCode = "500", description = "Internal Server Error") })
	ResponseEntity<VotingPanelVoteResponse> vote(@RequestBody(required = true) VotingPanelVoteRequest votingPanelVoteRequest, HttpServletRequest servletRequest);
}