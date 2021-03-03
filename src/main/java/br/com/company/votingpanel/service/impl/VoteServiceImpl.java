package br.com.company.votingpanel.service.impl;

import org.springframework.stereotype.Service;

import br.com.company.votingpanel.domain.VoteEntity;
import br.com.company.votingpanel.repository.VoteRepository;
import br.com.company.votingpanel.service.VoteService;

@Service
public class VoteServiceImpl implements VoteService {
	
	private final VoteRepository voteRepository;

	public VoteServiceImpl(VoteRepository voteRepository) {
		this.voteRepository = voteRepository;
	}

	@Override
	public VoteEntity saveVote(VoteEntity voteEntity) {
		return voteRepository.save(voteEntity);
	}
}
