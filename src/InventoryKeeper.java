import SkipList.src.SkipList;
import ThreadedAVLTree.ThreadedAVLTree;

import java.util.List;

public class InventoryKeeper {

    SkipList<Store> db;
    String name;
    protected enum IPC {ID, PRICE, COUNT}

    public InventoryKeeper(String name) {
        this.name = name;
        db = new SkipList<>();
    }

    public int totalStores() {
        return db.getCount();
    }

    public void addStore(int storeId) {
        db.insert(new Store(storeId));
    }

    private Store findStore(int storeId) throws IllegalArgumentException {
        Store store = db.search(new Store(storeId));
        if(store != null) return store;
        else throw new IllegalArgumentException("Store with ID: " + storeId + " is not present in database");
    }

    public void addItemInStore(int storeId, int itemId, int itemCount, int itemPrice) throws IllegalArgumentException {
        findStore(storeId).addItem(itemId, itemCount, itemPrice);
    }

    public void getItemPrice(int storeId, int itemId) throws IllegalArgumentException {
        findStore(storeId).getItemIPC(itemId, IPC.PRICE);
    }

    public void getItemCount(int storeId, int itemId) throws IllegalArgumentException {
        findStore(storeId).getItemIPC(itemId, IPC.COUNT);
    }

    public void sellItemInStore(int storeId, int itemId, int itemCount, boolean reOrder) throws IllegalArgumentException {
        findStore(storeId).sellItem(itemId, itemCount, reOrder);
    }

    public void changePriceOfItem(int storeId, int itemId, int newPrice) throws IllegalArgumentException {
        findStore(storeId).changePriceOfItem(itemId, newPrice);
    }

    public void deleteStore(int storeId) {
        db.delete(new Store(storeId));
    }

    public void removeStoreAndTransferInventory(int removeStoreId, int transferStoreId) {
        for(Item item: findStore(removeStoreId).itemsInStore())
            findStore(transferStoreId).addItem(item.getId(), item.getCount(), item.getPrice());
        deleteStore(removeStoreId);
    }

    public double getStoreProfit(int storeId) {
        return findStore(storeId).getProfit();
    }

    public List<Item> withinPriceRange(int storeId, double min, double max) {
        return findStore(storeId).withinPriceRange(new Item(0, 0, min), new Item(0, 0, max));
    }

    public List<Item> withinCountRange(int storeId, double min, double max) {
        return findStore(storeId).withinCount(new Item(0, 0, min), new Item(0, 0, max));
    }

    public List<Item> withinIdRange(int storeId, double min, double max) {
        return findStore(storeId).withinIdRange(new Item(0, 0, min), new Item(0, 0, max));
    }






}
