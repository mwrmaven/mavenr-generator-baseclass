package com.mavenr;

import lombok.*;
import javax.persistence.Column;
import javax.persistence.Table;
import java.util.Date;
import java.lang.String;
import java.lang.Integer;

/**
* @author
* @Classname CountryEntity
* @Description TODO
* @Date 2022-08-19 14:17:04
*/
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Table(name = "country")
public class CountryEntity {

/**
* Code
*/
@Column(name = "Code")
private char Code;


/**
* Name
*/
@Column(name = "Name")
private char Name;


/**
* Continent
*/
@Column(name = "Continent")
private enum Continent;


/**
* Region
*/
@Column(name = "Region")
private char Region;


/**
* SurfaceArea
*/
@Column(name = "SurfaceArea")
private float SurfaceArea;


/**
* IndepYear
*/
@Column(name = "IndepYear")
private smallint IndepYear;


/**
* Population
*/
@Column(name = "Population")
private int Population;


/**
* LifeExpectancy
*/
@Column(name = "LifeExpectancy")
private float LifeExpectancy;


/**
* GNP
*/
@Column(name = "GNP")
private float GNP;


/**
* GNPOld
*/
@Column(name = "GNPOld")
private float GNPOld;


/**
* LocalName
*/
@Column(name = "LocalName")
private char LocalName;


/**
* GovernmentForm
*/
@Column(name = "GovernmentForm")
private char GovernmentForm;


/**
* HeadOfState
*/
@Column(name = "HeadOfState")
private char HeadOfState;


/**
* Capital
*/
@Column(name = "Capital")
private int Capital;


/**
* Code2
*/
@Column(name = "Code2")
private char Code2;


}