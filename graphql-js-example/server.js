var { graphql, buildSchema, defaultFieldResolver, GraphQLFieldResolver } = require('graphql');
 
// Construct a schema, using GraphQL schema language
var schema = buildSchema(`
  type Query {
    hello: String
    tasks: Tasks!
  }

  type Tasks {
    tasks: [Task!]!
    nextCursor: ID
  }
  type Task {
    id: ID
    title: String
    done: Boolean
    titleSize: Int
  }
`);

const tasks = [
  { id: '1', title: 'foo' , done: false },
  { id: '2', title: 'bar' , done: false },
  { id: '3', title: 'baz' , done: true  },
  { id: '4', title: 'hoge', done: true  },
];
 
// The root provides a resolver function for each API endpoint
var root = {
  hello: 'Hello world!',
  tasks: () => {
    return ({
      tasks,
      nextCursor: '5',
    });
  },
};
 
// Run the GraphQL query '{ hello }' and print out the response
graphql(schema, '{ hello }', root).then((response) => {
  console.log(response);
});

const source = `
  {
    tasks {
      tasks {
        id
        title
        done
        titleSize
      }
      nextCursor
    }
  }
`;

const resolvers = {
  Task: {
    titleSize: (source, args, contextValue, info) => source.title.length,
  },
};

const fieldResolver = (...args) => {
  const result = defaultFieldResolver(...args);
  if (result !== undefined) {
    return result;
  }
  const info = args[3];
  const property = resolvers[info.path.typename][info.fieldName];
  if (typeof property === 'function') {
    return property(...args);
  }
  return property;
};

graphql({ schema, source, rootValue: root, fieldResolver }).then((response) => {
  console.log(JSON.stringify(response.data));
});

