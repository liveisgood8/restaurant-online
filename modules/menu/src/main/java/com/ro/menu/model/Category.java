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
  @GeneratedValue
  private Long id;

  @NotEmpty
  @Size(max = 128)
  @Column(length = 128, nullable = false)
  private String name;

  @JsonIgnore
  @Column
  @Basic(fetch = FetchType.LAZY)
  private String imagePath;

  @Transient
  private String imageUrl;

  @JsonManagedReference
  @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true, mappedBy = "category")
  private List<Dish> dishes;
}
