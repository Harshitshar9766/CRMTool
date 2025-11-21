package com.crm.tool.restImpl;

import com.crm.tool.Utils.CafeUtil;
import com.crm.tool.consents.CafeConstants;
import com.crm.tool.rest.ProductRest;
import com.crm.tool.service.ProductService;
import com.crm.tool.wrapper.ProductWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@RestController
public class ProductRestImpl implements ProductRest {

    @Autowired
    ProductService productService;

    @Override
    public ResponseEntity<String> addNewProduct(Map<String, String> requestMap) {

        try{
return productService.addNewProduct(requestMap);
        } catch (Exception e) {
            e.printStackTrace();


        }

        return CafeUtil.getResponeEntity(CafeConstants.SOMETHING_WENT_WRONG, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @Override
    public ResponseEntity<List<ProductWrapper>> getAllProduct() {
        try{

            return productService.getAllProduct();
        } catch (Exception e) {
            e.printStackTrace();

        }
        return new ResponseEntity<>(new ArrayList<>(), HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
