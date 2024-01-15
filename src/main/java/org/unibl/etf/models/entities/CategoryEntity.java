package org.unibl.etf.models.entities;

import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Getter
@Setter
@ToString
@EqualsAndHashCode(of = {"id"})
@NoArgsConstructor
@AllArgsConstructor
@Entity(name = "category")
public class CategoryEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="id",nullable=false)
    private Long id;

    @Basic
    @Column(name = "name",nullable = false,unique = true)
    private String name;

    @OneToMany(mappedBy = "category")
    List<CategoryAttributeEntity> attributes;

    @Basic
    @Column(name="status",nullable = false)
    private boolean status=true;
}
