package br.com.company.votingpanel.service;

import org.springframework.http.ResponseEntity;

import br.com.company.votingpanel.dto.commons.CommonResponse;
import br.com.company.votingpanel.dto.request.VotingPanelCreateRequest;
import br.com.company.votingpanel.dto.request.VotingPanelOpenSessionRequest;
import br.com.company.votingpanel.dto.request.VotingPanelVoteRequest;
import br.com.company.votingpanel.dto.response.VotingPanelCreateResponse;

public interface VotingPanelService {
	
	ResponseEntity<VotingPanelCreateResponse> create(VotingPanelCreateRequest votingPanelCreatreRequest);

	ResponseEntity<CommonResponse> vote(VotingPanelVoteRequest votingPanelVoteRequest);
	
	ResponseEntity<CommonResponse> openSession(VotingPanelOpenSessionRequest votingPanelOpenSessionRequest);
}
