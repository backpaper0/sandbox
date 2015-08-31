package sample;

import javax.annotation.Resource;
import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.inject.Produces;
import javax.sql.DataSource;
import javax.transaction.TransactionSynchronizationRegistry;

import org.seasar.extension.jdbc.JdbcManager;
import org.seasar.extension.jdbc.dialect.DerbyDialect;
import org.seasar.extension.jdbc.manager.JdbcManagerImpl;
import org.seasar.extension.jdbc.meta.ColumnMetaFactoryImpl;
import org.seasar.extension.jdbc.meta.EntityMetaFactoryImpl;
import org.seasar.extension.jdbc.meta.PropertyMetaFactoryImpl;
import org.seasar.extension.jdbc.meta.TableMetaFactoryImpl;
import org.seasar.framework.convention.impl.PersistenceConventionImpl;

@ApplicationScoped
public class JdbcManagerProvider {

    //JdbcManagerImplを構築するのに必要なもののうちDataSourceと
    //TransactionSynchronizationRegistryはサーバーが管理するリソースなので
    //@Resourceでインジェクションしたものを使う。

    @Resource
    private DataSource dataSource;

    @Resource
    private TransactionSynchronizationRegistry syncRegistry;

    /*
     * CDIは@Producesを付けたメソッドが返す値をCDI管理Beanとして扱える。
     * (ちなみに@Producesはフィールドに付ける事もできる。例えば上記のフィールド
     *  dataSourceに@Producesを付けるとDataSourceを@Injectでインジェクションできる)
     * 
     * また、スコープのアノテーションを付けるとそのスコープになる。
     * JdbcManagerはS2Containerではシングルトンで扱っていたのでここでは
     * ApplicationScopedにしている。
     * 
     */
    @Produces
    @ApplicationScoped
    public JdbcManager getJdbcManager() {

        //テーブル名とエンティティ名やカラム名とプロパティ名とフィールド名を
        //対応付けるためのインターフェース。
        //デフォルトではDB側がスネークアッパーケース、Java側がキャメルケース。
        PersistenceConventionImpl persistenceConvention = new PersistenceConventionImpl();

        //ほげほげめたふぁくとり〜たち〜(～ 'ω' )～

        ColumnMetaFactoryImpl columnMetaFactory = new ColumnMetaFactoryImpl();
        columnMetaFactory.setPersistenceConvention(persistenceConvention);

        PropertyMetaFactoryImpl propertyMetaFactory = new PropertyMetaFactoryImpl();
        propertyMetaFactory.setColumnMetaFactory(columnMetaFactory);
        propertyMetaFactory.setPersistenceConvention(persistenceConvention);

        TableMetaFactoryImpl tableMetaFactory = new TableMetaFactoryImpl();
        tableMetaFactory.setPersistenceConvention(persistenceConvention);

        EntityMetaFactoryImpl entityMetaFactory = new EntityMetaFactoryImpl();
        entityMetaFactory.setPersistenceConvention(persistenceConvention);
        entityMetaFactory.setPropertyMetaFactory(propertyMetaFactory);
        entityMetaFactory.setTableMetaFactory(tableMetaFactory);

        //RDBMS毎の差を吸収するためのクラス。
        //とりあえずPayaraにデプロイするつもりなのでDerbyのDialectを選択した。
        DerbyDialect dialect = new DerbyDialect();

        //JdbcManagerのインスタンスを構築して返す。
        JdbcManagerImpl jdbcManager = new JdbcManagerImpl();
        jdbcManager.setDataSource(dataSource);
        jdbcManager.setDialect(dialect);
        jdbcManager.setEntityMetaFactory(entityMetaFactory);
        jdbcManager.setPersistenceConvention(persistenceConvention);
        jdbcManager.setSyncRegistry(syncRegistry);
        return jdbcManager;
    }
}
