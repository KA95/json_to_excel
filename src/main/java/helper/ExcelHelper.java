package helper;

import lombok.extern.java.Log;
import model.Category;
import model.Item;
import model.Vendor;
import model.WarrantyInfo;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.*;

import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.reflect.Field;
import java.util.List;
import java.util.Set;
import java.util.logging.Level;

/**
 * Created by Anton Klimansky.
 */
@Log
public class ExcelHelper {

    public static void createExcelFileWithCategories(List<Category> categories) throws IOException {
        Workbook wb = new HSSFWorkbook();
        Sheet sheet = wb.createSheet("new sheet");
        Row headerRow = sheet.createRow(0);
        headerRow.createCell(0).setCellValue("id");
        headerRow.createCell(1).setCellValue("name");
        short currentRow = 1;
        for (Category category : categories) {
            Row row = sheet.createRow(currentRow);
            row.createCell(0).setCellValue(category.id);
            row.createCell(1).setCellValue(category.name);
            currentRow++;
        }

        FileOutputStream fileOut = new FileOutputStream("D:\\categories.xls");
        wb.write(fileOut);
        fileOut.close();
    }


    public static void createExcelFileWithCategoryItems(List<Item> items, Set<String> customFields) throws IOException {
        Workbook wb = new HSSFWorkbook();
        Sheet sheet = wb.createSheet("new sheet");
        Row headerRow = sheet.createRow(0);
        fillItemHeaderRow(headerRow, customFields);
        short currentRow = 1;
        for (Item item : items) {
            Row row = sheet.createRow(currentRow++);
            fillItemProperties(item, row, customFields);
        }

        FileOutputStream fileOut = new FileOutputStream("D:\\items.xls");
        wb.write(fileOut);
        fileOut.close();
    }

    private static void fillItemHeaderRow(Row headerRow, Set<String> customFields) {
        int currentCell = 0;
        for (Field field : Item.class.getDeclaredFields()) {
            if ("shortDescription".equals(field.getName())) {
                for (String customField : customFields) {
                    headerRow.createCell(currentCell++).setCellValue(customField);
                }
            } else {
                headerRow.createCell(currentCell++).setCellValue(field.getName());
            }
        }
    }

    private static void fillItemProperties(Item item, Row row, Set<String> customFields) {
        int currentCell = 0;
        for (Field field : Item.class.getDeclaredFields()) {
            try {
                if ("shortDescription".equals(field.getName())) {
                    for (String customField : customFields) {
                        String customValue = item.shortDescription.containsKey(customField)
                                ? getCustomValues(item.shortDescription.get(customField))
                                : "";
                        row.createCell(currentCell++).setCellValue(customValue);
                    }
                } else if ("vendor".equals(field.getName())) {
                    row.createCell(currentCell++).setCellValue(((Vendor) field.get(item)).name);
                } else if ("warrantyInfo".equals(field.getName())) {
                    row.createCell(currentCell++).setCellValue(((WarrantyInfo) field.get(item)).warrantyJson.toString());
                } else if ("category".equals(field.getName())) {
                    row.createCell(currentCell++).setCellValue(((Category) field.get(item)).name);
                } else {
                    row.createCell(currentCell++).setCellValue(field.get(item).toString());
                }
            } catch (IllegalAccessException e) {
                log.log(Level.WARNING, "Illegal access at field: " + field.getName());
            }
        }
    }

    private static String getCustomValues(List<String> values) {
        String result = "";
        for (String value : values) {
            result += value + ", ";
        }
        return result.substring(0, result.length() - 2);
    }

}
