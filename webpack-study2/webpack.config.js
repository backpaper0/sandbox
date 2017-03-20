var path = require('path')
var webpack = require('webpack')

module.exports = {
  entry: {
    foo: './src/foo.js',
    bar: ['./src/bar1.js', './src/bar2.js']
  },
  output: {
    path: path.resolve(__dirname, './dist'),
    filename: '[name].js'
  },
  module: {
    rules: [
      {
        test: /\.js$/,
        loader: 'babel-loader',
        exclude: /node_modules/
      }
    ]
  }
}
