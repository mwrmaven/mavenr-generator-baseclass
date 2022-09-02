// 文件中的${}参数会被替换，属性（Object variable）会根据表字段循环生成；其中 className为类名，createTime为创建时间，tableName为表名， columnComments为字段注释， columnName为字段名
package ${packagePath}.service.impl;

import ${packagePath}.entity.${entityClassName};
import ${packagePath}.mapper.${mapperClassName};
import ${packagePath}.service.${serviceClassName};
import lombok.extern.slf4j.Slf4j;
import org.springframeword.beans.factory.annotation.Autowired;
import org.springframeword.stereotype.Service;

import java.util.List;

/**
 * @author
 * @Classname ${className}
 * @Description TODO
 * @Date ${createTime}
 */
@Slf4j
@Service
public class ${className} implements ${serviceClassName}{

    @Autowired
    private ${mapperClassName} ${mapperClassPropertyName};

    /**
     * 通用--动态查询数据
     * @param ${entityClassPropertyName}
     * @return
     */
    @Override
    public List<${entityClassName}> seletByModel(${entityClassName} ${entityClassPropertyName}) {
        return ${mapperClassPropertyName}.selectByModel(${entityClassPropertyName});
    }

    /**
     * 通用--动态插入数据
     * @param ${entityClassPropertyName}
     * @return
     */
    @Override
    public int insertSelective(${entityClassName} ${entityClassPropertyName}) {
        return ${mapperClassPropertyName}.insertSelective(${entityClassPropertyName});
    }

    /**
     * 通用--批量插入数据
     * @param insertList
     * @return
     */
    @Override
    public int insertList(List<${entityClassName}> insertList) {
        return ${mapperClassPropertyName}.insertList(insertList);
    }

    /**
     * 动态更新--根据主键更新
     * @param ${entityClassPropertyName}
     * @return
     */
    @Override
    public int updateByPrimaryKeySelective(${entityClassName} ${entityClassPropertyName}) {
        return ${mapperClassPropertyName}.updateByPrimaryKeySelective(${entityClassPropertyName});
    }

}