import helper.ExcelHelper;
import helper.JSONHelper;
import model.Category;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Main {

    public static void main(String[] args) throws IOException, JSONException {
        JSONArray categoriesArray = getCategories();
        List<Category> categories = JSONHelper.parseCategories("", categoriesArray);
//        for(Category category : categories) {
//            System.out.println("category.getId() = " + category.getId());
//            System.out.println("category.getPath() = " + category.getPath());
//        }
        ExcelHelper.createExcelFileWithCategories(categories);
        System.out.println("hello world");
    }

    private static JSONArray getCategories() throws IOException {
        return JSONHelper.readJsonArrayFromUrl("http://export.zoomos.by/api/categories?key=api-help");
    }

}