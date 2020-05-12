package com.shop.spider.service.product;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.icoderman.woocommerce.ApiVersionType;
import com.icoderman.woocommerce.EndpointBaseType;
import com.icoderman.woocommerce.WooCommerce;
import com.icoderman.woocommerce.WooCommerceAPI;
import com.icoderman.woocommerce.oauth.OAuthConfig;
import com.shop.spider.WooUtils;
import com.shop.spider.bean.Warehouse;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ProductServiceImpl implements IProductService {
    private static final Logger logger = LogManager.getLogger();
    @Autowired
    private RestTemplate restTemplate;
    private static final String CONSUMER_KEY = "ck_1e85d27ddf7bc6c4b65e7e9ec66ce4e8b24d3e1b";
    private static final String CONSUMER_SECRET = "cs_3fa6d4b991df7717124cab92628b377d1184ede0";
    private static final String WC_URL = "http://www.xiaohaid.com/index.php";
    String token = "token=6mwzeyNWCQFxdpKk6yukYPZjDCIGFwwbqmSM_OJYtqyB6BMjKWN8yaNrlfsiBS0lwbDkzNi2XzFpdPK0CO7n1ybZdTXPkXi8SQouX2LDtyzNNSg8_IRVMzAbaUyhzQi1GvYUZl_rHq6n8Es4q19oyyAGX2S9nw1SY5WDPyJXvzZb0ihEBxNrDklRgtbKdQayiaOEtxY1TQ2-pbA-8Br4u-IeSFyPQ5v34IbCgUT3K34xVbYhnsDkNpkaDXlCvSQWWi4LQ3DU7n3Dq9RuNgPU58EQLBCbg-oHah7pLKK_F1kbtYBpD1nSGo13RPiX-TOZnaKfwlNBLDmTDpf0l4iJEw==";
    WooCommerce  wooCommerce = new WooCommerceAPI(new OAuthConfig(WC_URL, CONSUMER_KEY, CONSUMER_SECRET), ApiVersionType.V2);
    @Override
    @Test
    public void addProducts() {

        float total = 4588;
        int pageSize = 50;
        int repeat = Math.round(total / 50);
        logger.debug("一共"+repeat+"页");
        for (int i = 66; i < repeat; i++) {
            logger.debug("查询当前第"+i+"页");
            JSONObject object = getListProducts(i, pageSize);
            creatProductToWoo(object,i);

        }
    }

    @Test
    public void addProduct(){
        JSONObject product = getProduct("HN9344949000174-6");
        creatProductToWoo(product,1);

    }
    public void creatProductToWoo(JSONObject object,int i){
        if (object != null) {
            JSONObject data = object.getJSONObject("data");
            JSONArray items = data.getJSONArray("items");
            for (int j = 0; j < items.size(); j++) {
                logger.debug("查询当前第"+i+"页的第"+j+"个商品");
                JSONObject o = (JSONObject) items.get(j);
                JSONObject product = o.getJSONObject("product");

                Map m = new HashMap();
                WooUtils.sku(product,m);
                String sku = m.get("sku")+"";
                int id = getProductsBySku(sku);
                if(id>0){
                    logger.debug("产品已经存在id是："+id);
                    logger.debug("开始修改");
                    Map productInfo = WooUtils.updateProduct(o, wooCommerce);
                    wooCommerce.update(EndpointBaseType.PRODUCTS.getValue(),id,productInfo);
                }else {
                    logger.debug("产品不存在，开始新增");
                    Map productInfo = WooUtils.creatProduct(o, wooCommerce);
                    Map newProduct = wooCommerce.create(EndpointBaseType.PRODUCTS.getValue(), productInfo);
                    if (newProduct.size() == 3) {
                        logger.debug(newProduct.get("message"));
                        logger.debug("查询当前第" + i + "页的第" + j + "个商品失败");
//                        return;
                    }
                }
                //Assert.assertNotNull(newProduct);
            }
        }
    }
    public int getProductsBySku(String sku) {
        Map<String, String> params = new HashMap<>();
        params.put("per_page","1");
        params.put("offset","0");
        params.put("sku",sku);
        try {
            Object products = wooCommerce.getAll(EndpointBaseType.PRODUCTS.getValue(), params);
            List ls = (ArrayList) products;
            for (int i = 0; i < ls.size(); i++) {
                Map m = (Map) ls.get(i);
                String name = "" + m.get("name");
                Integer id = (Integer) m.get("id");
                return id;
            }
        }catch (Exception e){
            logger.debug("查询sku时报错："+sku);
            logger.error("查询sku时报错："+sku);
        }
        return 0;
    }
    public JSONObject getProduct(String params){
        String queryParams = params;
        getRestTemplate();
        HttpHeaders headers = new HttpHeaders();
        List<String> cookies = new ArrayList<String>();
        /* 登录获取Cookie 这里是直接给Cookie，可使用下方的login方法拿到Cookie给入*/
        cookies.add(token);       //在 header 中存入cookies
        headers.put(HttpHeaders.COOKIE, cookies);        //将cookie存入头部
        MultiValueMap<String, String> map = new LinkedMultiValueMap<String, String>();      //请求体给予内容
        HttpEntity<String> httpEntity = new HttpEntity(map, headers);
        ResponseEntity<String> response = restTemplate.exchange("https://ltj.nz/api/products?includeFee=true&page=1&pageSize=10&queryString="+queryParams+"&salesSort=true&showCurrencyId=2&status=1", HttpMethod.GET, httpEntity, String.class);
        System.out.println(response.getBody());        // {"code":200,"msg":null,"content":null}   返回此，且数据库增加数据即为成功
        JSONObject object = JSONObject.parseObject(response.getBody().toString());
        return object;
    }



    public JSONObject getListProducts(int page, int pageSize) {
        getRestTemplate();
        String token = "token=6mwzeyNWCQFxdpKk6yukYPZjDCIGFwwbqmSM_OJYtqyB6BMjKWN8yaNrlfsiBS0lwbDkzNi2XzFpdPK0CO7n1ybZdTXPkXi8SQouX2LDtyzNNSg8_IRVMzAbaUyhzQi1GvYUZl_rHq6n8Es4q19oyyAGX2S9nw1SY5WDPyJXvzZb0ihEBxNrDklRgtbKdQayiaOEtxY1TQ2-pbA-8Br4u-IeSFyPQ5v34IbCgUT3K34xVbYhnsDkNpkaDXlCvSQWWi4LQ3DU7n3Dq9RuNgPU58EQLBCbg-oHah7pLKK_F1kbtYBpD1nSGo13RPiX-TOZnaKfwlNBLDmTDpf0l4iJEw==";
        HttpHeaders headers = new HttpHeaders();
        List<String> cookies = new ArrayList<String>();
        /* 登录获取Cookie 这里是直接给Cookie，可使用下方的login方法拿到Cookie给入*/
        cookies.add(token);       //在 header 中存入cookies
        headers.put(HttpHeaders.COOKIE, cookies);        //将cookie存入头部
        MultiValueMap<String, String> map = new LinkedMultiValueMap<String, String>();      //请求体给予内容
        map.add("includeFee", "true");        //应用名称
        map.add("page", page+"");      //执行器名称
        map.add("pageSize", pageSize+"");          //排序方式
        map.add("salesSort", "true");        //注册方式 ：  0为
        map.add("showCurrencyId", "2");
        map.add("status", "1");
//        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<MultiValueMap<String, String>>(map,headers);

        HttpEntity<String> httpEntity = new HttpEntity(map, headers);
        ResponseEntity<String> response = restTemplate.exchange("https://ltj.nz/api/products?includeFee=true&page=" + page + "&pageSize=" + pageSize + "&salesSort=true&showCurrencyId=2&status=1", HttpMethod.GET, httpEntity, String.class);
        System.out.println(response.getBody());        // {"code":200,"msg":null,"content":null}   返回此，且数据库增加数据即为成功
        JSONObject object = JSONObject.parseObject(response.getBody().toString());
        return object;

    }

    public RestTemplate getRestTemplate() {
        if (restTemplate == null) {
            HttpComponentsClientHttpRequestFactory clientHttpRequestFactory = new HttpComponentsClientHttpRequestFactory();
            clientHttpRequestFactory.setConnectionRequestTimeout(5000);
            clientHttpRequestFactory.setReadTimeout(5000);
            restTemplate = new RestTemplate(clientHttpRequestFactory);
            return restTemplate;
        }
        return restTemplate;
    }

    public static void main(String a[]) {
//        BigDecimal a = new BigDecimal(4588);
//        BigDecimal a = new BigDecimal(50);
        float ad = 4588;

        System.out.println(Math.round(ad / 50));
    }
}
