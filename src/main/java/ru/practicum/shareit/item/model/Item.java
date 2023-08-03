package ru.practicum.shareit.item.model;

import lombok.AccessLevel;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.FieldDefaults;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.user.model.User;

import javax.persistence.*;


@Table(name = "items")
@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
@EqualsAndHashCode(of = "id")
@Entity
public class Item {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Integer id;
    @Column(name = "name")
    String name;
    @Column(name = "description")
    String description;
    @Column(name = "is_available")
    Boolean available;
    @ManyToOne
    @JoinColumn(name = "owner_id")
    User owner;
    @ManyToOne
    @JoinColumn(name = "request_id", referencedColumnName = "id")
    ItemRequest request;
}
