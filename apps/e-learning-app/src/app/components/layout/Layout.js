import React from 'react';

import NavMenu from '../navMenu/NavMenu';

//This component will render the NavBar and the content of the current page
export default function Layout(props) {
  return (
    <>
      <NavMenu />
      <div
        style={{
          width: '100vw',
          position: 'fixed',
          top: '7.68vh',
          bottom: '0px',
          margin: '0px',
          padding: '0px',
          overflowY: 'overlay',
        }}
      >
        {props.children}
      </div>
    </>
  );
}
