package br.com.company.votingpanel.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import br.com.company.votingpanel.domain.AssociateEntity;

public interface AssociateRepository extends JpaRepository<AssociateEntity, Long> {

	AssociateEntity findByCpf(String cpf);
}
