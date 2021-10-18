import { useEffect, useState } from 'react';
import http from './HttpService';

const HttpServiceDemo = (props) => {
  const resourceUrl = 'lecture-materials';

  // State
  const [managedId, setManagedId] = useState(-1);
  const [username, setUsername] = useState('');
  const [password, setPassword] = useState('');
  const [authenticated, setAuthenticated] = useState(false);

  useEffect(() => {
    return () => {
      http.logout();
    };
  }, []);

  // HTTP Service Usage Example
  const login = () => {
    http.login(username, password, () => {
      setAuthenticated(true);
    });
  };

  const fetchAll = () => {
    http.getAll(resourceUrl, (response) => {
      console.log(response);
    });
  };

  const addTest = () => {
    const testLectureMaterial = {
      document: 'testMe.pdf',
    };

    http.add(resourceUrl, testLectureMaterial, (response) => {
      setManagedId(response.data.id);
      console.log(response);
    });
  };

  const fetchById = () => {
    http.getById(resourceUrl, managedId, (response) => {
      console.log(response);
    });
  };

  const editById = () => {
    const testLectureMaterial = {
      document: 'tested.pdf',
    };

    http.update(resourceUrl, managedId, testLectureMaterial, (response) => {
      console.log(response);
    });
  };

  const deleteById = () => {
    http.delete(resourceUrl, managedId, (response) => {
      setManagedId(-1);
      console.log(response);
    });
  };

  const renderExtraOptions = () => {
    if (managedId !== -1) {
      return (
        <>
          <button onClick={fetchById}>
            Fetch the lecture material you just added
          </button>
          <button onClick={editById}>
            Edit the lecture material you just added
          </button>
          <button onClick={deleteById}>
            Delete the lecture material you just added
          </button>
        </>
      );
    }
  };

  return (
    <div>
      <p>Http Service Demo on /lecture-materials</p>
      <p>
        In order to see the effect of the buttons, press 'Ctrl+Shift+I' to open
        up the inspector and look in the console
      </p>
      {authenticated ? (
        <>
          <button onClick={fetchAll}> Fetch all Lecture materials </button>
          <button onClick={addTest}>Create a new test Lecture material</button>
          {renderExtraOptions()}
        </>
      ) : (
        <>
          <input
            type="username"
            onInput={(value) => setUsername(value.currentTarget.value)}
            placeholder="example@email.com"
          ></input>
          <input
            type="password"
            onInput={(value) => setPassword(value.currentTarget.value)}
            placeholder="abcABC132@!"
          ></input>
          <button onClick={login}>Login</button>
        </>
      )}
    </div>
  );
};

export default HttpServiceDemo;
