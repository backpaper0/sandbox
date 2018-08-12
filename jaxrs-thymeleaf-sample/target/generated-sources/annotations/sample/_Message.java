package sample;

/** */
@javax.annotation.Generated(value = { "Doma", "1.27.0" }, date = "2018-08-12T14:47:08.616+0900")
public final class _Message extends org.seasar.doma.jdbc.entity.AbstractEntityType<sample.Message> {

    static {
        org.seasar.doma.internal.Artifact.validateVersion("1.27.0");
    }

    private static final _Message __singleton = new _Message();

    /** the id */
    public final org.seasar.doma.jdbc.entity.AssignedIdPropertyType<java.lang.Object, sample.Message, java.lang.Long, java.lang.Object> $id = new org.seasar.doma.jdbc.entity.AssignedIdPropertyType<java.lang.Object, sample.Message, java.lang.Long, java.lang.Object>(sample.Message.class, java.lang.Long.class, org.seasar.doma.wrapper.LongWrapper.class, null, null, "id", "id");

    /** the template */
    public final org.seasar.doma.jdbc.entity.BasicPropertyType<java.lang.Object, sample.Message, java.lang.String, java.lang.Object> $template = new org.seasar.doma.jdbc.entity.BasicPropertyType<java.lang.Object, sample.Message, java.lang.String, java.lang.Object>(sample.Message.class, java.lang.String.class, org.seasar.doma.wrapper.StringWrapper.class, null, null, "template", "template", true, true);

    private final org.seasar.doma.jdbc.entity.NullEntityListener __listener;

    private final org.seasar.doma.jdbc.entity.NamingType __namingType;

    private final String __catalogName;

    private final String __schemaName;

    private final String __tableName;

    private final String __qualifiedTableName;

    private final String __name;

    private final java.util.List<org.seasar.doma.jdbc.entity.EntityPropertyType<sample.Message, ?>> __idPropertyTypes;

    private final java.util.List<org.seasar.doma.jdbc.entity.EntityPropertyType<sample.Message, ?>> __entityPropertyTypes;

    private final java.util.Map<String, org.seasar.doma.jdbc.entity.EntityPropertyType<sample.Message, ?>> __entityPropertyTypeMap;

    private _Message() {
        __listener = new org.seasar.doma.jdbc.entity.NullEntityListener();
        __namingType = org.seasar.doma.jdbc.entity.NamingType.NONE;
        __name = "Message";
        __catalogName = "";
        __schemaName = "";
        __tableName = "Message";
        __qualifiedTableName = "Message";
        java.util.List<org.seasar.doma.jdbc.entity.EntityPropertyType<sample.Message, ?>> __idList = new java.util.ArrayList<org.seasar.doma.jdbc.entity.EntityPropertyType<sample.Message, ?>>();
        java.util.List<org.seasar.doma.jdbc.entity.EntityPropertyType<sample.Message, ?>> __list = new java.util.ArrayList<org.seasar.doma.jdbc.entity.EntityPropertyType<sample.Message, ?>>(2);
        java.util.Map<String, org.seasar.doma.jdbc.entity.EntityPropertyType<sample.Message, ?>> __map = new java.util.HashMap<String, org.seasar.doma.jdbc.entity.EntityPropertyType<sample.Message, ?>>(2);
        __idList.add($id);
        __list.add($id);
        __map.put("id", $id);
        __list.add($template);
        __map.put("template", $template);
        __idPropertyTypes = java.util.Collections.unmodifiableList(__idList);
        __entityPropertyTypes = java.util.Collections.unmodifiableList(__list);
        __entityPropertyTypeMap = java.util.Collections.unmodifiableMap(__map);
    }

    @Override
    public org.seasar.doma.jdbc.entity.NamingType getNamingType() {
        return __namingType;
    }

    @Override
    public String getName() {
        return __name;
    }

    @Override
    public String getCatalogName() {
        return __catalogName;
    }

    @Override
    public String getSchemaName() {
        return __schemaName;
    }

    @Override
    public String getTableName() {
        return __tableName;
    }

    @Override
    public String getQualifiedTableName() {
        return __qualifiedTableName;
    }

    @Override
    public void preInsert(sample.Message entity, org.seasar.doma.jdbc.entity.PreInsertContext context) {
        __listener.preInsert(entity, context);
    }

    @Override
    public void preUpdate(sample.Message entity, org.seasar.doma.jdbc.entity.PreUpdateContext context) {
        __listener.preUpdate(entity, context);
    }

    @Override
    public void preDelete(sample.Message entity, org.seasar.doma.jdbc.entity.PreDeleteContext context) {
        __listener.preDelete(entity, context);
    }

    @Override
    public void postInsert(sample.Message entity, org.seasar.doma.jdbc.entity.PostInsertContext context) {
        __listener.postInsert(entity, context);
    }

    @Override
    public void postUpdate(sample.Message entity, org.seasar.doma.jdbc.entity.PostUpdateContext context) {
        __listener.postUpdate(entity, context);
    }

    @Override
    public void postDelete(sample.Message entity, org.seasar.doma.jdbc.entity.PostDeleteContext context) {
        __listener.postDelete(entity, context);
    }

    @Override
    public java.util.List<org.seasar.doma.jdbc.entity.EntityPropertyType<sample.Message, ?>> getEntityPropertyTypes() {
        return __entityPropertyTypes;
    }

    @Override
    public org.seasar.doma.jdbc.entity.EntityPropertyType<sample.Message, ?> getEntityPropertyType(String __name) {
        return __entityPropertyTypeMap.get(__name);
    }

    @Override
    public java.util.List<org.seasar.doma.jdbc.entity.EntityPropertyType<sample.Message, ?>> getIdPropertyTypes() {
        return __idPropertyTypes;
    }

    @Override
    public org.seasar.doma.jdbc.entity.GeneratedIdPropertyType<java.lang.Object, sample.Message, ?, ?> getGeneratedIdPropertyType() {
        return null;
    }

    @Override
    public org.seasar.doma.jdbc.entity.VersionPropertyType<java.lang.Object, sample.Message, ?, ?> getVersionPropertyType() {
        return null;
    }

    @Override
    public sample.Message newEntity() {
        return new sample.Message();
    }

    @Override
    public Class<sample.Message> getEntityClass() {
        return sample.Message.class;
    }

    @Override
    public sample.Message getOriginalStates(sample.Message __entity) {
        return null;
    }

    @Override
    public void saveCurrentStates(sample.Message __entity) {
    }

    /**
     * @return the singleton
     */
    public static _Message getSingletonInternal() {
        return __singleton;
    }

    /**
     * @return the new instance
     */
    public static _Message newInstance() {
        return new _Message();
    }

}
