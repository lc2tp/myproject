package com.shop.spider;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.icoderman.woocommerce.EndpointBaseType;
import com.icoderman.woocommerce.WooCommerce;
import com.shop.spider.bean.Warehouse;
import org.apache.commons.net.ftp.FTP;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.Assert;
import org.omg.CORBA.portable.InputStream;
import org.springframework.util.StringUtils;

import javax.imageio.ImageIO;
import java.io.*;
import java.math.BigDecimal;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLDecoder;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class WooUtils {
    static String scr = "http://www.xiaohaid.com/wp-content/uploads/2020/05";
    static String url = "http://www.xiaohaid.com/wp-content/uploads/2020/05";
    static FTPClient ftpClient;
    static Map catMap = new HashMap();
    static Map tagMap = new HashMap();
    static WooCommerce wooCommerce1;
    private static final Logger logger = LogManager.getLogger();

    /**
     * 新增产品
     * @param object
     * @param wooCommerce
     * @return
     */
    public static Map creatProduct(JSONObject object,WooCommerce wooCommerce){
        wooCommerce1 = wooCommerce;
        getAllCategories();
        getAllTags();
        try{
            if(object !=null){
                JSONObject product = object.getJSONObject("product");
                Map productInfo = new HashMap();
                productInfo.put("name", product.get("name"));
                productInfo.put("manage_stock", true);
//                productInfo.put("sku", product.get("sku"));
                sku(product,productInfo);
                productInfo.put("weight", product.getString("weight").trim());//价格
                price(object,productInfo,product.get("name")+"");
                detailUrl(product,productInfo);
                short_description(product,productInfo);
                metaData(product,productInfo);
                images(product,productInfo);
                category(product,productInfo);
                tags(product,productInfo,product.get("name")+"");
                return productInfo;
                //productInfo.put("description", product.get("detailInfo"));

            }
        }catch (Exception e){
            logger.error(e.getMessage());
        }finally{
            closeConnect();
        }
        return null;
    }

    /**
     * 修改产品
     * @param object
     * @param wooCommerce
     * @return
     */
    public static Map updateProduct(JSONObject object,WooCommerce wooCommerce){
        wooCommerce1 = wooCommerce;
        getAllCategories();
        getAllTags();
        try{
            if(object !=null){
                JSONObject product = object.getJSONObject("product");
                Map productInfo = new HashMap();
                productInfo.put("name", product.get("name"));
                productInfo.put("manage_stock", true);
//                productInfo.put("sku", product.get("sku"));
                //sku(product,productInfo);
                productInfo.put("weight", product.getString("weight").trim());//价格
                price(object,productInfo,product.get("name")+"");
                //detailUrl(product,productInfo);
                short_description(product,productInfo);
                metaData(product,productInfo);
                //images(product,productInfo);
                //category(product,productInfo);
                tags(product,productInfo,product.get("name")+"");
                return productInfo;
                //productInfo.put("description", product.get("detailInfo"));

            }
        }catch (Exception e){
            logger.error(e.getMessage());
        }finally{
            closeConnect();
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
            System.out.println(i);
            String imageName = downloadPicture(imgs.get(""+i+"")+"",product.getString("sku"));
            b.append("<img src=\""+url+"/"+imageName+"\" class=\"\">");
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

        String short_description = "<ul>";

        short_description = short_description +"<li><span class=\\\"info-title\\\">重量</span>";
        short_description = short_description +"<span class=\\\"info-title\\\">"+weight+"g</span></li>";

        short_description = short_description +"<li><span class=\\\"info-title\\\">库存</span>";
        short_description = short_description +"<span class=\\\"info-title\\\">"+stockQty+"</span></li>";

        short_description = short_description +"<li><span class=\\\"info-title\\\">有效期</span>";
        short_description = short_description +"<span class=\\\"info-title\\\">"+validDate+"</span></li>";

        short_description = short_description +"<li><span class=\\\"info-title\\\">发货仓</span>";
        short_description = short_description +"<span class=\\\"info-title\\\">"+onlineName+"</span></li>";

        short_description =  short_description+"</ul>";

        map.put("short_description",short_description);
//        Integer stockQty  = Integer.parseInt(stockQty);
        logger.debug("库存数量为"+stockQty);
        if(stockQty.equals("0")||stockQty.equals("")||stockQty==null){
            map.put("manage_stock", false);
            map.put("in_stock",false);
        }
        map.put("stock_quantity",Integer.parseInt(stockQty));

    }

    /**
     * 产品标签，特价标签
     * @param product
     * @param map
     */
    public static void metaData(JSONObject product,Map map){
        JSONArray jsonArray  = product.getJSONArray("tags");

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
                List ls = new ArrayList<>();
                ls.add(m);
                map.put("meta_data",ls);
                return;
            }
        }
    }
    /**
     * 产品图片
     */
    public static void images(JSONObject product,Map map){

        String s = product.getString("carouselImgs");
        if(!StringUtils.isEmpty(s)){

            if(!"".equals(s)){
                String at [] = s.split(",");
                List ls = new ArrayList<>();
                for(int i=0;i<at.length;i++){
                    String nu = at[i].replaceAll("\\[","").replaceAll("\\]","").replaceAll("\"","").replaceAll("\"\"","");
                    String imageName = downloadPicture(nu,product.getString("sku"));
                    Map m = new HashMap();
                    m.put("src",scr+"/"+imageName);
                    m.put("position",i);
                    m.put("name",imageName.substring(0,imageName.lastIndexOf(".")));
                    ls.add(m);
                }
                map.put("images",ls);
            }
        }
    }
    /**
     * 标签
     */
    public static void tags(JSONObject product,Map map,String pName){

        Map imges= new HashMap();
        JSONArray array = product.getJSONArray("tags");
        List ls = new ArrayList();
        for(int i=0;i<array.size();i++){
            JSONObject o  = array.getJSONObject(i);
            String name = o.getString("name");
            Integer id = (Integer) tagMap.get(name);
            Map idMap = new HashMap();
            if(id!=null){
                idMap.put("id",id);
                ls.add(idMap);
            }else{
                Map m = new HashMap();
                m.put("name",name);
                Map response = wooCommerce1.create(EndpointBaseType.PRODUCTS_TAGS.getValue(),m);
                id = (Integer)response.get("id");
                idMap.put("id",id);
                ls.add(idMap);
            }
        }
        Warehouse warehouse = JSONObject.toJavaObject(product.getJSONObject("warehouse"), Warehouse.class);
        String onlineName = warehouse.getOnlineName();
        Map idMap = new HashMap();
        Integer id = (Integer) tagMap.get(onlineName);

        if(id!=null){
            idMap.put("id",id);
            ls.add(idMap);
        }else{
            Map m = new HashMap();
            m.put("name",onlineName);
            Map response = wooCommerce1.create(EndpointBaseType.PRODUCTS_TAGS.getValue(),m);
            id = (Integer)response.get("id");
            idMap.put("id",id);
            ls.add(idMap);
        }
//        if(pName.indexOf(""))
//         id = (Integer) tagMap.get(pName);
//
//        if(id!=null){
//            idMap.put("id",id);
//            ls.add(idMap);
//        }else{
//            Map m = new HashMap();
//            m.put("name",onlineName);
//            Map response = wooCommerce1.create(EndpointBaseType.PRODUCTS_TAGS.getValue(),m);
//            id = (Integer)response.get("id");
//            idMap.put("id",id);
//            ls.add(idMap);
//        }

        map.put("tags",ls);
    }

    public static void category(JSONObject product,Map map){
        Map imges= new HashMap();
        JSONObject object = product.getJSONObject("category");
        String name = object.getString("name");
        Integer id = (Integer) catMap.get(name);
        Map idMap = new HashMap();
        List ls = new ArrayList();
        if(id!=null) {
            idMap.put("id",id);

            ls.add(idMap);

            map.put("categories",ls);
        }else{
            Map m = new HashMap();
            m.put("name",name);
            Map response = wooCommerce1.create(EndpointBaseType.PRODUCTS_CATEGORIES.getValue(),m);
            id = (Integer) response.get("id");
            idMap.put("id",id);
            ls.add(idMap);
            map.put("categories",ls);
            catMap.put(name,id);
        }
    }
    /**
     * 下载图片
     * @param urlList
     * @return
     */
    private static String downloadPicture(String urlList,String sku) {
        URL url = null;
        int imageNumber = 0;

        try {
            url = new URL(urlList);
            DataInputStream dataInputStream = new DataInputStream(url.openStream());

            String imageName =  urlList.substring(urlList.lastIndexOf("/")+1);
            if(imageName.indexOf(".")<0){

                imageName = imageName+".jpg";
            }
            String fileUrl = "D:\\imgs\\"+sku.replaceAll("\\*","-");
            File dir = new File(fileUrl);
            if(!dir.exists()){
                dir.mkdirs();//创建目录
            }

            fileUrl = fileUrl+"\\"+imageName;
            File localImg = new File(fileUrl);
            if(!localImg.exists()) {//不存在就创建一个
                FileOutputStream fileOutputStream = new FileOutputStream(localImg);
                ByteArrayOutputStream output = new ByteArrayOutputStream();

                byte[] buffer = new byte[1024];
                int length;

                while ((length = dataInputStream.read(buffer)) > 0) {
                    output.write(buffer, 0, length);
                }
                byte[] context = output.toByteArray();
                fileOutputStream.write(output.toByteArray());
                dataInputStream.close();
                fileOutputStream.close();
            }
            uploadImg(localImg,imageName);//上传文件
            return imageName;
        } catch (MalformedURLException e) {
            logger.error(e.getMessage());
            e.printStackTrace();
        } catch (IOException e) {
            logger.error(e.getMessage());
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
        String  name = "Bioshine 倍恩喜 婴幼儿配方羊奶粉 1段 800g *6【6罐直邮包邮，必须上传身份证照片】程光承运";
        BigDecimal b1 = new BigDecimal("502.23");
        BigDecimal b2 = new BigDecimal(10);
        int bc =10;
        int index = name.indexOf("*");
        if(index>-1){
            String num= name.substring(index+1,index+2);
            if(isInteger(num)){
                bc = bc*Integer.parseInt(num);
                System.out.println(bc);
            }
        }else if(name.indexOf("3罐")>-1||name.indexOf("3代")>-1){
            bc = bc*3;
        }else  if(name.indexOf("6罐")>-1||name.indexOf("6代")>-1){
            bc = bc*6;
        }
        Float add = b1.add(b2).floatValue();
        System.out.println(b1.compareTo(new BigDecimal(600))>-1);
        System.out.println("add=========" + add);
    }

    public static boolean isInteger(String str) {
        Pattern pattern = Pattern.compile("^[-\\+]?[\\d]*$");
        return pattern.matcher(str).matches();
    }
    public static FTPClient connectHost(){
        if(ftpClient !=null){
            return ftpClient;
        }else{
            ftpClient = new FTPClient();
            ftpClient.setControlEncoding("GBK");
            String hostname = "112.126.95.64";
            int port = 21;
            String username = "lorence";
            String password = "Pengyi527";
            try {
                ftpClient.connect(hostname, port);
                //登录ftp
                ftpClient.login(username, password);
                return ftpClient;
            } catch (IOException e) {
                logger.error(e.getMessage());
            }
        }
        return null;
    }

    public static void uploadImg(File file,String fileName){

        ftpClient = connectHost();
        if(file != null){
            try {
                ftpClient.setFileType(FTP.BINARY_FILE_TYPE);
                FileInputStream input = new FileInputStream(file);
                ftpClient.storeFile(fileName, input);//文件你若是不指定就会上传到root目录下
                input.close();
            } catch (IOException e) {
                logger.error(e.getMessage());
            }

        }
    }
    public static void closeConnect(){
        if(ftpClient !=null){
            try {
                ftpClient.logout();
                ftpClient=null;
            } catch (IOException e) {
                logger.error(e.getMessage());
            }
        }
    }
    public static void getAllCategories() {
        Map<String, String> params = new HashMap<>();
        params.put("per_page","100");
        params.put("offset","0");
        Object products = wooCommerce1.getAll(EndpointBaseType.PRODUCTS_CATEGORIES.getValue(), params);
        List ls = (ArrayList)products;
        for(int i=0;i<ls.size();i++){
            Map m = (Map)ls.get(i);
            String name = ""+m.get("name");
            Integer id = (Integer)m.get("id");
            catMap.put(name,id);

        }
        params.put("per_page","100");
        params.put("offset","100");
        products = wooCommerce1.getAll(EndpointBaseType.PRODUCTS_CATEGORIES.getValue(), params);
        ls = (ArrayList)products;
        for(int i=0;i<ls.size();i++){
            Map m = (Map)ls.get(i);
            String name = ""+m.get("name");
            Integer id = (Integer)m.get("id");
            catMap.put(name,id);

        }
    }
    public static  void getAllTags() {
        Map<String, String> params = new HashMap<>();
        params.put("per_page","100");
        params.put("offset","0");
        Object products = wooCommerce1.getAll(EndpointBaseType.PRODUCTS_TAGS.getValue(), params);
        List ls = (ArrayList)products;
        for(int i=0;i<ls.size();i++){
            Map m = (Map)ls.get(i);
            String name = ""+m.get("name");
            Integer id = (Integer)m.get("id");
            tagMap.put(name,id);
        }
    }

    public static void price(JSONObject product,Map map,String name){
        logger.debug(name);
        JSONObject o = product.getJSONObject("businessPrice");
        String regulerprice =  o.getString("exportPrice");
        BigDecimal b1 = new BigDecimal(regulerprice);

        int bc =10;
        int index = name.indexOf("*");
        if(index>-1){
            String num= name.substring(index+1,index+2);
            if(isInteger(num)){
                bc = bc*Integer.parseInt(num);
                logger.debug(bc);
            }
        }else if(name.indexOf("3罐")>-1||name.indexOf("3袋")>-1){
            bc = bc*3;
            logger.debug(bc);
        }else  if(name.indexOf("6罐")>-1||name.indexOf("6袋")>-1){
            bc = bc*6;
            logger.debug(bc);
        }else if(name.indexOf("美国直邮")>-1){//包包
            if(b1.compareTo(new BigDecimal(500))>-1){//美国商品大于500加100元
                bc = bc+100;
            }else{//否则只加50元
                bc = bc+50;
            }
        }
        BigDecimal b2 = new BigDecimal(bc);

        Float add = b1.add(b2).floatValue();
        logger.debug("商品价格wei ："+add);
        map.put("regular_price",add+"");//价格
    }
    public static void sku(JSONObject product,Map map){
        String sku = product.get("sku")+"";
        sku = sku.replaceAll("\\*","-");
        logger.debug("原始sku为"+sku);
        Warehouse warehouse = JSONObject.toJavaObject(product.getJSONObject("warehouse"), Warehouse.class);
        String onlineName = warehouse.getOnlineName();
        if(onlineName.equals("")){

        }else if(onlineName.equals("新西兰仓")) {
            sku = sku+"-1";
        }else if(onlineName.equals("澳洲仓")) {
            sku = sku+"-2";
        }else if(onlineName.equals("环球精品仓")) {
            sku = sku+"-3";
        }else if(onlineName.equals("香港仓")) {
            sku = sku+"-4";
        }

        map.put("sku", sku);
    }


}
