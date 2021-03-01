package br.com.company.schedulevote.votingpanel.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import br.com.company.schedulevote.votingpanel.domain.VotingPanelEntity;
import br.com.company.schedulevote.votingpanel.enumaration.VotingPanelStatusEnum;

public interface VotingPanelRepository extends JpaRepository<VotingPanelEntity, Long>{

	VotingPanelEntity findByTittleIgnoreCaseAndDescriptionIgnoreCase(String tittle, String description);

	VotingPanelEntity findByIdAndStatus(Long votingPanelId, VotingPanelStatusEnum status);
}
