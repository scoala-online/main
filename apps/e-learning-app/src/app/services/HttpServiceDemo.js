import { useState } from 'react';
import http from './HttpService';

const HttpServiceDemo = (props) => {
  // Props
  const resourceUrl = props.resourceUrl;

  // State
  const [content, setContent] = useState({});
  const [managedId, setManagedId] = useState(-1);

  const fetchAll = () => {
    http.customGet(resourceUrl).then((response) => {
      setContent(response.data.toString());
      console.log(response);
    });
  };

  const addTest = () => {
    const testLectureMaterial = {
      document: 'testMe.pdf',
    };

    http.customPost(resourceUrl, testLectureMaterial).then((response) => {
      setManagedId(response.data.id);
      setContent(response.data.toString());
      console.log(response);
    });
  };

  const fetchById = () => {
    http
      .customGet(resourceUrl + '/' + managedId.toString())
      .then((response) => {
        setContent(response.data.toString());
        console.log(response);
      });
  };

  const editById = () => {
    const testLectureMaterial = {
      document: 'tested.pdf',
    };

    http
      .customPatch(
        resourceUrl + '/' + managedId.toString(),
        testLectureMaterial
      )
      .then((response) => {
        setContent(response.data.toString());
        console.log(response);
      });
  };

  const deleteById = () => {
    http
      .customGet(resourceUrl + '/' + managedId.toString())
      .then((response) => {
        setManagedId(-1);
        setContent(response.data.toString());
        console.log(response);
      });
  };

  return (
    <>
      <div>
        <p>Http Service Demo on /lecture-materials</p>
        <p>
          In order to see the effect of the buttons, press 'Ctrl+Shift+I' to
          open up the inspector and look in the console
        </p>
        <button onClick={fetchAll}> Fetch all Lecture materials </button>
        <button onClick={addTest}> Create a new test Lecture material </button>
        {managedId !== -1 ? (
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
        ) : null}
      </div>
      {/* <div>{content}</div> */}
    </>
  );
};

export default HttpServiceDemo;
