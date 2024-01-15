package org.unibl.etf.models.entities;

import jakarta.persistence.*;
import lombok.*;
import org.unibl.etf.models.enums.Difficulty;
import org.unibl.etf.models.enums.Location;

import java.math.BigDecimal;
import java.util.List;

@Getter
@Setter
@ToString
@EqualsAndHashCode(of = {"id"})
@NoArgsConstructor
@AllArgsConstructor
@Entity(name = "fitness_program")
public class FitnessProgramEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="id",nullable=false)
    private Long id;
    @Basic
    @Column(name="name",nullable = false)
    private String name;//
    @Basic
    @Column(name="description",nullable = false)
    private String description;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id", referencedColumnName = "id")
    private CategoryEntity category;

    @Basic
    @Column(name="price",nullable = false)
    private BigDecimal price;

    @Basic
    @Column(name="duration",nullable = false)
    private Integer duration;//

    @Basic
    @Column(name = "difficulty", nullable = false)
    @Enumerated(EnumType.ORDINAL)
    private Difficulty difficulty;


    @Basic
    @Column(name = "location", nullable = false)
    @Enumerated(EnumType.ORDINAL)
    private Location location;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "image_id", referencedColumnName = "id")
    private ImageEntity image;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "client_id", referencedColumnName = "id")
    private ClientEntity client;

    @Basic
    @Column(name="instructor_name",nullable = false)
    private String instructorName;

    @Basic
    @Column(name="instructor_surname",nullable = false)
    private String instructorSurname;

    @Basic
    @Column(name="instructor_contact",nullable = false)
    private String instructorContact;

    @Basic
    @Column(name="location_link")
    private String locationLink;

    @Basic
    @Column(name="status",nullable = false)
    private boolean status=true;

    @OneToMany(mappedBy = "fitnessProgram")
    private List<FitnessProgramAttributeEntity> attributes;



}
