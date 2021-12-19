const { DaprServer, DaprClient } = require('dapr-client');

function add({ arg1, arg2 }) {
  const result = arg1 + arg2;
  return ({ result });
}

async function start() {
  const server = new DaprServer('localhost', parseInt(process.env.PORT) || 3001, 'localhost', process.env.DAPR_HTTP_PORT);
  const client = new DaprClient('localhost', process.env.DAPR_HTTP_PORT);

  await server.startServer();

  await server.invoker.listen('add', async ({ body }) => {
    const data = JSON.parse(body);
    return add(data);
  }, { method: 'post' });

  await server.pubsub.subscribe('pubsub', 'add', async (data) => {
    const result = add(data);
    await client.state.save('share-statestore', [{
      key: 'result',
      value: result
    }]);
  });
}

start();

