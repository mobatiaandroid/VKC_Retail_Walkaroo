/**
 *
 */
package com.mobatia.vkcretailer.model;

/**
 * @author mobatia-user
 *
 */
public class ColorModel extends ParentFilterModel {

    String colorcode;
    String url;
    String product_id;
    String id;
    String name;

    public String getProduct_id() {
        return product_id;
    }

    public void setProduct_id(String product_id) {
        this.product_id = product_id;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getColorcode() {
        return colorcode;
    }

    public void setColorcode(String colorcode) {
        this.colorcode = colorcode;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
