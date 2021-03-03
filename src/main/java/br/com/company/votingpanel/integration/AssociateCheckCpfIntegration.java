package br.com.company.votingpanel.integration;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;

import br.com.company.votingpanel.dto.integration.response.associatecheckcpf.AssociateCheckCpfIntegrationResponse;

@FeignClient(
		name = "associateCheckCpfIntegration",
		url = "${api.integration.associate.uri}"
)
public interface AssociateCheckCpfIntegration {

	@GetMapping(
			path = "${api.integration.associate.checkCpf}",
			produces = MediaType.APPLICATION_JSON_VALUE
	)
	AssociateCheckCpfIntegrationResponse checkAssociateCpfStatus(String cpf);
}
