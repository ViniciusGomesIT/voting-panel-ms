package br.com.company.schedulevote.votingpanel.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;

import br.com.company.schedulevote.votingpanel.dto.commons.ErrorCommonsResponse;

public class VotingPanelCreateResponse extends ErrorCommonsResponse {

	@JsonProperty(value = "id")
	private Long id;

	@JsonProperty(value = "tittle")
	private String tittle;

	@JsonProperty(value = "description")
	private String description;
	
	@JsonProperty(value = "status")
	private String status;

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
	
	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	@Override
	public String toString() {
		return "VotingPanelCreatreResponse [id=" + id + ", tittle=" + tittle + ", description=" + description
				+ ", status=" + status + "]";
	}
}
