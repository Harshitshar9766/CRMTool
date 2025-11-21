package com.crm.tool.restImpl;

import com.crm.tool.Utils.CafeUtil;
import com.crm.tool.consents.CafeConstants;
import com.crm.tool.rest.UserRest;
import com.crm.tool.service.UserService;
import com.crm.tool.wrapper.UserWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@RestController
public class UserRestImpl implements UserRest {

    @Autowired
    UserService userService;

    @Override
    public ResponseEntity<String> signup(Map<String, String> requestMap){
       try{
           return userService.signUp(requestMap);
       } catch (Exception e) {
          e.printStackTrace();
           return CafeUtil.getResponseEntity(CafeConstants.SOMETHING_WENT_WRONG,HttpStatus.INTERNAL_SERVER_ERROR);

       }


    }

    @Override
    public ResponseEntity<String> login(Map<String, String> requestMap) {
try{
return userService.login(requestMap);
} catch (Exception e) {
    e.printStackTrace();
return CafeUtil.getResponseEntity(CafeConstants.SOMETHING_WENT_WRONG, HttpStatus.INTERNAL_SERVER_ERROR);
}
    }

    @Override
    public ResponseEntity<List<UserWrapper>> getAllUser() {
        try{
         return userService.getAllUser();
        } catch (Exception e) {
          e.printStackTrace();
            return new ResponseEntity<List<UserWrapper>>(new ArrayList<>(), HttpStatus.INTERNAL_SERVER_ERROR);

        }
    }

    @Override
    public ResponseEntity<String> updateUser(Map<String, String> requestMap) {
        try{
        return userService.updateUser(requestMap);
        } catch (Exception e) {
            e.printStackTrace();
    }
        return CafeUtil.getResponseEntity(CafeConstants.SOMETHING_WENT_WRONG, HttpStatus.INTERNAL_SERVER_ERROR);      
    }

    @Override
    public ResponseEntity<String> checkToken() {
       try{
return userService.checkToken();
       } catch (Exception e) {
           e.printStackTrace();
           return CafeUtil.getResponseEntity(CafeConstants.SOMETHING_WENT_WRONG, HttpStatus.INTERNAL_SERVER_ERROR);
       }
    }

    @Override
    public ResponseEntity<String> changePassword(Map<String, String> requestMap) {
        try{
            return userService.changePassword(requestMap);

        } catch (Exception e) {
            e.printStackTrace();
            return CafeUtil.getResponseEntity(CafeConstants.SOMETHING_WENT_WRONG, HttpStatus.INTERNAL_SERVER_ERROR);

        }
    }
    @Override
    public ResponseEntity<String> forgetPassword(Map<String, String> requestMap) {
        try{
            return userService.forgetPassword(requestMap);
        } catch (Exception e) {
            e.printStackTrace();
            return CafeUtil.getResponseEntity(CafeConstants.SOMETHING_WENT_WRONG, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


}
