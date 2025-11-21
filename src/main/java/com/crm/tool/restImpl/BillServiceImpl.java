package com.crm.tool.restImpl;

import com.crm.tool.JWT.JwtFilter;
import com.crm.tool.JWT.JwtUtil;
import com.crm.tool.POJO.Bill;
import com.crm.tool.Utils.CafeUtil;
import com.crm.tool.Utils.EmailUtil;
import com.crm.tool.consents.CafeConstants;
import com.crm.tool.dao.BillDao;
import com.crm.tool.service.BillService;
import com.itextpdf.text.*;
import com.itextpdf.text.pdf.*;
import lombok.extern.slf4j.Slf4j;
import org.apache.pdfbox.io.IOUtils;
import org.json.JSONArray;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.stereotype.Service;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Stream;

@Slf4j
@Service
public class BillServiceImpl  implements BillService {


@Autowired
    BillDao billDao;

@Autowired
    AuthenticationManager authenticationManager;

@Autowired
    JwtUtil jwtUtil;
@Autowired
    JwtFilter jwtFilter;

@Autowired
    EmailUtil emailUtil;

    @Override
    public ResponseEntity<String> generateReport(Map<String, Object> requestMap) {
        log.info("Insert generateReport");
        try {
            String filename;
            if (validateResquestMap(requestMap)) {
                if (requestMap.containsKey("isGenerate") && !(Boolean) requestMap.get("isGenerate")) {
                    filename = (String) requestMap.get("uuid");
                } else {
                    filename = CafeUtil.getUUID();
                    requestMap.put("uuid", filename);
                    insertBill(requestMap);
                }
                // print user data (name , email m contactNumber , ...)
                String data = "Name: " + requestMap.get("name") + "\n" + "Contact Number: " + requestMap.get("contactNumber") +
                        "\n" + "Email: " + requestMap.get("email") + "\n" + "Payment Method: " + requestMap.get("paymentMethod");
                Document document = new Document();

                String filepath = CafeConstants.STORE_LOCATION + "\\" + filename + ".pdf";
                PdfWriter writer = PdfWriter.getInstance(document, new FileOutputStream(filepath));
                document.open();

//                PdfWriter.getInstance(document, new FileOutputStream(CafeConstants.STORE_LOCATION + "\\" + filename + ".pdf"));
//                document.open();

                setRectaangleInPdf(writer);

                // print pdf Header
                Paragraph chunk = new Paragraph("Cafe Management System", getFont("Header"));
                chunk.setAlignment(Element.ALIGN_CENTER);
                document.add(chunk);


                Paragraph paragraph = new Paragraph(data + "\n \n", getFont("Data"));
                document.add(paragraph);

                // Create table in pdf to print data
                PdfPTable table = new PdfPTable(5);
                table.setWidthPercentage(100);


                JSONArray jsonArray = null;
                addTableHeader(table);
                // Print table data
               jsonArray = CafeUtil.getJsonArrayFromString((String) requestMap.get("productDetails"));

                for (int i = 0; i < jsonArray.length(); i++) {
                    Map<String, Object> productData = CafeUtil.getMapFromJson(jsonArray.getString(i));
                    addRows(table, productData);
                }

                document.add(table);

                // print pdf Footer
                Paragraph footer = new Paragraph("Total : " + requestMap.get("totalAmount") + "\n"
                        + "Thank you for visiting our website.", getFont("Data"));
                document.add(footer);
                document.close();
                return new ResponseEntity<>("{\"uuid\":\"" + filename + "\"}", HttpStatus.OK);
            }
            return CafeUtil.getResponseEntity("Required data not found", HttpStatus.BAD_REQUEST);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return CafeUtil.getResponseEntity(CafeConstants.SOMETHING_WENT_WRONG, HttpStatus.INTERNAL_SERVER_ERROR);

    }


    private Font getFont(String type) {
        log.info("Inside getFont");
        switch (type) {
            case "Header":
                Font headerFont = FontFactory.getFont(FontFactory.HELVETICA_BOLDOBLIQUE, 18, BaseColor.BLACK);
                headerFont.setStyle(Font.BOLD);
                return headerFont;
            case "Data":
                Font dareFont = FontFactory.getFont(FontFactory.TIMES_ROMAN, 11, BaseColor.BLACK);
                dareFont.setStyle(Font.BOLD);
                return dareFont;
            default:
                return new Font();
        }
    }

    private void addRows(PdfPTable table, Map<String, Object> data) {
        log.info("Inside addRows");
        table.addCell((String) data.get("name"));
        table.addCell((String) data.get("category"));
        table.addCell((String) data.get("quantity"));
        table.addCell(Double.toString((Double) data.get("price")));
        table.addCell(Double.toString((Double) data.get("total")));
    }


    private void setRectaangleInPdf(PdfWriter writer) {
        log.info("Inside setRectaangleInPdf.");
//        Rectangle rectangle = new Rectangle(577, 825, 18, 15);
//        rectangle.enableBorderSide(1);
//        rectangle.enableBorderSide(2);
//        rectangle.enableBorderSide(4);
//        rectangle.enableBorderSide(8);
//        rectangle.setBorderColor(BaseColor.BLACK);
//        rectangle.setBorderWidth(1);
//        document.add(rectangle);

        PdfContentByte canvas = writer.getDirectContent();
        Rectangle rectangle = new Rectangle(18, 15, 577, 825);

        rectangle.setBorder(Rectangle.BOX); // draw all sides
        rectangle.setBorderWidth(1);
        rectangle.setBorderColor(BaseColor.BLACK);
        canvas.rectangle(rectangle);


        // Get the canvas to draw directly on the PDF

    }




    private void addTableHeader(PdfPTable table) {
        log.info("Inside addTableHeader");
        Stream.of("Name", "Category", "Quantity", "Price", "Sub Total")
                .forEach(columnTitle -> {
                    PdfPCell header = new PdfPCell();
                    header.setBackgroundColor(BaseColor.LIGHT_GRAY);
                    header.setBorderWidth(2);
                    header.setPhrase(new Phrase(columnTitle));
                    header.setBackgroundColor(BaseColor.YELLOW);
                    header.setHorizontalAlignment(Element.ALIGN_CENTER);
                    header.setVerticalAlignment(Element.ALIGN_CENTER);
                    table.addCell(header);
                });
    }


    private void insertBill(Map<String, Object> requestMap) {
        try {
              Bill bill = new Bill();

//            bill.setUuid((String) requestMap.get("uuid"));
//            bill.setName((String) requestMap.get("name"));
//            bill.setEmail((String) requestMap.get("email"));
//            bill.setContactNumber((String) requestMap.get("contactNumber"));
//            bill.setPaymentMethod((String) requestMap.get("paymentMethod"));
//            bill.setTotal(Integer.parseInt((String) requestMap.get("totalAmount")));
//            bill.setProductDetails((String) requestMap.get("productDetails"));

            bill.setUuid(String.valueOf(requestMap.get("uuid")));
            bill.setName(String.valueOf(requestMap.get("name")));
            bill.setEmail(String.valueOf(requestMap.get("email")));
            bill.setContactNumber(String.valueOf(requestMap.get("contactNumber")));
            bill.setPaymentMethod(String.valueOf(requestMap.get("paymentMethod")));


            Object totalObj = requestMap.get("totalAmount");
            int total = 0;
            if (totalObj instanceof Number) {
                total = ((Number) totalObj).intValue();
            } else if (totalObj instanceof String) {
                total = Integer.parseInt((String) totalObj);
            }
            bill.setTotal(total);

            bill.setProductDetails(String.valueOf(requestMap.get("productDetails")));
            bill.setCreatedBy(jwtFilter.getCurrentUsername());
            billDao.save(bill);

//            bill.setCreatedBy(jwtFilter.getCurrentUsername());
//            billDao.save(bill);




        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private boolean validateResquestMap(Map<String, Object> requestMap) {
        return requestMap.containsKey("name") &&
                requestMap.containsKey("contactNumber") &&
                requestMap.containsKey("email") &&
                requestMap.containsKey("paymentMethod") &&
                requestMap.containsKey("productDetails") &&
                requestMap.containsKey("totalAmount");
    }


    @Override
    public ResponseEntity<List<Bill>> getBills() {
        List<Bill> list = new ArrayList<>();
        if (jwtFilter.isAdmin()) {
            list = billDao.findAll();
        } else {
            list = billDao.getBillByCreatedBy(jwtFilter.getCurrentUsername());
        }
        return new ResponseEntity<>(list, HttpStatus.OK);
    }

    @Override
    public ResponseEntity<byte[]> getPdf(Map<String, Object> requestMap) {
        log.info("Inside getPdf : requestMap {}", requestMap);
        try {
            byte[] byteArray = new byte[0];
            if (!requestMap.containsKey("uuid") && validateResquestMap(requestMap)) {
                return new ResponseEntity<>(byteArray, HttpStatus.BAD_REQUEST);
            }
            String filepath = CafeConstants.STORE_LOCATION + "\\" + (String) requestMap.get("uuid") + ".pdf";

            if (CafeUtil.isFileExist(filepath)) {
                byteArray = getByteArray(filepath);
                return new ResponseEntity<>(byteArray, HttpStatus.OK);
            } else {
                requestMap.put("isGenerate", false);
                generateReport(requestMap);
                byteArray = getByteArray(filepath);
                return new ResponseEntity<>(byteArray, HttpStatus.OK);
            }

        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return null;
    }

    private byte[] getByteArray(String filepath) throws Exception {
        File initalFile = new File(filepath);
        InputStream targetStream = new FileInputStream(initalFile);
        byte[] byteArray = IOUtils.toByteArray(targetStream);
        targetStream.close();
        return byteArray;
    }

    @Override
            public ResponseEntity<String> delete(Integer id){
                try {
                    if (jwtFilter.isAdmin()) {
                        Optional optional = billDao.findById(id);
                        if (!optional.isEmpty()) {
                            billDao.deleteById(id);
                            //System.out.println("Product is deleted successfully");
                            return CafeUtil.getResponseEntity("Bill is deleted successfully", HttpStatus.OK);
                        }
                        //System.out.println("Product id doesn't exist");
                        return CafeUtil.getResponseEntity("Bill id doesn't exist", HttpStatus.OK);
                    } else {
                        return CafeUtil.getResponseEntity(CafeConstants.UNAUTHORIZED_ACCESS, HttpStatus.UNAUTHORIZED);
                    }
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
                return CafeUtil.getResponseEntity(CafeConstants.SOMETHING_WENT_WRONG, HttpStatus.INTERNAL_SERVER_ERROR);
            }

    @Override
    public List<String> getAllCategory() {
        return billDao.getAllCategory();
    }

}