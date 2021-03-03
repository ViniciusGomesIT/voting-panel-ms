package br.com.company.votingpanel.service.impl;

import java.time.LocalDateTime;
import java.util.Objects;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import br.com.company.votingpanel.constants.VotingPanelConstants;
import br.com.company.votingpanel.domain.AssociateEntity;
import br.com.company.votingpanel.domain.VoteEntity;
import br.com.company.votingpanel.domain.VotingPanelEntity;
import br.com.company.votingpanel.dto.commons.CommonResponse;
import br.com.company.votingpanel.dto.integration.response.associatecheckcpf.AssociateCheckCpfIntegrationResponse;
import br.com.company.votingpanel.dto.request.VotingPanelCreateRequest;
import br.com.company.votingpanel.dto.request.VotingPanelOpenSessionRequest;
import br.com.company.votingpanel.dto.request.VotingPanelVoteRequest;
import br.com.company.votingpanel.dto.response.VotingPanelCreateResponse;
import br.com.company.votingpanel.enumaration.MethodNamesEnum;
import br.com.company.votingpanel.enumaration.VotingPanelStatusEnum;
import br.com.company.votingpanel.mappers.VotingPanelMapper;
import br.com.company.votingpanel.repository.VoteRepository;
import br.com.company.votingpanel.repository.VotingPanelRepository;
import br.com.company.votingpanel.service.VotingPanelService;
import br.com.company.votingpanel.util.MessageService;
import br.com.company.votingpanel.util.ValidatorDataUtils;

//TODO adicionar properties para as mensagens de erro;
//TODO adicionar properties para o regex da validação do cpf e do tamanho do cpf e tempo de expiração da sessão;
//TODO adicionar properties para as mensagens de validação dos dtos;
//TODO adicionar schedule para verificar a expiração da pauta e alterar o status e mandar o email com thymeleaf com o resultado(tentar aplicar fila) com intervalo de 5 minutos;
//TODO Verificar swagger;
//TODO adicionar testes unitários nos resources e services;
@Service
public class VotingPanelServiceImpl implements VotingPanelService {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(VotingPanelServiceImpl.class);

	private final VotingPanelRepository votingPanelRepository;
	private final VoteRepository voteRepository;
	private final VotingPanelMapper votingPanelMapper;
	private final AssociateServiceImpl associateService;
	private final MessageService messageService;
	
	public VotingPanelServiceImpl(
			VotingPanelRepository votingPanelRepository,
			VoteRepository voteRepository,
			VotingPanelMapper votingPanelMapper,
			AssociateServiceImpl associateService,
			MessageService messageService) {
		
		this.votingPanelRepository = votingPanelRepository;
		this.voteRepository = voteRepository;
		this.votingPanelMapper = votingPanelMapper;
		this.associateService = associateService;
		this.messageService = messageService;
	}

	@Override
	public ResponseEntity<VotingPanelCreateResponse> create(VotingPanelCreateRequest votingPanelCreateRequest) {
		VotingPanelCreateResponse votingPanelCreatreResponse = new VotingPanelCreateResponse();
		HttpStatus httpStatus = HttpStatus.CREATED;
		
		printInitialLog(votingPanelCreateRequest, MethodNamesEnum.CREATE.getMethodName());
		
		VotingPanelEntity votingPanelEntity = votingPanelRepository.findByTittleIgnoreCaseAndDescriptionIgnoreCase(votingPanelCreateRequest.getTittle(), votingPanelCreateRequest.getDescription());
		if ( Objects.nonNull(votingPanelEntity) ) {
			votingPanelCreatreResponse.addError("Pauta já cadastrada");

			httpStatus = HttpStatus.BAD_REQUEST;
		} else {
			votingPanelEntity = votingPanelMapper.toVotingPanelEntity(votingPanelCreateRequest, VotingPanelStatusEnum.READY_TO_OPEN);
			
			votingPanelEntity = votingPanelRepository.save(votingPanelEntity);
			votingPanelCreatreResponse = votingPanelMapper.toVotingPanelCreatreResponse(votingPanelEntity);
			
			httpStatus = HttpStatus.CREATED;
		}
		
		printFinalLog(votingPanelCreatreResponse, httpStatus, MethodNamesEnum.CREATE.getMethodName());
		
		return new ResponseEntity<VotingPanelCreateResponse>(votingPanelCreatreResponse, httpStatus);
	}
	
	@Override
	public ResponseEntity<CommonResponse> vote(VotingPanelVoteRequest votingPanelVoteRequest) {
		CommonResponse commonResposne = new CommonResponse();
		HttpStatus httpStatus = HttpStatus.OK;
		
		printInitialLog(votingPanelVoteRequest, MethodNamesEnum.VOTE.getMethodName());
		
		if ( !ValidatorDataUtils.isValidCpf(votingPanelVoteRequest.getCpf()) ) {
			httpStatus = HttpStatus.BAD_REQUEST;
			commonResposne.addError("CPF inválido");
		} else {
			commonResposne = processVote(votingPanelVoteRequest, commonResposne, httpStatus);
		}
		
		printFinalLog(commonResposne, httpStatus, MethodNamesEnum.VOTE.getMethodName());
		
		return new ResponseEntity<CommonResponse>(commonResposne, httpStatus);
	}
	
