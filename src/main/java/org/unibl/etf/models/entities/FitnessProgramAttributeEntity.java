package org.unibl.etf.models.entities;

import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@ToString
@EqualsAndHashCode(of = {"id"})
@NoArgsConstructor
@Builder
@AllArgsConstructor
@Entity(name = "fitness_program_category_attribute")
public class FitnessProgramAttributeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Basic
    @Column(name="value")
    private String value;

    @ManyToOne
    @JoinColumn(name="category_attribute_id", referencedColumnName="id",nullable = false)
    private CategoryAttributeEntity attribute;

    @ManyToOne
    @JoinColumn(name="fitness_program_id", referencedColumnName="id",nullable = false)
    private FitnessProgramEntity fitnessProgram;
}
