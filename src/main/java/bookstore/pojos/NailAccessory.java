package bookstore.pojos;

import java.util.Objects;
import java.util.Scanner;

public class NailAccessory extends NailProduct{
    private String accessoryType;
    private int price;
    private int stock;
    private String isbn;

    public NailAccessory() {
        super();
    }

    public NailAccessory(String accessoryType, String brand, double price, int stock) {
        super(stock, brand, price);
        this.accessoryType = accessoryType;
    }

    @Override
    public void initialize(Scanner input) {
        // Pass scanner up to parent
        super.initialize(input);

        System.out.println("Enter accessory type:");
        this.accessoryType = getInput(input, "Unknown accessory");

        // Pass scanner to helper
        super.initStock(input);
    }

    @Override
    public String toString() {
        return "NailAccessory{" +
                "accessoryType='" + accessoryType + '\'' +
                "} " + super.toString();
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        NailAccessory that = (NailAccessory) o;
        return Objects.equals(accessoryType, that.accessoryType);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(accessoryType);
    }

    public String getAccessoryType() {
        return accessoryType;
    }

    public void setAccessoryType(String accessoryType) {
        this.accessoryType = accessoryType;
    }

    @Override
    public void edit(Scanner input) {
        super.edit(input);
        System.out.println("Edit Accessory Type [" + this.accessoryType + "]:");
        this.accessoryType = getInput(input, this.accessoryType);
    }


    @Override
    public void sellItem() {
        System.out.println("Enjoy your new nail accessory. Your nails will be fabulous!");
        setStock(getStock()-1);
    }

    @Override
    public double getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    @Override
    public int getStock() {
        return stock;
    }

    @Override
    public void setStock(int stock) {
        this.stock = stock;
    }

    public String getIsbn() {
        return isbn;
    }
}
