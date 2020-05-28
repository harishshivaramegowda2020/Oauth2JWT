package com.harish.repo;

import java.util.Optional;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.harish.entity.Account;

@Repository
public interface AccountRepository extends CrudRepository<Account, Long>{
	Optional<Account> findByUsername(final String username);
}