package ca.deltagis.success.v1.domain.core.entities.project.specificTaxes;
import java.sql.Timestamp;
import java.util.UUID;

import com.fasterxml.jackson.annotation.JsonBackReference;

import ca.deltagis.success.v1.domain.core.entities.project.Project;
import ca.deltagis.success.v1.domain.ports.out.AbstractCommonEntity;
import ca.deltagis.success.v1.infrastructure.schema.Property;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "project_specific_taxes")
public class ProjectSpecificTaxes extends AbstractCommonEntity<ProjectSpecificTaxes> {
    @Id
     @GeneratedValue(strategy = GenerationType.IDENTITY)
     private Long id;

     @Property(required = Property.Value.TRUE)
     @Column
     private String designation;

     @Property(required = Property.Value.TRUE)
     @Column
     private Boolean isTaxable = false;

     @ManyToOne
     @JoinColumn(name="project_code")
     @JsonBackReference
     private Project project;



     //****Abstract common entity fields */

      private UUID uuid; 

      private Long updateBy; 

      private Timestamp created; 

      private Timestamp updated; 

      private Timestamp deleted; 

    @Override
    public String getDisplayName() {
        return designation;
    }

    @Override
    public Long getUpdateBy() {
        return updateBy;
    }

    @Override
    public void setAutoFields() {
        Timestamp currentTimestamp = new Timestamp(System.currentTimeMillis());
        created = currentTimestamp;
        updated = currentTimestamp;
    }


    @Override
    public int compareTo(ProjectSpecificTaxes object) {
        return object.getDesignation().compareTo(designation);
    }

   
 }
