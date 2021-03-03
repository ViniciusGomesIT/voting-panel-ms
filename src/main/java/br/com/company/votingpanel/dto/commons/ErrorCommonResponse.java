package br.com.company.votingpanel.dto.commons;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import com.fasterxml.jackson.annotation.JsonProperty;

public class ErrorCommonResponse {

	@JsonProperty(value = "errors")
	private List<String> errors;

	public List<String> getErrors() {
		return errors;
	}

	public void setErrors(List<String> errors) {
		this.errors = errors;
	}

	@Override
	public String toString() {
		return "ErrorCommonsResponse [errors=" + errors + "]";
	}
	
	public void addError(String error) {
		if ( Objects.isNull(errors) ) {
			errors = new ArrayList<>();
		}
		
		errors.add(error);
	}
}
