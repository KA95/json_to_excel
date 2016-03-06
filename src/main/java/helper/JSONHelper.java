package helper;

import model.Category;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.*;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

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
        for(Object categoryObj : categoriesArray) {
            JSONObject categoryJson = (JSONObject) categoryObj;
            if(categoryJson.has("children")) {
                result.addAll(parseCategories(currentPath + categoryJson.getString("name") + " > ", categoryJson.getJSONArray("children")));
            } else {
                Category category = new Category(categoryJson.getInt("id"), currentPath + categoryJson.getString("name"));
                result.add(category);
            }
        }
        return result;
    }
}
