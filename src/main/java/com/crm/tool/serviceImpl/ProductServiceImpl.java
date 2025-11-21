package com.crm.tool.serviceImpl;

import com.crm.tool.JWT.JwtFilter;
import com.crm.tool.POJO.Category;
import com.crm.tool.POJO.Product;
import com.crm.tool.Utils.CafeUtil;
import com.crm.tool.consents.CafeConstants;
import com.crm.tool.dao.ProductDao;
import com.crm.tool.service.ProductService;
import com.crm.tool.wrapper.ProductWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static com.crm.tool.JWT.JwtFilter.isAdmin;

@Service
public class ProductServiceImpl implements ProductService {

    @Autowired
    ProductDao productDao;

    @Autowired
    JwtFilter jwtFilter;

    @Override
    public ResponseEntity<String> addNewProduct(Map<String, String> requestMap) {
try{

    if(jwtFilter.isAdmin()){
if(validateProductMap(requestMap, false)){
productDao.save(getProductFromMap(requestMap, false));
{
return CafeUtil.getResponeEntity("Product Added successfully ", HttpStatus.OK);
    }
}
        return CafeUtil.getResponeEntity(CafeConstants.INVALID_DATA, HttpStatus.BAD_REQUEST);

    } else {
        return CafeUtil.getResponeEntity(CafeConstants.UNAUTHORIZED_ACCESS, HttpStatus.UNAUTHORIZED);

    }
} catch (Exception e) {
    e.printStackTrace();
}
        return CafeUtil.getResponeEntity(CafeConstants.SOMETHING_WENT_WRONG, HttpStatus.INTERNAL_SERVER_ERROR);
    }




    private boolean validateProductMap(Map<String, String> requestMap, boolean validateId) {
        if(requestMap.containsKey("name")){
            if(requestMap.containsKey("id") && validateId){
                return true;
            } else if (!validateId) {
                return true;
            }
        }
        return  false;
    }

    private Product getProductFromMap(Map<String, String> requestMap, boolean isAdd) {

        Category category = new Category();
        category.setId(Integer.parseInt(requestMap.get("categoryId")));

        Product product = new Product();
        if(isAdd){
            product.setId(Integer.parseInt(requestMap.get("id")));
        } else {
            product.setStatus("true");
        }
        product.setCategory(category);
        product.setName(requestMap.get("name"));
        product.setPrice(Integer.parseInt(requestMap.get("price")));
        product.setDescription(requestMap.get("description"));
        return product;
    }


    @Override
    public ResponseEntity<List<ProductWrapper>> getAllProduct() {
        try{
return new ResponseEntity<>(productDao.getAllProduct(), HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new ResponseEntity<>(new ArrayList<>(), HttpStatus.INTERNAL_SERVER_ERROR);
    }

}
