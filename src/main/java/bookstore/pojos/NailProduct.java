package bookstore.pojos;

import java.util.Objects;
import java.util.Scanner;

public abstract class NailProduct extends Product{
    private String brand;
    private int stock = 0;
    private double price;

    @Override
    public String toString() {
        return "NailProduct{brand='" + brand + "', " + "stock=" + stock + "', " + "price=" + price + "}";
    }

    public NailProduct() {
    }

    public NailProduct(int stock, String brand, double price) {
        this.stock = stock;
        this.brand = brand;
        this.price = price;
    }

    public int getStock() {
        return stock;
    }

    public void setStock(int stock) {
        this.stock = stock;
    }

    @Override
    public void initialize(Scanner input) {
        System.out.println("Enter Brand:");
        this.brand = getInput(input, "Available Brand");
        System.out.println("Enter price:");
        this.price = getInput(input, 0);

        System.out.println("Enter stock:");
        this.stock = getInput(input, 0);
    }

    protected void initStock(Scanner input) {
        System.out.println("Enter stock:");
        this.stock = getInput(input, 0);

        System.out.println("Enter price:");
        this.price = getInput(input, 0.0);
    }
    @Override
    public void edit(Scanner input) {
        System.out.println("Edit Brand [" + this.brand + "]:");
        this.brand = getInput(input, this.brand);

        System.out.println("Edit Price [" + this.price + "]:");
        this.price = getInput(input, this.price);

        System.out.println("Edit Stock [" + this.stock + "]:");
        this.stock = getInput(input, this.stock);
    }
    @Override
    public double getPrice() {
        return price;
    }
    public void setPrice(double price) {
        this.price = price;
    }
    public String getBrand() {
        return brand;
    }
    public void setBrand(String brand) {
        this.brand = brand;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        NailProduct that = (NailProduct) o;
        return stock == that.stock && Double.compare(price, that.price) == 0 && Objects.equals(brand, that.brand);
    }

    @Override
    public int hashCode() {
        return Objects.hash(brand, stock, price);
    }
}
