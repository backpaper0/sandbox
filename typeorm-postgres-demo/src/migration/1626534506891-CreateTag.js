const { Table } = require('typeorm');

module.exports = class CreateTag1626534506891 {

    async up(queryRunner) {
      await queryRunner.createTable(new Table({
        name: 'tags',
        columns: [
          {
            name: 'id',
            type: 'int',
            isGenerated: true,
            isPrimary: true,
          },
          {
            name: 'name',
            type: 'varchar',
          },
        ],
      }));
    }

    async down(queryRunner) {
      await queryRunner.dropTable('tags');
    }
}
        
