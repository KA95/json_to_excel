package model;

import lombok.AllArgsConstructor;

import java.util.List;
import java.util.Map;

/**
 * Created by Anton Klimansky.
 */
@AllArgsConstructor
public class Item {
    public Long id;
    public Vendor vendor;
    public String model;
    public String typePrefix;
    public String linkRewrite;
    public String price;
    public String priceCurrency;
    public Long status;
    public Long isNew;
    public String image;
    public Category category;
    public WarrantyInfo warrantyInfo;
    public Map<String, List<String>> shortDescription;
}
