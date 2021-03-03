package br.com.company.votingpanel.service;

import br.com.company.votingpanel.domain.AssociateEntity;
import br.com.company.votingpanel.dto.integration.response.associatecheckcpf.AssociateCheckCpfIntegrationResponse;
import br.com.company.votingpanel.dto.request.VotingPanelVoteRequest;

public interface AssociateService {

	AssociateEntity validateAndCreateOrUpdate(VotingPanelVoteRequest votingPanelVoteRequest);
	
	AssociateCheckCpfIntegrationResponse validateAssociateCpf(String cpf);
}
