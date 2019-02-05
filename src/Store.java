import ThreadedAVLTree.ThreadedAVLTree;

import java.util.ArrayList;
import java.util.List;

class Store implements Comparable<Store> {

    private int id;
    private ThreadedAVLTree<Item> inventory;
    private int totalItemsInStore;
    private double moneySpentBuying;
    private double moneyMadeSelling;

    @Override
    public int compareTo(Store o) {
        if(id > o.id) return 1;
        else if (id == o.id) return 0;
        else return -1;
    }

    protected Store (int id) {
        this.id = id;
        totalItemsInStore = 0;
        inventory = new ThreadedAVLTree<Item>();
        moneySpentBuying = 0;
        moneyMadeSelling = 0;
    }

    private Item findItem(int itemId) throws IllegalArgumentException {
        Item item = inventory.search(new Item(itemId));
        if(item != null) return item;
        else throw new IllegalArgumentException("Store with ID: " + itemId + " is not present in database");
    }

    protected void addItem (int id, int count, double price) {
        Item toAdd = new Item(id, count, price);
        Item inTree = inventory.search(toAdd);
        if(inTree == null) {
            inventory.insert(toAdd);
            totalItemsInStore++;
        }
        else inTree.incrementCount(count);
        moneySpentBuying += count*price;
    }

    protected List<Item> itemsInStore() {
        return inventory.inorderTraversalList();
    }

    protected double getItemIPC(int id, InventoryKeeper.IPC type) throws IllegalArgumentException {
        Item inTree = findItem(id);
        switch (type) {
            case ID:
                return inTree.getId();
            case COUNT:
                return inTree.getCount();
            case PRICE:
                return inTree.getPrice();
        }
        return -1;
    }

    protected int totalItemsInStore() {
        return totalItemsInStore;
    }

    protected void changePriceOfItem(int id, int newPrice) throws IllegalArgumentException {
        findItem(id).updatePrice(newPrice);
    }

    protected void sellItem(int id, int count, boolean reOrder) throws IllegalArgumentException {
        Item inTree = findItem(id);
        inTree.decrementCount(count);
        moneyMadeSelling += count*inTree.getPrice();
        if(inTree.getCount() <= 0 && reOrder)
            inTree.setCount(10);
        if(inTree.getCount() <= 0 && !reOrder) {
            inventory.delete(new Item(id));
            totalItemsInStore--;
        }
    }

    protected void removeAllItems() {
        inventory = new ThreadedAVLTree<Item>();
        totalItemsInStore = 0;
    }

    protected double getProfit() {
        return moneyMadeSelling - moneySpentBuying;
    }

    protected List<Item> withinPriceRange(Item min, Item max) {
        return inventory.rangeQuery(min, max, new ArrayList<>(), Item.priceComparator());
    }

    protected List<Item> withinCount(Item min, Item max) {
        return inventory.rangeQuery(min, max, new ArrayList<>(), Item.countComparator());
    }

    protected List<Item> withinIdRange(Item min, Item max) {
        return inventory.rangeQuery(min, max, new ArrayList<>(), Item.IdComparator());
    }
}
