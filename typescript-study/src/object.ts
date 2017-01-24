interface ObjectSample {
    name: string;
    data: () => { foo: string, bar: number }
}

export default <ObjectSample>{
    name: 'object-sample',
    data () {
        return {
            foo: 'hello',
            bar: 12345
        }
    }
}
