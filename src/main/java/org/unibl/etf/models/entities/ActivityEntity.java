package org.unibl.etf.models.entities;

import jakarta.persistence.*;
import lombok.*;

import java.util.Date;

@Getter
@Setter
@ToString
@EqualsAndHashCode(of = {"id"})
@NoArgsConstructor
@AllArgsConstructor
@Entity(name = "activity")
public class ActivityEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="id",nullable=false)
    private Long id;

    @Basic
    @Column(name="name",nullable = false)
    private String name;

    @Basic
    @Column(name="sets")
    private Integer sets;

    @Basic
    @Column(name="reps")
    private Integer reps;

    @Basic
    @Column(name="weight")
    private Integer weight;

    @Basic
    @Column(name="status")
    private boolean status;

    @Basic
    @Column(name="created_at",nullable = false)
    private Date createdAt;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "client_id", referencedColumnName = "id")
    private ClientEntity client;
}
