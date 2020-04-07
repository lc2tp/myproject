package com.shop.spider;

import com.alibaba.fastjson.JSONObject;
import org.omg.CORBA.portable.InputStream;
import org.springframework.util.StringUtils;

import javax.imageio.ImageIO;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
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
    public static void short_description(JSONObject product,Map map){

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

        String a ="[aaaa,bbb]";
        System.out.println(a.split(","));
    }

}
