package ca.deltagis.success.v1.domain.core.entities.periode;

import java.sql.Timestamp;
import java.sql.Date;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.github.javafaker.Currency;

import ca.deltagis.success.v1.domain.core.entities.project.ProjectSetting;
import ca.deltagis.success.v1.domain.core.entities.user.User;
import ca.deltagis.success.v1.domain.core.models.project.AccountingSystem;
import ca.deltagis.success.v1.domain.core.models.project.ProjectType;
import ca.deltagis.success.v1.domain.ports.out.AbstractCommonEntity;
import ca.deltagis.success.v1.infrastructure.schema.Property;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import jakarta.persistence.Transient;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;




@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor


@Table(name = "periode")

public class Periode extends AbstractCommonEntity<Periode> {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(cascade = {CascadeType.ALL}, optional = true)
    private ProjectSetting settings;

    @Property(required = Property.Value.TRUE)
    @Column(unique = true, nullable = false)
    private String code;

    @Property(required = Property.Value.TRUE)
    @Enumerated(EnumType.STRING)
    private ProjectType type;

    @Column
    private String name;

     @Property(required = Property.Value.TRUE)
     @Column
     protected Date startDate;

     @Property(required = Property.Value.TRUE)
     @Column
     protected Date endDate;

     @Property(required = Property.Value.TRUE)
     @Column
     private String codeExercice;


     @Property(required = Property.Value.TRUE)
     @Column
     private Boolean closed = false;

     private String periodeNumber;


    

    //  // TODO : @Convert(converter = CurrencyConverter.class)
    //  protected Set<Currency> otherCurrencies = new HashSet<>();

     
    private UUID uuid; // from AbstractCommonEntity

    private Long updateBy; // from AbstractCommonEntity

    private Timestamp created; // from AbstractCommonEntity

    private Timestamp updated; // from AbstractCommonEntity

    private Timestamp deleted; // from AbstractCommonEntity

    


     @Override
     public String getDisplayName() {
         return periodeNumber;
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
     public int compareTo(Periode object) {
         return object.getName().compareTo(periodeNumber);
     }

    



}
