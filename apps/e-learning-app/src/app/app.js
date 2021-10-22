import { useEffect, useState } from 'react';
import { BrowserRouter as Router, Switch, Route } from 'react-router-dom';

import Layout from './components/layout/Layout';
import Test from './components/Test';

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
        </Layout>
      </Switch>
    </Router>
  );
}
export default App;
