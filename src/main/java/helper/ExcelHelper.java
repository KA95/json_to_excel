package helper;

import lombok.extern.java.Log;
import model.Category;
import model.Item;
import model.Vendor;
import model.WarrantyInfo;
import org.apache.poi.ss.usermodel.*;

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

    public static int addExcelRowsWithCategoryItems(Sheet sheet, List<Item> items, Set<String> customFields, int currentRow) throws IOException {
        Row headerRow = sheet.createRow(currentRow++);
        fillItemHeaderRow(headerRow, customFields);
        for (Item item : items) {
            Row row = sheet.createRow(currentRow++);
            fillItemProperties(item, row, customFields);
            if(currentRow % 100 == 0) {
                log.log(Level.INFO, String.valueOf(currentRow));
            }
        }
        return currentRow;
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
                    Object fieldValue = field.get(item);
                    if(fieldValue == null) {
                        row.createCell(currentCell++).setCellValue("");
                    } else {
                        row.createCell(currentCell++).setCellValue(((WarrantyInfo) fieldValue).warrantyJson.toString());
                    }
                } else if ("category".equals(field.getName())) {
                    row.createCell(currentCell++).setCellValue(((Category) field.get(item)).name);
                } else {
                    Object fieldValue = field.get(item);
                    row.createCell(currentCell++).setCellValue(fieldValue == null ? "" : fieldValue.toString());
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
