package com.mavenr;

import lombok.*;
import javax.persistence.Column;
import javax.persistence.Table;
import java.util.Date;
import java.lang.String;
import java.lang.Integer;

/**
* @author
* @Classname CityEntity
* @Description TODO
* @Date 2022-08-19 14:17:02
*/
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Table(name = "city")
public class CityEntity {

/**
* ID
*/
@Column(name = "ID")
private int ID;


/**
* Name
*/
@Column(name = "Name")
private char Name;


/**
* CountryCode
*/
@Column(name = "CountryCode")
private char CountryCode;


/**
* District
*/
@Column(name = "District")
private char District;


/**
* Population
*/
@Column(name = "Population")
private int Population;


}