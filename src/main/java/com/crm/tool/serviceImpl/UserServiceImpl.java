package com.crm.tool.serviceImpl;

import com.crm.tool.JWT.JwtFilter;
import com.crm.tool.JWT.JwtUtil;
import com.crm.tool.POJO.User;
import com.crm.tool.Utils.CafeUtil;
import com.crm.tool.Utils.EmailUtil;
import com.crm.tool.consents.CafeConstants;
import com.crm.tool.dao.UserDao;
import com.crm.tool.service.UserService;
import com.crm.tool.wrapper.UserWrapper;
import io.jsonwebtoken.lang.Strings;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.MultiValueMap;

import java.util.*;


@Slf4j
@Service
public class UserServiceImpl implements UserService {

    @Autowired
 private UserDao userDao;

    @Autowired
    private JwtFilter jwtFilter;

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private EmailUtil emailUtil;

    @Override
    public ResponseEntity<String> signUp(Map<String, String> requestMap) {
        log.info("Inside signup {}", requestMap);
        try{
            if(validateSignUpMap(requestMap)){
                User user = userDao.findByEmail(requestMap.get("email"));
                if(Objects.isNull(user)){
                    userDao.save(getUserFromMap(requestMap));
                    return CafeUtil.getResponseEntity("Successfully Registered.", HttpStatus.OK);
                } else {
                    return CafeUtil.getResponseEntity("Email already exists.", HttpStatus.BAD_REQUEST);
                }
            }
            else{
                return CafeUtil.getResponseEntity(CafeConstants.INVALID_DATA, HttpStatus.BAD_REQUEST);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return CafeUtil.getResponseEntity(CafeConstants.SOMETHING_WENT_WRONG, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @Override
    public ResponseEntity<List<UserWrapper>> getAllUser() {
        try{
if(jwtFilter.isAdmin()){
return  new ResponseEntity<List<UserWrapper>>((MultiValueMap<String, String>) userDao.getAllUser(), HttpStatus.OK);
} else {
    return new ResponseEntity<>(new ArrayList<>(), HttpStatus.UNAUTHORIZED);
}
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new ResponseEntity<>(new ArrayList<>(), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @Override
    public ResponseEntity<String> login(Map<String, String> requestMap) {
        log.info("inside login {}"+ requestMap);
        try{
if(requestMap.containsKey("email") && requestMap.containsKey("password")){
    User user = userDao.findByEmail(requestMap.get("email"));

    if(Objects.isNull(user)){
        return CafeUtil.getResponseEntity("Invalid email", HttpStatus.BAD_REQUEST);
    }

    if(!user.getPassword().equals(requestMap.get("password"))){
        return CafeUtil.getResponseEntity("Invalid password.", HttpStatus.BAD_REQUEST);
    }

    if ( !"true".equalsIgnoreCase(user.getStatus())){
        return CafeUtil.getResponseEntity("Account not approved yet.", HttpStatus.BAD_REQUEST);
    }

    String token = jwtUtil.generateToken(user.getEmail(), user.getRole());
    return  new ResponseEntity<>("{\"token\":\""+token+"\"}", HttpStatus.OK);
}
return CafeUtil.getResponseEntity(CafeConstants.INVALID_DATA, HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
e.printStackTrace();
            return CafeUtil.getResponseEntity(CafeConstants.SOMETHING_WENT_WRONG, HttpStatus.INTERNAL_SERVER_ERROR);

        }
    }

    @Override
    public ResponseEntity<String> updateUser(Map<String, String> requestMap) {
try{
    if(jwtFilter.isAdmin()){
Optional<User> optional = userDao.findById(Integer.parseInt(requestMap.get("id")));
if(!optional.isEmpty()){
userDao.updateStatus(requestMap.get("status"), Integer.parseInt(requestMap.get("id")));
sendMailToAllAdmin(requestMap.get("status"), optional.get().getEmail(), userDao.getAllUsers());

return CafeUtil.getResponseEntity("User Status Updated Successfully", HttpStatus.OK);

} else {
 return   CafeUtil.getResponseEntity("User id dosen't exist", HttpStatus.OK);
}
    }else {
     return   CafeUtil.getResponseEntity(CafeConstants.UNAUTHORIZED_ACCESS, HttpStatus.UNAUTHORIZED);
    }
} catch (Exception e) {
    e.printStackTrace();

}
        return CafeUtil.getResponseEntity(CafeConstants.SOMETHING_WENT_WRONG, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @Override
    public ResponseEntity<String> checkToken() {
        return CafeUtil.getResponseEntity("true", HttpStatus.OK);
    }

    @Override
    public ResponseEntity<String> changePassword(Map<String, String>requestMap) {
        try{
            String currentUserEmail = jwtFilter.getCurrentUser();
            User userObj = userDao.findByemail(currentUserEmail);

            if (currentUserEmail == null) {
                return CafeUtil.getResponseEntity(CafeConstants.UNAUTHORIZED_ACCESS, HttpStatus.UNAUTHORIZED);
            }

            String oldPasswordDB = userObj.getPassword().trim();
            String oldPasswordReq = requestMap.get("oldPassword") != null ? requestMap.get("oldPassword").trim(): "";

            System.out.println("Current User Email: " + currentUserEmail);
            System.out.println("Old Password from DB: " + oldPasswordDB);
            System.out.println("Old Password from Request: " + oldPasswordReq);

if(oldPasswordDB.equals(oldPasswordReq)){
 userObj.setPassword(requestMap.get("newPassword").trim());
 userDao.save(userObj);
 return CafeUtil.getResponseEntity("Passsword updated successfully", HttpStatus.OK);
} else {
    return CafeUtil.getResponseEntity("Incorrent Old Password", HttpStatus.BAD_REQUEST);

}
        } catch (Exception e) {
            e.printStackTrace();
            return CafeUtil.getResponseEntity(CafeConstants.SOMETHING_WENT_WRONG, HttpStatus.INTERNAL_SERVER_ERROR);

        }
    }

    @Override
    public ResponseEntity<String> forgetPassword(Map<String, String> requestMap) {
        try {

            User user = userDao.findByEmail(requestMap.get("email"));
            if (user != null && user.getEmail() != null && !user.getEmail().trim().isEmpty()) {
                emailUtil.forgotMail(user.getEmail(), "Credentials by Cafe Management System ", user.getPassword());
                return CafeUtil.getResponseEntity("Change your mail chendentials",HttpStatus.OK );
            }

        } catch (Exception e) {
            e.printStackTrace();
            return CafeUtil.getResponseEntity(CafeConstants.SOMETHING_WENT_WRONG, HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return CafeUtil.getResponseEntity(CafeConstants.SOMETHING_WENT_WRONG, HttpStatus.BAD_REQUEST);

    }


    private void sendMailToAllAdmin(String status, String user, List<String> allAdmin) {
 allAdmin.remove(jwtFilter.getCurrentUser());

if (status != null && status.equalsIgnoreCase("true")){
emailUtil.sendSimpleMessage(jwtFilter.getCurrentUser(), "Account approved", "USER:-"+user+"\n is approved by \nAdmin:-"+jwtFilter.getCurrentUser(), allAdmin);
} else {
    emailUtil.sendSimpleMessage(jwtFilter.getCurrentUser(), "Account Disabled", "USER:-"+user+"\n is Disabled by \nAdmin:-"+jwtFilter.getCurrentUser(), allAdmin);

}
    }


    private boolean validateSignUpMap(Map<String, String> requestMap){
      if(
              requestMap.containsKey("name") &&
              requestMap.containsKey("contactNumber") &&
              requestMap.containsKey("email") &&
              requestMap.containsKey("password")
      ) {
          return true;
      }
      return false;
    }

    private User getUserFromMap(Map<String, String> requestMap){
        User user = new User();
        user.setName(requestMap.get("name"));
        user.setContactNumber(requestMap.get("contactNumber"));
        user.setEmail(requestMap.get("email"));
        user.setPassword(requestMap.get("password"));
        user.setStatus("false");
        user.setRole("user");
        return user;
    }




}
