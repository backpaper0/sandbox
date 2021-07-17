const { EntitySchema } = require('typeorm');
const { Tag } = require('../entity/Tag');

module.exports = new EntitySchema({
  name: 'Tag',
  target: Tag,
  tableName: 'tags',
  columns: {
    id: {
      primary: true,
      type: 'int',
      generated: true
    },
    name: {
      type: 'varchar'
    }
  }
});

