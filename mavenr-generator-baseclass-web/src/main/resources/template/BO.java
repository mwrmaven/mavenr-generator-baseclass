// 文件中的${}参数会被替换，属性（Object variable）会根据表字段循环生成；其中 className为类名，createTime为创建时间，tableName为表名， columnComments为字段注释， columnName为字段名
package ${packagePath}.bo;

import ${packagePath}.anno.SuppDataColumn;
import ${packagePath}.anno.SuppDataTable;
import ${packagePath}.bo.model.base.ModelBaseBO;
import ${packagePath}.sysenum.FieldType;
import lombok.*;

/**
 * @author
 * @Classname ${className}
 * @Description ${tableNameCn}
 * @Date ${createTime}
 */
@Data
@EqualsAndHashCode(callSuper = true)
@SuppDataTable(name = "${tableName}", reportName = "${tableName}")
public class ${className} extends ModelBaseBO {

    /**
     * ${columnComments}
     */
    @SuppDataColumn(fieldName = "${columnName}", fieldChineseName = "${columnComments}", fieldType = "${columnType}")
    private ${propertyType} ${propertyName};

}
