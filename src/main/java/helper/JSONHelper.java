package helper;

import model.Category;
import model.Item;
import model.Vendor;
import model.WarrantyInfo;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.*;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.*;

/**
 * Created by Anton Klimansky.
 */
public class JSONHelper {
    private static String readAll(Reader rd) throws IOException {
        StringBuilder sb = new StringBuilder();
        int cp;
        while ((cp = rd.read()) != -1) {
            sb.append((char) cp);
        }
        return sb.toString();
    }

    public static JSONObject readJsonObjectFromUrl(String url) throws IOException {
        InputStream is = new URL(url).openStream();
        BufferedReader rd = new BufferedReader(new InputStreamReader(is, Charset.forName("UTF-8")));
        String jsonText = readAll(rd);
        return new JSONObject(jsonText);
    }

    public static JSONArray readJsonArrayFromUrl(String url) throws IOException, JSONException {
        InputStream is = new URL(url).openStream();
        BufferedReader rd = new BufferedReader(new InputStreamReader(is, Charset.forName("UTF-8")));
        String jsonText = readAll(rd);
        return new JSONArray(jsonText);
    }

    public static List<Category> parseCategories(String currentPath, JSONArray categoriesArray) {
        List<Category> result = new ArrayList<Category>();
        for (Object categoryObj : categoriesArray) {
            JSONObject categoryJson = (JSONObject) categoryObj;
            if (categoryJson.has("children")) {
                result.addAll(parseCategories(currentPath + categoryJson.getString("name") + " > ", categoryJson.getJSONArray("children")));
            } else {
                Category category = new Category(categoryJson.getInt("id"), currentPath + categoryJson.getString("name"));
                result.add(category);
            }
        }
        return result;
    }

    public static Item parseJSONToItem(JSONObject itemObject) {
        return new Item(
                itemObject.getLong("id"),
                parseJSONToVendor(itemObject.getJSONObject("vendor")),
                getString(itemObject, "model"),
                getString(itemObject, "typePrefix"),
                getString(itemObject, "linkRewrite"),
                getString(itemObject, "price"),
                getString(itemObject, "priceCurrency"),
                getLong(itemObject, "status"),
                getLong(itemObject, "isNew"),
                getString(itemObject, "image"),
                parseJSONToCategory(getJSONObject(itemObject, "category")),
                parseJSONToWarrantyInfo(getJSONObject(itemObject, "warrantyInfo")),
                parseJSONToShortDescription(getJSONArray(itemObject, "shortDescriptionFeatures"))
        );
    }

    private static WarrantyInfo parseJSONToWarrantyInfo(JSONObject warrantyObject) {
        if (warrantyObject == null) return null;
        return new WarrantyInfo(
                getString(warrantyObject, "country"),
                getLong(warrantyObject, "lifeMonth"),
                getString(warrantyObject, "warranty"),
                getLong(warrantyObject, "warrantyMonth"),
                getString(warrantyObject, "supplier"),
                getString(warrantyObject, "serviceCenters"),
                getString(warrantyObject, "manufacturer"),
                warrantyObject
        );
    }

    private static Vendor parseJSONToVendor(JSONObject vendorObject) {
        if (vendorObject == null) return null;
        return new Vendor(
                getLong(vendorObject, "id"),
                getString(vendorObject, "name")
        );
    }

    private static Category parseJSONToCategory(JSONObject categoryObject) {
        return new Category(categoryObject.getInt("id"), categoryObject.getString("name"));
    }

    private static Map<String, List<String>> parseJSONToShortDescription(JSONArray array) {
        Map<String, List<String>> result = new TreeMap<>();
        for (Object fieldObj : array) {
            JSONObject field = (JSONObject) fieldObj;
            List<String> values = new ArrayList<>();
            for (Object valueObj : field.getJSONArray("values")) {
                values.add((String) valueObj);
            }
            result.put(field.getString("name"), values);
        }
        return result;
    }

    private static String getString(JSONObject o, String field) {
        return o.has(field) ? o.getString(field) : null;
    }

    private static Long getLong(JSONObject o, String field) {
        return o.has(field) ? o.getLong(field) : null;
    }

    private static JSONArray getJSONArray(JSONObject o, String field) {
        return o.has(field) ? o.getJSONArray(field) : null;
    }

    private static JSONObject getJSONObject(JSONObject o, String field) {
        return o.has(field) ? o.getJSONObject(field) : null;
    }

}
