package com.lcwd.user.user.service.repositories;

import com.lcwd.user.user.service.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User,String> {
}
