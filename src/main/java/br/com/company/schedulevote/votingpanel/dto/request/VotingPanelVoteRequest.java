package br.com.company.schedulevote.votingpanel.dto.request;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonProperty;

import br.com.company.schedulevote.votingpanel.enumaration.VotingPanelVoteValueEnum;

public class VotingPanelVoteRequest {

	@NotBlank(message = "CPF precisa ser informado")
	@JsonProperty(value = "cpf")
	private String cpf;

	@JsonProperty(value = "email")
	@NotBlank(message = "Email precisa ser informado")
	@Email(message = "Email inválido")
	private String email;

	@NotNull(message = "É preciso informar o id da pauta")
	@JsonProperty(value = "votingPanelId")
	private Long votingPanelId;

	@NotNull(message = "Necessário informar um valor para o voto. S/N")
	@JsonProperty(value = "vote")
	private VotingPanelVoteValueEnum vote;

	public String getCpf() {
		return cpf;
	}

	public void setCpf(String cpf) {
		this.cpf = cpf;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public Long getVotingPanelId() {
		return votingPanelId;
	}

	public void setVotingPanelId(Long votingPanelId) {
		this.votingPanelId = votingPanelId;
	}

	public VotingPanelVoteValueEnum getVote() {
		return vote;
	}

	public void setVote(VotingPanelVoteValueEnum vote) {
		this.vote = vote;
	}

	@Override
	public String toString() {
		return "VotingPanelVoteRequest [cpf=" + cpf + ", email=" + email + ", votingPanelId=" + votingPanelId
				+ ", vote=" + vote + "]";
	}
}
