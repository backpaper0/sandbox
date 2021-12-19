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
  const server = new DaprServer('localhost', process.env.PORT || 3001, 'localhost', process.env.DAPR_HTTP_PORT);
  const client = new DaprClient('localhost', process.env.DAPR_HTTP_PORT);

  // startServerよりも前にinit, registerActorをしないといけない
  await server.actor.init();
  server.actor.registerActor(CounterActor);

  await server.invoker.listen('add', async ({ body }) => {
    const data = JSON.parse(body);
    return add(data);
  }, { method: 'post' });

  // routeのデフォルト値は route-${pubsubName}-${topic} 。
  // ここでは pubsubName = pubsub, topic = add のためrouteは route-pubsub-add となる。
  // このパターン以外の名前にしたい場合、subscribeメソッドの第4引数で指定できる。
  await server.pubsub.subscribe('pubsub', 'add', async (data) => {
    const result = add(data);
    await client.state.save('share-statestore', [{
      key: 'result',
      value: result
    }]);
  });

  await server.startServer();
}

start();

