package ca.deltagis.success.v1.domain.core.entities.billetage;

import java.sql.Timestamp;
import java.util.UUID;

import com.fasterxml.jackson.annotation.JsonProperty;

import ca.deltagis.success.v1.domain.core.entities.user.User;
import ca.deltagis.success.v1.domain.core.models.billetage.BilletageStatus;
import ca.deltagis.success.v1.domain.ports.out.AbstractCommonEntity;
import jakarta.persistence.Column;
import ca.deltagis.success.v1.infrastructure.schema.Property;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
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
@Table(name = "billetage")
public class Billetage extends AbstractCommonEntity<Billetage>{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Property(required = Property.Value.TRUE)
    @Column(unique = false, nullable = false)
    private String currencyCode;
    @Column(length = 255) 
    private String description;
    @Column
    private  Long valeur;

     @Enumerated(EnumType.STRING)
    private BilletageStatus type= BilletageStatus.BILLET ;

    private UUID uuid; // from AbstractCommonEntity

    private Long updateBy; // from AbstractCommonEntity

    private Timestamp created; // from AbstractCommonEntity

    private Timestamp updated; // from AbstractCommonEntity

    private Timestamp deleted; // from AbstractCommonEntity

    @JsonProperty
    @Transient
    private User owner;

   
    public String getDisplayDescription() {
        return description;
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
    }

    @Override
    public int compareTo(Billetage object) {
        return object.getDisplayDescription().compareTo(description);
    }

    @Override
    public String getDisplayName() {
        // TODO Auto-generated method stub
       return description;
    }




}
