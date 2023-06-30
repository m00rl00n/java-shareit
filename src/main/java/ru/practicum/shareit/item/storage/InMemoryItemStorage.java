package ru.practicum.shareit.item.storage;

import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.exception.ValidationException;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.storage.UserStorage;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@Component
@FieldDefaults(level = AccessLevel.PRIVATE)
public class InMemoryItemStorage implements ItemStorage {
    Integer id = 1;
    final HashMap<Integer, List<Item>> items;
    final UserStorage userStorage;

    @Autowired
    public InMemoryItemStorage(UserStorage userStorage) {
        this.items = new HashMap<>();
        this.userStorage = userStorage;
    }

    @Override
    public Item addItem(Integer id, Item item) {
        validateItem(item);
        loadItems();
        if (!items.containsKey(id)) throw new NotFoundException("Пользователь id=" + id + " не найден.");
        if (item.getId() == null) item.setId(generateId());
        addItemToList(id, item);
        return item;
    }

    @Override
    public Item updateItem(Integer userId, Integer itemId, Item updatedItem) {
        List<Item> userItems = getItemsByUserId(userId);
        for (Item item : userItems) {
            if (item.getId().equals(itemId)) {
                if (updatedItem.getName() != null) {
                    item.setName(updatedItem.getName());
                }
                if (updatedItem.getDescription() != null) {
                    item.setDescription(updatedItem.getDescription());
                }
                if (updatedItem.getAvailable() != null) {
                    item.setAvailable(updatedItem.getAvailable());
                }
                if (updatedItem.getOwner() != null) {
                    item.setOwner(updatedItem.getOwner());
                }
                if (updatedItem.getRequestId() != null) {
                    item.setRequestId(updatedItem.getRequestId());
                }
                return item;
            }
        }
        throw new NotFoundException("Вещь не найдена");
    }

    @Override
    public List<Item> getItemsByUserId(Integer id) {
        loadItems();
        return items.get(id);
    }

    @Override
    public List<Item> getItems() {
        loadItems();
        List<Item> allItems = new ArrayList<>();
        for (List<Item> items : items.values()) {
            allItems.addAll(items);
        }
        return allItems;
    }

    public Item getItemById(Integer id) {
        for (List<Item> userList : items.values()) {
            for (Item item : userList) {
                if (item.getId().equals(id)) {
                    return item;
                }
            }
        }
        throw new NotFoundException("Вещь id=" + id + " не найдена");
    }

    @Override
    public List<Item> searchItems(Integer id, String text) {
        if (text.isBlank()) return new ArrayList<>();
        List<Item> foundItems = new ArrayList<>();
        if (items.get(id) == null) throw new NotFoundException("У пользователя id=" + id + " не найдены вещи");
        for (Item item : getItems()) {
            if ((item.getName().toLowerCase().contains(text.toLowerCase())
                    || item.getDescription().toLowerCase().contains(text.toLowerCase()))
                    && item.getAvailable()) {
                foundItems.add(item);
            }
        }
        if (foundItems.isEmpty()) throw new NotFoundException("Вещи, содержащие \"" + text + "\" не найдены");
        return foundItems;
    }

    private void addItemToList(Integer id, Item newItem) {
        int indexItemSameId = -1;
        for (int i = 0; i < items.get(id).size(); i++) {
            if (items.get(id).get(i).getId().equals(newItem.getId())) {
                indexItemSameId = i;
            }
        }
        if (indexItemSameId >= 0) {
            items.get(id).remove(indexItemSameId);
        }
        items.get(id).add(newItem);
    }

    private void loadItems() {
        for (User user : userStorage.getAllUsers()) {
            if (!items.containsKey(user.getId())) {
                items.put(user.getId(), new ArrayList<>());
            }
        }
    }

    private Integer generateId() {
        return id++;
    }

    private void validateItem(Item item) {
        StringBuilder message = new StringBuilder();
        if (item.getDescription() == null || item.getName().isBlank()) {
            message.append("Не указано название. ");
        }
        if (item.getDescription() == null || item.getDescription().isBlank()) {
            message.append("Нет описания вещи. ");
        }
        if (item.getAvailable() == null) {
            message.append("Не указано значение доступности. ");
        }
        if (!message.toString().isBlank()) {
            throw new ValidationException("Ошибка валидации вещи");
        }
    }

}


