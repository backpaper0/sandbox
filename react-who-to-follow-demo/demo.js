'use strict';

const {
  useState,
  useEffect,
  useContext,
  useRef,
} = React;

////////////////////////////////////////////////////////////////////////////////
// Components

function App() {
  return (
    <FetchersProvider>
      <WhoToFollow/>
    </FetchersProvider>
  );
}

function WhoToFollow() {
  const [users, updateUsers] = useUsers();
  return (
    <div className="container">
      <Header updateUsers={updateUsers}/>
      <Suggestions users={users}/>
    </div>
  );
}

function Header({ updateUsers }) {
  useEffect(updateUsers, []);
  return (
    <div className="header">
      <h2>Who to follow</h2><a href="#" className="refresh" onClick={updateUsers}>Refresh</a>
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
  const [user, nextUser] = useUser(users);
  useEffect(nextUser, [users]);

  if (user === undefined) {
    return null;
  }

  return (
    <li>
      <Avatar src={user.avatar_url}/>
      <a href={user.html_url} target="_blank" className="username">{user.login}</a>
      <span> </span>
      <a href="#" className="close" onClick={nextUser}>x</a>
    </li>
  );
}

function Avatar({ src }) {
  const [avatarUrl, setAvatarUrl] = useState('');
  // アバターがロードされるまで前のアバターが残ってしまうため一旦空文字列を設定する
  useEffect(() => {
    setAvatarUrl('');
    window.setTimeout(() => {
      setAvatarUrl(src);
    }, 0);
  }, [src]);
  return (
    <img src={avatarUrl}/>
  );
}

////////////////////////////////////////////////////////////////////////////////
// Hooks

function useUsers() {
  const { fetchUsers } = useFetchers();
  const [users, setUsers] = useState([]);
  const updateUsers = () => {
    fetchUsers().then(users => setUsers(users));
  };
  return [users, updateUsers];
}

function useUser(users) {
  const [user, setUser] = useState();
  const nextUser = () => {
    const user = users[Math.floor(Math.random() * users.length)];
    setUser(user);
  };
  return [user, nextUser];
}

////////////////////////////////////////////////////////////////////////////////
// Fetch functions

async function fetchUsers() {
  const randomOffset = Math.floor(Math.random() * 500);
  const resp = await fetch(`https://api.github.com/users?since=${randomOffset}`);
  return await resp.json();
}

////////////////////////////////////////////////////////////////////////////////
// Contexts

const FetchersContext = React.createContext();

function useFetchers() {
  const ref = useContext(FetchersContext);
  return ref.current;
}

function FetchersProvider({ children }) {
  const ref = useRef({
    fetchUsers,
  });
  return (
    <FetchersContext.Provider value={ref}>
      {children}
    </FetchersContext.Provider>
  );
}

////////////////////////////////////////////////////////////////////////////////
// Render to DOM

const domContainer = document.querySelector('#root');
ReactDOM.render(<App/>, domContainer);
