package br.com.alura.forum.repository;

import java.util.Optional;

import org.springframework.data.repository.Repository;

import br.com.alura.forum.model.User;

public interface UserRepository extends Repository<User, Long> {
	public Optional<User> findByEmail(String email);

	public Optional<User> findById(Long userId);
}
