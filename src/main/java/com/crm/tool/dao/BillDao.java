package com.crm.tool.dao;

import com.crm.tool.POJO.Bill;
import com.crm.tool.POJO.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface BillDao extends JpaRepository<Bill, Integer> {

//    List<Bill> getAllBills();

//    List<Bill> getBillByCreatedBy(String Username);

    @Query("SELECT DISTINCT b.category FROM Bill b")
    List<String> getAllCategory();

    // Fetch all bills created by a specific user
    @Query("SELECT b FROM Bill b WHERE b.createdBy = :username")
    List<Bill> getBillByCreatedBy(@Param("username") String username);
}
