import helper.ExcelHelper;
import helper.JSONHelper;
import model.Category;
import model.Item;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.FileOutputStream;
import java.io.IOException;
import java.util.*;

public class Main {

    public static void main(String[] args) throws IOException, JSONException {
        testCategories();
    }


    private static void testCategories() throws IOException, JSONException {
        JSONArray categoriesArray = getCategories();
        List<Category> categories = JSONHelper.parseCategories("", categoriesArray);


        Workbook wb = new HSSFWorkbook();
        Sheet sheet = wb.createSheet("Sheet 1");
        try {
            int currentRow = 0;
            for (Category category : categories) {
                currentRow++;
                Row row = sheet.createRow(currentRow++);
                row.createCell(0).setCellValue(category.name);
                currentRow++;

                //extract!//////////////////////
                JSONArray itemsArray = getCategoryItems(category.id);
                List<Item> items = new ArrayList<>();
                Set<String> customFields = new TreeSet<>();
                for (Object itemObj : itemsArray) {
                    JSONObject itemJson = (JSONObject) itemObj;
                    Item item = JSONHelper.parseJSONToItem(itemJson);
                    items.add(item);
                    customFields.addAll(item.shortDescription.keySet());
                }
                ///////////////////////////////

                currentRow = ExcelHelper.addExcelRowsWithCategoryItems(sheet, items, customFields, currentRow);
            }

        } finally {
            FileOutputStream fileOut = new FileOutputStream("D:\\categories.xls");
            wb.write(fileOut);
            fileOut.close();
            System.out.println("hello world");
        }
    }

    private static JSONArray getCategories() throws IOException {
        return JSONHelper.readJsonArrayFromUrl("http://export.zoomos.by/api/categories?key=api-help");
    }


    private static JSONArray getCategoryItems(long categoryId) throws IOException {
        return JSONHelper.readJsonArrayFromUrl("http://export.zoomos.by/api/category/" +
                categoryId +
                "/offers?key=api-help");
    }

}