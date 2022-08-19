package com.mavenr;

import lombok.*;
import javax.persistence.Column;
import javax.persistence.Table;
import java.util.Date;
import java.lang.String;
import java.lang.Integer;

/**
* @author
* @Classname CountrylanguageEntity
* @Description TODO
* @Date 2022-08-19 14:17:04
*/
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Table(name = "countrylanguage")
public class CountrylanguageEntity {

/**
* CountryCode
*/
@Column(name = "CountryCode")
private char CountryCode;


/**
* Language
*/
@Column(name = "Language")
private char Language;


/**
* IsOfficial
*/
@Column(name = "IsOfficial")
private enum IsOfficial;


/**
* Percentage
*/
@Column(name = "Percentage")
private float Percentage;


}