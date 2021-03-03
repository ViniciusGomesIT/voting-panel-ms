package br.com.company.votingpanel.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import br.com.company.votingpanel.enumaration.VotingPanelVoteValueEnum;

@Entity
@Table(name = "vote_entity")
public class VoteEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "id", unique = true, nullable = false)
	private Long id;
	
	@Column(name = "associate_id", nullable = false)
	private Long associateId;
	
	@Column(name = "voting_panel_id", nullable = false)
	private Long votingPanelId;
	
	@Column(name = "vote", nullable = false)
	private VotingPanelVoteValueEnum vote;

	public VoteEntity() {
	}
	
	public VoteEntity(Long associateId, Long votingPanelId, VotingPanelVoteValueEnum vote) {
		this.associateId = associateId;
		this.votingPanelId = votingPanelId;
		this.vote = vote;
	}

	public Long getAssociateId() {
		return associateId;
	}

	public void setAssociateId(Long associateId) {
		this.associateId = associateId;
	}

	public Long getVotingPanelId() {
		return votingPanelId;
	}

	public void setVotingPanelId(Long votingPanelId) {
		this.votingPanelId = votingPanelId;
	}

}
