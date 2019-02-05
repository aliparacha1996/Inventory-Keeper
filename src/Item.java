import java.util.Comparator;

public class Item implements Comparable<Item> {
    private int id;
    private int count;
    private double price;
    // Can be extended to have name stored as String
    // Can be extended to have categories (represented by enums) like apparel, food etc.

    Item(int id) {
        this.id = id;
    }

    Item(int id, int count, double price) {
        this.id = id;
        this.count = count;
        this.price = price;
    }

    protected void incrementCount(int count) {
        this.count += count;
    }

    protected void decrementCount(int count) {
        this.count -= count;
    }

    protected void updatePrice(double price) {
        this.price = price;
    }

    protected int getId() {
        return id;
    }

    protected void setId(int id) {
        this.id = id;
    }

    protected int getCount() {
        return count;
    }

    protected void setCount(int count) {
        this.count = count;
    }

    protected double getPrice() {
        return price;
    }

    protected void setPrice(double price) {
        this.price = price;
    }

    @Override
    public int compareTo(Item o) {
        return Integer.compare(id, o.id);
    }

    public static Comparator<Item> priceComparator() {
        return (o1, o2) -> {
            if(o1 == o2) return 0;
            else if(o1 == null) return 1;
            else if(o2 == null) return -1;
            else if(o1.price > o2.price) return 1;
            else return o1.price == o2.price? 0:-1;
        };
    }

    public static Comparator<Item> countComparator() {
        return (o1, o2) -> {
            if(o1 == o2) return 0;
            else if(o1 == null) return 1;
            else if(o2 == null) return -1;
            if(o1.count > o2.count) return 1;
            else return o1.count == o2.count? 0:-1;
        };
    }

    public static Comparator<Item> IdComparator() {
        return (o1, o2) -> {
            if(o1 == o2) return 0;
            else if(o1 == null) return 1;
            else if(o2 == null) return -1;
            if(o1.id > o2.id) return 1;
            else return o1.id == o2.id? 0:-1;
        };
    }
}
