package br.com.company.votingpanel.dto.request;

import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonProperty;

public class VotingPanelOpenSessionRequest {

	@JsonProperty(value = "votingPanelId")
	@NotNull(message = "É necessário informar o id da pauta")
	private Long votingPanelId;

	@JsonProperty(value = "expirationTimeInMinutes")
	private Long expirationTimeInMinutes;

	public Long getVotingPanelId() {
		return votingPanelId;
	}

	public void setVotingPanelId(Long votingPanelId) {
		this.votingPanelId = votingPanelId;
	}

	public Long getExpirationTimeInMinutes() {
		return expirationTimeInMinutes;
	}

	public void setExpirationTimeInMinutes(Long expirationTimeInMinutes) {
		this.expirationTimeInMinutes = expirationTimeInMinutes;
	}

	@Override
	public String toString() {
		return "VotingPanelOpenSessionRequest [votingPanelId=" + votingPanelId + ", expirationTimeInMinutes="
				+ expirationTimeInMinutes + "]";
	}
}
