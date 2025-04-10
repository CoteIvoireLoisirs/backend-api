package ca.deltagis.success.v1.domain.core.entities.project;

import java.sql.Timestamp;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;

import ca.deltagis.success.v1.domain.core.entities.currency.Currency;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.fasterxml.jackson.annotation.JsonProperty;

import ca.deltagis.success.v1.domain.core.entities.project.specificTaxes.ProjectSpecificTaxes;
import ca.deltagis.success.v1.domain.core.entities.user.User;
import ca.deltagis.success.v1.domain.core.entities.workspace.Workspace;
import ca.deltagis.success.v1.domain.core.models.project.AccountingSystem;
import ca.deltagis.success.v1.domain.core.models.project.ProjectStatus;
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
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
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
@Table(name = "project")
public class Project extends AbstractCommonEntity<Project> {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(cascade = {CascadeType.ALL}, optional = true)
    private ProjectSetting settings;

    @Property(required = Property.Value.TRUE)
    @Enumerated(EnumType.STRING)
    private ProjectType type;

    @Property(required = Property.Value.TRUE)
    @Column(unique = true, nullable = false)
    private String code;

    @Property(required = Property.Value.TRUE)
    @Column
    private String libelle;

    private String projectNumber;

    @Column
    private String name;

    private String imagePath = null;

    @Transient
    String imageUrl = null;

     @Property(required = Property.Value.TRUE)
     @Column
     protected Date startDate;

    @Property(required = Property.Value.TRUE)
     @Column
     protected Date endDate;

    // @Property(required = Property.Value.TRUE)
    // @Column
    // protected float duration;

    @Property(required = Property.Value.TRUE)
    @Column
    // TODO : must be Currency Entity
    protected String localCurrency;

   
    // @ManyToMany
    // @JoinTable(
    // name = "project_currency",
    // joinColumns = @JoinColumn(name = "project_id", referencedColumnName = "id"),
    // inverseJoinColumns = @JoinColumn(name = "currency_id", referencedColumnName = "id")
    // )

    // private Set<Currency> currencies = new HashSet<>();

    @Property(required = Property.Value.TRUE)
    @Column
    protected int formatQuantity;

    @Property(required = Property.Value.TRUE)
    @Column
    protected int formatAmount;

    @Property(required = Property.Value.TRUE)
    @Column
    protected int formatRate;

    // TODO : @Convert(converter = CurrencyConverter.class)
    // protected Set<Currency> otherCurrencies = new HashSet<>();
    protected Set<String> otherCurrencies = new HashSet<>();

    @Enumerated(EnumType.STRING)
    private ProjectStatus status = ProjectStatus.PENDING;

    // @Column(name = "owner_id", nullable = false)
    // private Long ownerId;
    @JsonProperty
    @Transient
    private User owner;

    @JsonProperty
    @Transient
    private Set<Project> projects;

    @Enumerated(EnumType.STRING)
    private AccountingSystem accountingSystem = AccountingSystem.OTHER;

    @Column
    private boolean costabUse = false;

    @Column
    private boolean budgetTrackingUse = false;

    @Column
    private boolean sigfipUse = false;

    @Column
    private boolean paymentOrderUse = false;

    @Column
    private boolean wasfUse = false;

    @Column
    private boolean taxAccountingUse;

    @Column
    private String workspaceCode;

    @Column
    private String contact;

    @Column
    private String email;

    @Column
    private String country;

    @Column
    private String city;

    @Column
    private String adress;

    @JsonProperty
    @Transient
    private List<ProjectSpecificTaxes> taxes;

    @Transient
    @ManyToOne
    private Workspace workspace;

    @OneToMany(mappedBy = "project", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference
    private Set<ProjectSpecificTaxes> projectSpecificTaxes;

    @Transient
    private boolean favorite = false;

    public Set<String> getCurrencies() {
        Set<String> currencies = new HashSet<>();
        currencies.add(this.localCurrency);

        if (otherCurrencies != null) {
            currencies.addAll(this.otherCurrencies);
        }

        return currencies;
    }

    private UUID uuid; // from AbstractCommonEntity

    private Long updateBy; // from AbstractCommonEntity

    private Timestamp created; // from AbstractCommonEntity

    private Timestamp updated; // from AbstractCommonEntity

    private Timestamp deleted; // from AbstractCommonEntity

    @Override
    public String getDisplayName() {
        return projectNumber;
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
    public int compareTo(Project object) {
        return object.getName().compareTo(projectNumber);
    }
}