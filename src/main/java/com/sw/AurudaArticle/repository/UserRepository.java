package com.sw.AurudaArticle.repository;

import com.sw.AurudaArticle.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {

}
