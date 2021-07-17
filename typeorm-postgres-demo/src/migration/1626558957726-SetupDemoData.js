const { Tag } = require('../entity/Tag');

module.exports = class SetupDemoData1626558957726 {

  names = ['JavaScript', 'PostgreSQL'];

  async up(queryRunner) {
    const { manager } = queryRunner;
    const tagRepository = manager.getRepository(Tag);
    const tags = this.names.map(name => tagRepository.create({ name }));
    await tagRepository.save(tags);
  }

  async down(queryRunner) {
    const { manager } = queryRunner;
    const tagRepository = manager.getRepository(Tag);
    const tags = this.names.map(name => tagRepository.delete({ name }));
    await Promise.all(tags);
  }
}

