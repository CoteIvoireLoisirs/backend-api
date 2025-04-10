package com.erastedev.ciexplore.v1.domain.entities.currency;

import java.sql.Timestamp;
import java.util.UUID;

import com.erastedev.ciexplore.v1.domain.ports.out.AbstractCommonEntity;
import com.erastedev.ciexplore.v1.infrastructure.schema.Property;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.*;


@Entity
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Setter
@Table(name = "currency")
public class Currency extends AbstractCommonEntity<Currency> {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Property(required = Property.Value.TRUE)
    @Column(unique = true, nullable = false)
    private String code;

    private String name;

    private UUID uuid; // from AbstractCommonEntity

    private Long updateBy; // from AbstractCommonEntity

    private Timestamp created; // from AbstractCommonEntity

    private Timestamp updated; // from AbstractCommonEntity

    private Timestamp deleted; // from AbstractCommonEnt

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
    public int compareTo(Currency object) {
        return object.getName().compareTo(name);
    }
}
