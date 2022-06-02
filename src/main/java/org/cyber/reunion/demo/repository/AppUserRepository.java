package org.cyber.reunion.demo.repository;

import javax.transaction.Transactional;

import org.cyber.reunion.demo.models.AppUser;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AppUserRepository extends JpaRepository<AppUser, Integer> {

  boolean existsByUsername(String username);

  AppUser findByUsername(String username);

  @Transactional
  void deleteByUsername(String username);

}
