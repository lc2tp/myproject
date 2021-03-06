package com.shop.spider;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.shop.spider.bean.Product;
import com.shop.spider.bean.Warehouse;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ClientController {
    @Autowired
    private RestTemplate restTemplate;
    @Test
    public void login(){
        HttpComponentsClientHttpRequestFactory clientHttpRequestFactory = new HttpComponentsClientHttpRequestFactory();
        System.out.println("....");
        clientHttpRequestFactory.setConnectionRequestTimeout(5000);
        clientHttpRequestFactory.setReadTimeout(5000);
        restTemplate = new RestTemplate(clientHttpRequestFactory);
        String url = "https://ltj.nz/api/user/login";
        HttpHeaders headers = new HttpHeaders();
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("username", "15652367095");
        map.put("password","123456");
        map.put("type","account");
        MediaType type = MediaType.parseMediaType("application/json; charset=UTF-8");
        headers.setContentType(type);
        String requestJson = JSONObject.toJSONString(map);
        HttpEntity<String> entity = new HttpEntity<String>(requestJson,headers);
        ResponseEntity<String> response = restTemplate.postForEntity(url, entity, String.class);
        String token = response.getHeaders().get("Set-Cookie").get(0);
        System.out.println(token);
        //getListProducts(token.split(";")[0]);
    }
    @Test
    public void getListProducts(){
        getRestTemplate();
        String token ="token=6mwzeyNWCQFxdpKk6yukYPZjDCIGFwwbqmSM_OJYtqyB6BMjKWN8yaNrlfsiBS0lwbDkzNi2XzFpdPK0CO7n1ybZdTXPkXi8SQouX2LDtyzNNSg8_IRVMzAbaUyhzQi1GvYUZl_rHq6n8Es4q19oyyAGX2S9nw1SY5WDPyJXvzZb0ihEBxNrDklRgtbKdQayiaOEtxY1TQ2-pbA-8Br4u-IeSFyPQ5v34IbCgUT3K34xVbYhnsDkNpkaDXlCvSQWWi4LQ3DU7n3Dq9RuNgPU58EQLBCbg-oHah7pLKK_F1kbtYBpD1nSGo13RPiX-TOZnaKfwlNBLDmTDpf0l4iJEw==";
        HttpHeaders headers = new HttpHeaders();
        List<String> cookies =new ArrayList<String>();
        /* 登录获取Cookie 这里是直接给Cookie，可使用下方的login方法拿到Cookie给入*/
        cookies.add(token);       //在 header 中存入cookies
            headers.put(HttpHeaders.COOKIE,cookies);        //将cookie存入头部
        MultiValueMap<String, String> map = new LinkedMultiValueMap<String, String>();      //请求体给予内容
        map.add("includeFee", "true");        //应用名称
        map.add("page", "1");      //执行器名称
        map.add("pageSize", "100");          //排序方式
        map.add("salesSort", "true");        //注册方式 ：  0为
        map.add("showCurrencyId","2");
        map.add("status","1");
//        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<MultiValueMap<String, String>>(map,headers);

        HttpEntity<String> httpEntity = new HttpEntity(map,headers);
        ResponseEntity<String> response = restTemplate.exchange("https://ltj.nz/api/products?includeFee=true&page=1&pageSize=10&salesSort=true&showCurrencyId=2&status=1",HttpMethod.GET, httpEntity, String.class);
        System.out.println(response.getBody());        // {"code":200,"msg":null,"content":null}   返回此，且数据库增加数据即为成功
        JSONObject object = JSONObject.parseObject(response.getBody().toString());
        JSONObject data = object.getJSONObject("data");
        JSONArray items = data.getJSONArray("items");
        for(int i=0;i<items.size();i++){
            JSONObject o = (JSONObject) items.get(i);
            JSONObject product = o.getJSONObject("product");
            Warehouse warehouse =  JSONObject.toJavaObject(product.getJSONObject("warehouse"), Warehouse.class);

//            Product product  = JSONObject.toJavaObject(o.getJSONObject("product"), Product.class);
        }


    }

    public void getProductBySku(String sku){
        System.out.println();
    }

    public RestTemplate getRestTemplate(){
        if(restTemplate ==null){
            HttpComponentsClientHttpRequestFactory clientHttpRequestFactory = new HttpComponentsClientHttpRequestFactory();
            System.out.println("....");
            clientHttpRequestFactory.setConnectionRequestTimeout(5000);
            clientHttpRequestFactory.setReadTimeout(5000);
            restTemplate = new RestTemplate(clientHttpRequestFactory);
            return restTemplate;
        }
        return restTemplate;
    }


}
