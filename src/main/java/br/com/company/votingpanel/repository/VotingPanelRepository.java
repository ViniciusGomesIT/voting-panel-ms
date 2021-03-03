package br.com.company.votingpanel.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import br.com.company.votingpanel.domain.VotingPanelEntity;
import br.com.company.votingpanel.enumaration.VotingPanelStatusEnum;

public interface VotingPanelRepository extends JpaRepository<VotingPanelEntity, Long>{

	VotingPanelEntity findByTittleIgnoreCaseAndDescriptionIgnoreCase(String tittle, String description);

	Optional<VotingPanelEntity> findByIdAndStatus(Long votingPanelId, VotingPanelStatusEnum status);
}
