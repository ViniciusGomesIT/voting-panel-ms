package br.com.company.schedulevote.votingpanel.mappers;

import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

import br.com.company.schedulevote.votingpanel.domain.VotingPanelEntity;
import br.com.company.schedulevote.votingpanel.dto.request.VotingPanelCreateRequest;
import br.com.company.schedulevote.votingpanel.dto.response.VotingPanelCreateResponse;
import br.com.company.schedulevote.votingpanel.enumaration.VotingPanelStatusEnum;

@Mapper(componentModel = "spring", unmappedSourcePolicy = ReportingPolicy.IGNORE)
public interface VotingPanelMapper {
	
	VotingPanelEntity toVotingPanelEntity(VotingPanelCreateRequest votingPanelCreatreRequest, VotingPanelStatusEnum status);
	
	VotingPanelCreateResponse toVotingPanelCreatreResponse(VotingPanelEntity votingPanelEntity);
}
