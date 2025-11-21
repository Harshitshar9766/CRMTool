package com.crm.tool.restImpl;

import com.crm.tool.JWT.JwtFilter;
import com.crm.tool.POJO.Category;
import com.crm.tool.Utils.CafeUtil;
import com.crm.tool.consents.CafeConstants;
import com.crm.tool.dao.CategoryDao;
import com.crm.tool.rest.CategoryRest;
import com.crm.tool.service.CategoryService;
import com.crm.tool.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@RestController
public class CategoryRestImpl implements CategoryRest {

    @Autowired
   private CategoryService categoryService;


    @Override
    public ResponseEntity<String> addNewCategory(Map<String, String> requestMap) {
try{
    return categoryService.addNewCategory(requestMap);
} catch (Exception e) {
    e.printStackTrace();
}
return  CafeUtil.getResponseEntity(CafeConstants.SOMETHING_WENT_WRONG, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @Override
    public ResponseEntity<List<Category>> getAllCategory(String filterValue) {
        try{
return categoryService.getAllCategory(filterValue);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(new ArrayList<>(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Override
    public ResponseEntity<String> updateCategory(Map<String, String> requestMap) {
        try{
            return categoryService.updateCategory(requestMap);
        } catch (Exception e) {
            e.printStackTrace();

            return  CafeUtil.getResponseEntity(CafeConstants.SOMETHING_WENT_WRONG, HttpStatus.INTERNAL_SERVER_ERROR);

        }
    }

}
