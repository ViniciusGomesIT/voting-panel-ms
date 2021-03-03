package br.com.company.votingpanel.service.impl;

import java.time.LocalDateTime;
import java.util.Objects;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import br.com.company.votingpanel.config.ConfigurationParameters;
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
import br.com.company.votingpanel.repository.VotingPanelRepository;
import br.com.company.votingpanel.service.VotingPanelService;
import br.com.company.votingpanel.util.MessageService;
import br.com.company.votingpanel.util.ValidatorDataUtils;

//TODO adicionar properties para as mensagens de validação dos dtos;
//TODO adicionar schedule para verificar a expiração da pauta e alterar o status e mandar o email com thymeleaf com o resultado(tentar aplicar fila) com intervalo de 5 minutos;
//TODO MELHORAR DOCUMENTAÇÃO swagger;
//TODO adicionar testes unitários nos resources e services;
//TODO Verificar problema com as anotações de validação dos campos dos dtos
//TODO MELHORAR VALIDAÇÃO DA RESPOSTA DO FEIGN no ASSOCIATE SERVICE CRIANDO UM ENUM BUILDER
@Service
public class VotingPanelServiceImpl implements VotingPanelService {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(VotingPanelServiceImpl.class);

	private final VotingPanelRepository votingPanelRepository;
	private final VotingPanelMapper votingPanelMapper;
	private final AssociateServiceImpl associateService;
	private final VoteServiceImpl voteService;
	private final MessageService messageService;
	private final ValidatorDataUtils validatorDataUtils;
	private final ConfigurationParameters configurationParameters;
	
	public VotingPanelServiceImpl(
			VotingPanelRepository votingPanelRepository,
			VoteServiceImpl voteService,
			AssociateServiceImpl associateService,
			VotingPanelMapper votingPanelMapper,
			MessageService messageService,
			ValidatorDataUtils validatorDataUtils,
			ConfigurationParameters configurationParameters) {
		
		this.votingPanelRepository = votingPanelRepository;
		this.voteService = voteService;
		this.associateService = associateService;
		this.votingPanelMapper = votingPanelMapper;
		this.messageService = messageService;
		this.validatorDataUtils = validatorDataUtils;
		this.configurationParameters = configurationParameters;
	}

	@Override
	public ResponseEntity<VotingPanelCreateResponse> create(VotingPanelCreateRequest votingPanelCreateRequest) {
		VotingPanelCreateResponse votingPanelCreatreResponse = new VotingPanelCreateResponse();
		HttpStatus httpStatus = HttpStatus.CREATED;
		
		printInitialLog(votingPanelCreateRequest, MethodNamesEnum.CREATE.getMethodName());
		
		VotingPanelEntity votingPanelEntity = votingPanelRepository.findByTittleIgnoreCaseAndDescriptionIgnoreCase(votingPanelCreateRequest.getTittle(), votingPanelCreateRequest.getDescription());
		if ( Objects.nonNull(votingPanelEntity) ) {
			votingPanelCreatreResponse.addError(messageService.getMessage("error.voting.panel.already.registered"));

			httpStatus = HttpStatus.BAD_REQUEST;
		} else {
			votingPanelEntity = votingPanelMapper.toVotingPanelEntity(votingPanelCreateRequest, VotingPanelStatusEnum.READY_TO_OPEN);
			
			votingPanelEntity = votingPanelRepository.save(votingPanelEntity);
			votingPanelCreatreResponse = votingPanelMapper.toVotingPanelCreatreResponse(votingPanelEntity);
		}
		
		printFinalLog(votingPanelCreatreResponse, httpStatus, MethodNamesEnum.CREATE.getMethodName());
		
		return new ResponseEntity<>(votingPanelCreatreResponse, httpStatus);
	}
	
	@Override
	public ResponseEntity<CommonResponse> vote(VotingPanelVoteRequest votingPanelVoteRequest) {
		CommonResponse commonResposne = new CommonResponse();
		HttpStatus httpStatus = HttpStatus.OK;
		
		printInitialLog(votingPanelVoteRequest, MethodNamesEnum.VOTE.getMethodName());
		
		if ( !validatorDataUtils.isValidCpf(votingPanelVoteRequest.getCpf()) ) {
			httpStatus = HttpStatus.BAD_REQUEST;
			commonResposne.addError(messageService.getMessage("error.invalid.cpf"));
		} else {
			commonResposne = processVote(votingPanelVoteRequest, commonResposne, httpStatus);
		}
		
		printFinalLog(commonResposne, httpStatus, MethodNamesEnum.VOTE.getMethodName());
		
		return new ResponseEntity<>(commonResposne, httpStatus);
	}
	
