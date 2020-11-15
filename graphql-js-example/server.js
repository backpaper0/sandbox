var { graphql, buildSchema, defaultFieldResolver, GraphQLFieldResolver } = require('graphql');
 
// Construct a schema, using GraphQL schema language
var schema = buildSchema(`
  type Query {
    hello: String
    hello2: String
    tasks: Tasks!
  }
  type Mutation {
    addTask(title: String): Task
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

const idGenerator = (() => {
  let value = 0;
  return () => {
    value++;
    return value.toString();
  };
})();
const tasks = [
  { id: idGenerator(), title: 'foo' , done: false },
  { id: idGenerator(), title: 'bar' , done: false },
  { id: idGenerator(), title: 'baz' , done: true  },
  { id: idGenerator(), title: 'hoge', done: true  },
];
 
// The root provides a resolver function for each API endpoint
var root = {
  hello: 'Hello world!',
  hello2: () => new Promise(resolve => setTimeout(() => resolve('Hello world!(2)'), 100)),
  tasks: () => {
    return ({
      tasks,
      nextCursor: (tasks.length + 1).toString(),
    });
  },
  addTask: ({ title }) => {
    const task = { id: idGenerator(), title, done: false };
    tasks.push(task);
    return task;
  },
};
 
// Run the GraphQL query '{ hello }' and print out the response
graphql(schema, '{ hello }', root).then((response) => {
  console.log(response);
});

graphql(schema, '{ hello2 }', root).then((response) => {
  console.log(response);
});

graphql(schema, '{ hello, hello2 }', root).then((response) => {
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

graphql({ schema, source: `
  mutation AddTask($title: String!) {
    addTask(title: $title) {
      id
      title
      done
    }
  }
`, rootValue: root, fieldResolver, variableValues: {
  title: 'helloworld',
} }).then((response) => {
  console.log(response);
});

graphql({ schema, source, rootValue: root, fieldResolver }).then((response) => {
  console.log(JSON.stringify(response.data));
});

