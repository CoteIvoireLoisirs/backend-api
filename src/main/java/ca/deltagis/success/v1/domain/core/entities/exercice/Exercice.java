package ca.deltagis.success.v1.domain.core.entities.exercice;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.Transient;
import lombok.*;
import java.sql.Timestamp;
import java.util.*;
import com.fasterxml.jackson.annotation.JsonProperty;
import ca.deltagis.success.v1.domain.core.entities.user.User;
import ca.deltagis.success.v1.domain.core.models.exercice.ExercicePeriode;
import ca.deltagis.success.v1.domain.core.models.exercice.ExerciceStatus;
import ca.deltagis.success.v1.domain.ports.out.AbstractCommonEntity;
import ca.deltagis.success.v1.infrastructure.schema.Property;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Table(name = "exercice")

public class Exercice extends AbstractCommonEntity<Exercice> {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true)
    private UUID uuid = UUID.randomUUID();

    @Column(unique = true, nullable = false)
    private String code;

    @Column(nullable = false)
    private String name;

   @Property(required = Property.Value.TRUE)
    private Date startDay;
    
    @Property(required = Property.Value.TRUE)
    private Date endDay;

    @Column(nullable = false)
    private String projectCode;

    @Column()

    // private Date projectStart;

    // private Date beginning;

    // private Date end;

    // private Integer numberPeriode;
    
   
    

    private Boolean closing;

    @Enumerated(EnumType.STRING)
    private ExerciceStatus status = ExerciceStatus.PENDING;


    // @Enumerated(EnumType.STRING)
    // private ExercicePeriode labelPeriode = ExercicePeriode.QUARTER;

    // @Column(name = "owner_id", nullable = false)
    // private Long ownerId;

    @JsonProperty
    @Transient
    private User owner;

    /**
     * * START REQUIRED AbstractCommonEntity
     */

    private Long updateBy; // from AbstractCommonEntity

    private Timestamp created; // from AbstractCommonEntity

    private Timestamp updated; // from AbstractCommonEntity

    private Timestamp deleted; // from AbstractCommonEntity

    @Override
    public String getDisplayName() {
        return name;
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
        uuid = UUID.randomUUID();
        // deleted = currentTimestamp ;
    }

    @Override
    public int compareTo(Exercice object) {
        return object.getName().compareTo(name);
    }
    /**
     * * END REQUIRED AbstractCommonEntity
     */

}
