import { BrowserRouter as Router, Switch } from 'react-router-dom';
import LoginRoute from './components/loginRoute/LoginRoute';
import PrivateRoute from './components/privateRoute/PrivateRoute';
import JwtVerification from './services/jwtVerification.service';
import HomePage from './pages/home/HomePage';
import LoginPage from './pages/login/LoginPage';

export function App() {
  return (
    <Router>
      <Switch>
        <PrivateRoute exact path={['/home', '/']}>
          <HomePage />
        </PrivateRoute>
        <LoginRoute exact path="/login">
          <LoginPage />
        </LoginRoute>
      </Switch>
      <JwtVerification />
    </Router>
  );
}
export default App;
