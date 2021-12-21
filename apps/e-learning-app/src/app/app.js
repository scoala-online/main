import { useEffect, useState } from 'react';
import { BrowserRouter as Router, Switch, Route } from 'react-router-dom';
import styles from './app.module.css';

import Layout from './components/layout/Layout';
import Test from './components/Test';
import HttpDemo from './services/HttpServiceDemo';
import ModulePage from './pages/ModulePage';

/**
 * App renders the Layout component.
 * State:
 * - dimensions: object ( contains the current height and width of the viewport )
 */
export function App() {
  // State
  const [dimensions, setDimensions] = useState({
    height: window.innerHeight,
    width: window.innerWidth,
  });

  /**
   * Changes the dimensions state whenever the viewport sizes are changed.
   */
  useEffect(() => {
    function handleResize() {
      setDimensions({
        height: window.innerHeight,
        width: window.innerWidth,
      });
    }

    window.addEventListener('resize', handleResize);

    return (_) => {
      window.removeEventListener('resize', handleResize);
    };
  });

  return (
    <Router>
      <Switch>
        <Layout dimensions={dimensions}>
          <Route path="/" component={Test} />
          <Route exact path={'/demo'}>
            <HttpDemo />
          </Route>
        </Layout>
      </Switch>
    </Router>
    <div className={styles.app}>
      <Router>
        <Switch>
          <Route exact path={'/module'} component={ModulePage} />
        </Switch>
      </Router>
    </div>
  );
}
export default App;
