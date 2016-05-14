package app;

import javax.faces.bean.ManagedBean;

@ManagedBean
public class StatelessBean {

    private String input;
    private String output;

    public void echo() {
        output = input;
    }

    public String getInput() {
        return input;
    }

    public void setInput(String input) {
        this.input = input;
    }

    public String getOutput() {
        return output;
    }

    public void setOutput(String output) {
        this.output = output;
    }
}
