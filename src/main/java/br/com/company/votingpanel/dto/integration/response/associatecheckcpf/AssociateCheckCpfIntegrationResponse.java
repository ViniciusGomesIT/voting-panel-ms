package br.com.company.votingpanel.dto.integration.response.associatecheckcpf;

import org.springframework.http.HttpStatus;

import br.com.company.votingpanel.enumaration.AssociateCpfCheckerStatusEnum;

public class AssociateCheckCpfIntegrationResponse {

	private AssociateCpfCheckerStatusEnum status;
	private HttpStatus requestStatus;
	private String message;

	public AssociateCpfCheckerStatusEnum getStatus() {
		return status;
	}

	public void setStatus(AssociateCpfCheckerStatusEnum status) {
		this.status = status;
	}

	public HttpStatus getRequestStatus() {
		return requestStatus;
	}

	public void setRequestStatus(HttpStatus requestStatus) {
		this.requestStatus = requestStatus;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}
}
