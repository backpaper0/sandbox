const { DaprServer, DaprClient } = require('dapr-client');

async function start() {
  const server = new DaprServer('localhost', process.env.PORT || 3000, 'localhost', process.env.DAPR_HTTP_PORT);
  const client = new DaprClient('localhost', process.env.DAPR_HTTP_PORT);

  await server.startServer();

  await server.invoker.listen('calc', async ({ body }) => {
    const data = JSON.parse(body);
    await client.pubsub.publish('pubsub', 'add', data);
    return await client.invoker.invoke('add-app', 'add', 'post', data);
  }, { method: 'post' });

  await server.invoker.listen('result', async () => {
    return await client.state.get('share-statestore', 'result');
  });

  await server.invoker.listen('counter', async ({ body }) => {
    const { actorId } = JSON.parse(body);
    return await client.actor.invoke('GET', 'CounterActor', actorId, 'countUp');
  }, { method: 'post' });
}

start();

