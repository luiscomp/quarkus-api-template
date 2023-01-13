package com.logicsoftware.models;

import java.time.ZonedDateTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.Filter;
import org.hibernate.annotations.FilterDef;
import org.hibernate.annotations.FilterDefs;
import org.hibernate.annotations.Filters;
import org.hibernate.annotations.ParamDef;
import org.hibernate.annotations.UpdateTimestamp;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@EqualsAndHashCode(callSuper = false)
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "User", schema = "flashlight")
@FilterDefs({
    @FilterDef(name = "name", parameters = @ParamDef(name = "name", type = "string")),
    @FilterDef(name = "email", parameters = @ParamDef(name = "email", type = "string"))
})
@Filters({
    @Filter(name = "name", condition = "name = :name"),
    @Filter(name = "email", condition = "email = :email")
})
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(unique = true)
    private String email;
    
    @CreationTimestamp
    public ZonedDateTime createdAt;

    @UpdateTimestamp
    public ZonedDateTime updatedAt;
}
