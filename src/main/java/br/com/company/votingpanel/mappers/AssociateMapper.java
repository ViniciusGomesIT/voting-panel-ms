package br.com.company.votingpanel.mappers;

import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

import br.com.company.votingpanel.domain.AssociateEntity;
import br.com.company.votingpanel.dto.request.VotingPanelVoteRequest;

@Mapper(componentModel = "spring", unmappedSourcePolicy = ReportingPolicy.IGNORE)
public interface AssociateMapper {

	AssociateEntity toAssociateEntity(VotingPanelVoteRequest votingPanelVoteRequest);
}
