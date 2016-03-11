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
                itemObject.getString("model"),
                itemObject.getString("typePrefix"),
                itemObject.getString("linkRewrite"),
                itemObject.getString("price"),
                itemObject.getString("priceCurrency"),
                itemObject.getLong("status"),
                itemObject.getLong("isNew"),
                itemObject.getString("image"),
                parseJSONToCategory(itemObject.getJSONObject("category")),
                parseJSONToWarrantyInfo(itemObject.getJSONObject("warrantyInfo")),
                parseJSONToShortDescription(itemObject.getJSONArray("shortDescriptionFeatures"))
        );
    }

    private static WarrantyInfo parseJSONToWarrantyInfo(JSONObject warrantyObject) {
        return new WarrantyInfo(
                warrantyObject.getString("country"),
                warrantyObject.getLong("lifeMonth"),
                warrantyObject.getString("warranty"),
                warrantyObject.has("warrantyMonth") ?
                        warrantyObject.getLong("warrantyMonth") : null,
                warrantyObject.getString("supplier"),
                warrantyObject.getString("serviceCenters"),
                warrantyObject.getString("manufacturer"),
                warrantyObject
        );
    }

    private static Vendor parseJSONToVendor(JSONObject vendorObject) {
        return new Vendor(
                vendorObject.getLong("id"),
                vendorObject.getString("name")
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

}
