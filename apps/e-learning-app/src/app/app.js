import styles from './app.module.css';
import { ReactComponent as Logo } from './logo.svg';

import { BrowserRouter as Router, Switch, Route } from 'react-router-dom';
import { environment } from '../environments/environment';

import HomePage from './components/homePage.component'

export function App() {
  return (
    <div className={styles.app}>
      <Router>
        <Switch>
          <Route exact path={'/'} component={HomePage} />
        </Switch>
      </Router>
    </div>
  );
}
export default App;
