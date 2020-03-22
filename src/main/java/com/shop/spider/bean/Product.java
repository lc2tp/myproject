package com.shop.spider.bean;

import java.util.List;

public class Product {
           String id;
    String  name ; //产品名称
    String  brandId ;
    String  currencyId ;
    String  categoryId ;
    String   stockId ;
    String   warehouseId ;
    String   status ;
    String   sourceStatus ;
    String  businessPriceTemplateId ;
    String  transactionFeeRate ;
    String  type ;
    String  priceMin ;
    String  priceMax ;
    String  position ;
    String detailInfo ;
    String sku ;
    String cost ;
    String priceCustomer ;
    String priceProxy ;
    String exportPriceCustomer;
    String exportPriceProxy ;
    String  weight ;
    String validDate ;
    String nameAlias ;
    String description ;
    String carouselImgs;
    String imgUrl ;
    String supportPlatform ;
    String idInSource ;
    String shippingFeeWay ;
    String shippingFree ;
    String zeroShippingFeeQty ;
    String  stockQty ;
    String  salesVolume ;
    String createTime ;
    String  scoreType ;
    String  typeValue ;
    String  teamScore ;
    String  businessPriceTemplate;
    Brand bran;
    Stock stock;
    Warehouse warehouse;//
    List<Tags> tags;
    BusinessPrice businessPrice;

    public BusinessPrice getBusinessPrice() {
        return businessPrice;
    }

    public void setBusinessPrice(BusinessPrice businessPrice) {
        this.businessPrice = businessPrice;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getBrandId() {
        return brandId;
    }

    public void setBrandId(String brandId) {
        this.brandId = brandId;
    }

    public String getCurrencyId() {
        return currencyId;
    }

    public void setCurrencyId(String currencyId) {
        this.currencyId = currencyId;
    }

    public String getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(String categoryId) {
        this.categoryId = categoryId;
    }

    public String getStockId() {
        return stockId;
    }

    public void setStockId(String stockId) {
        this.stockId = stockId;
    }

    public String getWarehouseId() {
        return warehouseId;
    }

    public void setWarehouseId(String warehouseId) {
        this.warehouseId = warehouseId;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getSourceStatus() {
        return sourceStatus;
    }

    public void setSourceStatus(String sourceStatus) {
        this.sourceStatus = sourceStatus;
    }

    public String getBusinessPriceTemplateId() {
        return businessPriceTemplateId;
    }

    public void setBusinessPriceTemplateId(String businessPriceTemplateId) {
        this.businessPriceTemplateId = businessPriceTemplateId;
    }

    public String getTransactionFeeRate() {
        return transactionFeeRate;
    }

    public void setTransactionFeeRate(String transactionFeeRate) {
        this.transactionFeeRate = transactionFeeRate;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getPriceMin() {
        return priceMin;
    }

    public void setPriceMin(String priceMin) {
        this.priceMin = priceMin;
    }

    public String getPriceMax() {
        return priceMax;
    }

    public void setPriceMax(String priceMax) {
        this.priceMax = priceMax;
    }

    public String getPosition() {
        return position;
    }

    public void setPosition(String position) {
        this.position = position;
    }

    public String getDetailInfo() {
        return detailInfo;
    }

    public void setDetailInfo(String detailInfo) {
        this.detailInfo = detailInfo;
    }

    public String getSku() {
        return sku;
    }

    public void setSku(String sku) {
        this.sku = sku;
    }

    public String getCost() {
        return cost;
    }

    public void setCost(String cost) {
        this.cost = cost;
    }

    public String getPriceCustomer() {
        return priceCustomer;
    }

    public void setPriceCustomer(String priceCustomer) {
        this.priceCustomer = priceCustomer;
    }

    public String getPriceProxy() {
        return priceProxy;
    }

    public void setPriceProxy(String priceProxy) {
        this.priceProxy = priceProxy;
    }

    public String getExportPriceCustomer() {
        return exportPriceCustomer;
    }

    public void setExportPriceCustomer(String exportPriceCustomer) {
        this.exportPriceCustomer = exportPriceCustomer;
    }

    public String getExportPriceProxy() {
        return exportPriceProxy;
    }

    public void setExportPriceProxy(String exportPriceProxy) {
        this.exportPriceProxy = exportPriceProxy;
    }

    public String getWeight() {
        return weight;
    }

    public void setWeight(String weight) {
        this.weight = weight;
    }

    public String getValidDate() {
        return validDate;
    }

    public void setValidDate(String validDate) {
        this.validDate = validDate;
    }

    public String getNameAlias() {
        return nameAlias;
    }

    public void setNameAlias(String nameAlias) {
        this.nameAlias = nameAlias;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getCarouselImgs() {
        return carouselImgs;
    }

    public void setCarouselImgs(String carouselImgs) {
        this.carouselImgs = carouselImgs;
    }

    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }

    public String getSupportPlatform() {
        return supportPlatform;
    }

    public void setSupportPlatform(String supportPlatform) {
        this.supportPlatform = supportPlatform;
    }

    public String getIdInSource() {
        return idInSource;
    }

    public void setIdInSource(String idInSource) {
        this.idInSource = idInSource;
    }

    public String getShippingFeeWay() {
        return shippingFeeWay;
    }

    public void setShippingFeeWay(String shippingFeeWay) {
        this.shippingFeeWay = shippingFeeWay;
    }

    public String getShippingFree() {
        return shippingFree;
    }

    public void setShippingFree(String shippingFree) {
        this.shippingFree = shippingFree;
    }

    public String getZeroShippingFeeQty() {
        return zeroShippingFeeQty;
    }

    public void setZeroShippingFeeQty(String zeroShippingFeeQty) {
        this.zeroShippingFeeQty = zeroShippingFeeQty;
    }

    public String getStockQty() {
        return stockQty;
    }

    public void setStockQty(String stockQty) {
        this.stockQty = stockQty;
    }

    public String getSalesVolume() {
        return salesVolume;
    }

    public void setSalesVolume(String salesVolume) {
        this.salesVolume = salesVolume;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public String getScoreType() {
        return scoreType;
    }

    public void setScoreType(String scoreType) {
        this.scoreType = scoreType;
    }

    public String getTypeValue() {
        return typeValue;
    }

    public void setTypeValue(String typeValue) {
        this.typeValue = typeValue;
    }

    public String getTeamScore() {
        return teamScore;
    }

    public void setTeamScore(String teamScore) {
        this.teamScore = teamScore;
    }

    public String getBusinessPriceTemplate() {
        return businessPriceTemplate;
    }

    public void setBusinessPriceTemplate(String businessPriceTemplate) {
        this.businessPriceTemplate = businessPriceTemplate;
    }

    public Brand getBran() {
        return bran;
    }

    public void setBran(Brand bran) {
        this.bran = bran;
    }

    public Stock getStock() {
        return stock;
    }

    public void setStock(Stock stock) {
        this.stock = stock;
    }

    public Warehouse getWarehouse() {
        return warehouse;
    }

    public void setWarehouse(Warehouse warehouse) {
        this.warehouse = warehouse;
    }

    public List<Tags> getTags() {
        return tags;
    }

    public void setTags(List<Tags> tags) {
        this.tags = tags;
    }
}
