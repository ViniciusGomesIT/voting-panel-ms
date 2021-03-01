package br.com.company.schedulevote.votingpanel.service.impl;

import java.util.Optional;

import org.springframework.stereotype.Service;

import br.com.company.schedulevote.votingpanel.domain.AssociateEntity;
import br.com.company.schedulevote.votingpanel.dto.request.VotingPanelVoteRequest;
import br.com.company.schedulevote.votingpanel.mappers.AssociateMapper;
import br.com.company.schedulevote.votingpanel.repository.AssociateRepository;
import br.com.company.schedulevote.votingpanel.service.AssociateService;

@Service
public class AssociateServiceImpl implements AssociateService {
	
	private final AssociateRepository associateRepository;
	private final AssociateMapper associateMapper;
	
	public AssociateServiceImpl(
			AssociateRepository associateRepository,
			AssociateMapper associateMapper) {
		
		this.associateRepository = associateRepository;
		this.associateMapper = associateMapper;
	}

	@Override
	public AssociateEntity validateAndCreateOrUpdate(VotingPanelVoteRequest votingPanelVoteRequest) {
		Optional<AssociateEntity> associateOpt = associateRepository.findByCpf(votingPanelVoteRequest.getCpf());
		
		AssociateEntity associateEntity;
		if ( associateOpt.isPresent() ) {
			associateEntity = associateOpt.get();
			if ( votingPanelVoteRequest.getEmail().equals(associateEntity.getEmail()) ) {
				associateEntity.setEmail(associateEntity.getEmail());
				associateEntity = associateRepository.save(associateEntity);
			}
		} else {
			associateEntity = associateMapper.toAssociateEntity(votingPanelVoteRequest);
			associateEntity = associateRepository.save(associateEntity);
		}
		
		return associateEntity;
	}
}
