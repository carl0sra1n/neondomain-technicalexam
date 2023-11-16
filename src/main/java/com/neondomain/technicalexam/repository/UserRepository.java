package com.neondomain.technicalexam.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.neondomain.technicalexam.model.User;

@Repository
public interface UserRepository extends JpaRepository<User, Long>{
    User findByUserName(String userName);
    User findUserById(Long userId);
    void deleteById(Long userId);

}
