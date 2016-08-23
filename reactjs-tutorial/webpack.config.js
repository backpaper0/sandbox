module.exports = {
    entry: [ "./tutorial.jsx" ],
    output: {
        path: __dirname,
        filename: "bundle.js"
    },
    module: {
        loaders: [
            { test: /\.jsx$/, loader: "babel" },
            { test: /\.jsx$/, loader: "jsx" }
        ]
    }
};
