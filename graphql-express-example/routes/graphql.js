var { graphqlHTTP } = require('express-graphql');
var { buildSchema, defaultFieldResolver } = require('graphql');

// Construct a schema, using GraphQL schema language
var schema = buildSchema(`
  type Query {
    hello: String
  }
`);
 
// The root provides a resolver function for each API endpoint
var root = {
  hello: () => {
    return 'Hello world!';
  },
};

var router = graphqlHTTP({
  schema: schema,
  rootValue: root,
  graphiql: true,
});

module.exports = router;
