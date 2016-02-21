import helper.JSONHelper;
import org.json.JSONArray;
import org.json.JSONException;

import java.io.IOException;

public class Main {

    public static void main(String[] args) throws IOException, JSONException {
        JSONArray jsonArray = JSONHelper.readJsonArrayFromUrl("http://export.zoomos.by/api/categories?key=api-help");
        System.out.println("hello world");
    }
}