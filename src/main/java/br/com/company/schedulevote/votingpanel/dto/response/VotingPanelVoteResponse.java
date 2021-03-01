package br.com.company.schedulevote.votingpanel.dto.response;

import br.com.company.schedulevote.votingpanel.dto.commons.ErrorCommonsResponse;

public class VotingPanelVoteResponse extends ErrorCommonsResponse {

	private String message;

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	@Override
	public String toString() {
		return "VotingPanelVoteResponse [message=" + message + "]";
	}
}
