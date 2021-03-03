package br.com.company.votingpanel.util;

import java.time.LocalDateTime;
import java.util.Objects;
import java.util.Optional;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

import br.com.company.votingpanel.config.ConfigurationParameters;
import br.com.company.votingpanel.domain.VotingPanelEntity;
import br.com.company.votingpanel.dto.commons.CommonResponse;
import br.com.company.votingpanel.dto.integration.response.associatecheckcpf.AssociateCheckCpfIntegrationResponse;
import br.com.company.votingpanel.enumaration.AssociateCpfCheckerStatusEnum;
import br.com.company.votingpanel.enumaration.VotingPanelStatusEnum;

@Component
public class ValidatorDataUtils {
	
	private final MessageService messageService;
	private final ConfigurationParameters configurationParameters;
	
	public ValidatorDataUtils(
			MessageService messageService,
			ConfigurationParameters configurationParameters) {
		
		this.messageService = messageService;
		this.configurationParameters = configurationParameters;
	}

	public boolean isValidCpf(String cpf) {
		return cpf.matches(configurationParameters.getNumericRegexValidator()) && cpf.length() == configurationParameters.getDefaultCpfLengthValidator();
	}
	
	public HttpStatus checkVotingPanel(CommonResponse commonResponse, Optional<VotingPanelEntity> votingPanelEntityOpt) {
		
		if ( !votingPanelEntityOpt.isPresent() ) {
			commonResponse.addError(messageService.getMessage("error.voting.panel.not.found"));
			return HttpStatus.NOT_FOUND;
		}
		
		if ( votingPanelEntityOpt.get().getStatus().equals(VotingPanelStatusEnum.FINISHED) ) {
			commonResponse.addError(messageService.getMessage("error.voting.panel.finished"));
			return HttpStatus.BAD_REQUEST;
		}
		
		if ( Objects.nonNull(votingPanelEntityOpt.get().getExpiredDate())
				&& votingPanelEntityOpt.get().getExpiredDate().isBefore(LocalDateTime.now()) ) {
			commonResponse.addError(messageService.getMessage("error.voting.panel.expired"));
			return HttpStatus.BAD_REQUEST;
		}
		
		return HttpStatus.OK;
	}
	
	public boolean canAssociateVoteInVotingPanel(AssociateCheckCpfIntegrationResponse integrationCpfCheckResponse) {
		return Objects.nonNull(integrationCpfCheckResponse.getStatus()) 
				&& integrationCpfCheckResponse.getStatus().equals(AssociateCpfCheckerStatusEnum.ABLE_TO_VOTE);
	}
}
