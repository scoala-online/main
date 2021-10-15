import styles from './app.module.css';
import { BrowserRouter as Router, Switch, Route } from 'react-router-dom';

import ModulePage from './pages/ModulePage';

export function App() {
  return (
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
