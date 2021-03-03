package br.com.company.votingpanel.service;

import br.com.company.votingpanel.domain.VoteEntity;

public interface VoteService {

	VoteEntity saveVote(VoteEntity voteEntity);
}
