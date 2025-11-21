package com.crm.tool.JWT;

import com.crm.tool.dao.UserDao;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Objects;

@Slf4j
@Service
public class CustomerUserDetailsService implements UserDetailsService {

    @Autowired
    UserDao userDao;

    private com.crm.tool.POJO.User userDetail;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        log.info("Inside loadUserByUsername {}", username);
        userDetail = userDao.findByEmail(username);
        if(!Objects.isNull(userDetail)){
            return new User(userDetail.getEmail(), userDetail.getPassword(), new ArrayList<>());
        } else {
throw  new UsernameNotFoundException("User not found");
        }
    }

    public  com.crm.tool.POJO.User getUserDetail(){
        return userDetail;
    }
}
