package com.person.model;

import java.time.LocalDate;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
public class Person {

  @Id
 // @GeneratedValue(strategy = GenerationType.AUTO)
  private Integer id;
  private String name;
  @Column(insertable = true, updatable = false)
  private LocalDate created;
  private LocalDate modified;

  @PrePersist
  void onCreate(){
    this.created = LocalDate.now();
    this.modified = LocalDate.now();
  }

  @PreUpdate
  void onUpdate(){
    this.modified = LocalDate.now();
  }
}
