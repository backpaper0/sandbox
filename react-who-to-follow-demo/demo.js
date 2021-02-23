'use strict';

const useState = React.useState;
const useEffect = React.useEffect;

function App() {
  const [users, setUsers] = useState([]);

  return (
    <div className="container">
      <Header setUsers={setUsers}/>
      <Suggestions users={users}/>
    </div>
  );
}

function Header({ setUsers }) {
  const refresh = () => {
    const randomOffset = Math.floor(Math.random() * 500);
    fetch(`https://api.github.com/users?since=${randomOffset}`).then(resp => resp.json()).then(users => setUsers(users));
  };
  useEffect(refresh, []);
  return (
    <div className="header">
      <h2>Who to follow</h2><a href="#" className="refresh" onClick={refresh}>Refresh</a>
    </div>
  );
}

function Suggestions({ users }) {
  return (
    <ul className="suggestions">
      <Suggestion users={users}/>
      <Suggestion users={users}/>
      <Suggestion users={users}/>
    </ul>
  );
}

function Suggestion({ users }) {
  const [user, setUser] = useState();
  const [avatarUrl, setAvatarUrl] = useState();

  const nextUser = () => {
    const user = users[Math.floor(Math.random() * users.length)];
    setUser(user);
    // アバターがロードされるまで前のアバターが残ってしまうため一旦空文字列を設定する
    setAvatarUrl('');
    if (user !== undefined) {
      window.setTimeout(() => {
        setAvatarUrl(user.avatar_url);
      }, 0);
    }
  };

  useEffect(nextUser, [users]);

  useEffect(() => {
  }, [user]);

  if (user === undefined) {
    return null;
  }

  return (
    <li>
      <img src={avatarUrl}/>
      <a href={user.html_url} target="_blank" className="username">{user.login}</a>
      <span> </span>
      <a href="#" className="close" onClick={nextUser}>x</a>
    </li>
  );
}

const domContainer = document.querySelector('#root');
ReactDOM.render(<App/>, domContainer);
