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
> &ensp;&ensp;&ensp;&ensp;若想要指定配置文件，则添加命令行参数 -conf  
> **注意：** 命令行执行会在jar包同目录下生成logs日志文件夹
