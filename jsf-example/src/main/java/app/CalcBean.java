package app;

import javax.faces.bean.ManagedBean;

@ManagedBean(name = "calc")
public class CalcBean {

    private int left;

    private int right;

    private int answer;

    public Object add() {
        answer = left + right;
        return null;
    }

    public int getLeft() {
        return left;
    }

    public void setLeft(int left) {
        this.left = left;
    }

    public int getRight() {
        return right;
    }

    public void setRight(int right) {
        this.right = right;
    }

    public int getAnswer() {
        return answer;
    }

    public void setAnswer(int answer) {
        this.answer = answer;
    }
}
