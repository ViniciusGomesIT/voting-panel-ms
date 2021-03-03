package br.com.company.votingpanel.controller.impl;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import br.com.company.votingpanel.controller.VotingPanelApi;
import br.com.company.votingpanel.dto.commons.CommonResponse;
import br.com.company.votingpanel.dto.request.VotingPanelCreateRequest;
import br.com.company.votingpanel.dto.request.VotingPanelOpenSessionRequest;
import br.com.company.votingpanel.dto.request.VotingPanelVoteRequest;
import br.com.company.votingpanel.dto.response.VotingPanelCreateResponse;
import br.com.company.votingpanel.service.impl.VotingPanelServiceImpl;

@RestController
public class VotingPanelResource implements VotingPanelApi {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(VotingPanelResource.class);
	
	private final VotingPanelServiceImpl votingPanelService;
	
	public VotingPanelResource(VotingPanelServiceImpl votingPanelService) {
		this.votingPanelService = votingPanelService;
	}

	@Override
	public ResponseEntity<VotingPanelCreateResponse> create(
			VotingPanelCreateRequest votingPanelCreateRequest, HttpServletRequest servletRequest) {
		
		ResponseEntity<VotingPanelCreateResponse> response = null;
		try {
			response = votingPanelService.create(votingPanelCreateRequest);
		} catch (Exception e) {
			LOGGER.error("Error while trying to create a voting panel: {}", e.getMessage());
			response = new ResponseEntity<VotingPanelCreateResponse>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
		
		return response;
	}

	@Override
	public ResponseEntity<CommonResponse> vote(
			VotingPanelVoteRequest votingPanelVoteRequest, HttpServletRequest servletRequest) {
		
		ResponseEntity<CommonResponse> response = null;
		try {
			response = votingPanelService.vote(votingPanelVoteRequest);
		} catch (Exception e) {
			LOGGER.error("Error while trying to register an vote: {}", e.getMessage());
			response = new ResponseEntity<CommonResponse>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
		
		return response;
	}

	@Override
	public ResponseEntity<CommonResponse> openSession(
			VotingPanelOpenSessionRequest votingPanelOpenSessionRequest, HttpServletRequest servletRequest) {
		
		ResponseEntity<CommonResponse> response = null;
		try {
			response = votingPanelService.openSession(votingPanelOpenSessionRequest);
		} catch (Exception e) {
			LOGGER.error("Error while trying to open an session: {}", e.getMessage());
			response = new ResponseEntity<CommonResponse>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
		
		return response;
	}
}
