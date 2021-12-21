package com.maven;

import com.clamc.report.submit.business.anno.SubmitReportColumn;
import com.clamc.report.submit.business.anno.SubmitReportTable;
import com.clamc.report.submit.business.constant.SystemParaConstant;
import com.clamc.report.submit.business.sysenum.FieldType;
import com.clamc.report.submit.business.sysenum.FieldValueWay;
import lombok.Data;
import lombok.ToString;
import java.util.Date;

@Data
@ToString
@SubmitReportTable(tableName = "t", reportName = "", startRow = , endRow = , insertType = "ALL")
public class TBO {

    @SubmitReportColumn(fieldName = "id", fieldChineseName = "id", fieldType = FieldType.int, columnIndex = )
    private Integer id;

    @SubmitReportColumn(fieldName = "name", fieldChineseName = "name", fieldType = FieldType.varchar, columnIndex = )
    private String name;

    @SubmitReportColumn(fieldName = "age", fieldChineseName = "age", fieldType = FieldType.int, columnIndex = )
    private Integer age;

}