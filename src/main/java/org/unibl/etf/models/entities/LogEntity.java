package org.unibl.etf.models.entities;

import jakarta.persistence.*;
import lombok.*;
import org.unibl.etf.models.enums.Difficulty;
import org.unibl.etf.models.enums.LogLevel;

import java.util.Date;

@Getter
@Setter
@ToString
@EqualsAndHashCode(of = {"id"})
@NoArgsConstructor
@AllArgsConstructor
@Entity(name = "log")
public class LogEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="id",nullable=false)
    private Long id;

    @Basic
    @Column(name="description",nullable = false)
    private String description;

    @Basic
    @Column(name="date",nullable = false)
    private Date date;

    @Basic
    @Column(name = "level", nullable = false)
    @Enumerated(EnumType.ORDINAL)
    private LogLevel level;
}
