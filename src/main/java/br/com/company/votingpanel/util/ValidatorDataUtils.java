package br.com.company.votingpanel.util;

import java.time.LocalDateTime;
import java.util.Objects;

import org.springframework.http.HttpStatus;

import br.com.company.votingpanel.constants.VotingPanelConstants;
import br.com.company.votingpanel.domain.VotingPanelEntity;
import br.com.company.votingpanel.dto.commons.CommonResponse;
import br.com.company.votingpanel.dto.integration.response.associatecheckcpf.AssociateCheckCpfIntegrationResponse;
import br.com.company.votingpanel.enumaration.AssociateCpfCheckerStatusEnum;

public class ValidatorDataUtils {
	
	public static boolean isValidCpf(String cpf) {
		return cpf.matches(VotingPanelConstants.NUMERIC_REGEX_VALIDATOR) && cpf.length() == VotingPanelConstants.DEFAULT_CPF_LENGTH;
	}
	
	public static HttpStatus checkVotingPanel(CommonResponse commonResponse, VotingPanelEntity votingPanelEntity) {
		if ( Objects.isNull(votingPanelEntity) ) {
			commonResponse.addError("Pauta não encontrada!");
			return HttpStatus.NOT_FOUND;
		}
		
		if ( Objects.nonNull(votingPanelEntity.getExpiredDate())
				&& votingPanelEntity.getExpiredDate().isBefore(LocalDateTime.now()) ) {
			commonResponse.addError("Pauta expirada!");
			return HttpStatus.BAD_REQUEST;
		}
		
		return HttpStatus.OK;
	}
	
	public static boolean canAssociateVoteInVotingPanel(AssociateCheckCpfIntegrationResponse integrationCpfCheckResponse) {
		return Objects.nonNull(integrationCpfCheckResponse.getStatus()) 
				&& integrationCpfCheckResponse.getStatus().equals(AssociateCpfCheckerStatusEnum.ABLE_TO_VOTE);
	}
}
