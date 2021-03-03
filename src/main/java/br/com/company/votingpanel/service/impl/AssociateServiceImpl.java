package br.com.company.votingpanel.service.impl;

import java.util.Objects;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import br.com.company.votingpanel.domain.AssociateEntity;
import br.com.company.votingpanel.dto.integration.response.associatecheckcpf.AssociateCheckCpfIntegrationResponse;
import br.com.company.votingpanel.dto.request.VotingPanelVoteRequest;
import br.com.company.votingpanel.integration.AssociateCheckCpfIntegration;
import br.com.company.votingpanel.mappers.AssociateMapper;
import br.com.company.votingpanel.repository.AssociateRepository;
import br.com.company.votingpanel.service.AssociateService;
import feign.FeignException;

//TODO MELHORAR VALIDAÇÃO DA RESPOSTA DO FEIGN CRIANDO UM ENUM BUILDER
//TODO ADICIONAR AS MENSAGENS DE ERRO NO PROPERTIES
@Service
public class AssociateServiceImpl implements AssociateService {
	
	private final AssociateRepository associateRepository;
	private final AssociateMapper associateMapper;
	private final AssociateCheckCpfIntegration associateCheckCpfIntegration;
	
	public AssociateServiceImpl(
			AssociateRepository associateRepository,
			AssociateMapper associateMapper,
			AssociateCheckCpfIntegration associateCheckCpfIntegration) {
		
		this.associateRepository = associateRepository;
		this.associateMapper = associateMapper;
		this.associateCheckCpfIntegration = associateCheckCpfIntegration;
	}

	@Override
	public AssociateEntity validateAndCreateOrUpdate(VotingPanelVoteRequest votingPanelVoteRequest) {
		AssociateEntity associateEntity = associateRepository.findByCpf(votingPanelVoteRequest.getCpf());
		
		if ( Objects.nonNull(associateEntity) ) {
			if ( votingPanelVoteRequest.getEmail().equals(associateEntity.getEmail()) ) {
				associateEntity.setEmail(associateEntity.getEmail());
				associateEntity = associateRepository.save(associateEntity);
			}
		} else {
			associateEntity = associateMapper.toAssociateEntity(votingPanelVoteRequest);
			associateEntity = associateRepository.save(associateEntity);
		}
		
		return associateEntity;
	}

	@Override
	public AssociateCheckCpfIntegrationResponse validateAssociateCpf(String cpf) {
		AssociateCheckCpfIntegrationResponse associateCheckCpfIntegrationResponse = new AssociateCheckCpfIntegrationResponse();
		
		try {
			associateCheckCpfIntegrationResponse = associateCheckCpfIntegration.checkAssociateCpfStatus(cpf);
			associateCheckCpfIntegrationResponse.setRequestStatus(HttpStatus.OK);
		} catch (FeignException e) {
			validateFeignException(e, associateCheckCpfIntegrationResponse);
		}
		
		return associateCheckCpfIntegrationResponse;
	}

	private void validateFeignException(
			FeignException e, AssociateCheckCpfIntegrationResponse associateCheckCpfIntegrationResponse) {

		if ( e.status() == HttpStatus.NOT_FOUND.value() ) {
			associateCheckCpfIntegrationResponse.setRequestStatus(HttpStatus.NOT_FOUND);
			associateCheckCpfIntegrationResponse.setMessage("CPF inválido!");
		} else if ( e.status() != HttpStatus.OK.value() ) {
			associateCheckCpfIntegrationResponse.setRequestStatus(HttpStatus.INTERNAL_SERVER_ERROR);
			associateCheckCpfIntegrationResponse.setMessage("Problemas ao validar o CPF informado. Tente novamente mais tarde.");
		}
	}
}
