package br.com.company.schedulevote.votingpanel.service.impl;

import java.util.Objects;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import br.com.company.schedulevote.votingpanel.domain.AssociateEntity;
import br.com.company.schedulevote.votingpanel.domain.VotingPanelEntity;
import br.com.company.schedulevote.votingpanel.dto.request.VotingPanelCreateRequest;
import br.com.company.schedulevote.votingpanel.dto.request.VotingPanelVoteRequest;
import br.com.company.schedulevote.votingpanel.dto.response.VotingPanelCreateResponse;
import br.com.company.schedulevote.votingpanel.dto.response.VotingPanelVoteResponse;
import br.com.company.schedulevote.votingpanel.enumaration.VotingPanelStatusEnum;
import br.com.company.schedulevote.votingpanel.mappers.VotingPanelMapper;
import br.com.company.schedulevote.votingpanel.repository.VotingPanelRepository;
import br.com.company.schedulevote.votingpanel.service.VotingPanelService;

@Service
//TODO adicionar properties para as mensagens de erro;
//TODO adicionar properties para o regex da validação do cpf e do tamanho do cpf e tempo de expiração da sessão;
//TODO adicionar properties para as mensagens de validação dos dtos;
//TODO adicionar chamada do feignClient para validar o CPF;
//TODO adicionar schedule para verificar a expiração da pauta e alterar o status e mandar o email com thymeleaf com o resultado(tentar aplicar fila) com intervalo de 5 minutos;
//TODO organizar métodos de validação de build das informações em classes utils
//TODO organizar pacotes dependendo das respostas das dúvidas com relação aos associados;
//TODO Verificar swagger;
//TODO adicionar testes unitários nos resources e services;
//TODO adicionar endpoint de abertura da pauta
public class VotingPanelServiceImpl implements VotingPanelService {
	
	private static final String NUMERIC_REGEX_VALIDATOR = "[0-9]+";
	private static final Integer DEFAULT_CPF_LENGTH = 11;
	private static final Long DEFAULT_SESSION_EXPIRATION_TIME_IN_MINUTES = 1L;

	private static final Logger LOGGER = LoggerFactory.getLogger(VotingPanelServiceImpl.class);
	
	private final VotingPanelRepository votingPanelRepository;
	private final VotingPanelMapper votingPanelMapper;
	private final AssociateServiceImpl associateService;
	
	public VotingPanelServiceImpl(
			VotingPanelRepository votingPanelRepository,
			VotingPanelMapper votingPanelMapper,
			AssociateServiceImpl associateService) {
		
		this.votingPanelRepository = votingPanelRepository;
		this.votingPanelMapper = votingPanelMapper;
		this.associateService = associateService;
	}

	@Override
	public ResponseEntity<VotingPanelCreateResponse> create(VotingPanelCreateRequest votingPanelCreateRequest) {
		VotingPanelCreateResponse votingPanelCreatreResponse = new VotingPanelCreateResponse();
		HttpStatus httpStatus = HttpStatus.CREATED;
		
		printInitialLog(votingPanelCreateRequest);
		
		VotingPanelEntity votingPanelEntity = votingPanelRepository.findByTittleIgnoreCaseAndDescriptionIgnoreCase(votingPanelCreateRequest.getTittle(), votingPanelCreateRequest.getDescription());
		if ( Objects.nonNull(votingPanelEntity) ) {
			votingPanelCreatreResponse.addError("Pauta já cadastrada");

			httpStatus = HttpStatus.BAD_REQUEST;
		} else {
			votingPanelEntity = votingPanelMapper.toVotingPanelEntity(votingPanelCreateRequest, VotingPanelStatusEnum.OPEN);
			
			votingPanelEntity = votingPanelRepository.save(votingPanelEntity);
			votingPanelCreatreResponse = votingPanelMapper.toVotingPanelCreatreResponse(votingPanelEntity);
			
			httpStatus = HttpStatus.CREATED;
		}
		
		printFinalLog(votingPanelCreatreResponse, httpStatus);
		
		return new ResponseEntity<VotingPanelCreateResponse>(votingPanelCreatreResponse, httpStatus);
	}
	
	@Override
	public ResponseEntity<VotingPanelVoteResponse> vote(VotingPanelVoteRequest votingPanelVoteRequest) {
		VotingPanelVoteResponse votingPanelVoteResponse = new VotingPanelVoteResponse();
		HttpStatus httpStatus = HttpStatus.OK;
		
		printInitialLog(votingPanelVoteRequest);
		
		if ( !isValidCpf(votingPanelVoteRequest.getCpf()) ) {
			httpStatus = HttpStatus.BAD_REQUEST;
			votingPanelVoteResponse.addError("CPF informado é inválido");
		} else {
			processVote(votingPanelVoteRequest, votingPanelVoteResponse, httpStatus);
		}
		
		printFinalLog(votingPanelVoteResponse, httpStatus);
		
		return new ResponseEntity<VotingPanelVoteResponse>(votingPanelVoteResponse, httpStatus);
	}

	private void processVote(VotingPanelVoteRequest votingPanelVoteRequest, VotingPanelVoteResponse votingPanelVoteResponse, HttpStatus httpStatus) {
		VotingPanelEntity votingPanelEntity = votingPanelRepository.findByIdAndStatus(votingPanelVoteRequest.getVotingPanelId(), VotingPanelStatusEnum.OPEN);
		if ( Objects.isNull(votingPanelEntity) ) {
			httpStatus = HttpStatus.NOT_FOUND;
			votingPanelVoteResponse.addError("Pauta não encontrada!");
		}
		
		//TODO adicionar checagem do cpf com feign
		//TODO só realizar a validação do associado se ele puder votar
		AssociateEntity associateEntity = associateService.validateAndCreateOrUpdate(votingPanelVoteRequest);
	}
	
	private boolean isValidCpf(String cpf) {
		return cpf.matches(NUMERIC_REGEX_VALIDATOR) && cpf.length() == DEFAULT_CPF_LENGTH;
	}

	private void printInitialLog(Object request) {
		LOGGER.info("[Method: create].[Thread: {}].[Request: {}]", Thread.currentThread().getName(), request.toString());
	}
	
	private void printFinalLog(Object response, HttpStatus httpStatus) {
		LOGGER.info("[Method: create].[Thread: {}].[Status: {}].[Response: {}]", Thread.currentThread().getName(), httpStatus.toString(), response.toString());
	}
}
