package com.schedulebackend.database.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@JsonIgnoreProperties(ignoreUnknown = true)
@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "teachers")
public class Teacher {
    @Id
    @JsonIgnore
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Expose
    @SerializedName("first_name")
    @JsonProperty("first_name") // устанавливает соответствие с полем JSON
    private String firstname;
    @Expose
    @SerializedName("mid_name")
    @JsonProperty("mid_name") // устанавливает соответствие с полем JSON
    private String midname;
    @Expose
    @SerializedName("last_name")
    @JsonProperty("last_name") // устанавливает соответствие с полем JSON
    private String lastname;
    @Expose
    @SerializedName("id")
    @JsonProperty("id")
    @Column(name = "external_id")
    private Integer externalID;

    public Teacher(Integer externalID) {
        this.externalID = externalID;
    }
}
