// 文件中的${}参数会被替换，属性（Object variable）会根据表字段循环生成；其中 className为类名，createTime为创建时间，tableName为表名， columnComments为字段注释， columnName为字段名
package ${packagePath}.entity;

import lombok.*;
import javax.persistence.Column;
import javax.persistence.Table;
import java.util.Date;
import java.lang.String;
import java.lang.Integer;

/**
 * @author
 * @Classname ${className}
 * @Description ${tableNameCn}
 * @Date ${createTime}
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Table(name = "${tableName}")
public class ${className} {

    /**
     * ${columnComments}
     */
    @Column(name = "${columnName}")
    private ${propertyType} ${propertyName};

}
