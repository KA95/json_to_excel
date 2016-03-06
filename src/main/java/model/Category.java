package model;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * Created by Anton Klimansky.
 */
@Data
@AllArgsConstructor
public class Category {
    private long id;
    private String path;
}
