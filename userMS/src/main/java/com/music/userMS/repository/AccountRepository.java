package com.music.userMS.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.music.userMS.model.Account;
import com.music.userMS.model.User;

@Repository(value = "accountRepository")
public interface AccountRepository extends JpaRepository<Account, Integer> {
	
	@Query("SELECT a"
			+ " FROM Account a"
			+ " WHERE :user IN elements(a.users)")
	public List<Account> findAllByUser(User user);

	@Query("SELECT"
			+ " CASE"
				+ " WHEN :user MEMBER OF a.users THEN TRUE"
				+ " ELSE FALSE"
			+ " END"
			+ " FROM Account a"
			+ " WHERE a = :account")
	public Boolean accountContainsUser(Account account, User user);
}
