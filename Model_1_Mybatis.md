#1、Mybatis动态sql是做什么的？都有哪些动态sql？简述一下动态sql的执行原理？
	1、动态SQL通常是用来根据不同的需求完成不同的任务。
	2、MyBatis的动态SQL是基于OGNL表达式的，它可以帮助我们方便的在SQL语句中实现某些逻辑。mybatis中用于实现动态sql的元素主要有：if、choose（when、otherwise）、trim、where、set、foreach、bind等。
	3、执行原理为：使用OGNL从sql参数对象中计算表达式的值，根据表达式的值动态的拼接sql，以此来完成动态sql的功能。
	4、sql的解析过程为：
		 a、首先调用Resources的getResourcesAsSteam方法，将我们的xml文件转化为输入流。
		 b、调用SqlSessionFactoryBuild的build方法，解析配置文件。
				解析配置文件使用的XMLConfigBuilder去解析的，解析后，将数据封装在Configuration对象中。
		 c、解析映射文件是使用XMLMapperBuilder去解析的。解析后封装在configuration中的mappedStatements中。
		 d、然后调用使用SQLSessionFactory的openSession方法，获取SqlSession。最后由SQLSession去取执行sql。

#2、Mybatis是否支持延迟加载？如果支持，它的实现原理是什么？
	1、mybatis是支持延迟加载的。延迟加载是指：在真正使用数据的时候才发起查询，不用的时候，不查询关联的数据。延迟加载又叫懒加载。
	2、延迟加载的执行原理为：
		a、在mybatis的配置文件中，配置开启全局的懒加载<setting name="lazyLoadingEnabled" value="true">
		b、配置mapper及实体对象
		c、在执行查询的时候，从查询结果集解析数据到实体对象中，当数据解析到判断需要执行关联查询时。因lazyLoadingEnabled=true，所以将创建延迟加载对象对应的Proxy延迟执行对象lazyLoader，并保存。
		d、当逻辑触发lazyLoadTriggerMethods对应的方法时，则执行延迟加载。
		e、如果aggressiveLazyLoading=true，则只要触发到对象的任何方法，就会立即加载所有属性的加载。
	3、延迟加载主要是通过动态代理实现的，通过代理拦截到指定的方法，执行加载数据。
		
#3、Mybatis都有哪些Executor执行器？它们之间的区别是什么？
	1、Mybatis主要有BaseExecutor、SimpleExecutor、ReuseExecutor、CachingExecutor、BatchExecutor。Mybatis默认的Executor是SimpleExecutor。
	2、BaseExecutor是一个抽象类，采用模板方法的设计模式，实现了Executor的基本功能。其构造方法默认支持一级缓存。而insert、update、delete操作都会调用update方法，调用此方法时，会清空一级缓存（调用clearLocalCache()方法）。查询query方法会先在缓存中查询，缓存命中失败后再去数据中查询。
	3、SimpleExecutor实现了BaseExcutor，主要实现是doUpdate/doQuery/doFlushStatements。根据对应的sql直接执行，不会做一些额外的操作。拼接为sql后就交给StatementHanddler去执行了。
	4、BatchExcutor通过批量操作来优化性能。
	5、ReuserExecutor，可重用的执行器，重用的对象是Statement，也就是说该执行器会缓存同一个sql的statement，省去Statement的重新创建，优化性能。内部的实现是通过一个hashMap来维护Statement对象的。由于当前Map只在该Session中有效，使用完成后记得调用flushStatement来清除map
	6、CachingExecutor，启用于二级缓存时的执行器。采用静态代理，代理一个Executor对象。执行update方法前判断是否清空二级缓存。执行query方法前先做二级缓存中查询，命中失败再通过被代理类查询。
	

#4、简述下Mybatis的一级、二级缓存（分别从存储结构、范围、失效场景。三个方面来作答）？
	1、一级缓存有SqlSession级别的和Statement级别的。SQLSession级别的实现同一个会话中的数据共享。Statement级别的可以理解为只对当前执行的这个Statement有效，执行完后就清空缓存。底层HashMap的键对象根据SQL的ID，参数，SQL本身，分页参数以及JDBC的参数信息构成。执行query方法时，先会在一级缓存中查询，查询不到则会去数据库中查询，查询出来后会存入一级缓存中后返回。在执行update、insert、delete或者close方法后，会清空缓存。Mybatis的一级缓存最大范围是SQLSession内部，有多个SQLSession或者分布式的环境下，数据库读写操作会引起脏数据，建议设定缓存为Statement级别的。
	2、二级缓存底层也是一个HashMap的数据结构，是SQLSessionFactory级别的，实现不同会话中数据的共享，是一个全局变量。存储作用域为Mapper的namespace级别。当SqlSession执行update、insert、delete操作后，会清空二级缓存。二级缓存需要我们手动开启在mybatis的配置文件配置：<setting name="cacheEnabled" value="true"/> ，在mapper配置文件添加<cache></cache>,并且需要实体对象实现Serializable接口。

#5、简述Mybatis的插件运行原理，以及如何编写一个插件？
	1、运行原理为：
		a、在四大对象Excutor、StatementHandler、ParameterHandler、ResultSetHandler创建对象的时候，每个创建出来的对象都不是直接返回的，而是interceptorChain.pluginAll(parameterHandler)获取到所有的interceptor接口，调用interceptor.plugin(target)返回target包装后的对象。
		b、插件的底层是使用动态代理实现的。
	2、如何编写一个插件：
		a、创建一个类，实现Mybatis的插件接口Interceptor
		b、实现重载Interceptor的intecept、pugin方法，plugin方法生产target的代理对象
		c、重载setProperties方法，传递插件所需要的参数。
		d、在mybatis的配置文件中添加自定义的plugin配置<plugin interceptor="自定义插件类的权限定名">