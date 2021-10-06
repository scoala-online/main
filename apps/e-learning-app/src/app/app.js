import styles from './app.module.css';
import { BrowserRouter as Router, Switch, Route } from 'react-router-dom';

import Module from './components/Module';

export function App() {
  return (
    <div className={styles.app}>
      <Router>
        <Switch>
          <Route exact path={'/module'} component={Module} />
        </Switch>
      </Router>
    </div>
  );
}
export default App;
