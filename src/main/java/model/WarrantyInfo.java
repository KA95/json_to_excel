package model;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.json.JSONObject;

/**
 * Created by Anton Klimansky.
 */
@AllArgsConstructor
public class WarrantyInfo {
    public String country;
    public Long lifeMonth;
    public String warranty;
    public Long warrantyMonth;
    public String supplier;
    public String serviceCenters;
    public String manufacturer;
    public JSONObject warrantyJson;
}

