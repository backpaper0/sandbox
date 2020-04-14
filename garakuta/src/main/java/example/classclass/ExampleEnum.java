package example.classclass;

public enum ExampleEnum {

    VALUE1,
    VALUE2 {
        @Override
        void m() {
        }
    };

    void m() {
    }
}
