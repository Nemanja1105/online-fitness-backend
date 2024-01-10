package org.unibl.etf.models.entities;

import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@ToString
@EqualsAndHashCode(of = {"id"})
@NoArgsConstructor
@AllArgsConstructor
@Entity(name = "advisor")
public class AdvisorEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="id",nullable=false)
    private Long id;

    @Basic
    @Column(name = "username",nullable = false,length=50,unique = true)
    private String username;

    @Basic
    @Column(name = "password",nullable = false,length=512)
    private String password;

    @Basic
    @Column(name="name",nullable = false)
    private String name;

    @Basic
    @Column(name="surname",nullable = false)
    private String surname;

    @Basic
    @Column(name="email",nullable = false)
    private String email;

    @Basic
    @Column(name="status",nullable = false)
    private boolean status=true;
}
