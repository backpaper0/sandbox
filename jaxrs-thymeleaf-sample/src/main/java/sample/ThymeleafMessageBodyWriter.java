package sample;

import java.beans.BeanInfo;
import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.MessageBodyWriter;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;
import org.thymeleaf.templateresolver.ClassLoaderTemplateResolver;
import org.thymeleaf.templateresolver.TemplateResolver;

public class ThymeleafMessageBodyWriter implements
        MessageBodyWriter<Object> {

    private TemplateEngine templateEngine;

    public ThymeleafMessageBodyWriter() {
        templateEngine = new TemplateEngine();
        TemplateResolver templateResolver = new ClassLoaderTemplateResolver();
        templateEngine.setTemplateResolver(templateResolver);
    }

    @Override
    public boolean isWriteable(Class<?> type, Type genericType,
            Annotation[] annotations, MediaType mediaType) {
        return getTemplateName(annotations) != null;
    }

    @Override
    public long getSize(Object t, Class<?> type, Type genericType,
            Annotation[] annotations, MediaType mediaType) {
        return -1;
    }

    @Override
    public void writeTo(Object t, Class<?> type, Type genericType,
            Annotation[] annotations, MediaType mediaType,
            MultivaluedMap<String, Object> httpHeaders,
            OutputStream entityStream) throws IOException,
            WebApplicationException {
        try {
            String templateName = getTemplateName(annotations);
            Context context = new Context();
            Map<String, Object> variables = new HashMap<>();
            BeanInfo beanInfo = Introspector.getBeanInfo(type);
            for (PropertyDescriptor pd : beanInfo.getPropertyDescriptors()) {
                Method readMethod = pd.getReadMethod();
                if (readMethod != null) {
                    String key = pd.getName();
                    Object value = readMethod.invoke(t);
                    variables.put(key, value);
                }
            }
            context.setVariables(variables);
            Writer writer = new OutputStreamWriter(entityStream);
            templateEngine.process(templateName, context, writer);
            writer.flush();
        } catch (IllegalAccessException | IntrospectionException ex) {
        } catch (InvocationTargetException ex) {
            throw new WebApplicationException(ex.getCause(), Response.Status.INTERNAL_SERVER_ERROR);
        }
    }

    private String getTemplateName(Annotation[] annotations) {
        for (Annotation a : annotations) {
            if (a.annotationType() == Template.class) {
                return ((Template) a).value();
            }
        }
        return null;
    }
}
