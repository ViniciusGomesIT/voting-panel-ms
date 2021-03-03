package br.com.company.votingpanel.mappers;

import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

import br.com.company.votingpanel.domain.VotingPanelEntity;
import br.com.company.votingpanel.dto.request.VotingPanelCreateRequest;
import br.com.company.votingpanel.dto.response.VotingPanelCreateResponse;
import br.com.company.votingpanel.enumaration.VotingPanelStatusEnum;

@Mapper(componentModel = "spring", unmappedSourcePolicy = ReportingPolicy.IGNORE)
public interface VotingPanelMapper {
	
	VotingPanelEntity toVotingPanelEntity(VotingPanelCreateRequest votingPanelCreatreRequest, VotingPanelStatusEnum status);
	
	VotingPanelCreateResponse toVotingPanelCreatreResponse(VotingPanelEntity votingPanelEntity);
}
