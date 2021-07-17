const { createConnection } = require('typeorm');
const { Tag } = require('./src/entity/Tag');

(async function() {

  const con = await createConnection();
  const manager = con.manager;

  const tagRepository = manager.getRepository(Tag);
  const tags = await tagRepository.find();
  console.log(tags);

  await con.close();
})();

