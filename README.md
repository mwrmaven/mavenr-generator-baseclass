# 代码生成工具
* **介绍**
> 根据配置的数据库信息，扫描数据库，自动生成javaweb项目中entity、vo、service、mapper以及数据库xml映射文件的工具
* **工具执行的依赖环境**
> Java8
* **配置文件中参数的说明**
> database.type 指定数据库类型，当前支持的数据库类型包括 oracle、mysql；  
> database.address 指定数据库的host地址；  
> database.port 指定数据库的端口号；  
> database.dbName 指定数据库名称；  
> database.username 指定数据库的用户名；
> database.password 指定数据库的密码；  
> database.tableName 指定要扫描的表名；  
> &ensp;&ensp;&ensp;&ensp;（**注意：** 若database.scanAllTables=true，则该条不生效）  
> database.tableNameCn 指定要扫描的表名的注释  
> database.scanAllTables 是否扫描库中所有的表，true代表扫描全库，false代表只扫描database.tableName指定的表；  
> database.outPath 生成的类文件的输出路径，默认为当前路径 ./  
> database.packagePath 类的基础包路径（即entity、vo、server之前的包路径）
* **配置文件示例**
> database.type=mysql  
> database.address=localhost  
> database.port=3306  
> database.dbName=test_project  
> database.username=root  
> database.password=test  
> database.tableName=test_table  
> database.tableNameCn=测试表  
> database.scanAllTables=false  
> database.outPath=./baseClass  
> database.packagePath=com.generator  
* **java命令行运行工具**
> java -jar base-class-generator.jar  
> &ensp;&ensp;&ensp;&ensp;命令行执行默认读取jar包同目录下的conf.properties配置文件  
> java -jar base-class-generator.jar -conf ./conf/conf.properties  
> &ensp;&ensp;&ensp;&ensp;若想要指定配置文件，则添加命令行参数 -conf；  
> &ensp;&ensp;&ensp;&ensp;如果不需要lombok注解，可以在命令行中添加 -nolombok；  
> &ensp;&ensp;&ensp;&ensp;如果不需要swagger注解，可以在命令行中添加 -noswagger；  
> **注意：** 命令行执行会在jar包同目录下生成logs日志文件夹
* **生成的代码格式示例如下**
> java代码示例
``` 
package com.generator.entity;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ApiModel(value = "用户表实例")
public class User {

    @ApiModelProperty(value = "用户ID")
    private Integer id;

    @ApiModelProperty(value = "用户名")
    private String username;
}
```
> Mapper xml映射文件示例  
```
<?xml version="1.0" encoding="UTF-8" ?>
<!DOCTYPE mapper PUBLIC "-//mybatis.org//DTD Mapper 3.0//EN" "http://mybatis.org/dtd/mybatis-3-mapper.dtd" >
<mapper namespace="com.generator.mapper.UserMapper">

    <sql id="All_Columns">
        id, username
    </sql>

    <!-- 通用——动态插入数据 -->
    <insert id="insertSelective" parameterType="com.generator.entity.User">
        insert into user
        <trim prefix="(" suffix=")" suffixOverrides=",">
            <if test="id != null">
                id,
            </if>
            <if test="username != null">
                username,
            </if>
        </trim>
        <trim prefix="(" suffix=")" suffixOverrides=",">
            <if test="id != null">
                #{id},
            </if>
            <if test="username != null">
                #{username},
            </if>
        </trim>
    </insert>
</mapper>
```