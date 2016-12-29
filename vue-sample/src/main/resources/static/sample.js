var app = new Vue({
    el : '#app',
    data : {
        username : '',
        password : ''
    },
    methods : {
        login : function() {
            console.log(this.username);
            console.log(this.password);
        }
    }
});
