package ru.practicum.shareit.request.model;


import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;
import ru.practicum.shareit.user.model.User;

import javax.persistence.*;

@Entity
@Table(name = "requests")
@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ItemRequest {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Integer id;
    @Column(name = "description", nullable = false)
    String description;
    @ManyToOne
    @JoinColumn(name = "requestor_id", referencedColumnName = "id", nullable = false)
    User requestor;
}
