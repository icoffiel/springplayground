package com.nerdery.icoffiel.views.xlsx;

import com.nerdery.icoffiel.web.rest.user.model.User;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.springframework.web.servlet.view.document.AbstractXlsxView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * View for Users that will export a .xlsl file
 */
public class UserXlsxView extends AbstractXlsxView {

    @Override
    protected void buildExcelDocument(Map<String, Object> model, org.apache.poi.ss.usermodel.Workbook workbook, HttpServletRequest request, HttpServletResponse response) throws Exception {

        // Set the filename
        Date now = new Date();
        String pattern = "MMddyyyy";
        SimpleDateFormat format = new SimpleDateFormat(pattern);
        response.setHeader("Content-disposition","attachment; filename=Users_" + format.format(now) + ".xlsx");

        // Get the data model
        @SuppressWarnings("unchecked")
        List<User> users = (List<User>) model.get("users");

        // Create the worksheet
        Sheet sheet = workbook.createSheet("Users");

        // Create Header row
        Row header = sheet.createRow(0);
        header.createCell(0).setCellValue("ID");
        header.createCell(1).setCellValue("Username");
        header.createCell(2).setCellValue("Enabled");

        // Create data rows
        int rowCount = 1;
        for(User user : users) {
            Row dataRow = sheet.createRow(rowCount++);
            dataRow.createCell(0).setCellValue(user.getId());
            dataRow.createCell(1).setCellValue(user.getUsername());
            dataRow.createCell(2).setCellValue(user.isEnabled());
        }
    }
}
