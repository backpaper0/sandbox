package app;

import java.util.Optional;
import org.apache.wicket.markup.html.WebPage;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.Button;
import org.apache.wicket.markup.html.form.StatelessForm;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.model.AbstractReadOnlyModel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.request.mapper.parameter.PageParameters;

public class HomePage extends WebPage {

    private final IModel<Integer> left = new Model<>();
    private final IModel<Integer> right = new Model<>();
    private final IModel<Integer> answer = new AbstractReadOnlyModel<Integer>() {

        @Override
        public Integer getObject() {
            Optional<Integer> l = Optional.ofNullable(left.getObject());
            Optional<Integer> r = Optional.ofNullable(right.getObject());
            Optional<Integer> a = l.flatMap(x -> r.map(y -> x + y));
            return a.orElse(null);
        }
    };

    public HomePage(final PageParameters parameters) {
        super(parameters);

        //Form<?> form = new Form<>("form");
        StatelessForm<?> form = new StatelessForm<>("form");

        TextField<Integer> left = new TextField<>("left", this.left,
                Integer.class);

        TextField<Integer> right = new TextField<>("right", this.right,
                Integer.class);

        Label answer = new Label("answer", this.answer);

        Button calc = new Button("calc");

        form.add(left, right, answer, calc);

        add(form);
    }
}
