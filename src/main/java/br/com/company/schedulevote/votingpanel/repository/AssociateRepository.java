package br.com.company.schedulevote.votingpanel.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import br.com.company.schedulevote.votingpanel.domain.AssociateEntity;

public interface AssociateRepository extends JpaRepository<AssociateEntity, Long> {

	Optional<AssociateEntity> findByCpf(String cpf);
}
