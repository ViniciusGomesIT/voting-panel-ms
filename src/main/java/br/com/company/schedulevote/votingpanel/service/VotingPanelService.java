package br.com.company.schedulevote.votingpanel.service;

import org.springframework.http.ResponseEntity;

import br.com.company.schedulevote.votingpanel.dto.request.VotingPanelCreateRequest;
import br.com.company.schedulevote.votingpanel.dto.request.VotingPanelVoteRequest;
import br.com.company.schedulevote.votingpanel.dto.response.VotingPanelCreateResponse;
import br.com.company.schedulevote.votingpanel.dto.response.VotingPanelVoteResponse;

public interface VotingPanelService {
	
	ResponseEntity<VotingPanelCreateResponse> create(VotingPanelCreateRequest votingPanelCreatreRequest);

	ResponseEntity<VotingPanelVoteResponse> vote(VotingPanelVoteRequest votingPanelVoteRequest);
}
