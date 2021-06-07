package com.person.model;

import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.media.Schema.AccessMode;
import java.time.LocalDate;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import javax.persistence.SequenceGenerator;
import lombok.Data;
import lombok.NoArgsConstructor;

@Schema(description = "The person entity")
@Entity
@Data
@NoArgsConstructor
public class Person {

  @Schema(description = "identifier")
  @Id
  @SequenceGenerator(name = "hibernate_sequence", sequenceName = "hibernate_sequence",
  allocationSize = 1, initialValue = 1)
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "hibernate_sequence")
  private Integer id;
  @Schema(description = "name", example = "Bill")
  private String name;
  @Schema(description = "Created date")
  @Column(insertable = true, updatable = false)
  private LocalDate created;
  @Schema(description = "Modified date")
  private LocalDate modified;

  @PrePersist
  void onCreate(){
    this.created = LocalDate.now();
    this.modified = LocalDate.now();
  }

  @PreUpdate
  public void onUpdate(){
    this.modified = LocalDate.now();
  }
}
