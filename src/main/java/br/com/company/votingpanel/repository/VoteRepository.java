package br.com.company.votingpanel.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import br.com.company.votingpanel.domain.VoteEntity;

public interface VoteRepository extends JpaRepository<VoteEntity, Long> {

}
