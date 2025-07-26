package br.com.jeftavares.jbank.repository;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import br.com.jeftavares.jbank.entities.Deposit;

public interface DepositRepository extends JpaRepository<Deposit, UUID> {

}
