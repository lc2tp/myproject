package com.shop.spider;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.shop.spider.bean.Warehouse;
import org.omg.CORBA.portable.InputStream;
import org.springframework.util.StringUtils;

import javax.imageio.ImageIO;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLDecoder;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class WooUtils {
    static String scr = "http://112.126.95.64/wordpress/wp-content/uploads/2020/04";
    static String url = "http://112.126.95.64/wordpress/wp-content/uploads/2020/04";

    public static Map creatProduct(JSONObject object){
        if(object !=null){
            Map map = new HashMap();
            JSONObject product = object.getJSONObject("product");
            Map productInfo = new HashMap();
            productInfo.put("name", product.get("name"));
            productInfo.put("sku", object.get("sku"));
            productInfo.put("regular_price", product.get("exportPriceProxy"));//价格

            //productInfo.put("description", product.get("detailInfo"));

        }
        return null;
    }

    /**
     * 详细说明
     * @param product
     * @param map
     */
    public static void detailUrl(JSONObject product,Map map){

        Map imgs = getImgStr(product.get("detailInfo")+"");
        StringBuilder b = new StringBuilder();
        b.append("<p>");
        for(int i=1;i<=imgs.size();i++){
            String imageName = downloadPicture(imgs.get(""+i+"")+"");
            b.append("<img src=\""+url+imageName+"\" class=\"\">");
        }
        b.append("</p>");
        map.put("description",b.toString());
    }

    /**
     * 简短描述
     * @param product
     * @param map
     */
    public static void short_description(JSONObject product,Map map){


        String weight  = product.getString("weight");
        String stockQty =  product.getString("stockQty");
        String validDate = DateUtil.timeStamp2Date(product.getString("validDate"),null);//有效期
        Warehouse warehouse = JSONObject.toJavaObject(product.getJSONObject("warehouse"), Warehouse.class);
        String onlineName = warehouse.getOnlineName();
        onlineName = onlineName.equals("0")? "缺货":onlineName;

        String short_description = "<ul>\\n";

        short_description = short_description +"<li><span class=\\\"info-title\\\">重量</span>";
        short_description = short_description +"<span class=\\\"info-title\\\">"+weight+"g</span></li>\\n";

        short_description = short_description +"<li><span class=\\\"info-title\\\">库存</span>";
        short_description = short_description +"<span class=\\\"info-title\\\">"+stockQty+"</span></li>\\n";

        short_description = short_description +"<li><span class=\\\"info-title\\\">有效期</span>";
        short_description = short_description +"<span class=\\\"info-title\\\">"+validDate+"</span></li>\\n";

        short_description = short_description +"<li><span class=\\\"info-title\\\">发货仓</span>";
        short_description = short_description +"<span class=\\\"info-title\\\">"+onlineName+"</span></li>\\n";

        short_description =  short_description+"</ul>\\n";

        map.put("short_description",short_description);

    }

    /**
     * 产品标签，特价标签
     * @param product
     * @param map
     */
    public static void metaData(JSONObject product,Map map){
        JSONArray jsonArray  = product.getJSONArray("meta_data");
        for(int i=0;i<jsonArray.size();i++){
            JSONObject  o = jsonArray.getJSONObject(i);
            String name = o.getString("name");
            if(name.equals("特价")){
                Map  m  = new HashMap();
                Map value = new HashMap();
                String a[] = new String [1];
                a[0]="5262";
                value.put("id_badge",a[0]);
                value.put("start_date","");
                value.put("end_date","");
                m.put("key","_yith_wcbm_product_meta");
                m.put("value",value);
                map.put("meta_data",new ArrayList<>().add(m));
            }
            return;
        }
    }
    /**
     * 产品图片
     */
    public static void images(JSONObject product,Map map){

        Map imges= new HashMap();
        String s = product.getString("carouselImgs");
        if(!StringUtils.isEmpty(s)){
            s = s.replaceAll("\\[","").replaceAll("\\]","");
            if(!"".equals(s)){
                String at [] = s.split(",");
                List ls = new ArrayList<>();
                for(int i=0;i<at.length;i++){
                    String imageName = downloadPicture(at[i]);
                    Map m = new HashMap();
                    m.put("src",scr+imageName);
                    m.put("position",i);
                    m.put("name",imageName.substring(0,imageName.lastIndexOf(".")));
                    ls.add(m);
                }
                imges.put("images",ls);
            }
        }
    }
    /**
     * 产品图片
     */
    public static void tags(JSONObject product,Map map){

        Map imges= new HashMap();
        String s = product.getString("carouselImgs");
        if(!StringUtils.isEmpty(s)){
            s = s.replaceAll("\\[","").replaceAll("\\]","");
            if(!"".equals(s)){
                String at [] = s.split(",");
                List ls = new ArrayList<>();
                for(int i=0;i<at.length;i++){
                    String imageName = downloadPicture(at[i]);
                    Map m = new HashMap();
                    m.put("src",scr+imageName);
                    m.put("position",i);
                    m.put("name",imageName.substring(0,imageName.lastIndexOf(".")));
                    ls.add(m);
                }
                imges.put("images",ls);
            }
        }
    }
    /**
     * 下载图片
     * @param urlList
     * @return
     */
    private static String downloadPicture(String urlList) {
        URL url = null;
        int imageNumber = 0;

        try {
            url = new URL(urlList);
            DataInputStream dataInputStream = new DataInputStream(url.openStream());

            String imageName =  urlList.substring(urlList.lastIndexOf("/")+1);
            if(imageName.indexOf(".")<0){

                imageName = imageName+".jpg";
            }

            FileOutputStream fileOutputStream = new FileOutputStream(new File("D:\\imgs\\"+imageName));
            ByteArrayOutputStream output = new ByteArrayOutputStream();

            byte[] buffer = new byte[1024];
            int length;

            while ((length = dataInputStream.read(buffer)) > 0) {
                output.write(buffer, 0, length);
            }
            byte[] context=output.toByteArray();
            fileOutputStream.write(output.toByteArray());
            dataInputStream.close();
            fileOutputStream.close();
            return imageName;
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
    /**
     * 格式化图片
     * @param htmlStr
     * @return
     */
    public static Map<String,String> getImgStr(String htmlStr){
        String img="";
        Pattern p_image;
        Matcher m_image;
        Map<String,String> pics = new HashMap<String, String>();

        //     String regEx_img = "<img.*src=(.*?)[^>]*?>"; //图片链接地址

        String regEx_img = "<img.*src\\s*=\\s*(.*?)[^>]*?>";
        p_image = Pattern.compile
                (regEx_img,Pattern.CASE_INSENSITIVE);
        m_image = p_image.matcher(htmlStr);
        int i=1;
        while(m_image.find()){
            img = img + "," + m_image.group();
            // Matcher m  = Pattern.compile("src=\"?(.*?)(\"|>|\\s+)").matcher(img); //匹配src

            Matcher m  = Pattern.compile("src\\s*=\\s*\"?(.*?)(\"|>|\\s+)").matcher(img);

            while(m.find()){
                pics.put(String.valueOf(i),m.group(1));
                i++;
            }
        }
        return pics;
    }

    public static  void  main(String[] args) throws  Exception{
//        String str="<p><img src=\"https://oss.bestl2.com/nzh/product/ZgkrDS0FCaYGO9lQ4DRqX-qW.jpg\"><img src=\"https://oss.bestl2.com/nzh/product/CDJX_BcewnsFp4PcnxFl-NDc.jpg\"><img src=\"https://oss.bestl2.com/nzh/product/tkN3P93N18Q4E10ZEF_hcw_o.jpg\"><img src=\"https://oss.bestl2.com/nzh/product/bW1XMSi1eDoLQk1rZzbP8JrB.jpg\"><img src=\"https://oss.bestl2.com/nzh/product/suQfaXXm5xV0Zb7eyvlfW59c.jpg\"><img src=\"https://oss.bestl2.com/nzh/product/q4X8g15xCmdImV53LMTTcM6u.jpg\"><img src=\"https://oss.bestl2.com/nzh/product/z-6DFrfG3k2HfdK2RluNXudx.jpg\"><img src=\"https://oss.bestl2.com/nzh/product/uJdGYvd0Q6Nc9ABBtU-HqCwO.jpg\"><img src=\"https://oss.bestl2.com/nzh/product/oKzj1g63dhY8KlwdWyQCfRky.jpg\"><img src=\"https://oss.bestl2.com/nzh/product/HvOx6ohYU_kBFyEDfv2OgUww.jpg\"><img src=\"https://oss.bestl2.com/nzh/product/LPIH07wfHTzqeA9IeF7t5yKc.jpg\"></p>";
//        Map<String,String> map=    getImgStr(URLDecoder.decode(str,"UTF-8"));
//        System.out.println(map.values());
//        downloadPicture("https://oss.bestl2.com/nzh/product/WrG6YXRM8xKsX5KK48E3yTAGzsKWahJT");

       // System.out.println(timeStamp2Date("1643670000",null));

        System.out.println("<span class=\\\"info-title\\\">重量</span>");
    }



}
