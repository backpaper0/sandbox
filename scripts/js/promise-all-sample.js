
const getSprints = () => new Promise(resolve => {
  setTimeout(() => {
    resolve([
      {id: 1, name: 'Sprint 1'},
      {id: 2, name: 'Sprint 2'},
      {id: 3, name: 'Sprint 3'}
    ]);
  }, 100);
});

const getTasks = sprintId => new Promise(resolve => {
  const tasks = {
    1: [
      {id: 1, name: 'Task 1', status: 'TODO'},
      {id: 2, name: 'Task 2', status: 'Doing'},
      {id: 3, name: 'Task 3', status: 'Done'}
    ],
    2: [
      {id: 4, name: 'Task 4', status: 'TODO'},
      {id: 5, name: 'Task 5', status: 'TODO'}
    ],
    3: [
      {id: 6, name: 'Task 6', status: 'Done'}
    ]
  };
  setTimeout(() => {
    resolve(tasks[sprintId]);
  }, 100);
});



//getSprints().then(sprints => {
//  return Promise.all(sprints.map(({ id, name }) => {
//    return getTasks(id).then(tasks => {
//      return { sprintId: id, name, tasks };
//    });
//  }));
//}).then(a => console.log(JSON.stringify(a, null, '  ')));

const run = async () => {
  const sprints = await getSprints();
  const tasks = await Promise.all(sprints.map(async ({ id, name }) => {
    const tasks = await getTasks(id);
    return { sprintId: id, name, tasks };
  }))
  console.log(JSON.stringify(tasks, null, '  '));
};

run();
