package com.example.demo;


import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.icoderman.woocommerce.ApiVersionType;
import com.icoderman.woocommerce.EndpointBaseType;
import com.icoderman.woocommerce.WooCommerce;
import com.icoderman.woocommerce.WooCommerceAPI;
import com.icoderman.woocommerce.oauth.OAuthConfig;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class WooCommerceClientTest {

    private static final String CONSUMER_KEY = "ck_1e85d27ddf7bc6c4b65e7e9ec66ce4e8b24d3e1b";
    private static final String CONSUMER_SECRET = "cs_3fa6d4b991df7717124cab92628b377d1184ede0";
    private static final String WC_URL = "http://www.xiaohaid.com/index.php";

    private WooCommerce wooCommerce;

    @Before
    public void setUp() {
        wooCommerce = new WooCommerceAPI(new OAuthConfig(WC_URL, CONSUMER_KEY, CONSUMER_SECRET), ApiVersionType.V2);
    }

    @Ignore
    @Test
    public void apiCreateProductTest() {
        Map<String, Object> productInfo = new HashMap<>();
        productInfo.put("name", "Premium Quality");
        productInfo.put("type", "simple");
        productInfo.put("regular_price", "21.99");
        productInfo.put("description", "Pellentesque habitant morbi tristique senectus et netus");
        Map product = wooCommerce.create(EndpointBaseType.PRODUCTS.getValue(), productInfo);
        Assert.assertNotNull(product);
    }

    @Ignore
    @Test
    public void apiGetAllProductsTest() {
        Map<String, String> params = new HashMap<>();
        params.put("per_page","1");
        params.put("offset","0");
        params.put("sku","0735850381015 －0735850381008-1");
        Object products = wooCommerce.getAll(EndpointBaseType.PRODUCTS.getValue(), params);
        List ls = (ArrayList)products;
        for(int i=0;i<ls.size();i++){
            Map m = (Map)ls.get(i);
            String name = ""+m.get("name");
            Integer id = (Integer)m.get("id");

        }
        String json = JSON.toJSONString(products);
        Assert.assertNotNull(products);
    }
    @Ignore
    @Test
    public void apiGetAllCategories() {
        Map<String, String> params = new HashMap<>();
        params.put("per_page","100");
        params.put("offset","0");
//        params.put("sku","708177119845-1");
        Object products = wooCommerce.getAll(EndpointBaseType.PRODUCTS_CATEGORIES.getValue(), params);
        List ls = (ArrayList)products;
        for(int i=0;i<ls.size();i++){
            Map m = (Map)ls.get(i);
            String name = ""+m.get("name");
            Integer id = (Integer)m.get("id");

        }
        String json = JSON.toJSONString(products);
        Assert.assertNotNull(products);
    }
    @Ignore
    @Test
    public void apiGetProductTest() {
        Map product = wooCommerce.get(EndpointBaseType.PRODUCTS.getValue(), 79);
        Assert.assertNotNull(product);
    }

    @Ignore
    @Test
    public void apiUpdateProductTest() {
        Map<String, Object> productInfo = new HashMap<>();
        productInfo.put("name", "Premium Quality UPDATED");
        Map product = wooCommerce.update(EndpointBaseType.PRODUCTS.getValue(), 10, productInfo);
        Assert.assertNotNull(product);
    }

    @Ignore
    @Test
    public void apiDeleteProductTest() {
        Map product = wooCommerce.delete(EndpointBaseType.PRODUCTS.getValue(), 10);
        Assert.assertNotNull(product);
    }

    @Ignore
    @Test
    public void apiBatchVariationTest() {
        Integer productId = 20072;

        List<Map<String, Object>> variations = new ArrayList<>();
        for (int variationId = 20073; variationId <= 20081; variationId++){
            Map<String, Object> variation = new HashMap<>();
            variation.put("id", variationId+"");
            variation.put("regular_price", "10");
            variations.add(variation);
        }

        Map<String, Object> reqOptions = new HashMap<>();
        reqOptions.put("update", variations);

        Map response = wooCommerce.batch(String.format("products/%d/variations", productId), reqOptions);
        Assert.assertNotNull(response);
    }

    @Ignore
    @Test
    public void apiBatchProductTest() {

        List<Map<String, Object>> products = new ArrayList<>();
        Map<String, Object> product = new HashMap<>();
        product.put("id", "19916");
        product.put("name", "MODIFIED NAME");
        products.add(product);

        Map<String, Object> reqOptions = new HashMap<>();
        reqOptions.put("update", products);

        Map response = wooCommerce.batch(EndpointBaseType.PRODUCTS.getValue(), reqOptions);
        Assert.assertNotNull(response);
    }

}
