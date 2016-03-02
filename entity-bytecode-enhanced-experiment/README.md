# GlassFish上でJPA使った場合にバイトコードエンハンスされる件

`gradle run` して `curl http://localhost:8080/sample/ -d hello` でエンティティを作ったら
`curl http://localhost:8080/sample/` してみてください。

次のようなレスポンスを得られると思います。

```
class
    sample.Sample
fields
    private java.lang.Long sample.Sample.id
    private java.lang.String sample.Sample.text
    static final long sample.Sample.serialVersionUID
    protected transient java.lang.Object sample.Sample._persistence_primaryKey
    protected transient org.eclipse.persistence.internal.identitymaps.CacheKey sample.Sample._persistence_cacheKey
    protected transient java.beans.PropertyChangeListener sample.Sample._persistence_listener
    protected org.eclipse.persistence.queries.FetchGroup sample.Sample._persistence_fetchGroup
    protected transient boolean sample.Sample._persistence_shouldRefreshFetchGroup
    protected transient org.eclipse.persistence.sessions.Session sample.Sample._persistence_session
    protected transient java.util.List sample.Sample._persistence_relationshipInfo
    protected transient org.eclipse.persistence.internal.jpa.rs.metadata.model.Link sample.Sample._persistence_href
    protected transient org.eclipse.persistence.internal.jpa.rs.metadata.model.ItemLinks sample.Sample._persistence_links
methods
    public java.lang.Long sample.Sample.getId()
    public void sample.Sample.setId(java.lang.Long)
    public void sample.Sample.setText(java.lang.String)
    public java.lang.String sample.Sample.getText()
    public void sample.Sample._persistence_setId(java.lang.Object)
    public org.eclipse.persistence.internal.identitymaps.CacheKey sample.Sample._persistence_getCacheKey()
    public java.beans.PropertyChangeListener sample.Sample._persistence_getPropertyChangeListener()
    public void sample.Sample._persistence_setPropertyChangeListener(java.beans.PropertyChangeListener)
    public java.lang.Object sample.Sample._persistence_getId()
    public void sample.Sample._persistence_setCacheKey(org.eclipse.persistence.internal.identitymaps.CacheKey)
    public java.lang.Object sample.Sample._persistence_get(java.lang.String)
    public void sample.Sample._persistence_set(java.lang.String,java.lang.Object)
    public java.lang.Object sample.Sample._persistence_new(org.eclipse.persistence.internal.descriptors.PersistenceObject)
    public void sample.Sample._persistence_setFetchGroup(org.eclipse.persistence.queries.FetchGroup)
    public org.eclipse.persistence.sessions.Session sample.Sample._persistence_getSession()
    public org.eclipse.persistence.queries.FetchGroup sample.Sample._persistence_getFetchGroup()
    public boolean sample.Sample._persistence_isAttributeFetched(java.lang.String)
    public void sample.Sample._persistence_resetFetchGroup()
    public boolean sample.Sample._persistence_shouldRefreshFetchGroup()
    public void sample.Sample._persistence_setShouldRefreshFetchGroup(boolean)
    public void sample.Sample._persistence_setSession(org.eclipse.persistence.sessions.Session)
    public void sample.Sample._persistence_propertyChange(java.lang.String,java.lang.Object,java.lang.Object)
    public void sample.Sample._persistence_checkFetchedForSet(java.lang.String)
    public void sample.Sample._persistence_checkFetched(java.lang.String)
    public java.lang.Object sample.Sample._persistence_post_clone()
    public java.util.List sample.Sample._persistence_getRelationships()
    public void sample.Sample._persistence_setRelationships(java.util.List)
    public org.eclipse.persistence.internal.jpa.rs.metadata.model.Link sample.Sample._persistence_getHref()
    public void sample.Sample._persistence_setHref(org.eclipse.persistence.internal.jpa.rs.metadata.model.Link)
    public org.eclipse.persistence.internal.jpa.rs.metadata.model.ItemLinks sample.Sample._persistence_getLinks()
    public void sample.Sample._persistence_setLinks(org.eclipse.persistence.internal.jpa.rs.metadata.model.ItemLinks)
    public java.lang.Object sample.Sample._persistence_shallow_clone()
    public java.lang.Long sample.Sample._persistence_get_id()
    public void sample.Sample._persistence_set_id(java.lang.Long)
    public java.lang.String sample.Sample._persistence_get_text()
    public void sample.Sample._persistence_set_text(java.lang.String)
```

JPA用の様々なフィールド、メソッドが生えていますね！