	@Override
	public ResponseEntity<CommonResponse> openSession(VotingPanelOpenSessionRequest votingPanelOpenSessionRequest) {
		CommonResponse commonResponse = new CommonResponse();
		HttpStatus httpStatus;
		
		printInitialLog(votingPanelOpenSessionRequest, MethodNamesEnum.OPEN_SESSION.getMethodName());
		
		Optional<VotingPanelEntity> votingPanelEntityOpt = votingPanelRepository.findByIdAndStatus(votingPanelOpenSessionRequest.getVotingPanelId(), VotingPanelStatusEnum.READY_TO_OPEN);
		httpStatus = validatorDataUtils.checkVotingPanel(commonResponse, votingPanelEntityOpt);
		if ( httpStatus.value() != HttpStatus.OK.value() ) {
			return new ResponseEntity<>(commonResponse, httpStatus);
		}
		
		buildOpenSessionInformation(votingPanelOpenSessionRequest, votingPanelEntityOpt);
		commonResponse.setMessage(messageService.getMessage("message.open.session.sucess"));
		
		printInitialLog(commonResponse, MethodNamesEnum.OPEN_SESSION.getMethodName());
		
		return new ResponseEntity<>(commonResponse, httpStatus);
	}

	private CommonResponse processVote(VotingPanelVoteRequest votingPanelVoteRequest, CommonResponse votingPanelVoteResponse, HttpStatus httpStatus) {
		Optional<VotingPanelEntity> votingPanelEntity = votingPanelRepository.findById(votingPanelVoteRequest.getVotingPanelId());
		
		httpStatus = validatorDataUtils.checkVotingPanel(votingPanelVoteResponse, votingPanelEntity);
		if ( httpStatus.value() != HttpStatus.OK.value() ) {
			return votingPanelVoteResponse;
		}
		
		AssociateCheckCpfIntegrationResponse integrationCpfCheckResponse = associateService.validateAssociateCpf(votingPanelVoteRequest.getCpf());
		if ( integrationCpfCheckResponse.getRequestStatus().value() == HttpStatus.OK.value() ) {
			if ( validatorDataUtils.canAssociateVoteInVotingPanel(integrationCpfCheckResponse) ) {
				
				AssociateEntity associateEntity = associateService.validateAndCreateOrUpdate(votingPanelVoteRequest);
				VoteEntity voteEntity = new VoteEntity(associateEntity.getId(), votingPanelEntity.get().getId(), votingPanelVoteRequest.getVote());
				
				voteService.saveVote(voteEntity);
				
				votingPanelVoteResponse.setMessage(messageService.getMessage("message.vote.registered.sucess"));
				
			} else {
				votingPanelVoteResponse.setMessage(messageService.getMessage("message.associate.not.allowed.to.vote.in.voting.panel"));
				httpStatus = HttpStatus.UNAUTHORIZED;
			}
			
		} else {
			votingPanelVoteResponse.setMessage(integrationCpfCheckResponse.getMessage());
			httpStatus = integrationCpfCheckResponse.getRequestStatus();
		}
		
		return votingPanelVoteResponse;
	}

	private void buildOpenSessionInformation(VotingPanelOpenSessionRequest votingPanelOpenSessionRequest, Optional<VotingPanelEntity> votingPanelEntityOpt) {
		Long sessionExpirationInMinutes = null;
		if ( Objects.isNull(votingPanelOpenSessionRequest.getExpirationTimeInMinutes()) 
				|| votingPanelOpenSessionRequest.getExpirationTimeInMinutes().longValue() < configurationParameters.getDefaultSessionExpirationTimeInMinutes().longValue() ) {
			sessionExpirationInMinutes = configurationParameters.getDefaultSessionExpirationTimeInMinutes();
		} else {
			sessionExpirationInMinutes = votingPanelOpenSessionRequest.getExpirationTimeInMinutes();
		}

		VotingPanelEntity votingPanelEntity = votingPanelEntityOpt.get();
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
