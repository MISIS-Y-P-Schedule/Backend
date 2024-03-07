package com.schedulebackend.database.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import jakarta.persistence.*;
import lombok.Data;
import lombok.ToString;

@Data
@Entity
@Table(name = "classrooms")
@JsonIgnoreProperties(ignoreUnknown = true)
@ToString
public class Classroom {
    @Id
    @JsonIgnore
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Expose
    @SerializedName("name")
    @JsonProperty("name") // устанавливает соответствие с полем JSON
    private String name;

    @Expose
    @SerializedName("id")
    @JsonProperty("id")
    @Column(name = "external_id")
    private Integer externalID;

}
