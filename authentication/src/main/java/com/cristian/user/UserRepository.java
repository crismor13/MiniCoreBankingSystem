package com.cristian.user;

import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface UserRepository extends CrudRepository<UserEntity, Long> {
    Optional<UserEntity> findOneByEmail(String email);
}
