import { BrowserRouter as Router, Switch } from 'react-router-dom';
import LoginRoute from './components/loginRoute/LoginRoute';

import PrivateRoute from './components/privateRoute/PrivateRoute';
import HomePage from './pages/home/HomePage';
import LoginPage from './pages/login/LoginPage';

export function App() {
  return (
    <Router>
      <Switch>
        <PrivateRoute path="/home">
          <HomePage />
        </PrivateRoute>
        <LoginRoute path="/login">
          <LoginPage />
        </LoginRoute>
      </Switch>
    </Router>
  );
}
export default App;
