package com.macondo_cs.MacondoFashionPrototype4.models;

import jakarta.persistence.*;
import org.apache.commons.lang3.StringUtils;

@Entity
@Table(name="Products")
public class Product {
    @Id
    @GeneratedValue(
        strategy = GenerationType.SEQUENCE,
        generator = "product_id_generator"
        )
    @SequenceGenerator(
        name = "product_id_generator",
        sequenceName = "id_generator_products",
        allocationSize = 1
    )
    @Column(name = "productID", nullable = false)
    private Long productId;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "price", nullable = false)
    private double price;

    @Column(name = "category", nullable = false)
    private String category;

    @Column(name = "sex", nullable = false)
    private int sex;  // 0 - female, 1 - male

    @Column(name = "description", nullable = false)
    private String description;

    @Column(name = "quantity", nullable = false)
    private int quantity;

    @Column(name = "totalSold", nullable = false)
    private int totalSold;

    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private Image image;

    // @OneToOne(cascade = CascadeType.REFRESH, fetch = FetchType.EAGER)
    // private Cart cart;

    @PrePersist
    private void init() {}

    public void addImageToProduct(Image image) {
        image.setProduct(this);
        this.image = image;
    }

    public Product(Long productId, String name, double price, String category, Integer sex, String description, int quantity, int totalSold) {
        this.productId = productId;
        this.name = name;
        this.price = price;
        this.category = category;
        this.sex = sex;
        this.description = description;
        this.quantity = quantity;
        this.totalSold = totalSold;
    }

    public Product(String name, double price, String category, int sex, String description, int quantity) {
        this.name = name;
        this.price = price;
        this.category = category;
        this.sex = sex;
        this.description = description;
        this.quantity = quantity;
        this.totalSold = 0;  // initialisation for the DB
    }

    public Product(String name, double price) {
        this.name = name;
        this.price = price;
    }

    public Product() {}

    public Long getProductId() {
        return productId;
    }

    public void setProductId(Long productId) {
        this.productId = productId;
    }

    public String getName() {
        return StringUtils.capitalize(name);
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getSex() {
        return sex;
    }

    public void setSex(int sex) {
        this.sex = sex;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public int getTotalSold() {
        return totalSold;
    }

    public void setTotalSold(int totalSold) {
        this.totalSold = totalSold;
    }

    public Image getImage() {
        return image;
    }

    public void setImage(Image image) {
        this.image = image;
    }

    // public String getImgPath() {
    //     return imgPath;
    // }

    // public void setImgPath(String imgPath) {
    //     this.imgPath = imgPath;
    // }

    public String getName(String mode) throws Exception {
        return ServiceFunctionality.formatTableParameter(name, mode);
    }

    public String getCategory(String mode) throws Exception {
        return ServiceFunctionality.formatTableParameter(category, mode);
    }

    public String getDescription(String mode) throws Exception {
        return ServiceFunctionality.formatTableParameter(description, mode);
    }

    // public String getImgPath(String mode) throws Exception {
    //     return ServiceFunctionality.formatTableParameter(imgPath, mode);
    // }
}
