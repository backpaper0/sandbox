const { DaprServer, DaprClient, AbstractActor } = require('dapr-client');

function add({ arg1, arg2 }) {
  const result = arg1 + arg2;
  return ({ result });
}

class CounterActor extends AbstractActor {
  count = 0;
  countUp() {
    return ++this.count;
  }
}

async function start() {
  const server = new DaprServer('localhost', parseInt(process.env.PORT) || 3001, 'localhost', process.env.DAPR_HTTP_PORT);
  const client = new DaprClient('localhost', process.env.DAPR_HTTP_PORT);

  // startServerよりも前にinit, registerActorをしないといけない
  await server.actor.init();
  server.actor.registerActor(CounterActor);
  
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

