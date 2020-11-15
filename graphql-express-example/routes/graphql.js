var { graphqlHTTP } = require('express-graphql');
var { buildSchema, defaultFieldResolver } = require('graphql');

// Construct a schema, using GraphQL schema language
var schema = buildSchema(`
  type Query {
    hello: String
    tweets(nextCursor: ID): Tweets
  }
  type Mutation {
    addTweet(content: String!): Tweet
  }
  type Tweets {
    tweets: [Tweet]
    nextCursor: ID
  }
  type Tweet {
    id: ID
    content: String!
    createdAt: String!
  }
`);

const {
  addTweet,
  findTweets,
} = require('../model/tweets');

// The root provides a resolver function for each API endpoint
var root = {
  hello: () => {
    return 'Hello world!';
  },
  tweets: (...args) => {
    return findTweets(...args);
  },
  addTweet: ({ content }) => {
    return addTweet(content);
  },
};

var router = graphqlHTTP({
  schema: schema,
  rootValue: root,
  graphiql: true,
});

module.exports = router;
