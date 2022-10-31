// 文件中的${}参数会被替换，属性（Object variable）会根据表字段循环生成；其中 className为类名，createTime为创建时间，tableName为表名， columnComments为字段注释， columnName为字段名
package ${packagePath}.mapper;

import ${packagePath}.entity.${entityClassName};
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * @author
 * @Classname ${className}
 * @Description TODO
 * @Date ${createTime}
 */
@Mapper
public interface ${className} {

    /**
     * 通用--动态查询数据
     * @param ${entityClassPropertyName}
     * @return
     */
    List<${entityClassName}> selectByModel(${entityClassName} ${entityClassPropertyName});

    /**
     * 通用--动态插入数据
     * @param ${entityClassPropertyName}
     * @return
     */
    int insertSelective(${entityClassName} ${entityClassPropertyName});

    /**
     * 通用--批量插入数据
     * @param insertList
     * @return
     */
    int insertList(List<${entityClassName}> insertList);

    /**
     * 动态更新--根据主键更新
     * @param ${entityClassPropertyName}
     * @return
     */
    int updateByPrimaryKeySelective(${entityClassName} ${entityClassPropertyName});

}
