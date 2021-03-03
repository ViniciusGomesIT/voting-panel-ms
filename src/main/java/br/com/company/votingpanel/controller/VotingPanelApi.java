package br.com.company.votingpanel.controller;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import br.com.company.votingpanel.dto.commons.CommonResponse;
import br.com.company.votingpanel.dto.request.VotingPanelCreateRequest;
import br.com.company.votingpanel.dto.request.VotingPanelOpenSessionRequest;
import br.com.company.votingpanel.dto.request.VotingPanelVoteRequest;
import br.com.company.votingpanel.dto.response.VotingPanelCreateResponse;
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
	ResponseEntity<VotingPanelCreateResponse> create(@Valid @RequestBody(required = true) VotingPanelCreateRequest votingPanelCreatreRequest, HttpServletRequest servletRequest);
	
	@PostMapping(value = "/management/vote", consumes = "application/json;charset=UTF-8")
	@Operation(summary = "Vota em uma determinada pauta", description = "Realiza a votação em uma determinada pauta de votação")
	@ApiResponses(value = {
			  @ApiResponse(responseCode = "20", description = "Ok"),
			  @ApiResponse(responseCode = "400", description = "Bad Request"), 
			  @ApiResponse(responseCode = "404", description = "Not found"),
			  @ApiResponse(responseCode = "401", description = "Unauthorized"),
			  @ApiResponse(responseCode = "500", description = "Internal Server Error") })
	ResponseEntity<CommonResponse> vote(@Valid @RequestBody(required = true) VotingPanelVoteRequest votingPanelVoteRequest, HttpServletRequest servletRequest);
	
	@PostMapping(value = "/management/open-session", consumes = "application/json;charset=UTF-8")
	@Operation(summary = "Abre a sessão de votação", description = "Realiza a abertura da sessão de votação")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "20", description = "Ok"),
			@ApiResponse(responseCode = "400", description = "Bad Request"), 
			@ApiResponse(responseCode = "404", description = "Not found"),
			@ApiResponse(responseCode = "500", description = "Internal Server Error") })
	ResponseEntity<CommonResponse> openSession(@Valid @RequestBody(required = true) VotingPanelOpenSessionRequest votingPanelOpenSessionRequest, HttpServletRequest servletRequest);
}