package sample;

/** */
@javax.annotation.Generated(value = { "Doma", "1.27.0" }, date = "2018-08-12T14:47:08.712+0900")
public class MessageDaoImpl extends org.seasar.doma.internal.jdbc.dao.AbstractDao implements sample.MessageDao {

    static {
        org.seasar.doma.internal.Artifact.validateVersion("1.27.0");
    }

    /**
     * @param config the config
     */
    @javax.inject.Inject()
    public MessageDaoImpl(org.seasar.doma.jdbc.Config config) {
        super(config);
    }

    @Override
    public sample.Message select(java.lang.Long id) {
        entering("sample.MessageDaoImpl", "select", id);
        try {
            org.seasar.doma.internal.jdbc.query.SqlFileSelectQuery __query = new org.seasar.doma.internal.jdbc.query.SqlFileSelectQuery();
            __query.setConfig(config);
            __query.setSqlFilePath("META-INF/sample/MessageDao/select.sql");
            __query.addParameter("id", java.lang.Long.class, id);
            __query.setCallerClassName("sample.MessageDaoImpl");
            __query.setCallerMethodName("select");
            __query.setResultEnsured(false);
            __query.setQueryTimeout(-1);
            __query.setMaxRows(-1);
            __query.setFetchSize(-1);
            __query.prepare();
            org.seasar.doma.internal.jdbc.command.SelectCommand<sample.Message> __command = new org.seasar.doma.internal.jdbc.command.SelectCommand<sample.Message>(__query, new org.seasar.doma.internal.jdbc.command.EntitySingleResultHandler<sample.Message>(sample._Message.getSingletonInternal()));
            sample.Message __result = __command.execute();
            __query.complete();
            exiting("sample.MessageDaoImpl", "select", __result);
            return __result;
        } catch (java.lang.RuntimeException __e) {
            throwing("sample.MessageDaoImpl", "select", __e);
            throw __e;
        }
    }

}
