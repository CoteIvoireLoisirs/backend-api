package com.erastedev.ciexplore.v1.domain.entities.user;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Locale;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserSettings {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @JsonAlias( "keyStyle" )
    @JsonProperty
    private String style;

    @JsonAlias( "keyMessageEmailNotification" )
    @JsonProperty
    private Boolean messageEmailNotification;

    @JsonAlias( "keyMessageSmsNotification" )
    @JsonProperty
    private Boolean messageSmsNotification;

    @JsonAlias( "keyUiLocale" )
    @JsonProperty
    private Locale uiLocale;

    @JsonAlias( "keyDbLocale" )
    @JsonProperty
    private Locale dbLocale;

//    @JsonAlias( "keyAnalysisDisplayProperty" )
//    @JsonProperty
//    private DisplayProperty analysisDisplayProperty;

    @JsonAlias( "keyTrackerDashboardLayout" )
    @JsonProperty
    private String trackerDashboardLayout;
}
