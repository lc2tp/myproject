package com.shop.spider.service.product;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.shop.spider.WooUtils;
import com.shop.spider.bean.Warehouse;
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
import java.util.List;

public class ProductServiceImpl implements IProductService {
    @Autowired
    private RestTemplate restTemplate;

    @Override
    @Test
    public void runProducts() {
        float total = 4588;
        int pageSize = 50;
        int repeat = Math.round(total / 50);
        for (int i = 1; i < repeat; i++) {
            JSONObject object = getListProducts(i, pageSize);
            if (object != null) {
                JSONObject data = object.getJSONObject("data");
                JSONArray items = data.getJSONArray("items");
                for (int j = 0; j < items.size(); j++) {
                    JSONObject o = (JSONObject) items.get(j);
                    WooUtils.creatProduct(o);
                    JSONObject product = o.getJSONObject("product");
                    Warehouse warehouse = JSONObject.toJavaObject(product.getJSONObject("warehouse"), Warehouse.class);

                }
            }
        }
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
        map.add("page", "1");      //执行器名称
        map.add("pageSize", "100");          //排序方式
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
            System.out.println("....");
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
