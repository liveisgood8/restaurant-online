package com.ro.menu.model;

import com.fasterxml.jackson.annotation.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;
import java.sql.Timestamp;
import java.util.List;

@Getter
@Setter
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id", scope = Category.class)
@Entity
@Table(name = "categories", uniqueConstraints = {
    @UniqueConstraint(columnNames = "name")
})
public class Category {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(name = "name", nullable = false, length = 64)
  private String name;

  @Column(name = "archived", nullable = false, columnDefinition = "smallint not null default 0")
  private Boolean archived;

  @Column(name = "image_path")
  @Basic(fetch = FetchType.LAZY)
  private String imagePath;

  @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true, mappedBy = "category")
  private List<Dish> dishes;
}