	@Override
	public ResponseEntity<CommonResponse> openSession(VotingPanelOpenSessionRequest votingPanelOpenSessionRequest) {
		CommonResponse commonResponse = new CommonResponse();
		HttpStatus httpStatus = HttpStatus.OK;
		
		printInitialLog(votingPanelOpenSessionRequest, MethodNamesEnum.OPEN_SESSION.getMethodName());
		
		VotingPanelEntity votingPanelEntity = votingPanelRepository.findByIdAndStatus(votingPanelOpenSessionRequest.getVotingPanelId(), VotingPanelStatusEnum.READY_TO_OPEN);
		httpStatus = ValidatorDataUtils.checkVotingPanel(commonResponse, votingPanelEntity);
		if ( httpStatus.value() != HttpStatus.OK.value() ) {
			return new ResponseEntity<CommonResponse>(commonResponse, httpStatus);
		}
		
		setOpenSessionInformation(votingPanelOpenSessionRequest, votingPanelEntity);
		commonResponse.setMessage("Sessão Aberta com sucesso");
		
		printInitialLog(commonResponse, MethodNamesEnum.OPEN_SESSION.getMethodName());
		
		return new ResponseEntity<CommonResponse>(commonResponse, httpStatus);
	}

	private CommonResponse processVote(VotingPanelVoteRequest votingPanelVoteRequest, CommonResponse votingPanelVoteResponse, HttpStatus httpStatus) {
		VotingPanelEntity votingPanelEntity = votingPanelRepository.findByIdAndStatus(votingPanelVoteRequest.getVotingPanelId(), VotingPanelStatusEnum.OPEN);
		
		httpStatus = ValidatorDataUtils.checkVotingPanel(votingPanelVoteResponse, votingPanelEntity);
		if ( httpStatus.value() != HttpStatus.OK.value() ) {
			return votingPanelVoteResponse;
		}
		
		AssociateCheckCpfIntegrationResponse integrationCpfCheckResponse = associateService.validateAssociateCpf(votingPanelVoteRequest.getCpf());
		if ( integrationCpfCheckResponse.getRequestStatus().value() == HttpStatus.OK.value() ) {
			if ( ValidatorDataUtils.canAssociateVoteInVotingPanel(integrationCpfCheckResponse) ) {
				
				AssociateEntity associateEntity = associateService.validateAndCreateOrUpdate(votingPanelVoteRequest);
				VoteEntity voteEntity = new VoteEntity(associateEntity.getId(), votingPanelEntity.getId(), votingPanelVoteRequest.getVote());
				voteRepository.save(voteEntity);
				votingPanelVoteResponse.setMessage("Voto registrado com sucesso!");
				
			} else {
				votingPanelVoteResponse.setMessage("Associado não pode votar na pauta!");
				httpStatus = HttpStatus.UNAUTHORIZED;
			}
			
		} else {
			votingPanelVoteResponse.setMessage(integrationCpfCheckResponse.getMessage());
			httpStatus = integrationCpfCheckResponse.getRequestStatus();
		}
		
		return votingPanelVoteResponse;
	}

	private void setOpenSessionInformation(VotingPanelOpenSessionRequest votingPanelOpenSessionRequest, VotingPanelEntity votingPanelEntity) {
		Long sessionExpirationInMinutes = null;
		if ( Objects.isNull(votingPanelOpenSessionRequest.getExpirationTimeInMinutes()) 
				|| votingPanelOpenSessionRequest.getExpirationTimeInMinutes().longValue() < VotingPanelConstants.DEFAULT_SESSION_EXPIRATION_TIME_IN_MINUTES.longValue() ) {
			sessionExpirationInMinutes = VotingPanelConstants.DEFAULT_SESSION_EXPIRATION_TIME_IN_MINUTES;
		} else {
			sessionExpirationInMinutes = votingPanelOpenSessionRequest.getExpirationTimeInMinutes();
		}
		
		votingPanelEntity.setExpiredDate(LocalDateTime.now().plusMinutes(sessionExpirationInMinutes));
		votingPanelEntity.setStatus(VotingPanelStatusEnum.OPEN);
		
		votingPanelRepository.save(votingPanelEntity);
	}

	private void printInitialLog(Object request, String method) {
		LOGGER.info("[Method: {}].[Thread: {}].[Request: {}]", method, Thread.currentThread().getName(), request.toString());
	}
	
	private void printFinalLog(Object response, HttpStatus httpStatus, String method) {
		LOGGER.info("[Method: {}].[Thread: {}].[Status: {}].[Response: {}]", method, Thread.currentThread().getName(), httpStatus.toString(), response.toString());
	}
}
