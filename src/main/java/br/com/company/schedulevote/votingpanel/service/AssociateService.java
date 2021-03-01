package br.com.company.schedulevote.votingpanel.service;

import br.com.company.schedulevote.votingpanel.domain.AssociateEntity;
import br.com.company.schedulevote.votingpanel.dto.request.VotingPanelVoteRequest;

public interface AssociateService {

	AssociateEntity validateAndCreateOrUpdate(VotingPanelVoteRequest votingPanelVoteRequest);
}
