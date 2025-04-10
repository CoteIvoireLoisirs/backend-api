package ca.deltagis.success.v1.domain.core.entities.company;

import java.sql.Timestamp;
import java.util.UUID;

import com.fasterxml.jackson.annotation.JsonProperty;

import ca.deltagis.success.v1.domain.core.entities.user.User;
import ca.deltagis.success.v1.domain.ports.out.AbstractCommonEntity;
import ca.deltagis.success.v1.infrastructure.schema.Property;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
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
@Table(name = "company")
public class Company extends AbstractCommonEntity<Company> {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Property(required = Property.Value.TRUE)
    @Column(unique = true, nullable = false)
    private String workspaceCode;

    private String country;

    private String name;

    private String acronym;

    private String address;

    private String zipCode;

    private String city;

    private String phoneNumber;

    private String fax;

    private String email;

    private String insae;

    private String ifu;

    private String contact;

    private String activity;

    private String imageUrl;

    private String salaryTax;

    private String ipts;

    private String socialSecurity;

    private String imagePath;

    private UUID uuid; // from AbstractCommonEntity

    private Long updateBy; // from AbstractCommonEntity

    private Timestamp created; // from AbstractCommonEntity

    private Timestamp updated; // from AbstractCommonEntity

    private Timestamp deleted; // from AbstractCommonEntity

    @JsonProperty
    @Transient
    private User owner;

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
    }

    @Override
    public int compareTo(Company object) {
        return object.getName().compareTo(name);
    }

    
}
