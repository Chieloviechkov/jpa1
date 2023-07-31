package jpa1;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class MenuManager {
    private static Scanner sc = new Scanner(System.in);

    public static void main(String[] args) {
        EntityManagerFactory emf = null;
        EntityManager em = null;

        try {
            emf = Persistence.createEntityManagerFactory("JPATest");
            em = emf.createEntityManager();

            while (true) {
                System.out.println("0: показать все блюда");
                System.out.println("1: добавить новое блюдо");
                System.out.println("2: поиск по цене");
                System.out.println("3: показать блюда со скидкой");
                System.out.println("4: выбрать меню весом 1 кг");
                System.out.println("5: выйти");
                System.out.println("6: отформатировать базу данных");
                System.out.print("-> ");

                String s = sc.nextLine();
                switch (s) {
                    case "0":
                        showAllMenuItems(em);
                        break;
                    case "1":
                        addMenu(em);
                        break;
                    case "2":
                        searchByPrice(em);
                        break;
                    case "3":
                        showDiscount(em);
                        break;
                    case "4":
                        oneKgMenu(em);
                        break;
                    case "5":
                        return;
                    case "6":
                        formatDatabase(em);
                        break;
                    default:
                        System.out.println("Неверный выбор. Пожалуйста, попробуйте снова.");
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
            if (em != null) {
                em.close();
            }
            if (emf != null) {
                emf.close();
            }
            sc.close();
        }
    }

    private static void formatDatabase(EntityManager em) {
        try {
            em.getTransaction().begin();

            // Удаление всех записей из базы данных
            List<Dish> dishes = em.createQuery("FROM Dish", Dish.class).getResultList();
            for (Dish dish : dishes) {
                em.remove(dish);
            }

            em.getTransaction().commit();
            System.out.println("База данных успешно отформатирована.");
        } catch (Exception e) {
            if (em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
            e.printStackTrace();
        }
    }

    private static void addDish(EntityManager em, String dishName, double price, double weight, boolean discountAvailable) {
        try {
            em.getTransaction().begin();

            Dish dish = new Dish(dishName, price, weight, discountAvailable);
            em.persist(dish);

            em.getTransaction().commit();
        } catch (Exception e) {
            if (em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
            e.printStackTrace();
        }
    }

    private static void showAllMenuItems(EntityManager em) {
        try {
            List<Dish> dishes = em.createQuery("FROM Dish", Dish.class).getResultList();

            System.out.println("Все блюда в меню:");
            for (Dish dish : dishes) {
                System.out.println(dish.getDishName() + " - " + dish.getPrice() + " грн");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void addMenu(EntityManager em) {
        System.out.print("Введите название блюда: ");
        String dishName = sc.nextLine();

        System.out.print("Введите цену блюда: ");
        double price = Double.parseDouble(sc.nextLine());

        System.out.print("Введите вес блюда: ");
        double weight = Double.parseDouble(sc.nextLine());

        System.out.print("Есть ли скидка на блюдо (да/нет): ");
        boolean discountAvailable = sc.nextLine().equalsIgnoreCase("да");

        addDish(em, dishName, price, weight, discountAvailable);
        System.out.println("Блюдо успешно добавлено в меню.");
    }

    private static void searchByPrice(EntityManager em) {
        try {
            System.out.print("Введите минимальную цену: ");
            double minPrice = Double.parseDouble(sc.nextLine());

            System.out.print("Введите максимальную цену: ");
            double maxPrice = Double.parseDouble(sc.nextLine());

            List<Dish> dishes = em.createQuery("FROM Dish WHERE price BETWEEN :minPrice AND :maxPrice", Dish.class)
                    .setParameter("minPrice", minPrice)
                    .setParameter("maxPrice", maxPrice)
                    .getResultList();

            System.out.println("Блюда с ценой от " + minPrice + " до " + maxPrice + " грн:");
            for (Dish dish : dishes) {
                System.out.println(dish.getDishName() + " - " + dish.getPrice() + " грн");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void showDiscount(EntityManager em) {
        try {
            List<Dish> dishes = em.createQuery("FROM Dish WHERE discountAvailable = true", Dish.class)
                    .getResultList();

            System.out.println("Блюда со скидкой:");
            for (Dish dish : dishes) {
                System.out.println(dish.getDishName() + " - " + dish.getPrice() + " грн");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void oneKgMenu(EntityManager em) {
        try {
            List<Dish> dishes = em.createQuery("FROM Dish WHERE weight <= 1000 ORDER BY weight ASC", Dish.class)
                    .getResultList();

            double totalWeight = 0;
            List<Dish> selectedDishes = new ArrayList<>();

            for (Dish dish : dishes) {
                if (totalWeight + dish.getWeight() <= 1000) {
                    totalWeight += dish.getWeight();
                    selectedDishes.add(dish);
                }
            }

            System.out.println("Меню со суммарным весом до 1 кг:");
            for (Dish dish : selectedDishes) {
                System.out.println(dish.getDishName() + " - Вес: " + dish.getWeight() + "г");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
