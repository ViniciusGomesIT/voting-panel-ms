package br.com.company.schedulevote.votingpanel.dto.request;

import javax.validation.constraints.NotBlank;

import com.fasterxml.jackson.annotation.JsonProperty;

public class VotingPanelCreateRequest {

	@JsonProperty(value = "tittle")
	@NotBlank(message = "É necessário informar um título")
	private String tittle;
	
	@JsonProperty(value = "description")
	@NotBlank(message = "É necessário informar uma descrição")
	private String description;
	
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

	@Override
	public String toString() {
		return "VotingPanelCreateRequest [tittle=" + tittle + ", description=" + description + "]";
	}
}
