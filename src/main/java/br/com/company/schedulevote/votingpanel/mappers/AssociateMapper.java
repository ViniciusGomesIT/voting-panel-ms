package br.com.company.schedulevote.votingpanel.mappers;

import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

import br.com.company.schedulevote.votingpanel.domain.AssociateEntity;
import br.com.company.schedulevote.votingpanel.dto.request.VotingPanelVoteRequest;

@Mapper(componentModel = "spring", unmappedSourcePolicy = ReportingPolicy.IGNORE)
public interface AssociateMapper {

	AssociateEntity toAssociateEntity(VotingPanelVoteRequest votingPanelVoteRequest);
}
