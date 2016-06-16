package sample;

public class Jsp {

    private final String path;
    private final Object model;

    public Jsp(String path, Object model) {
        this.path = path;
        this.model = model;
    }

    public String getPath() {
        return path;
    }

    public Object getModel() {
        return model;
    }
}
