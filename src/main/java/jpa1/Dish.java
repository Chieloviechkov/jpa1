package jpa1;

import javax.persistence.*;
import java.util.Objects;

@Entity
@Table(name = "menu")
public class Dish {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "dish_name")
    private String dishName;

    @Column(name = "price")
    private double price;

    @Column(name = "weight")
    private double weight;

    @Column(name = "discount_available")
    private boolean discountAvailable;

    // Конструктор з параметрами
    public Dish(String dishName, double price, double weight, boolean discountAvailable) {
        this.dishName = dishName;
        this.price = price;
        this.weight = weight;
        this.discountAvailable = discountAvailable;
    }

    // Пустий конструктор (обов'язковий для Hibernate)
    public Dish() {
    }

    // Геттери і сеттери для полів класу

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getDishName() {
        return dishName;
    }

    public void setDishName(String dishName) {
        this.dishName = dishName;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public double getWeight() {
        return weight;
    }

    public void setWeight(double weight) {
        this.weight = weight;
    }

    public boolean isDiscountAvailable() {
        return discountAvailable;
    }

    public void setDiscountAvailable(boolean discountAvailable) {
        this.discountAvailable = discountAvailable;
    }

    // Перевизначені методи equals, hashCode і toString

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Dish dish = (Dish) o;
        return Double.compare(dish.price, price) == 0 && Double.compare(dish.weight, weight) == 0 && discountAvailable == dish.discountAvailable && Objects.equals(id, dish.id) && Objects.equals(dishName, dish.dishName);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, dishName, price, weight, discountAvailable);
    }

    @Override
    public String toString() {
        return "Страва{" +
                "id=" + id +
                ", назва='" + dishName + '\'' +
                ", ціна=" + price +
                ", вага=" + weight +
                ", зі знижкою=" + discountAvailable +
                '}';
    }
}
