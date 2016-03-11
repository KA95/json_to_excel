import helper.ExcelHelper;
import helper.JSONHelper;
import model.Category;
import model.Item;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.*;

public class Main {

    public static void main(String[] args) throws IOException, JSONException {
        testCategoryItems();
    }


    private static void testCategories() throws IOException, JSONException {
        JSONArray categoriesArray = getCategories();
        List<Category> categories = JSONHelper.parseCategories("", categoriesArray);
        ExcelHelper.createExcelFileWithCategories(categories);
        System.out.println("hello world");
    }

    private static void testCategoryItems() throws IOException, JSONException {
        JSONArray itemsArray = getCategoryItems(1544);
        List<Item> items = new ArrayList<>();
        Set<String> customFields = new TreeSet<>();
        for (Object itemObj: itemsArray) {
            JSONObject itemJson = (JSONObject) itemObj;
            Item item  = JSONHelper.parseJSONToItem(itemJson);
            items.add(item);
            customFields.addAll(item.shortDescription.keySet());
        }

        ExcelHelper.createExcelFileWithCategoryItems(items, customFields);
        System.out.println("hello world");
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