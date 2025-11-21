package com.crm.tool.dao;

import com.crm.tool.POJO.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
public interface UserDao extends JpaRepository<User, Integer> {
    User findByEmail(String email);
    List<String> getAllUser();


    List<String> getAllUsers();


    @Transactional
    @Modifying
    Integer updateStatus(@Param("status") String status, @Param("id") Integer id);


    User findByemail(String email);
}
