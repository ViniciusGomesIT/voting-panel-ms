package br.com.company.votingpanel.domain;

import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import br.com.company.votingpanel.enumaration.VotingPanelStatusEnum;

@Entity
@Table(name = "voting_panel")
public class VotingPanelEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "id", nullable = false, unique = true)
	private Long id;

	@Column(name = "tittle", nullable = false)
	private String tittle;

	@Column(name = "description", nullable = false)
	private String description;

	@Column(name = "expired_date")
	private LocalDateTime expiredDate;
	
	@Column(name = "result_sent")
	private boolean resultSent;

	@Enumerated(EnumType.STRING)
	@Column(name = "status", nullable = false)
	private VotingPanelStatusEnum status;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getTittle() {
		return tittle;
	}

	public void setTittle(String tittle) {
		this.tittle = tittle;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public LocalDateTime getExpiredDate() {
		return expiredDate;
	}

	public void setExpiredDate(LocalDateTime expiredDate) {
		this.expiredDate = expiredDate;
	}
	
	public boolean getResultSent() {
		return resultSent;
	}

	public void setResultSent(boolean resultSent) {
		this.resultSent = resultSent;
	}

	public VotingPanelStatusEnum getStatus() {
		return status;
	}

	public void setStatus(VotingPanelStatusEnum status) {
		this.status = status;
	}
}
