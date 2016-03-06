package helper;

import model.Category;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.*;

import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

/**
 * Created by Anton Klimansky.
 */
public class ExcelHelper {

    public static void createExcelFileWithCategories(List<Category> categories) throws IOException {
        Workbook wb = new HSSFWorkbook();
        Sheet sheet = wb.createSheet("new sheet");
        Row headerRow = sheet.createRow(0);
        headerRow.createCell(0).setCellValue("id");
        headerRow.createCell(1).setCellValue("name");
        short currentRow = 1;
        for(Category category : categories) {
            Row row = sheet.createRow(currentRow);
            row.createCell(0).setCellValue(category.getId());
            row.createCell(1).setCellValue(category.getPath());
            currentRow++;
        }

        FileOutputStream fileOut = new FileOutputStream("D:\\workbook.xls");
        wb.write(fileOut);
        fileOut.close();
    }

}
