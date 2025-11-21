package com.crm.tool.restImpl;

import com.crm.tool.POJO.Bill;
import com.crm.tool.Utils.CafeUtil;
import com.crm.tool.consents.CafeConstants;
import com.crm.tool.rest.BillRest;
import com.crm.tool.service.BillService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Slf4j
@RestController
public class BillRestImpl  implements BillRest {


@Autowired
BillService billService;


    @Override
    public ResponseEntity<String> generateReport(Map<String, Object> requestMap) {
        try {
            return billService.generateReport(requestMap);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return new ResponseEntity<>(CafeConstants.SOMETHING_WENT_WRONG, HttpStatus.INTERNAL_SERVER_ERROR);

    }

    @Override
    public ResponseEntity<List<Bill>> getBills() {
        try {
            return billService.getBills();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return new ResponseEntity<>(new ArrayList<>(), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @Override
    public ResponseEntity<byte[]> getPdf(Map<String, Object> requestMap) {
        try {
            return billService.getPdf(requestMap);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return null;
    }

    @Override
    public ResponseEntity<String> delete(Integer id) {
        try {
            return billService.delete(id);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return CafeUtil.getResponseEntity(CafeConstants.SOMETHING_WENT_WRONG, HttpStatus.INTERNAL_SERVER_ERROR);
    }


}
