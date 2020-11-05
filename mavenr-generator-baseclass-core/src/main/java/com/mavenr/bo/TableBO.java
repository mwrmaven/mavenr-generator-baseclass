package com.mavenr.bo;

import com.mavenr.entity.Table;
import lombok.*;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class TableBO {

    private String packagePath;

    private List<Table> tableList;
}
