package br.com.company.votingpanel.dto.commons;

public class CommonResponse extends ErrorCommonResponse {

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
